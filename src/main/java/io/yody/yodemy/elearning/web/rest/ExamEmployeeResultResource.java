package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.repository.ExamEmployeeResultRepository;
import io.yody.yodemy.elearning.service.ExamEmployeeResultService;
import io.yody.yodemy.elearning.service.dto.ExamEmployeeResultDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.yody.yodemy.elearning.domain.ExamEmployeeResultEntity}.
 */
@RestController
@RequestMapping("/api")
public class ExamEmployeeResultResource {

    private final Logger log = LoggerFactory.getLogger(ExamEmployeeResultResource.class);

    private static final String ENTITY_NAME = "examEmployeeResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamEmployeeResultService examEmployeeResultService;

    private final ExamEmployeeResultRepository examEmployeeResultRepository;

    public ExamEmployeeResultResource(
        ExamEmployeeResultService examEmployeeResultService,
        ExamEmployeeResultRepository examEmployeeResultRepository
    ) {
        this.examEmployeeResultService = examEmployeeResultService;
        this.examEmployeeResultRepository = examEmployeeResultRepository;
    }

    /**
     * {@code POST  /exam-employee-results} : Create a new examEmployeeResult.
     *
     * @param examEmployeeResultDTO the examEmployeeResultDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examEmployeeResultDTO, or with status {@code 400 (Bad Request)} if the examEmployeeResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exam-employee-results")
    public ResponseEntity<ExamEmployeeResultDTO> createExamEmployeeResult(@Valid @RequestBody ExamEmployeeResultDTO examEmployeeResultDTO)
        throws URISyntaxException {
        log.debug("REST request to save ExamEmployeeResult : {}", examEmployeeResultDTO);
        if (examEmployeeResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new examEmployeeResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamEmployeeResultDTO result = examEmployeeResultService.save(examEmployeeResultDTO);
        return ResponseEntity
            .created(new URI("/api/exam-employee-results/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exam-employee-results/:id} : Updates an existing examEmployeeResult.
     *
     * @param id the id of the examEmployeeResultDTO to save.
     * @param examEmployeeResultDTO the examEmployeeResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examEmployeeResultDTO,
     * or with status {@code 400 (Bad Request)} if the examEmployeeResultDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examEmployeeResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exam-employee-results/{id}")
    public ResponseEntity<ExamEmployeeResultDTO> updateExamEmployeeResult(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExamEmployeeResultDTO examEmployeeResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExamEmployeeResult : {}, {}", id, examEmployeeResultDTO);
        if (examEmployeeResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examEmployeeResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examEmployeeResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExamEmployeeResultDTO result = examEmployeeResultService.update(examEmployeeResultDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examEmployeeResultDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exam-employee-results/:id} : Partial updates given fields of an existing examEmployeeResult, field will ignore if it is null
     *
     * @param id the id of the examEmployeeResultDTO to save.
     * @param examEmployeeResultDTO the examEmployeeResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examEmployeeResultDTO,
     * or with status {@code 400 (Bad Request)} if the examEmployeeResultDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examEmployeeResultDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examEmployeeResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exam-employee-results/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamEmployeeResultDTO> partialUpdateExamEmployeeResult(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExamEmployeeResultDTO examEmployeeResultDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExamEmployeeResult partially : {}, {}", id, examEmployeeResultDTO);
        if (examEmployeeResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examEmployeeResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examEmployeeResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamEmployeeResultDTO> result = examEmployeeResultService.partialUpdate(examEmployeeResultDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examEmployeeResultDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /exam-employee-results} : get all the examEmployeeResults.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examEmployeeResults in body.
     */
    @GetMapping("/exam-employee-results")
    public ResponseEntity<List<ExamEmployeeResultDTO>> getAllExamEmployeeResults(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of ExamEmployeeResults");
        Page<ExamEmployeeResultDTO> page = examEmployeeResultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exam-employee-results/:id} : get the "id" examEmployeeResult.
     *
     * @param id the id of the examEmployeeResultDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examEmployeeResultDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exam-employee-results/{id}")
    public ResponseEntity<ExamEmployeeResultDTO> getExamEmployeeResult(@PathVariable Long id) {
        log.debug("REST request to get ExamEmployeeResult : {}", id);
        Optional<ExamEmployeeResultDTO> examEmployeeResultDTO = examEmployeeResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examEmployeeResultDTO);
    }

    /**
     * {@code DELETE  /exam-employee-results/:id} : delete the "id" examEmployeeResult.
     *
     * @param id the id of the examEmployeeResultDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exam-employee-results/{id}")
    public ResponseEntity<Void> deleteExamEmployeeResult(@PathVariable Long id) {
        log.debug("REST request to delete ExamEmployeeResult : {}", id);
        examEmployeeResultService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
