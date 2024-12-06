package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.repository.ExamEmployeeRepository;
import io.yody.yodemy.elearning.service.ExamEmployeeQueryService;
import io.yody.yodemy.elearning.service.ExamEmployeeService;
import io.yody.yodemy.elearning.service.criteria.ExamEmployeeCriteria;
import io.yody.yodemy.elearning.service.dto.CategoryDTO;
import io.yody.yodemy.elearning.service.dto.ExamEmployeeDTO;
import io.yody.yodemy.elearning.service.dto.ExamEmployeeResultDTO;
import io.yody.yodemy.elearning.service.dto.ExamEmployeeStatisticDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.ExamEmployeeSubmitRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

/**
 * REST controller for managing {@link io.yody.yodemy.elearning.domain.ExamEmployeeEntity}.
 */
@RestController
@RequestMapping("/api")
public class ExamEmployeeResource {

    private static final String ENTITY_NAME = "examEmployee";
    private final Logger log = LoggerFactory.getLogger(ExamEmployeeResource.class);
    private final ExamEmployeeService examEmployeeService;
    private final ExamEmployeeRepository examEmployeeRepository;
    private final ExamEmployeeQueryService examEmployeeQueryService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public ExamEmployeeResource(
        ExamEmployeeService examEmployeeService,
        ExamEmployeeRepository examEmployeeRepository,
        ExamEmployeeQueryService examEmployeeQueryService
    ) {
        this.examEmployeeService = examEmployeeService;
        this.examEmployeeRepository = examEmployeeRepository;
        this.examEmployeeQueryService = examEmployeeQueryService;
    }

    /**
     * {@code POST  /exam-employees} : Create a new examEmployee.
     *
     * @param examEmployeeDTO the examEmployeeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examEmployeeDTO, or with status {@code 400 (Bad Request)} if the examEmployee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exam-employees")
    public ResponseEntity<ExamEmployeeDTO> createExamEmployee(@Valid @RequestBody ExamEmployeeDTO examEmployeeDTO)
        throws URISyntaxException {
        log.debug("REST request to save ExamEmployee : {}", examEmployeeDTO);
        if (examEmployeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new examEmployee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamEmployeeDTO result = examEmployeeService.save(examEmployeeDTO);
        return ResponseEntity
            .created(new URI("/api/exam-employees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exam-employees/:id} : Updates an existing examEmployee.
     *
     * @param id              the id of the examEmployeeDTO to save.
     * @param examEmployeeDTO the examEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the examEmployeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exam-employees/{id}")
    public ResponseEntity<ExamEmployeeDTO> updateExamEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExamEmployeeDTO examEmployeeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExamEmployee : {}, {}", id, examEmployeeDTO);
        if (examEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examEmployeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExamEmployeeDTO result = examEmployeeService.update(examEmployeeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examEmployeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exam-employees/:id} : Partial updates given fields of an existing examEmployee, field will ignore if it is null
     *
     * @param id              the id of the examEmployeeDTO to save.
     * @param examEmployeeDTO the examEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the examEmployeeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examEmployeeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exam-employees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamEmployeeDTO> partialUpdateExamEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExamEmployeeDTO examEmployeeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExamEmployee partially : {}, {}", id, examEmployeeDTO);
        if (examEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examEmployeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamEmployeeDTO> result = examEmployeeService.partialUpdate(examEmployeeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examEmployeeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /exam-employees} : get all the examEmployees.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examEmployees in body.
     */
    @GetMapping("/exam-employees")
    public ResponseEntity<List<ExamEmployeeDTO>> getAllExamEmployees(
        ExamEmployeeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ExamEmployees");
        Page<ExamEmployeeDTO> page = examEmployeeQueryService.findAll(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/admin-exam-employees")
    public ResponseEntity<List<ExamEmployeeDTO>> getAdminExamEmployees(
        @RequestParam(name = "search", required = false) String search,
        @RequestParam(name = "root_id", required = false) Long rootId,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ExamEmployees");
        ExamEmployeeCriteria criteria = new ExamEmployeeCriteria();
        criteria.setSearch(search);
        criteria.setRootId(rootId);
        Page<ExamEmployeeDTO> page = examEmployeeQueryService.findAdmin(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exam-employees/:id} : get the "id" examEmployee.
     *
     * @param id the id of the examEmployeeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examEmployeeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exam-employees/{examId}")
    public ResponseEntity<ExamEmployeeDTO> getExamEmployee(@PathVariable Long examId) {
        log.debug("REST request to get ExamEmployee : {}", examId);
        ExamEmployeeDTO examEmployeeDTO = examEmployeeQueryService.findOne(examId);
        return ResponseEntity.ok().body(examEmployeeDTO);
    }

    @GetMapping("/exam-employees/exists/{examId}")
    public ResponseEntity<Boolean> checkExists(@PathVariable Long examId) {
        log.debug("Check exists ExamEmployee: {}", examId);
        Boolean existExam = examEmployeeService.exists(examId);
        return ResponseEntity.ok().body(existExam);
    }

    @PostMapping("/exam-employees/submit")
    public ResponseEntity<ExamEmployeeResultDTO> submit(@Valid @RequestBody ExamEmployeeSubmitRequest request) {
        log.debug("Rest request to submit");
        ExamEmployeeResultDTO resultDTO = examEmployeeService.submit(request);

        return ResponseEntity.ok().body(resultDTO);
    }

    @GetMapping("/exam-employees/{examId}/result")
    public ResponseEntity<ExamEmployeeStatisticDTO> getResult(@PathVariable Long examId) {
        log.debug("Rest request to get result: {}", examId);
        ExamEmployeeStatisticDTO resultDTO = examEmployeeQueryService.getResult(examId);

        return ResponseEntity.ok().body(resultDTO);
    }

    @GetMapping("/exam-employees/categories")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        log.debug("Rest request to get categories");
        List<CategoryDTO> categoryDTOS = examEmployeeQueryService.getCategories();

        return ResponseEntity.ok().body(categoryDTOS);
    }

    /**
     * {@code DELETE  /exam-employees/:id} : delete the "id" examEmployee.
     *
     * @param id the id of the examEmployeeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exam-employees/{id}")
    public ResponseEntity<Void> deleteExamEmployee(@PathVariable Long id) {
        log.debug("REST request to delete ExamEmployee : {}", id);
        examEmployeeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
