package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.repository.ExamRepository;
import io.yody.yodemy.elearning.service.ExamQueryService;
import io.yody.yodemy.elearning.service.ExamService;
import io.yody.yodemy.elearning.service.criteria.ExamCriteria;
import io.yody.yodemy.elearning.service.dto.ExamDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.ExamRequest;
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
 * REST controller for managing {@link io.yody.yodemy.elearning.domain.ExamEntity}.
 */
@RestController
@RequestMapping("/api")
public class ExamResource {

    private final Logger log = LoggerFactory.getLogger(ExamResource.class);

    private static final String ENTITY_NAME = "exam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamService examService;

    private final ExamRepository examRepository;
    private final ExamQueryService examQueryService;

    public ExamResource(ExamService examService, ExamRepository examRepository, ExamQueryService examQueryService) {
        this.examService = examService;
        this.examRepository = examRepository;
        this.examQueryService = examQueryService;
    }

    /**
     * {@code POST  /exams} : Create a new exam.
     *
     * @param examDTO the examDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examDTO, or with status {@code 400 (Bad Request)} if the exam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exams")
    public ResponseEntity<ExamDTO> createExam(@Valid @RequestBody ExamRequest examRequest) throws URISyntaxException {
        log.debug("REST request to save Exam : {}", examRequest);
        if (examRequest.getId() != null) {
            throw new BadRequestAlertException("A new exam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamDTO result = examService.save(examRequest);
        return ResponseEntity
            .created(new URI("/api/exams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/exams/{id}/assign")
    public ResponseEntity<Void> assignExam(@PathVariable(value = "id", required = true) final Long id) {
        log.debug("REST request to attend training: {}", id);
        examService.assign(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code PUT  /exams/:id} : Updates an existing exam.
     *
     * @param id the id of the examDTO to save.
     * @param examDTO the examDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examDTO,
     * or with status {@code 400 (Bad Request)} if the examDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exams/{id}")
    public ResponseEntity<ExamDTO> updateExam(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExamRequest examRequest
    ) throws URISyntaxException {
        log.debug("REST request to update Exam : {}, {}", id, examRequest);
        if (examRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExamDTO result = examService.update(examRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exams/:id} : Partial updates given fields of an existing exam, field will ignore if it is null
     *
     * @param id the id of the examDTO to save.
     * @param examDTO the examDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examDTO,
     * or with status {@code 400 (Bad Request)} if the examDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exams/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamDTO> partialUpdateExam(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExamDTO examDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Exam partially : {}, {}", id, examDTO);
        if (examDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamDTO> result = examService.partialUpdate(examDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /exams} : get all the exams.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exams in body.
     */
    @GetMapping("/exams")
    public ResponseEntity<List<ExamDTO>> getAllExams(
        ExamCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Exams");
        Page<ExamDTO> page = examQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exams/:id} : get the "id" exam.
     *
     * @param id the id of the examDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exams/{id}")
    public ResponseEntity<ExamDTO> getExam(@PathVariable Long id) {
        log.debug("REST request to get Exam : {}", id);
        ExamDTO examDTO = examQueryService.findOne(id);
        return ResponseEntity.ok().body(examDTO);
    }

    /**
     * {@code DELETE  /exams/:id} : delete the "id" exam.
     *
     * @param id the id of the examDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exams/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        log.debug("REST request to delete Exam : {}", id);
        examService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
