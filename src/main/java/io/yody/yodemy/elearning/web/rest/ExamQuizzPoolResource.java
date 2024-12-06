package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.repository.ExamQuizzPoolRepository;
import io.yody.yodemy.elearning.service.ExamQuizzPoolService;
import io.yody.yodemy.elearning.service.dto.ExamQuizzPoolDTO;
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
 * REST controller for managing {@link io.yody.yodemy.elearning.domain.ExamQuizzPoolEntity}.
 */
@RestController
@RequestMapping("/api")
public class ExamQuizzPoolResource {

    private final Logger log = LoggerFactory.getLogger(ExamQuizzPoolResource.class);

    private static final String ENTITY_NAME = "examQuizzPool";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExamQuizzPoolService examQuizzPoolService;

    private final ExamQuizzPoolRepository examQuizzPoolRepository;

    public ExamQuizzPoolResource(ExamQuizzPoolService examQuizzPoolService, ExamQuizzPoolRepository examQuizzPoolRepository) {
        this.examQuizzPoolService = examQuizzPoolService;
        this.examQuizzPoolRepository = examQuizzPoolRepository;
    }

    /**
     * {@code POST  /exam-quizz-pools} : Create a new examQuizzPool.
     *
     * @param examQuizzPoolDTO the examQuizzPoolDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new examQuizzPoolDTO, or with status {@code 400 (Bad Request)} if the examQuizzPool has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exam-quizz-pools")
    public ResponseEntity<ExamQuizzPoolDTO> createExamQuizzPool(@Valid @RequestBody ExamQuizzPoolDTO examQuizzPoolDTO)
        throws URISyntaxException {
        log.debug("REST request to save ExamQuizzPool : {}", examQuizzPoolDTO);
        if (examQuizzPoolDTO.getId() != null) {
            throw new BadRequestAlertException("A new examQuizzPool cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExamQuizzPoolDTO result = examQuizzPoolService.save(examQuizzPoolDTO);
        return ResponseEntity
            .created(new URI("/api/exam-quizz-pools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exam-quizz-pools/:id} : Updates an existing examQuizzPool.
     *
     * @param id the id of the examQuizzPoolDTO to save.
     * @param examQuizzPoolDTO the examQuizzPoolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examQuizzPoolDTO,
     * or with status {@code 400 (Bad Request)} if the examQuizzPoolDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the examQuizzPoolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exam-quizz-pools/{id}")
    public ResponseEntity<ExamQuizzPoolDTO> updateExamQuizzPool(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExamQuizzPoolDTO examQuizzPoolDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExamQuizzPool : {}, {}", id, examQuizzPoolDTO);
        if (examQuizzPoolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examQuizzPoolDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examQuizzPoolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExamQuizzPoolDTO result = examQuizzPoolService.update(examQuizzPoolDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examQuizzPoolDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exam-quizz-pools/:id} : Partial updates given fields of an existing examQuizzPool, field will ignore if it is null
     *
     * @param id the id of the examQuizzPoolDTO to save.
     * @param examQuizzPoolDTO the examQuizzPoolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated examQuizzPoolDTO,
     * or with status {@code 400 (Bad Request)} if the examQuizzPoolDTO is not valid,
     * or with status {@code 404 (Not Found)} if the examQuizzPoolDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the examQuizzPoolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exam-quizz-pools/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExamQuizzPoolDTO> partialUpdateExamQuizzPool(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExamQuizzPoolDTO examQuizzPoolDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExamQuizzPool partially : {}, {}", id, examQuizzPoolDTO);
        if (examQuizzPoolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, examQuizzPoolDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!examQuizzPoolRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExamQuizzPoolDTO> result = examQuizzPoolService.partialUpdate(examQuizzPoolDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, examQuizzPoolDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /exam-quizz-pools} : get all the examQuizzPools.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of examQuizzPools in body.
     */
    @GetMapping("/exam-quizz-pools")
    public ResponseEntity<List<ExamQuizzPoolDTO>> getAllExamQuizzPools(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ExamQuizzPools");
        Page<ExamQuizzPoolDTO> page = examQuizzPoolService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exam-quizz-pools/:id} : get the "id" examQuizzPool.
     *
     * @param id the id of the examQuizzPoolDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the examQuizzPoolDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exam-quizz-pools/{id}")
    public ResponseEntity<ExamQuizzPoolDTO> getExamQuizzPool(@PathVariable Long id) {
        log.debug("REST request to get ExamQuizzPool : {}", id);
        Optional<ExamQuizzPoolDTO> examQuizzPoolDTO = examQuizzPoolService.findOne(id);
        return ResponseUtil.wrapOrNotFound(examQuizzPoolDTO);
    }

    /**
     * {@code DELETE  /exam-quizz-pools/:id} : delete the "id" examQuizzPool.
     *
     * @param id the id of the examQuizzPoolDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exam-quizz-pools/{id}")
    public ResponseEntity<Void> deleteExamQuizzPool(@PathVariable Long id) {
        log.debug("REST request to delete ExamQuizzPool : {}", id);
        examQuizzPoolService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
