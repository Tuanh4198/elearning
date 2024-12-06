package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.CategoryEntity;
import io.yody.yodemy.elearning.domain.CourseEmployeeEntity;
import io.yody.yodemy.elearning.domain.CourseEntity;
import io.yody.yodemy.elearning.domain.DocumentEntity;
import io.yody.yodemy.elearning.domain.enumeration.CategoryTypeEnum;
import io.yody.yodemy.elearning.repository.CategoryRepository;
import io.yody.yodemy.elearning.repository.CourseEmployeeRepository;
import io.yody.yodemy.elearning.repository.CourseRepository;
import io.yody.yodemy.elearning.repository.DocumentRepository;
import io.yody.yodemy.elearning.service.business.CourseBO;
import io.yody.yodemy.elearning.service.business.DocumentBO;
import io.yody.yodemy.elearning.service.cache.EmployeeCache;
import io.yody.yodemy.elearning.service.dto.CourseDTO;
import io.yody.yodemy.elearning.service.helpers.CourseHelper;
import io.yody.yodemy.elearning.service.helpers.CourseTransactionManager;
import io.yody.yodemy.elearning.service.helpers.PegasusHelper;
import io.yody.yodemy.elearning.service.mapper.CourseMapper;
import io.yody.yodemy.elearning.service.mapper.DocumentMapper;
import io.yody.yodemy.elearning.web.rest.vm.request.CourseRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.DocumentRequest;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CourseEntity}.
 */
