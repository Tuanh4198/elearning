package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.*;
import io.yody.yodemy.elearning.repository.*;
import io.yody.yodemy.elearning.service.criteria.ExamEmployeeCriteria;
import io.yody.yodemy.elearning.service.dto.*;
import io.yody.yodemy.elearning.service.helpers.ExamEmployeeHelper;
import io.yody.yodemy.elearning.service.helpers.ExamHelper;
import io.yody.yodemy.elearning.service.helpers.Helper;
import io.yody.yodemy.elearning.service.helpers.QuizzHelper;
import io.yody.yodemy.elearning.service.mapper.CategoryMapper;
import io.yody.yodemy.elearning.service.mapper.ExamEmployeeMapper;
import io.yody.yodemy.elearning.service.specification.ExamEmployeeSpecification;
import org.nentangso.core.service.errors.NtsValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ExamEmployeeQueryService extends QueryService<ExamEmployeeEntity> {

    private static final String ENTITY_NAME = "exam_employee";
    private final Logger log = LoggerFactory.getLogger(ExamEmployeeQueryService.class);
    private final ExamEmployeeHelper examEmployeeHelper;
    private final ExamHelper examHelper;
    private final QuizzHelper quizzHelper;
    private final ExamEmployeeResultRepository examEmployeeResultRepository;
    public ExamEmployeeRepository examEmployeeRepository;
    private final ExamRepository examRepository;
    private final CategoryRepository categoryRepository;
    private final NodeRepository nodeRepository;

    public ExamEmployeeQueryService(
        ExamEmployeeRepository examEmployeeRepository,
        ExamEmployeeHelper examEmployeeHelper,
        ExamHelper examHelper,
        QuizzHelper quizzHelper,
        ExamEmployeeResultRepository examEmployeeResultRepository,
        ExamRepository examRepository,
        CategoryRepository categoryRepository,
        NodeRepository nodeRepository
    ) {
        this.examEmployeeRepository = examEmployeeRepository;
        this.examEmployeeHelper = examEmployeeHelper;
        this.examHelper = examHelper;
        this.quizzHelper = quizzHelper;
        this.examEmployeeResultRepository = examEmployeeResultRepository;
        this.examRepository = examRepository;
        this.categoryRepository = categoryRepository;
        this.nodeRepository = nodeRepository;
    }

    private void enrich(List<ExamEmployeeDTO> examEmployeeDTOS) {
        examEmployeeHelper.enrichQuizzs(examEmployeeDTOS);
        examEmployeeHelper.enrichExam(examEmployeeDTOS);
    }

    private void enrichExam(List<ExamEmployeeDTO> examEmployeeDTOS) {
        List<ExamDTO> examDTOS = new ArrayList<>();
        for (ExamEmployeeDTO examEmployeeDTO : examEmployeeDTOS) {
            examDTOS.add(examEmployeeDTO.getExam());
        }

        examHelper.enrichCategory(examDTOS);
        examHelper.enrichMetafields(examDTOS);
        examHelper.enrichQuizzPools(examDTOS);
        examHelper.enrichDocument(examDTOS);
        examHelper.enrichRules(examDTOS);
    }

    private void enrichNodeId(List<ExamEmployeeDTO> examEmployeeDTOS) {
        List<Long> nodeIds = new ArrayList<>();
        for (ExamEmployeeDTO examEmployeeDTO : examEmployeeDTOS) {
            ExamDTO exam = examEmployeeDTO.getExam();
            if (!Objects.isNull(exam)) {
                nodeIds.add(exam.getNodeId());
            }
        }

        List<NodeEntity> nodes = nodeRepository.findByIdIn(nodeIds);
        Map<Long, Long> containerNodeIdMap = nodes.stream().collect(Collectors.toMap(NodeEntity::getId, NodeEntity::getRootId));

        List<Long> rootNodeIds = nodes.stream().map(NodeEntity::getRootId).collect(Collectors.toList());
        List<NodeEntity> rootNodes = nodeRepository.findByIdIn(rootNodeIds);
        Map<Long, Long> rootNodeIdMap = rootNodes.stream().collect(Collectors.toMap(NodeEntity::getId, NodeEntity::getRootId));

        for (ExamEmployeeDTO examEmployeeDTO : examEmployeeDTOS) {
            ExamDTO exam = examEmployeeDTO.getExam();
            if (!Objects.isNull(exam)) {
                Long nodeId = exam.getNodeId();
                examEmployeeDTO.setNodeId(nodeId);

                Long containerNodeId = containerNodeIdMap.get(nodeId);
                if (!Objects.isNull(containerNodeId)) {
                    examEmployeeDTO.setRootNodeId(rootNodeIdMap.get(containerNodeId));
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public Page<ExamEmployeeDTO> findAll(ExamEmployeeCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ExamEmployees");
        String userCode = Helper.getUserCodeUpperCase();
        ExamEmployeeSpecification specification = new ExamEmployeeSpecification()
            .code(userCode)
            .status(criteria.getStatus())
            .from(criteria.getFrom())
            .to(criteria.getTo())
            .title(criteria.getTitle())
            .categoryId(criteria.getCategoryId());
        Page<ExamEmployeeEntity> examEmployeeEntities = examEmployeeRepository.findAll(specification, pageable);
        List<ExamEmployeeEntity> entityList = examEmployeeEntities.getContent();
        List<ExamEmployeeDTO> dtoList = ExamEmployeeMapper.INSTANCE.toDto(entityList);
        enrich(dtoList);
        enrichExam(dtoList);
        enrichNodeId(dtoList);
        Page<ExamEmployeeDTO> examEmployeeDTOS = new PageImpl<>(dtoList, pageable, examEmployeeEntities.getTotalElements());
        return examEmployeeDTOS;
    }

    @Transactional(readOnly = true)
    public Page<ExamEmployeeDTO> findAdmin(ExamEmployeeCriteria criteria, Pageable pageable) {
        ExamEmployeeSpecification specification = new ExamEmployeeSpecification()
            .code(criteria.getSearch())
            .status(criteria.getStatus())
            .title(criteria.getTitle())
            .categoryId(criteria.getCategoryId())
            .rootId(criteria.getRootId());
        Page<ExamEmployeeEntity> examEmployeeEntities = examEmployeeRepository.findAll(specification, pageable);
        List<ExamEmployeeEntity> entityList = examEmployeeEntities.getContent();
        List<ExamEmployeeDTO> dtoList = ExamEmployeeMapper.INSTANCE.toDto(entityList);
        enrich(dtoList);
        enrichExam(dtoList);
        Page<ExamEmployeeDTO> examEmployeeDTOS = new PageImpl<>(dtoList, pageable, examEmployeeEntities.getTotalElements());
        return examEmployeeDTOS;
    }

    @Transactional(readOnly = true)
    public ExamEmployeeDTO findOne(Long examId) {
        log.debug("Request to get ExamEmployee : {}", examId);
        String userCode = Helper.getUserCodeUpperCase();
        List<ExamEmployeeEntity> examEmployees = examEmployeeRepository.findByCodeAndRootId(userCode, examId);
        ExamEmployeeEntity examEmployee = examEmployees.stream().findFirst().orElse(null);
        if (Objects.isNull(examEmployee)) {
            throw new NtsValidationException("message", "Chưa được gán bài kiểm tra");
        }
        ExamEmployeeDTO examEmployeeDTO = ExamEmployeeMapper.INSTANCE.toDto(examEmployee);

        enrich(List.of(examEmployeeDTO));
        enrichExam(List.of(examEmployeeDTO));

        List<QuizzDTO> quizzDTOS = examEmployeeDTO.getQuizzs();
        quizzHelper.enrichAnswers(quizzDTOS);
        quizzHelper.enrichMetafields(quizzDTOS);

        return examEmployeeDTO;
    }

    private ExamEmployeeEntity getExamEmployee(Long examId) {
        String userCode = Helper.getUserCodeUpperCase();
        List<ExamEmployeeEntity> examEmployees = examEmployeeRepository.findByCodeAndRootId(userCode, examId);
        ExamEmployeeEntity examEmployee = examEmployees.stream().findFirst().orElse(null);

        return examEmployee;
    }

    private Optional<ExamEmployeeResultEntity> findBestResult(List<ExamEmployeeResultEntity> resultEntities) {
        return resultEntities
            .stream()
            .sorted(
                Comparator
                    .comparing(ExamEmployeeResultEntity::isPass, Comparator.reverseOrder())
                    .thenComparingDouble(entity -> (double) entity.getNumberOfCorrect() * -1 / entity.getNumberOfQuestion()) // Higher ratio first
            )
            .findFirst();
    }

    public ExamEmployeeStatisticDTO getResult(Long examId) {
        ExamEmployeeEntity examEmployee = getExamEmployee(examId);
        Long examEmployeeId = examEmployee.getId();
        List<ExamEmployeeResultEntity> resultEntities = examEmployeeResultRepository.findAllByRootId(examEmployeeId);
        Optional<ExamEmployeeResultEntity> bestResultOptional = findBestResult(resultEntities);

        if (!bestResultOptional.isPresent()) {
            return new ExamEmployeeStatisticDTO();
        } else {
            ExamEmployeeResultEntity examEmployeeResultEntity = bestResultOptional.get();
            return new ExamEmployeeStatisticDTO()
                .status(examEmployee.getStatus())
                .numberOfTest(resultEntities.size() * 1L)
                .startAt(examEmployeeResultEntity.getStartAt())
                .finishedAt(examEmployeeResultEntity.getFinishedAt())
                .numberOfCorrect(examEmployeeResultEntity.getNumberOfCorrect())
                .numberOfQuestion(examEmployeeResultEntity.getNumberOfQuestion());
        }
    }

    public List<CategoryDTO> getCategories() {
        String userCode = Helper.getUserCodeUpperCase();
        List<ExamEmployeeEntity> examEmployeeEntities = examEmployeeRepository.findAllByCode(userCode);
        List<Long> rootIds = examEmployeeEntities.stream().map(ExamEmployeeEntity::getRootId).collect(Collectors.toList());
        List<ExamEntity> exams = examRepository.findAllByIdIn(rootIds);
        List<Long> categoryIds = exams.stream().map(ExamEntity::getCategoryId).collect(Collectors.toList());

        List<CategoryEntity> categories = categoryRepository.findAllByIdIn(categoryIds);

        return CategoryMapper.INSTANCE.toDto(categories);
    }
}
