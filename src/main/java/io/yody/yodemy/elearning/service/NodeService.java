package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.*;
import io.yody.yodemy.elearning.domain.constant.MetafieldConstant;
import io.yody.yodemy.elearning.domain.enumeration.CategoryTypeEnum;
import io.yody.yodemy.elearning.repository.*;
import io.yody.yodemy.elearning.service.business.NodeAggregate;
import io.yody.yodemy.elearning.service.business.NodeMetafieldBO;
import io.yody.yodemy.elearning.service.business.RuleBO;
import io.yody.yodemy.elearning.service.dto.CategoryDTO;
import io.yody.yodemy.elearning.service.dto.NodeDTO;
import io.yody.yodemy.elearning.service.helpers.IdHelper;
import io.yody.yodemy.elearning.service.helpers.NodeTransactionManager;
import io.yody.yodemy.elearning.service.mapper.NodeMapper;
import io.yody.yodemy.elearning.service.mapper.NodeMetafieldMapper;
import io.yody.yodemy.elearning.web.rest.vm.request.*;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleNamespace;
import io.yody.yodemy.rule.knowledgeBase.mapper.RuleMapper;
import io.yody.yodemy.rule.knowledgeBase.repository.RuleRepository;
import io.yody.yodemy.rule.knowledgeBase.request.RuleRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.nentangso.core.service.errors.NtsValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.NODE;

@Service
public class NodeService {

    private static final String ENTITY_NAME = "node";
    private final Logger log = LoggerFactory.getLogger(NodeService.class);
    private final NodeRepository nodeRepository;
    private final NodeMapper nodeMapper;
    private final NodeMetafieldRepository nodeMetafieldRepository;
    private final NodeMetafieldMapper nodeMetafieldMapper;
    private final IdHelper idHelper;
    private final NodeTransactionManager transactionManager;
    private final CategoryService categoryService;
    private final CourseService courseService;
    private final RuleRepository ruleRepository;
    private final EdgeRepository edgeRepository;
    private final ExamService examService;
    private final CourseRepository courseRepository;
    private final ExamRepository examRepository;

    public NodeService(
        NodeRepository nodeRepository,
        NodeMapper nodeMapper,
        NodeMetafieldRepository nodeMetafieldRepository,
        NodeMetafieldMapper nodeMetafieldMapper,
        IdHelper idHelper,
        NodeTransactionManager transactionManager,
        CategoryService categoryService,
        CourseService courseService,
        RuleRepository ruleRepository,
        EdgeRepository edgeRepository,
        ExamService examService,
        CourseRepository courseRepository,
        ExamRepository examRepository
    ) {
        this.nodeRepository = nodeRepository;
        this.nodeMapper = nodeMapper;
        this.nodeMetafieldRepository = nodeMetafieldRepository;
        this.nodeMetafieldMapper = nodeMetafieldMapper;
        this.idHelper = idHelper;
        this.transactionManager = transactionManager;
        this.categoryService = categoryService;
        this.courseService = courseService;
        this.ruleRepository = ruleRepository;
        this.edgeRepository = edgeRepository;
        this.examService = examService;
        this.courseRepository = courseRepository;
        this.examRepository = examRepository;
    }

    private void processTempId(List<NodeRequest> list) {
        idHelper.processNodeId(list);
    }

    private <MTYPE> Map<Long, List<MTYPE>> convertToMap(
        List<NodeMetafieldEntity> metafields,
        Function<List<NodeMetafieldEntity>, List<MTYPE>> mapperFunction,
        Function<MTYPE, Long> getOwnerIdFunction
    ) {
        List<MTYPE> metafieldDTOS = mapperFunction.apply(metafields);
        Map<Long, List<MTYPE>> metafieldMap = metafieldDTOS.stream().collect(Collectors.groupingBy(getOwnerIdFunction));
        return metafieldMap;
    }

