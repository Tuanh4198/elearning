package io.yody.yodemy.elearning.service;

import io.yody.yodemy.domain.MetafieldEntity;
import io.yody.yodemy.elearning.domain.*;
import io.yody.yodemy.elearning.domain.enumeration.ExamEmployeeMetafieldEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamQuizzPoolEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamQuizzPoolStrategyEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamStrategyEnum;
import io.yody.yodemy.elearning.repository.*;
import io.yody.yodemy.elearning.service.business.DocumentBO;
import io.yody.yodemy.elearning.service.business.ExamBO;
import io.yody.yodemy.elearning.service.cache.EmployeeCache;
import io.yody.yodemy.elearning.service.dto.ExamDTO;
import io.yody.yodemy.elearning.service.dto.ExamQuizzPoolDTO;
import io.yody.yodemy.elearning.service.dto.MetafieldDTO;
import io.yody.yodemy.elearning.service.helpers.ExamHelper;
import io.yody.yodemy.elearning.service.helpers.ExamTransactionManager;
import io.yody.yodemy.elearning.service.helpers.PegasusHelper;
import io.yody.yodemy.elearning.service.mapper.DocumentMapper;
import io.yody.yodemy.elearning.service.mapper.ExamMapper;
import io.yody.yodemy.elearning.web.rest.vm.request.DocumentRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.ExamRequest;
import io.yody.yodemy.elearning.web.rest.vm.response.EmployeeDTO;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleEntity;
import io.yody.yodemy.rule.knowledgeBase.domain.RuleNamespace;
import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;
import io.yody.yodemy.rule.knowledgeBase.mapper.RuleMapper;
import io.yody.yodemy.rule.knowledgeBase.repository.RuleRepository;
import io.yody.yodemy.rule.ruleImpl.nodeRuleEngine.NodeDetail;
import io.yody.yodemy.rule.ruleImpl.nodeRuleEngine.NodeInferenceEngine;
import io.yody.yodemy.rule.ruleImpl.nodeRuleEngine.NodeValidDetail;
import org.apache.commons.lang3.ObjectUtils;
import org.nentangso.core.service.errors.NtsValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static io.yody.yodemy.elearning.domain.constant.MetafieldConstant.EXAM_EMPLOYEE_OWNER_RESOURCE;

/**
 * Service Implementation for managing {@link ExamEntity}.
 */
@Service
@Transactional
public class ExamService {

    private static final String ENTITY_NAME = "exam";
    private static final int IMPORT_START_ROW = 1;
    private final Logger log = LoggerFactory.getLogger(ExamService.class);
    private final ExamRepository examRepository;
    private final ExamTransactionManager transactionManager;
    private final MetafieldRepository metafieldRepository;
    private final ExamEmployeeRepository examEmployeeRepository;
    private final QuizzRepository quizzRepository;
    private final QuizzCategoryRepository quizzCategoryRepository;
    private final ExamHelper examHelper;
    private final CategoryRepository categoryRepository;
    private final PegasusHelper pegasusHelper;
    private final EmployeeCache employeeCache;
    private final RuleRepository ruleRepository;
    private final NodeInferenceEngine nodeInferenceEngine;
    private final DocumentRepository documentRepository;

    public ExamService(
        ExamRepository examRepository,
        ExamTransactionManager transactionManager,
        MetafieldRepository metafieldRepository,
        ExamQuizzPoolRepository examQuizzPoolRepository,
        ExamEmployeeRepository examEmployeeRepository,
        QuizzRepository quizzRepository,
        QuizzCategoryRepository quizzCategoryRepository,
        ExamHelper examHelper,
        CategoryRepository categoryRepository,
        PegasusHelper pegasusHelper,
        EmployeeCache employeeCache,
        RuleRepository ruleRepository,
        NodeInferenceEngine nodeInferenceEngine,
        DocumentRepository documentRepository
    ) {
        this.examRepository = examRepository;
        this.transactionManager = transactionManager;
        this.metafieldRepository = metafieldRepository;
        this.examEmployeeRepository = examEmployeeRepository;
        this.quizzRepository = quizzRepository;
        this.quizzCategoryRepository = quizzCategoryRepository;
        this.examHelper = examHelper;
        this.categoryRepository = categoryRepository;
        this.pegasusHelper = pegasusHelper;
        this.employeeCache = employeeCache;
        this.ruleRepository = ruleRepository;
        this.nodeInferenceEngine = nodeInferenceEngine;
        this.documentRepository = documentRepository;
    }

