package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.*;
import io.yody.yodemy.elearning.domain.enumeration.CourseEmployeeMetafieldEnum;
import io.yody.yodemy.elearning.domain.enumeration.CourseEmployeeStatusEnum;
import io.yody.yodemy.elearning.domain.enumeration.DocumentEmployeeStatusEnum;
import io.yody.yodemy.elearning.repository.*;
import io.yody.yodemy.elearning.service.business.MetafieldBO;
import io.yody.yodemy.elearning.service.cache.EmployeeCache;
import io.yody.yodemy.elearning.service.criteria.CourseEmployeeCriteria;
import io.yody.yodemy.elearning.service.dto.CategoryDTO;
import io.yody.yodemy.elearning.service.dto.CourseEmployeeDTO;
import io.yody.yodemy.elearning.service.helpers.CourseEmployeeHelper;
import io.yody.yodemy.elearning.service.helpers.CourseEmployeeTransactionManager;
import io.yody.yodemy.elearning.service.helpers.Helper;
import io.yody.yodemy.elearning.service.mapper.CategoryMapper;
import io.yody.yodemy.elearning.service.mapper.CourseEmployeeMapper;
import io.yody.yodemy.elearning.service.specification.CourseEmployeeSpecification;
import io.yody.yodemy.elearning.web.rest.vm.request.LearnCourseRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.nentangso.core.service.errors.NtsValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CourseEmployeeEntity}.
 */
@Service
@Transactional
public class CourseEmployeeService extends QueryService<CourseEmployeeEntity> {

    private final Logger log = LoggerFactory.getLogger(CourseEmployeeService.class);

    private final CourseEmployeeRepository courseEmployeeRepository;

    private final CourseEmployeeMapper courseEmployeeMapper;

    private final CourseEmployeeHelper courseEmployeeHelper;

    private final CourseEmployeeTransactionManager courseEmployeeTransactionManager;
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final EmployeeCache employeeCache;
    private final DocumentRepository documentRepository;
    private final DocumentEmployeeRepository documentEmployeeRepository;

    public CourseEmployeeService(
        CourseEmployeeRepository courseEmployeeRepository,
        CourseEmployeeMapper courseEmployeeMapper,
        CourseEmployeeHelper courseEmployeeHelper,
        CourseEmployeeTransactionManager courseEmployeeTransactionManager,
        CourseRepository courseRepository,
        CategoryRepository categoryRepository,
        EmployeeCache employeeCache,
        DocumentEmployeeRepository documentEmployeeRepository,
        DocumentRepository documentRepository
    ) {
        this.courseEmployeeRepository = courseEmployeeRepository;
        this.courseEmployeeMapper = courseEmployeeMapper;
        this.courseEmployeeHelper = courseEmployeeHelper;
        this.courseEmployeeTransactionManager = courseEmployeeTransactionManager;
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.employeeCache = employeeCache;
        this.documentEmployeeRepository = documentEmployeeRepository;
        this.documentRepository = documentRepository;
    }