    private <DTO, MTYPE> void enrichMetafields(
        List<DTO> dtos,
        Function<DTO, Long> getIdFunction,
        BiConsumer<DTO, List<MTYPE>> setMetafieldFunction,
        Function<List<NodeMetafieldEntity>, List<MTYPE>> metafieldMapperFunction,
        Function<MTYPE, Long> metafieldGetOwnerIdFunction,
        String metafieldEnum
    ) {
        List<Long> ids = dtos.stream().map(getIdFunction).collect(Collectors.toList());
        List<NodeMetafieldEntity> metafields = nodeMetafieldRepository.findAllByOwnerResourceAndOwnerIdIn(metafieldEnum, ids);
        Map<Long, List<MTYPE>> metafieldMap = convertToMap(metafields, metafieldMapperFunction, metafieldGetOwnerIdFunction);
        for (DTO dto : dtos) {
            setMetafieldFunction.accept(dto, metafieldMap.get(getIdFunction.apply(dto)));
        }
    }

    public static List<NodeMetafieldEntity> getAllMetafields(NodeAggregate aggregate) {
        List<NodeMetafieldEntity> metafields = new ArrayList<>();
        List<NodeMetafieldBO> metafieldBOS = aggregate.getMetafields();
        if (metafieldBOS != null && !metafieldBOS.isEmpty()) {
            metafields.addAll(NodeMetafieldMapper.INSTANCE.bosToEntites(metafieldBOS));
        }
        return metafields;
    }

    private void enrichRule(List<NodeAggregate> aggregates) {
        List<Long> nodeIds = aggregates.stream()
            .map(NodeAggregate::getId)
            .collect(Collectors.toList());

        List<String> ruleNamespaces = Arrays.asList(
            RuleNamespace.NODE_TIME.name(),
            RuleNamespace.NODE_EMPLOYEE.name()
        );
        List<RuleEntity> nodeRules = ruleRepository.findByNamespaceInAndRootIdIn(ruleNamespaces, nodeIds);

        List<RuleBO> nodeRuleBos = nodeRules.stream()
            .map(RuleMapper.INSTANCE::entityToBo)
            .collect(Collectors.toList());

        Map<Long, List<RuleBO>> nodeRuleDtoMap = nodeRuleBos.stream()
            .collect(Collectors.groupingBy(RuleBO::getRootId));

        for (NodeAggregate aggregate : aggregates) {
            List<RuleBO> rulesForNode = nodeRuleDtoMap.getOrDefault(aggregate.getId(), Collections.emptyList());
            aggregate.setRules(rulesForNode);
        }
    }

    private void validateLabel(NodeRequest request) {
        boolean labelExists;

        if ("CONTAINER".equals(request.getType())) return;

        if (request.getId() != null) {
            labelExists = nodeRepository.existsByLabelAndRootIdAndIdNot(request.getLabel(), request.getRootId(), request.getId());
        } else {
            labelExists = nodeRepository.existsByLabelAndRootId(request.getLabel(), request.getRootId());
        }

        if (labelExists) {
            throw new NtsValidationException("message", "Tên node đã tồn tại");
        }
    }

    @Transactional
    public NodeDTO save(NodeRequest request) {
        log.debug("Request to save Node : {}", request);
        if (ObjectUtils.isEmpty(request.getMetafields())) {
            throw new NtsValidationException("message", "Thiếu thông tin metafields");
        }
        validateLabel(request);
        processTempId(List.of(request));
        NodeAggregate nodeAggregate = new NodeAggregate(request);
        NodeDTO nodeDto = transactionManager.save(nodeAggregate);
        return nodeDto;
    }

    private List<RuleBO> getNonExistItems(NodeRequest request, NodeAggregate bo) {
        if (Objects.isNull(request.getRules()) || Objects.isNull(bo.getRules())) {
            return new ArrayList();
        }
        Set<Long> requestIds = request.getRules().stream()
            .filter(item -> item.getId() != null)
            .map(RuleRequest::getId)
            .collect(Collectors.toSet());

        List<RuleBO> filteredBOItems = bo.getRules().stream()
            .filter(item -> item.getId() == null || !requestIds.contains(item.getId()))
            .collect(Collectors.toList());

        return filteredBOItems;
    }