    /**
     * Save a exam.
     *
     * @param examDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public ExamDTO save(ExamRequest examRequest) {
        log.debug("Request to save Exam : {}", examRequest);
        ExamBO examBO = ExamMapper.INSTANCE.requestToBo(examRequest);
        examBO.validate();
        ExamDTO examDTO = transactionManager.save(examBO);
        assign(examDTO.getId());

        return examDTO;
    }

    private List<DocumentBO> getNonExistItems(ExamRequest request, ExamBO bo) {
        if (Objects.isNull(request.getDocuments()) || Objects.isNull(bo.getDocuments())) {
            return new ArrayList();
        }
        Set<Long> requestIds = request.getDocuments().stream()
            .filter(item -> item.getId() != null)
            .map(DocumentRequest::getId)
            .collect(Collectors.toSet());

        List<DocumentBO> filteredBOItems = bo.getDocuments().stream()
            .filter(item -> item.getId() == null || !requestIds.contains(item.getId()))
            .collect(Collectors.toList());

        return filteredBOItems;
    }
    /**
     * Update a exam.
     *
     * @param examDTO the entity to save.
     * @return the persisted en
     * tity.
     */
    @Transactional
    public ExamDTO update(ExamRequest request) {
        log.debug("Request to update Exam : {}", request);
        ExamBO examBO = transactionManager.findByExamId(request.getId());
        List<DocumentBO> orphanDocuments = getNonExistItems(request, examBO);
        List<DocumentEntity> orphanEntities = DocumentMapper.INSTANCE.bosToEntities(orphanDocuments);
        orphanEntities.forEach(document -> {
            document.setDeleted(true);
        });
        documentRepository.saveAll(orphanEntities);

        examBO.update(request);
        ExamDTO examDTO = transactionManager.save(examBO);
        assign(examDTO.getId());

        return examDTO;
    }

    public ExamEntity findExamById(Long examId) {
        Optional<ExamEntity> optionalExamEntity = examRepository.findById(examId);
        return optionalExamEntity.orElseThrow(() -> new EntityNotFoundException("Exam not found with id: " + examId));
    }

    private List<QuizzEntity> fetchQuizzPool(ExamDTO examDTO) {
        ExamQuizzPoolStrategyEnum poolStrategy = examDTO.getPoolStrategy();
        List<ExamQuizzPoolDTO> quizzPools = examDTO.getQuizzPools();
        List<Long> sourceIds = quizzPools.stream().map(ExamQuizzPoolDTO::getSourceId).collect(Collectors.toList());

        List<QuizzEntity> allQuizzs = new ArrayList<>();
        switch (poolStrategy) {
            case WEIGHT:
                List<QuizzCategoryEntity> quizzCategories = quizzCategoryRepository.findAllByIdIn(sourceIds);
                List<Long> quizzCategoryIds = quizzCategories.stream().map(QuizzCategoryEntity::getId).collect(Collectors.toList());
                allQuizzs = quizzRepository.findAllByCategoryIdIn(quizzCategoryIds);
                break;
            case MANUAL:
                allQuizzs = quizzRepository.findAllByIdIn(sourceIds);
                break;
        }
        return allQuizzs;
    }

