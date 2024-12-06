package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.repository.QuizzAnswerRepository;
import io.yody.yodemy.elearning.service.QuizzAnswerService;
import io.yody.yodemy.elearning.service.dto.QuizzAnswerDTO;
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
 * REST controller for managing {@link io.yody.yodemy.elearning.domain.QuizzAnswerEntity}.
 */
@RestController
@RequestMapping("/api")
public class QuizzAnswerResource {

    private final Logger log = LoggerFactory.getLogger(QuizzAnswerResource.class);

    private static final String ENTITY_NAME = "quizzAnswer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizzAnswerService quizzAnswerService;

    private final QuizzAnswerRepository quizzAnswerRepository;

    public QuizzAnswerResource(QuizzAnswerService quizzAnswerService, QuizzAnswerRepository quizzAnswerRepository) {
        this.quizzAnswerService = quizzAnswerService;
        this.quizzAnswerRepository = quizzAnswerRepository;
    }

    /**
     * {@code POST  /quizz-answers} : Create a new quizzAnswer.
     *
     * @param quizzAnswerDTO the quizzAnswerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizzAnswerDTO, or with status {@code 400 (Bad Request)} if the quizzAnswer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quizz-answers")
    public ResponseEntity<QuizzAnswerDTO> createQuizzAnswer(@Valid @RequestBody QuizzAnswerDTO quizzAnswerDTO) throws URISyntaxException {
        log.debug("REST request to save QuizzAnswer : {}", quizzAnswerDTO);
        if (quizzAnswerDTO.getId() != null) {
            throw new BadRequestAlertException("A new quizzAnswer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuizzAnswerDTO result = quizzAnswerService.save(quizzAnswerDTO);
        return ResponseEntity
            .created(new URI("/api/quizz-answers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quizz-answers/:id} : Updates an existing quizzAnswer.
     *
     * @param id the id of the quizzAnswerDTO to save.
     * @param quizzAnswerDTO the quizzAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizzAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the quizzAnswerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizzAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quizz-answers/{id}")
    public ResponseEntity<QuizzAnswerDTO> updateQuizzAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuizzAnswerDTO quizzAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update QuizzAnswer : {}, {}", id, quizzAnswerDTO);
        if (quizzAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizzAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizzAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuizzAnswerDTO result = quizzAnswerService.update(quizzAnswerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizzAnswerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /quizz-answers/:id} : Partial updates given fields of an existing quizzAnswer, field will ignore if it is null
     *
     * @param id the id of the quizzAnswerDTO to save.
     * @param quizzAnswerDTO the quizzAnswerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizzAnswerDTO,
     * or with status {@code 400 (Bad Request)} if the quizzAnswerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quizzAnswerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizzAnswerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/quizz-answers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuizzAnswerDTO> partialUpdateQuizzAnswer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuizzAnswerDTO quizzAnswerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuizzAnswer partially : {}, {}", id, quizzAnswerDTO);
        if (quizzAnswerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizzAnswerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizzAnswerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizzAnswerDTO> result = quizzAnswerService.partialUpdate(quizzAnswerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizzAnswerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quizz-answers} : get all the quizzAnswers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizzAnswers in body.
     */
    @GetMapping("/quizz-answers")
    public ResponseEntity<List<QuizzAnswerDTO>> getAllQuizzAnswers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of QuizzAnswers");
        Page<QuizzAnswerDTO> page = quizzAnswerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quizz-answers/:id} : get the "id" quizzAnswer.
     *
     * @param id the id of the quizzAnswerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizzAnswerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quizz-answers/{id}")
    public ResponseEntity<QuizzAnswerDTO> getQuizzAnswer(@PathVariable Long id) {
        log.debug("REST request to get QuizzAnswer : {}", id);
        Optional<QuizzAnswerDTO> quizzAnswerDTO = quizzAnswerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quizzAnswerDTO);
    }

    /**
     * {@code DELETE  /quizz-answers/:id} : delete the "id" quizzAnswer.
     *
     * @param id the id of the quizzAnswerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quizz-answers/{id}")
    public ResponseEntity<Void> deleteQuizzAnswer(@PathVariable Long id) {
        log.debug("REST request to delete QuizzAnswer : {}", id);
        quizzAnswerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
