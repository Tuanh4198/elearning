package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.repository.CourseEmployeeRepository;
import io.yody.yodemy.elearning.service.CourseEmployeeService;
import io.yody.yodemy.elearning.service.criteria.CourseEmployeeCriteria;
import io.yody.yodemy.elearning.service.dto.CategoryDTO;
import io.yody.yodemy.elearning.service.dto.CourseEmployeeDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.LearnCourseRequest;
import org.nentangso.core.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link io.yody.yodemy.elearning.domain.CourseEmployeeEntity}.
 */
@RestController
@RequestMapping("/api")
public class CourseEmployeeResource {

    private final Logger log = LoggerFactory.getLogger(CourseEmployeeResource.class);

    private static final String ENTITY_NAME = "courseEmployee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseEmployeeService courseEmployeeService;

    private final CourseEmployeeRepository courseEmployeeRepository;

    public CourseEmployeeResource(CourseEmployeeService courseEmployeeService, CourseEmployeeRepository courseEmployeeRepository) {
        this.courseEmployeeService = courseEmployeeService;
        this.courseEmployeeRepository = courseEmployeeRepository;
    }

    /**
     * {@code POST  /course-employees} : Create a new courseEmployee.
     *
     * @param courseEmployeeDTO the courseEmployeeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseEmployeeDTO, or with status {@code 400 (Bad Request)} if the courseEmployee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/course-employees")
    public ResponseEntity<CourseEmployeeDTO> createCourseEmployee(@Valid @RequestBody CourseEmployeeDTO courseEmployeeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CourseEmployee : {}", courseEmployeeDTO);
        if (courseEmployeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new courseEmployee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseEmployeeDTO result = courseEmployeeService.save(courseEmployeeDTO);
        return ResponseEntity
            .created(new URI("/api/course-employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /course-employees/:id} : Updates an existing courseEmployee.
     *
     * @param id the id of the courseEmployeeDTO to save.
     * @param courseEmployeeDTO the courseEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the courseEmployeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/course-employees/{id}")
    public ResponseEntity<CourseEmployeeDTO> updateCourseEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CourseEmployeeDTO courseEmployeeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CourseEmployee : {}, {}", id, courseEmployeeDTO);
        if (courseEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseEmployeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CourseEmployeeDTO result = courseEmployeeService.update(courseEmployeeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseEmployeeDTO.getId().toString()))
            .body(result);
    }

    @PutMapping("/course-employees/{id}/attend")
    public ResponseEntity<Object> attendCourseEmployee(@PathVariable(value = "id", required = true) final Long id)
        throws URISyntaxException {
        log.debug("REST request to update CourseEmployee : {}", id);
        courseEmployeeService.attend(id);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/course-employees/{id}/learn/{documentId}")
    public ResponseEntity<Object> leanCourseEmployee(
        @PathVariable(value = "id", required = true) final Long id,
        @PathVariable(value = "documentId", required = true) final Long documentId,
        @Valid @RequestBody LearnCourseRequest learnCourseRequest
    ) throws URISyntaxException {
        log.debug("REST request to update CourseEmployee : {}", id);
        courseEmployeeService.learn(id, documentId, learnCourseRequest);
        return ResponseEntity.ok().body(null);
    }

    /**
     * {@code PATCH  /course-employees/:id} : Partial updates given fields of an existing courseEmployee, field will ignore if it is null
     *
     * @param id the id of the courseEmployeeDTO to save.
     * @param courseEmployeeDTO the courseEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the courseEmployeeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the courseEmployeeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/course-employees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CourseEmployeeDTO> partialUpdateCourseEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CourseEmployeeDTO courseEmployeeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseEmployee partially : {}, {}", id, courseEmployeeDTO);
        if (courseEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseEmployeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseEmployeeDTO> result = courseEmployeeService.partialUpdate(courseEmployeeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseEmployeeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /course-employees} : get all the courseEmployees.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseEmployees in body.
     */
    @GetMapping("/course-employees")
    public ResponseEntity<List<CourseEmployeeDTO>> getAllCourseEmployees(
        CourseEmployeeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CourseEmployees");
        Page<CourseEmployeeDTO> page = courseEmployeeService.findAll(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    //    /**
    //     * {@code GET  /course-employees/:id} : get the "id" courseEmployee.
    //     *
    //     * @param id the id of the courseEmployeeDTO to retrieve.
    //     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseEmployeeDTO, or with status {@code 404 (Not Found)}.
    //     */
    @GetMapping("/course-employees/get-by-course/{id}")
    public ResponseEntity<CourseEmployeeDTO> getCourseEmployee(@PathVariable Long id) {
        log.debug("REST request to get CourseEmployee : {}", id);
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeService.findOne(id);
        return ResponseEntity.ok().body(courseEmployeeDTO);
    }

    @GetMapping("/course-employees/categories")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        log.debug("Rest request to get categories");
        List<CategoryDTO> categoryDTOS = courseEmployeeService.getCategories();

        return ResponseEntity.ok().body(categoryDTOS);
    }

    /**
     * {@code DELETE  /course-employees/:id} : delete the "id" courseEmployee.
     *
     * @param id the id of the courseEmployeeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/course-employees/{id}")
    public ResponseEntity<Void> deleteCourseEmployee(@PathVariable Long id) {
        log.debug("REST request to delete CourseEmployee : {}", id);
        courseEmployeeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