    private List<QuizzEntity> fetchQuizzs(ExamDTO examDTO, List<QuizzEntity> allQuizzs) {
        Long numberOfQuestion = examDTO.getNumberOfQuestion();
        if (numberOfQuestion > allQuizzs.size()) {
            throw new NtsValidationException("message", "Tổng số câu hỏi đã chọn nhỏ hơn số lượng câu hỏi trong đề bài");
        }

        ExamQuizzPoolStrategyEnum poolStrategy = examDTO.getPoolStrategy();
        List<ExamQuizzPoolDTO> quizzPools = examDTO.getQuizzPools();

        List<QuizzEntity> quizzs = new ArrayList<>();

        switch (poolStrategy) {
            case WEIGHT:
                /**
                 * lấy tỉ trọng của từng danh mục
                 */
                Map<Long, Long> weightMap = new HashMap<>();
                for (ExamQuizzPoolDTO quizzPool : quizzPools) {
                    Long sourceId = quizzPool.getSourceId();
                    List<MetafieldDTO> quizzPoolMetafields = quizzPool.getMetafields();
                    Long weight = Long.valueOf(
                        quizzPoolMetafields
                            .stream()
                            .filter(quizzPoolMetafield -> quizzPoolMetafield.getKey().equals(ExamQuizzPoolEnum.WEIGHT.getKey()))
                            .findFirst()
                            .map(MetafieldDTO::getValue)
                            .orElse(null)
                    );
                    if (!Objects.isNull(weight)) {
                        weightMap.put(sourceId, weight);
                    }
                }

                /**
                 * validate tổng số tỉ trọng
                 */
                long totalWeight = weightMap.values().stream().mapToLong(Long::longValue).sum();

                if (totalWeight > 100) {
                    throw new NtsValidationException("message", "Tổng tỉ trọng danh mục vượt quá 100%");
                }

                /**
                 * lấy danh sách câu hỏi theo danh mục
                 */
                Map<Long, List<QuizzEntity>> categoryQuizzes = allQuizzs
                    .stream()
                    .collect(Collectors.groupingBy(QuizzEntity::getCategoryId));

                /**
                 * sinh câu hỏi cho từng danh mục
                 */
                List<QuizzEntity> selectedQuestions = new ArrayList<>();
                List<QuizzEntity> remainingQuestions = new ArrayList<>(allQuizzs);

                Random random = new Random();
                for (Map.Entry<Long, Long> entry : weightMap.entrySet()) {
                    Long categoryId = entry.getKey();
                    Long weight = entry.getValue();

                    int numberOfCategoryQuizzs = (int) ((weight / 100.0) * numberOfQuestion);
                    List<QuizzEntity> categoryQuestionList = categoryQuizzes.get(categoryId);

                    if (categoryQuestionList == null || categoryQuestionList.size() < numberOfCategoryQuizzs) {
                        throw new NtsValidationException("message", "Không đảm bảo tỉ trọng trên đề bài của từng danh mục.");
                    }

                    Collections.shuffle(categoryQuestionList, random);
                    List<QuizzEntity> selectedFromCategory = categoryQuestionList
                        .stream()
                        .limit(numberOfCategoryQuizzs)
                        .collect(Collectors.toList());
                    selectedQuestions.addAll(selectedFromCategory);
                    remainingQuestions.removeAll(selectedFromCategory);
                }

                while (selectedQuestions.size() < numberOfQuestion) {
                    if (remainingQuestions.isEmpty()) {
                        throw new NtsValidationException("message", "Tổng số câu hỏi nhỏ hơn số lượng câu hỏi trong đề bài.");
                    }
                    selectedQuestions.add(remainingQuestions.remove(0));
                }

                quizzs = selectedQuestions;
                break;
            case MANUAL:
                if (numberOfQuestion < allQuizzs.size()) {
                    throw new NtsValidationException("message", "Tổng số câu hỏi đã chọn lớn hơn số lượng câu hỏi trong đề bài");
                }
                quizzs = allQuizzs;
                break;
            default:
                break;
        }
        return quizzs;
    }

    private List<EmployeeDTO> getListUser(Long nodeId) {
        List<RuleEntity> rules = ruleRepository.findByNamespaceAndRootId(String.valueOf(RuleNamespace.NODE_EMPLOYEE), nodeId);

        if (ObjectUtils.isEmpty(rules)) {
            return new ArrayList();
        }
        List<RuleDTO> ruleDTOS = RuleMapper.INSTANCE.toDtos(rules);


        List<EmployeeDTO> validEmployees = new ArrayList();
        List<EmployeeDTO> allEmployees = employeeCache.getAllEmployeesCache();

        allEmployees.forEach(employee -> {
            String code = employee.getCode();
            String role = employeeCache.getRoleByCode(code);
            String department = employeeCache.getDepartmentByCode(code);
            NodeDetail nodeDetail = new NodeDetail()
                .code(code)
                .role(role)
                .department(department);
            NodeValidDetail detail = nodeInferenceEngine.run(ruleDTOS, nodeDetail);
            if (!Objects.isNull(detail)) {
                validEmployees.add(employee);
            }
        });
        return validEmployees;
    }

