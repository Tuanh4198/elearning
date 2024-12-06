package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.*;
import io.yody.yodemy.elearning.domain.enumeration.CourseEmployeeStatusEnum;
import io.yody.yodemy.elearning.domain.enumeration.DocumentEmployeeStatusEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamEmployeeStatusEnum;
import io.yody.yodemy.elearning.repository.*;
import io.yody.yodemy.elearning.service.cache.EmployeeCache;
import io.yody.yodemy.elearning.service.criteria.SearchNodeCriteria;
import io.yody.yodemy.elearning.service.dto.CourseDTO;
import io.yody.yodemy.elearning.service.dto.DocumentDTO;
import io.yody.yodemy.elearning.service.dto.ExamDTO;
import io.yody.yodemy.elearning.service.dto.NodeDTO;
import io.yody.yodemy.elearning.service.helpers.*;
import io.yody.yodemy.elearning.service.mapper.CourseMapper;
import io.yody.yodemy.elearning.service.mapper.ExamMapper;
import io.yody.yodemy.elearning.service.mapper.NodeMapper;
import io.yody.yodemy.elearning.service.specification.NodeSpecification;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleNamespace;
import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;
import io.yody.yodemy.rule.knowledgeBase.mapper.RuleMapper;
import io.yody.yodemy.rule.knowledgeBase.repository.RuleRepository;
import io.yody.yodemy.rule.ruleImpl.nodeRuleEngine.NodeDetail;
import io.yody.yodemy.rule.ruleImpl.nodeRuleEngine.NodeInferenceEngine;
import io.yody.yodemy.rule.ruleImpl.nodeRuleEngine.NodeValidDetail;
import org.nentangso.core.service.errors.NtsValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NodeQueryService {

    private static final String ENTITY_NAME = "node";
    private final Logger log = LoggerFactory.getLogger(NodeQueryService.class);
    private final NodeRepository repo;
    private final NodeMetafieldHelper metafieldHelper;
    private final NodeMapper nodeMapper;
    private final RuleRepository ruleRepo;
    private final RuleMapper ruleMapper;
    private final NodeInferenceEngine nodeInferenceEngine;
    private final EmployeeCache employeeCache;
    private final CourseRepository courseRepository;
    private final CourseHelper courseHelper;
    private final CourseEmployeeRepository courseEmployeeRepository;
    private final ThumbHelper thumbHelper;
    private final DocumentEmployeeRepository documentEmployeeRepository;
    private final ExamRepository examRepository;
    private final ExamEmployeeRepository examEmployeeRepository;
    private final ExamHelper examHelper;

    public NodeQueryService(
        NodeRepository repo,
        NodeMetafieldHelper metafieldHelper,
        NodeMapper nodeMapper,
        RuleRepository ruleRepo,
        RuleMapper ruleMapper,
        NodeInferenceEngine nodeInferenceEngine,
        EmployeeCache employeeCache,
        CourseRepository courseRepository,
        CourseHelper courseHelper,
        CourseEmployeeRepository courseEmployeeRepository,
        ThumbHelper thumbHelper,
        DocumentEmployeeRepository documentEmployeeRepository,
        ExamRepository examRepository,
        ExamEmployeeRepository examEmployeeRepository,
        ExamHelper examHelper
    ) {
        this.repo = repo;
        this.metafieldHelper = metafieldHelper;
        this.nodeMapper = nodeMapper;
        this.ruleRepo = ruleRepo;
        this.ruleMapper = ruleMapper;
        this.nodeInferenceEngine = nodeInferenceEngine;
        this.employeeCache = employeeCache;
        this.courseRepository = courseRepository;
        this.courseHelper = courseHelper;
        this.courseEmployeeRepository = courseEmployeeRepository;
        this.thumbHelper = thumbHelper;
        this.documentEmployeeRepository = documentEmployeeRepository;
        this.examRepository = examRepository;
        this.examEmployeeRepository = examEmployeeRepository;
        this.examHelper = examHelper;
    }

    @Transactional(readOnly = true)
    public List<NodeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Nodes");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        List<NodeEntity> entities = repo.findAll();
        List<NodeDTO> dtos = nodeMapper.toDto(entities);
        metafieldHelper.enrichMetafieldNode(dtos);
        thumbHelper.enrichThumb(dtos, NodeDTO::getThumbUrl, NodeDTO::setErThumbUrl);
        return dtos;
    }

    @Transactional(readOnly = true)
    public NodeDTO findOne(Long id) {
        log.debug("Request to get Node : {}", id);
        Optional<NodeDTO> dtoOptional = repo.findById(id).map(nodeMapper::toDto);
        if (dtoOptional.isEmpty()) {
            return null;
        }
        NodeDTO dto = dtoOptional.get();
        metafieldHelper.enrichMetafieldNode(List.of(dto));
        if (dto.getType().equals("ISLAND")) {
            enrichCourse(List.of(dto), false);
        }
        if (dto.getType().equals("CREEP")) {
            enrichExam(List.of(dto), false);
        }
        enrichRule(List.of(dto));
        thumbHelper.enrichThumb(List.of(dto), NodeDTO::getThumbUrl, NodeDTO::setErThumbUrl);
        return dto;
    }

    private void enrichRule(List<NodeDTO> dtos) {
        List<Long> nodeIds = dtos.stream().map(NodeDTO::getId).collect(Collectors.toList());
        List<String> ruleNamespaces = Arrays.asList(
            RuleNamespace.NODE_TIME.name(),
            RuleNamespace.NODE_EMPLOYEE.name()
        );
        List<RuleEntity> nodeRules = ruleRepo.findByNamespaceInAndRootIdIn(ruleNamespaces, nodeIds);
        List<RuleDTO> nodeRuleDtos = nodeRules.stream().map(ruleMapper::toDto).collect(Collectors.toList());

        Map<Long, List<RuleDTO>> nodeRuleDtoMap = nodeRuleDtos.stream().collect(Collectors.groupingBy(RuleDTO::getRootId));

        for (NodeDTO dto : dtos) {
            List<RuleDTO> rulesForNode = nodeRuleDtoMap.getOrDefault(dto.getId(), Collections.emptyList());
            dto.setRules(rulesForNode);
        }
    }

    private void enrichCourse(List<NodeDTO> dtos, Boolean isSimple) {
        // Collect node IDs from NodeDTOs
        List<Long> nodeIds = dtos.stream().map(NodeDTO::getId).collect(Collectors.toList());

        // Fetch courses and map to DTOs
        List<CourseEntity> courses = courseRepository.findAllByNodeIdIn(nodeIds);
        List<CourseDTO> courseDtos = courses.stream().map(CourseMapper.INSTANCE::toDto).collect(Collectors.toList());

        // Collect course root IDs (IDs of courses)
        List<Long> rootIds = courseDtos.stream().map(CourseDTO::getId).collect(Collectors.toList());

        // Fetch course employee entities
        String userCode = Helper.getUserCodeUpperCase();
        List<CourseEmployeeEntity> courseEmployeeEntities = courseEmployeeRepository.findAllByCodeAndRootIdIn(userCode, rootIds);

        // Map course root ID (course ID) to course employee status
        Map<Long, CourseEmployeeStatusEnum> courseEmployeeStatusMap = courseEmployeeEntities
            .stream()
            .collect(Collectors.toMap(CourseEmployeeEntity::getRootId, CourseEmployeeEntity::getStatus));

        // Map Node ID to CourseDTO
        Map<Long, CourseDTO> courseDtoMap = courseDtos.stream().collect(Collectors.toMap(CourseDTO::getNodeId, Function.identity()));

        // Enrich with documents if not in simple mode
        if (!isSimple) {
            courseHelper.enrichDocument(courseDtos);
            List<DocumentDTO> documents = courseDtos
                .stream()
                .flatMap(courseDto -> courseDto.getDocuments().stream())
                .collect(Collectors.toList());
            thumbHelper.enrichThumb(documents, DocumentDTO::getContent, DocumentDTO::setErContent);
            List<Long> documentIds = documents.stream().map(DocumentDTO::getId).collect(Collectors.toList());

            List<DocumentEmployeeEntity> documentEmployeeEntities = documentEmployeeRepository.findAllByCodeAndRootIdIn(
                userCode,
                documentIds
            );

            Map<Long, DocumentEmployeeStatusEnum> documentStatusMap = documentEmployeeEntities
                .stream()
                .collect(Collectors.toMap(DocumentEmployeeEntity::getRootId, DocumentEmployeeEntity::getStatus));

            documents.forEach(document -> {
                DocumentEmployeeStatusEnum status = documentStatusMap.get(document.getId());
                if (status != null) {
                    document.setStatus(status);
                }
            });
        }

        // Update NodeDTO with corresponding CourseDTO and status
        for (NodeDTO dto : dtos) {
            CourseDTO courseDto = courseDtoMap.get(dto.getId());
            if (courseDto != null) {
                // Set course DTO in NodeDTO
                dto.setCourse(courseDto);

                // Set status from course employee
                CourseEmployeeStatusEnum status = courseEmployeeStatusMap.get(courseDto.getId());
                if (status != null) {
                    courseDto.setStatus(status);
                } else {
                    courseDto.setStatus(CourseEmployeeStatusEnum.NOT_ATTENDED);
                }
            }
        }
    }

    private void enrichExam(List<NodeDTO> dtos, Boolean isSimple) {
        // Collect node IDs from NodeDTOs
        List<Long> nodeIds = dtos.stream().map(NodeDTO::getId).collect(Collectors.toList());

        // Fetch exams and map to DTOs
        List<ExamEntity> exams = examRepository.findAllByNodeIdIn(nodeIds);
        List<ExamDTO> examDtos = exams.stream().map(ExamMapper.INSTANCE::toDto).collect(Collectors.toList());

        // Collect exam IDs (root IDs)
        List<Long> rootIds = examDtos.stream().map(ExamDTO::getId).collect(Collectors.toList());

        // Fetch exam employee entities
        String userCode = Helper.getUserCodeUpperCase();
        List<ExamEmployeeEntity> examEmployeeEntities = examEmployeeRepository.findAllByCodeAndRootIdIn(userCode, rootIds);

        // Map exam root ID (exam ID) to exam employee status
        Map<Long, ExamEmployeeStatusEnum> examEmployeeStatusMap = examEmployeeEntities
            .stream()
            .collect(Collectors.toMap(ExamEmployeeEntity::getRootId, ExamEmployeeEntity::getStatus));

        // Map Node ID to ExamDTO
        Map<Long, ExamDTO> examDtoMap = examDtos.stream()
            .collect(Collectors.toMap(
                ExamDTO::getNodeId,
                Function.identity(),
                (existing, replacement) -> existing
            ));

        if (!isSimple) {
            examHelper.enrichDocument(examDtos);
            examHelper.enrichMetafields(examDtos);
            examHelper.enrichRules(examDtos);
            examHelper.enrichQuizzPools(examDtos);
        }

        // Update NodeDTO with corresponding ExamDTO and status
        for (NodeDTO dto : dtos) {
            ExamDTO examDto = examDtoMap.get(dto.getId());
            if (examDto != null) {
                // Set exam DTO in NodeDTO
                dto.setExam(examDto);

                // Set status from exam employee
                ExamEmployeeStatusEnum status = examEmployeeStatusMap.get(examDto.getId());
                if (status != null) {
                    examDto.setStatus(status);
                } else {
                    examDto.setStatus(ExamEmployeeStatusEnum.NOT_ATTENDED);
                }
            }
        }
    }

    private void validateNode(List<NodeDTO> dtos) {
        String userCode = Helper.getUserCodeUpperCase();
        String role = employeeCache.getRoleByCode(userCode);
        String department = employeeCache.getDepartmentByCode(userCode);
        NodeDetail nodeDetail = new NodeDetail()
            .code(userCode)
            .role(role)
            .department(department)
            .startTime(System.currentTimeMillis())
            .endTime(System.currentTimeMillis());

        List<Long> ids = dtos.stream().map(NodeDTO::getId).collect(Collectors.toList());
        List<RuleEntity> rules = ruleRepo.findByNamespaceAndRootIdIn(String.valueOf(RuleNamespace.NODE_EMPLOYEE), ids);
        List<RuleDTO> ruleDtos = ruleMapper.toDtos(rules);
        Map<Long, List<RuleDTO>> rulesByNodeId = ruleDtos.stream().collect(Collectors.groupingBy(RuleDTO::getRootId));

        List<RuleEntity> timeRules = ruleRepo.findByNamespaceAndRootIdIn(
            String.valueOf(RuleNamespace.NODE_TIME), ids
        );
        List<RuleDTO> timeRuleDtos = ruleMapper.toDtos(timeRules);
        Map<Long, List<RuleDTO>> timeRulesByNodeId = timeRuleDtos.stream()
            .collect(Collectors.groupingBy(RuleDTO::getRootId));

        for (NodeDTO dto : dtos) {
            List<RuleDTO> nodeRules = rulesByNodeId.get(dto.getId());
            if (nodeRules != null) {
                NodeValidDetail detail = nodeInferenceEngine.run(nodeRules, nodeDetail);
                if (Objects.isNull(detail)) {
                    dto.setValidEmployee(false);
                }
            }

            List<RuleDTO> timeSpecificRules = timeRulesByNodeId.get(dto.getId());
            if (timeSpecificRules != null) {
                NodeValidDetail timeValidation = nodeInferenceEngine.run(timeSpecificRules, nodeDetail);
                if (Objects.isNull(timeValidation)) {
                    dto.setValidTime(false);
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<NodeDTO> find(SearchNodeCriteria criteria) {
        log.debug("Request to get Nodes criteria : {}", criteria);
        if (Objects.isNull(criteria.getRootId()) && Objects.isNull(criteria.getType())) {
            throw new NtsValidationException("message", "Id node gốc và loại node đều trống");
        }
        NodeSpecification specification = new NodeSpecification()
            .rootId(criteria.getRootId())
            .type(criteria.getType())
            .sortType(criteria.getSortType())
            .sortColumn(criteria.getSortColumn());

        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getLimit(), Sort.by(Sort.Order.desc("createdAt")));

        Page<NodeEntity> entities = repo.findAll(specification, pageable);
        List<NodeEntity> contents = entities.getContent();
        List<NodeDTO> dtos = nodeMapper.toDto(contents);
        validateNode(dtos);
        metafieldHelper.enrichMetafieldNode(dtos);

        List<NodeDTO> islands = dtos.stream().filter(dto -> "ISLAND".equals(dto.getType())).collect(Collectors.toList());
        enrichCourse(islands, true);

        List<NodeDTO> creeps = dtos.stream().filter(dto -> "CREEP".equals(dto.getType())).collect(Collectors.toList());
        enrichExam(creeps, true);

        thumbHelper.enrichThumb(dtos, NodeDTO::getThumbUrl, NodeDTO::setErThumbUrl);
        return new PageImpl<>(dtos, pageable, entities.getTotalElements());
    }
}