    /**
     * Save a courseEmployee.
     *
     * @param courseEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    public CourseEmployeeDTO save(CourseEmployeeDTO courseEmployeeDTO) {
        log.debug("Request to save CourseEmployee : {}", courseEmployeeDTO);
        CourseEmployeeEntity courseEmployeeEntity = courseEmployeeMapper.toEntity(courseEmployeeDTO);
        courseEmployeeEntity = courseEmployeeRepository.save(courseEmployeeEntity);
        return courseEmployeeMapper.toDto(courseEmployeeEntity);
    }

    /**
     * Update a courseEmployee.
     *
     * @param courseEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    public CourseEmployeeDTO update(CourseEmployeeDTO courseEmployeeDTO) {
        log.debug("Request to update CourseEmployee : {}", courseEmployeeDTO);
        CourseEmployeeEntity courseEmployeeEntity = courseEmployeeMapper.toEntity(courseEmployeeDTO);
        courseEmployeeEntity = courseEmployeeRepository.save(courseEmployeeEntity);
        return courseEmployeeMapper.toDto(courseEmployeeEntity);
    }

    /**
     * Update a courseEmployee.
     *
     * @param courseEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    public void attend(Long id) {
        log.debug("Request to update CourseEmployee : {}", id);
        String userCode = Helper.getUserCodeUpperCase();

        List<CourseEmployeeEntity> courseEmployeeEntities = courseEmployeeRepository.findByCodeAndRootId(userCode, id);

        String name = employeeCache.getNameByCode(userCode);
        if (ObjectUtils.isEmpty(courseEmployeeEntities)) {
            CourseEmployeeEntity courseEmployee = new CourseEmployeeEntity()
                .code(userCode)
                .name(name)
                .rootId(id)
                .status(CourseEmployeeStatusEnum.ATTENDED);
            courseEmployeeEntities = List.of(courseEmployee);
        } else {
            for (CourseEmployeeEntity courseEmployeeEntity : courseEmployeeEntities) {
                courseEmployeeEntity.status(CourseEmployeeStatusEnum.ATTENDED);
            }
        }
        courseEmployeeRepository.saveAll(courseEmployeeEntities);
        MetafieldBO metafieldBO = new MetafieldBO(
            null,
            CourseEmployeeMetafieldEnum.ATTEND_AT.getKey(),
            String.valueOf(Instant.now()),
            null,
            null,
            null,
            null,
            null
        );
        CourseEmployeeEntity courseEmployeeEntity = courseEmployeeEntities.get(0);
        List<MetafieldBO> metafieldBOS = List.of(metafieldBO);
        courseEmployeeTransactionManager.saveLearnMetafields(metafieldBOS, courseEmployeeEntity.getId());
    }

    public void learn(Long courseId, Long documentId, LearnCourseRequest request) {
        log.debug("Request to update CourseEmployee : {}", courseId);

        String userCode = Helper.getUserCodeUpperCase();

        List<DocumentEmployeeEntity> documentEmployees = documentEmployeeRepository.findAllByCodeAndRootId(userCode, documentId);
        List<CourseEmployeeEntity> courseEmployeeEntities = courseEmployeeRepository.findByCodeAndRootId(userCode, courseId);
        CourseEmployeeEntity courseEmployeeEntity;

        String name = employeeCache.getNameByCode(userCode);

        if (ObjectUtils.isEmpty(courseEmployeeEntities)) {
            courseEmployeeEntity = new CourseEmployeeEntity()
                .code(userCode)
                .name(name)
                .rootId(courseId)
                .status(CourseEmployeeStatusEnum.ATTENDED);
        } else {
            courseEmployeeEntity = courseEmployeeEntities.get(0);
        }


        // Cập nhật trạng thái học cho tài liệu (documentId)
        DocumentEmployeeEntity documentEmployeeEntity = documentEmployees.stream()
            .filter(docEmp -> docEmp.getRootId().equals(documentId))
            .findFirst()
            .orElseGet(() -> {
                // Nếu tài liệu chưa tồn tại, khởi tạo mới
                DocumentEmployeeEntity newDocumentEmployee = new DocumentEmployeeEntity();
                newDocumentEmployee.setCode(userCode);
                newDocumentEmployee.setName(name);
                newDocumentEmployee.setRootId(documentId);
                newDocumentEmployee.setStatus(DocumentEmployeeStatusEnum.LEARNED);
                return newDocumentEmployee;
            });

        // Lưu thông tin thời gian học vào metafield
        Map<String, Instant> learnInfo = Map.of(
            CourseEmployeeMetafieldEnum.LEARN_START_AT.getKey(), request.getLearnStartAt(),
            CourseEmployeeMetafieldEnum.LEARN_FINISH_AT.getKey(), request.getLearnFinishAt()
        );

        MetafieldBO documentMetafieldBO = new MetafieldBO(
            null,
            CourseEmployeeMetafieldEnum.LEARN_INFO.getKey(),
            String.valueOf(learnInfo),
            null,
            null,
            null,
            null,
            null
        );
        // Lưu trạng thái của tài liệu
        documentEmployeeEntity.setStatus(DocumentEmployeeStatusEnum.LEARNED);
        documentEmployeeEntity = documentEmployeeRepository.save(documentEmployeeEntity);
        courseEmployeeTransactionManager.saveDocumentLearnMetafields(
            List.of(documentMetafieldBO), documentEmployeeEntity.getId()
        );

        List<DocumentEntity> documents = documentRepository.findAllByRootId(courseId);
        List<Long> allDocumentIds = documents.stream().map(DocumentEntity::getId).collect(Collectors.toList());
        List<DocumentEmployeeEntity> allDocumentEmployees = documentEmployeeRepository.findAllByCodeAndRootIdIn(userCode, allDocumentIds);

        // Kiểm tra nếu tất cả tài liệu đã được học
        boolean allDocumentsLearned = documents.stream()
            .allMatch(doc -> allDocumentEmployees.stream()
                .anyMatch(docEmp -> docEmp.getRootId().equals(doc.getId())
                    && docEmp.getStatus() == DocumentEmployeeStatusEnum.LEARNED)
            );

        // Nếu tất cả tài liệu được học, cập nhật trạng thái khóa học thành LEARNED
        if (allDocumentsLearned) {
            courseEmployeeEntity.setStatus(CourseEmployeeStatusEnum.LEARNED);
        }

        // Lưu trạng thái của khóa học
        courseEmployeeRepository.save(courseEmployeeEntity);
    }


    /**
     * Partially update a courseEmployee.
     *
     * @param courseEmployeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CourseEmployeeDTO> partialUpdate(CourseEmployeeDTO courseEmployeeDTO) {
        log.debug("Request to partially update CourseEmployee : {}", courseEmployeeDTO);

        return courseEmployeeRepository
            .findById(courseEmployeeDTO.getId())
            .map(existingCourseEmployee -> {
                courseEmployeeMapper.partialUpdate(existingCourseEmployee, courseEmployeeDTO);

                return existingCourseEmployee;
            })
            .map(courseEmployeeRepository::save)
            .map(courseEmployeeMapper::toDto);
    }

    /**
     * Get one courseEmployee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public CourseEmployeeDTO findOne(Long id) {
        //        log.debug("Request to get CourseEmployee : {}", id);
        List<CourseEmployeeEntity> courseEmployeeEntities = courseEmployeeRepository.findByCodeAndRootId(Helper.getUserCodeUpperCase(), id);
        if (courseEmployeeEntities.isEmpty()) {
            throw new NtsValidationException("message", String.format("Không tìm thấy thông tin khóa học", "attend"));
        }
        List<CourseEmployeeDTO> dtoList = CourseEmployeeMapper.INSTANCE.toDto(List.of(courseEmployeeEntities.get(0)));
        enrichDetail(dtoList);
        return dtoList.get(0);
    }

    /**
     * Delete the courseEmployee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CourseEmployee : {}", id);
        courseEmployeeRepository.deleteById(id);
    }

    private void enrich(List<CourseEmployeeDTO> courseEmployeeDTOS) {
        courseEmployeeHelper.enrichCourse(courseEmployeeDTOS);
    }

    private void enrichDetail(List<CourseEmployeeDTO> courseEmployeeDTOS) {
        courseEmployeeHelper.enrichMetafields(courseEmployeeDTOS);
        courseEmployeeHelper.enrichCourseDetail(courseEmployeeDTOS);
    }

    @Transactional(readOnly = true)
    public Page<CourseEmployeeDTO> findAll(CourseEmployeeCriteria criteria, Pageable pageable) {
        log.debug("Request to get all ExamEmployees");
        String userCode = Helper.getUserCodeUpperCase();
        CourseEmployeeSpecification specification = new CourseEmployeeSpecification()
            .from(criteria.getFrom())
            .to(criteria.getTo())
            .code(userCode)
            .status(criteria.getStatus())
            .title(criteria.getTitle())
            .categoryId(criteria.getCategoryId());
        Page<CourseEmployeeEntity> courseEmployeeEntities = courseEmployeeRepository.findAll(specification, pageable);
        List<CourseEmployeeEntity> entityList = courseEmployeeEntities.getContent();
        List<CourseEmployeeDTO> dtoList = CourseEmployeeMapper.INSTANCE.toDto(entityList);
        enrich(dtoList);
        return new PageImpl<>(dtoList, pageable, courseEmployeeEntities.getTotalElements());
    }

    public List<CategoryDTO> getCategories() {
        String userCode = Helper.getUserCodeUpperCase();
        List<CourseEmployeeEntity> courseEmployeeEntities = courseEmployeeRepository.findAllByCode(userCode);
        List<Long> rootIds = courseEmployeeEntities.stream().map(CourseEmployeeEntity::getRootId).collect(Collectors.toList());
        List<CourseEntity> exams = courseRepository.findAllByIdIn(rootIds);
        List<Long> categoryIds = exams.stream().map(CourseEntity::getCategoryId).collect(Collectors.toList());

        List<CategoryEntity> categories = categoryRepository.findAllByIdIn(categoryIds);

        return CategoryMapper.INSTANCE.toDto(categories);
    }
}