    public void generateEmployeeExamQuizzs(ExamDTO examDTO, List<ExamEmployeeEntity> examEmployeeToCreate) {
        int batchSize = 100;
        boolean isRandomize = examDTO.getExamStrategy().equals(ExamStrategyEnum.RANDOM);
        List<QuizzEntity> allQuizzs = fetchQuizzPool(examDTO);
        List<QuizzEntity> quizzs = fetchQuizzs(examDTO, allQuizzs);
        List<MetafieldEntity> quizzMetafields = new ArrayList<>();
        for (ExamEmployeeEntity examEmployee : examEmployeeToCreate) {
            if (isRandomize) quizzs = fetchQuizzs(examDTO, allQuizzs);
            for (QuizzEntity quizz : quizzs) {
                MetafieldEntity metafield = new MetafieldEntity()
                    .ownerResource(EXAM_EMPLOYEE_OWNER_RESOURCE)
                    .namespace(EXAM_EMPLOYEE_OWNER_RESOURCE)
                    .type(EXAM_EMPLOYEE_OWNER_RESOURCE)
                    .key(ExamEmployeeMetafieldEnum.QUIZZ_ID.getKey())
                    .value(String.valueOf(quizz.getId()))
                    .ownerId(examEmployee.getId());
                quizzMetafields.add(metafield);
            }
        }

        int totalMetafields = quizzMetafields.size();

        for (int i = 0; i < totalMetafields; i += batchSize) {
            int end = Math.min(i + batchSize, totalMetafields);
            List<MetafieldEntity> batch = quizzMetafields.subList(i, end);
            metafieldRepository.saveAll(batch);
        }
    }
    public void assign(Long examId) {
        log.debug("Request to assign Exam : {}", examId);
        if (Objects.isNull(examId)) return;
        ExamEntity examEntity = findExamById(examId);
        ExamDTO examDTO = ExamMapper.INSTANCE.toDto(examEntity);
        enrich(List.of(examDTO));

        List<EmployeeDTO> employees = getListUser(examEntity.getNodeId());
        List<String> employeeCodes = employees.stream().map(EmployeeDTO::getCode).collect(Collectors.toList());

        List<ExamEmployeeEntity> examEmployeeEntities = examEmployeeRepository.findAllByRootIdAndCodeIn(examId, employeeCodes);

        List<String> existingCodes = examEmployeeEntities.stream().map(ExamEmployeeEntity::getCode).collect(Collectors.toList());
        List<EmployeeDTO> employeeToCreate = new ArrayList<>();
        for (EmployeeDTO employee : employees) {
            String employeeCode = employee.getCode();
            if (!existingCodes.contains(employeeCode)) {
                employeeToCreate.add(employee);
            }
        }

        /**
         * Lưu bài thi cho từng user
         */
        List<ExamEmployeeEntity> examEmployeeToCreate = new ArrayList<>();

        for (EmployeeDTO employee : employeeToCreate) {
            examEmployeeToCreate.add(new ExamEmployeeEntity().code(employee.getCode()).name(employee.getDisplayName()).rootId(examId));
        }

        int batchSize = 100;
        int totalEntities = examEmployeeToCreate.size();

        for (int i = 0; i < totalEntities; i += batchSize) {
            int end = Math.min(i + batchSize, totalEntities);
            List<ExamEmployeeEntity> batch = examEmployeeToCreate.subList(i, end);
            examEmployeeRepository.saveAll(batch);
        }
        /**
         * Sinh câu hỏi cho từng bài thi
         */
        generateEmployeeExamQuizzs(examDTO, examEmployeeToCreate);

        List<String> createdCodes = employeeToCreate.stream().map(EmployeeDTO::getCode).collect(Collectors.toList());
        pegasusHelper.sendNotifyExam(examEntity, createdCodes);
    }

    /**
     * Partially update a exam.
     *
     * @param examDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ExamDTO> partialUpdate(ExamDTO examDTO) {
        log.debug("Request to partially update Exam : {}", examDTO);

        return examRepository
            .findById(examDTO.getId())
            .map(existingExam -> {
                ExamMapper.INSTANCE.partialUpdate(existingExam, examDTO);

                return existingExam;
            })
            .map(examRepository::save)
            .map(ExamMapper.INSTANCE::toDto);
    }

    private void enrich(List<ExamDTO> examDTOS) {
        examHelper.enrichMetafields(examDTOS);
        examHelper.enrichQuizzPools(examDTOS);
    }

    /**
     * Delete the exam by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Exam : {}", id);
        Optional<ExamEntity> examOptional = examRepository.findById(id);
        if (examOptional.isPresent()) {
            ExamEntity entity = examOptional.get();
            entity.setDeleted(true);
            examRepository.save(entity);
            List<ExamEmployeeEntity> examEmployees = examEmployeeRepository.findAllByRootId(id);
            for (ExamEmployeeEntity examEmployee : examEmployees) {
                examEmployee.setDeleted(true);
            }
            examEmployeeRepository.saveAll(examEmployees);
        }
    }
}