@Service
@Transactional
public class CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    private final CourseTransactionManager courseTransactionManager;

    private final CourseHelper courseHelper;

    private final CourseEmployeeRepository courseEmployeeRepository;
    private final CategoryRepository categoryRepository;
    private final PegasusHelper pegasusHelper;
    private final DocumentRepository documentRepository;
    private final RuleRepository ruleRepository;
    private final EmployeeCache employeeCache;
    private final NodeInferenceEngine nodeInferenceEngine;

    public CourseService(
        CourseRepository courseRepository,
        CourseMapper courseMapper,
        CourseTransactionManager courseTransactionManager,
        CourseHelper courseHelper,
        CourseEmployeeRepository courseEmployeeRepository,
        CategoryRepository categoryRepository,
        PegasusHelper pegasusHelper,
        DocumentRepository documentRepository,
        RuleRepository ruleRepository,
        EmployeeCache employeeCache,
        NodeInferenceEngine nodeInferenceEngine
    ) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.courseTransactionManager = courseTransactionManager;
        this.courseHelper = courseHelper;
        this.courseEmployeeRepository = courseEmployeeRepository;
        this.categoryRepository = categoryRepository;
        this.pegasusHelper = pegasusHelper;
        this.documentRepository = documentRepository;
        this.ruleRepository = ruleRepository;
        this.employeeCache = employeeCache;
        this.nodeInferenceEngine = nodeInferenceEngine;
    }

    /**
     * Save a course.
     *
     * @param courseDTO the entity to save.
     * @return the persisted entity.
     */

    @Transactional
    public CourseDTO save(CourseRequest courseRequest) {
        log.debug("Request to save Course : {}", courseRequest);
        validateRequest(courseRequest);
        CourseBO courseBO = courseMapper.requestToBo(courseRequest);
        courseBO.validate();
        CourseDTO result = courseTransactionManager.save(courseBO);
        assign(result.getId());
        return result;
    }

    public CourseEntity findCourseById(Long courseId) {
        Optional<CourseEntity> optionalCourseEntity = courseRepository.findById(courseId);
        return optionalCourseEntity.orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));
    }

    private void enrich(List<CourseDTO> courseDTOS) {
        courseHelper.enrichMetafields(courseDTOS);
        courseHelper.enrichCategory(courseDTOS);
        courseHelper.enrichDocument(courseDTOS);
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

    private List<String> getEmployeeCodes(CourseEntity course) {
        return new ArrayList();
    }

    @Transactional
    public void assign(Long courseId) {
        log.debug("Request to assign Exam : {}", courseId);
        if (Objects.isNull(courseId)) return;
        CourseEntity courseEntity = findCourseById(courseId);
        CourseDTO courseDTO = CourseMapper.INSTANCE.toDto(courseEntity);
        enrich(List.of(courseDTO));

        List<EmployeeDTO> employees = getListUser(courseEntity.getNodeId());
        List<String> employeeCodes = employees.stream().map(EmployeeDTO::getCode).collect(Collectors.toList());
        List<CourseEmployeeEntity> examEmployeeEntities = courseEmployeeRepository.findAllByRootIdAndCodeIn(courseId, employeeCodes);
        List<String> existingCodes = examEmployeeEntities.stream().map(CourseEmployeeEntity::getCode).collect(Collectors.toList());
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
        List<CourseEmployeeEntity> courseEmployeeEntities = new ArrayList<>();

        for (EmployeeDTO employee : employeeToCreate) {
            courseEmployeeEntities.add(
                new CourseEmployeeEntity().code(employee.getCode()).name(employee.getDisplayName()).rootId(courseId)
            );
        }

        int batchSize = 100;
        int totalEntities = courseEmployeeEntities.size();

        for (int i = 0; i < totalEntities; i += batchSize) {
            int end = Math.min(i + batchSize, totalEntities);
            List<CourseEmployeeEntity> batch = courseEmployeeEntities.subList(i, end);
            courseEmployeeRepository.saveAll(batch);
        }

        List<String> createdCodes = employeeToCreate.stream().map(EmployeeDTO::getCode).collect(Collectors.toList());
        pegasusHelper.sendNotifyTraining(courseEntity, createdCodes);
    }

    @Scheduled(cron = "1 * * * * *")
    public void remindCourse() {
        Instant now = Instant.now();
        Instant reminderStartTime = now;
        Instant reminderEndTime = now.plus(1, ChronoUnit.MINUTES);

        log.info("remind time between {} {}", reminderStartTime, reminderEndTime);
        List<CourseEntity> upcomingCourses = courseRepository.findByApplyTimeBetween(reminderStartTime, reminderEndTime);

        for (CourseEntity course : upcomingCourses) {
            log.info("upcoming training {}", course.getId());
            List<String> employeeCodes = getEmployeeCodes(course);
            pegasusHelper.sendNotifyTraining(course, employeeCodes);
        }
    }

    private List<DocumentBO> getNonExistItems(CourseRequest request, CourseBO bo) {
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
     * Update a course.
     *
     * @param courseDTO the entity to save.
     * @return the persisted entity.
     */
    public CourseDTO update(CourseRequest courseRequest) {
        log.debug("Request to update Course : {}", courseRequest);
        CourseBO courseBO = courseTransactionManager.findById(courseRequest.getId());
        CourseDTO courseDTO = CourseMapper.INSTANCE.boToDto(courseBO);
        enrich(List.of(courseDTO));
        courseBO = CourseMapper.INSTANCE.dtoToBo(courseDTO);
        List<DocumentBO> orphanDocuments = getNonExistItems(courseRequest, courseBO);
        List<DocumentEntity> orphanEntities = DocumentMapper.INSTANCE.bosToEntities(orphanDocuments);
        orphanEntities.forEach(document -> {
            document.setDeleted(true);
        });
        courseBO.update(courseRequest);
        courseBO.validate();

        documentRepository.saveAll(orphanEntities);

        CourseDTO result =  courseTransactionManager.save(courseBO);
        assign(result.getId());

        return result;
    }

    /**
     * Partially update a course.
     *
     * @param courseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CourseDTO> partialUpdate(CourseDTO courseDTO) {
        log.debug("Request to partially update Course : {}", courseDTO);

        return courseRepository
            .findById(courseDTO.getId())
            .map(existingCourse -> {
                courseMapper.partialUpdate(existingCourse, courseDTO);

                return existingCourse;
            })
            .map(courseRepository::save)
            .map(courseMapper::toDto);
    }

    /**
     * Get one course by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */

    /**
     * Delete the course by id.
     *
     * @param id the id of the entity.
     */
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Course : {}", id);
        Optional<CourseEntity> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            CourseEntity entity = courseOptional.get();
            entity.setDeleted(true);
            courseRepository.save(entity);
            List<CourseEmployeeEntity> courseEmployees = courseEmployeeRepository.findAllByRootId(id);
            for (CourseEmployeeEntity courseEmployee : courseEmployees) {
                courseEmployee.setDeleted(true);
            }
            courseEmployeeRepository.saveAll(courseEmployees);
        }
    }

    private void validateRequest(CourseRequest courseRequest) {
        ///validate if needed
        String title = courseRequest.getTitle().trim();
        Long categoryId = courseRequest.getCategoryId();

        if (title.isEmpty()) {
            throw new NtsValidationException("message", String.format("Tiêu đề khóa học không hợp lệ", "title"));
        }
        if (courseRequest.getId() != null) {
            List<CourseEntity> courses = courseRepository.findDifferentIdAndTitle(courseRequest.getId(), title, false);
            if (!courses.isEmpty()) {
                throw new NtsValidationException("message", String.format("Tiêu đề khóa học đã tồn tại", "title"));
            }
        } else {
            List<CourseEntity> cate = courseRepository.findByTitle(title, false);
            if (!cate.isEmpty()) {
                throw new NtsValidationException("message", String.format("Tiêu đề khóa học đã tồn tại", "title"));
            }
        }

        if (!Objects.isNull(categoryId)) {
            Optional<CategoryEntity> categoryEntityOptional = categoryRepository.findById(categoryId);
            if (categoryEntityOptional.isEmpty()) {
                throw new NtsValidationException("message", "Danh mục không tồn tại");
            } else {
                CategoryEntity category = categoryEntityOptional.get();
                if (!category.getType().equals(CategoryTypeEnum.COURSE)) {
                    throw new NtsValidationException("message", "Danh mục không hợp lệ");
                }
            }
        }
    }
}