    public NodeDTO update(NodeRequest request, Boolean updateNested) {
        log.debug("Request to update Node : {}", request);
        validateLabel(request);
        processTempId(List.of(request));
        NodeAggregate aggregate = transactionManager.findById(request.getId());
        if (!Objects.isNull(aggregate)) {
            enrichMetafields(
                List.of(aggregate),
                NodeAggregate::getId,
                NodeAggregate::setMetafields,
                NodeMetafieldMapper.INSTANCE::entitiesToBos,
                NodeMetafieldBO::getOwnerId,
                NODE
            );
            enrichRule(List.of(aggregate));
        }
        List<RuleEntity> orphanRuleEntities = new ArrayList();
        if (updateNested) {
            List<RuleBO> orphanRules = getNonExistItems(request, aggregate);
            orphanRuleEntities = RuleMapper.INSTANCE.bosToEntities(orphanRules);
        }
        List<NodeMetafieldEntity> oldMetafields = getAllMetafields(aggregate);
        aggregate.update(request);

        NodeDTO dto = transactionManager.update(oldMetafields, orphanRuleEntities, aggregate);

        return dto;
    }

    @Transactional
    public List<NodeDTO> updateMultiple(List<NodeRequest> requests) {
        log.debug("Request to update multiple Nodes : {}", requests);

        processTempId(requests);

        List<Long> nodeIds = requests.stream().map(NodeRequest::getId).collect(Collectors.toList());

        List<NodeAggregate> aggregates = transactionManager.findAllByIds(nodeIds);

        if (aggregates.isEmpty()) {
            throw new NtsValidationException("message", "Không tìm thấy các Node");
        }

        Map<Long, NodeRequest> requestMap = requests.stream().collect(Collectors.toMap(NodeRequest::getId, Function.identity()));

        List<NodeMetafieldEntity> allOldMetafields = new ArrayList<>();

        for (NodeAggregate aggregate : aggregates) {
            NodeRequest request = requestMap.get(aggregate.getId());

            enrichMetafields(
                List.of(aggregate),
                NodeAggregate::getId,
                NodeAggregate::setMetafields,
                NodeMetafieldMapper.INSTANCE::entitiesToBos,
                NodeMetafieldBO::getOwnerId,
                NODE
            );

            allOldMetafields.addAll(getAllMetafields(aggregate));
            aggregate.update(request);
        }

        List<NodeDTO> updatedNodes = transactionManager.updateMultiple(allOldMetafields, aggregates);

        return updatedNodes;
    }

    @Transactional
    public void delete(Long id) {
        NodeEntity entity = nodeRepository.findById(id)
            .orElseThrow(() -> new NtsValidationException("message", "Không tìm thấy node"));

        Boolean hasChild = nodeRepository.existsByRootId(id);
        if (hasChild) {
            throw new NtsValidationException("message", "Không thể xoá node có node con, vui lòng khoá node lại");
        }
        entity.setDeleted(true);
        nodeRepository.save(entity);

        List<EdgeEntity> sourceEdges = edgeRepository.findBySource(id);
        for (EdgeEntity edge : sourceEdges) {
            edge.setDeleted(true);
        }
        edgeRepository.saveAll(sourceEdges);
        List<EdgeEntity> targetEdges = edgeRepository.findByTarget(id);
        for (EdgeEntity edge : targetEdges) {
            edge.setDeleted(true);
        }
        edgeRepository.saveAll(targetEdges);

        if ("ISLAND".equals(entity.getType())) {
            CourseEntity course = courseRepository.findByNodeId(id);
            courseService.delete(course.getId());
        }

        if ("CREEP".equals(entity.getType())) {
            ExamEntity exam = examRepository.findByNodeId(id);
            examService.delete(exam.getId());
        }

        orderChildNode(entity.getRootId());
    }

    private void orderChildNode(Long rootId) {
        List<NodeEntity> childNodes = nodeRepository.findByRootId(rootId);
        List<Long> nodeIds = childNodes.stream().map(NodeEntity::getId).collect(Collectors.toList());

        List<NodeMetafieldEntity> metafields = nodeMetafieldRepository.findAllByOwnerResourceAndOwnerIdIn(MetafieldConstant.NODE, nodeIds)
            .stream()
            .filter(metafield -> "position".equals(metafield.getKey()))
            .collect(Collectors.toList());

        metafields.sort(Comparator.comparingInt(metafield -> Integer.parseInt(metafield.getValue())));

        for (int i = 0; i < metafields.size(); i++) {
            metafields.get(i).setValue(String.valueOf(i + 1));
        }

        nodeMetafieldRepository.saveAll(metafields);
    }

    private void processCategory(
        NodeRequest nodeRequest,
        Consumer<Long> setCategoryId,
        Function<CategoryDTO, CategoryDTO> categoryHandler
    ) {
        Long rootNodeId = nodeRequest.getRootId();
        if (rootNodeId != null) {
            NodeEntity containerRootNode = nodeRepository.findById(rootNodeId).orElse(null);
            if (Objects.isNull(containerRootNode)) return;
            NodeEntity rootNode = nodeRepository.findById(containerRootNode.getId()).orElse(null);
            if (!Objects.isNull(rootNode)) {
                CategoryDTO categoryRequest = new CategoryDTO()
                    .title(rootNode.getLabel())
                    .type(CategoryTypeEnum.COURSE);
                categoryRequest.setNodeId(rootNodeId);
                CategoryDTO category = categoryHandler.apply(categoryRequest);
                setCategoryId.accept(category.getId());
            }
        }
    }

    @Transactional
    public NodeDTO saveCourse(NodeCourseRequest request) {
        NodeRequest nodeRequest = request.getNode();
        CourseRequest courseRequest = request.getCourse();

        processCategory(
            nodeRequest,
            courseRequest::setCategoryId,
            categoryService::save
        );

        NodeDTO node = this.save(nodeRequest);
        courseRequest.setTitle(nodeRequest.getLabel());
        courseRequest.setNodeId(node.getId());
        courseService.save(courseRequest);

        return node;
    }

    @Transactional
    public NodeDTO updateCourse(NodeCourseRequest request) {
        NodeRequest nodeRequest = request.getNode();
        CourseRequest courseRequest = request.getCourse();
        processCategory(
            nodeRequest,
            courseRequest::setCategoryId,
            categoryService::update
        );

        NodeDTO node = this.update(nodeRequest, true);
        courseRequest.setTitle(nodeRequest.getLabel());
        courseRequest.setNodeId(node.getId());
        courseService.update(courseRequest);
        return node;
    }

    @Transactional
    public NodeDTO saveExam(NodeExamRequest request) {
        NodeRequest nodeRequest = request.getNode();
        ExamRequest examRequest = request.getExam();
        processCategory(
            nodeRequest,
            examRequest::setCategoryId,
            categoryService::save
        );
        NodeDTO node = this.save(nodeRequest);
        examRequest.setTitle(nodeRequest.getLabel());
        examRequest.setNodeId(node.getId());
        examService.save(examRequest);

        return node;
    }

    @Transactional
    public NodeDTO updateExam(NodeExamRequest request) {
        NodeRequest nodeRequest = request.getNode();
        ExamRequest examRequest = request.getExam();
        processCategory(
            nodeRequest,
            examRequest::setCategoryId,
            categoryService::save
        );
        NodeDTO node = this.update(nodeRequest, true);
        examRequest.setTitle(nodeRequest.getLabel());
        examRequest.setNodeId(node.getId());
        examService.update(examRequest);

        return node;
    }
}
