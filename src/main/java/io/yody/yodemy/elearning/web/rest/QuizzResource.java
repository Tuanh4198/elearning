package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.domain.enumeration.QuizzTypeEnum;
import io.yody.yodemy.elearning.repository.QuizzRepository;
import io.yody.yodemy.elearning.service.QuizzService;
import io.yody.yodemy.elearning.service.dto.QuizzDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzSearchRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link io.yody.yodemy.elearning.domain.QuizzEntity}.
 */
@RestController
@RequestMapping("/api")
public class QuizzResource {

    private final Logger log = LoggerFactory.getLogger(QuizzResource.class);

    private static final String ENTITY_NAME = "quizz";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizzService quizzService;

    private final QuizzRepository quizzRepository;

    public QuizzResource(QuizzService quizzService, QuizzRepository quizzRepository) {
        this.quizzService = quizzService;
        this.quizzRepository = quizzRepository;
    }

    /**
     * {@code POST  /quizzes} : Create a new quizz.
     *
     * @param quizzRequest the quizzDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizzDTO, or with status {@code 400 (Bad Request)} if the quizz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quizzes")
    public ResponseEntity<QuizzDTO> createQuizz(@Valid @RequestBody QuizzRequest quizzRequest) throws URISyntaxException {
        log.debug("REST request to save Quizz : {}", quizzRequest);
        if (quizzRequest.getId() != null) {
            throw new BadRequestAlertException("A new quizz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuizzDTO result = quizzService.save(quizzRequest);
        return ResponseEntity
            .created(new URI("/api/quizzes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /quizzes/:id} : Updates an existing quizz.
     *
     * @param id the id of the quizzDTO to save.
     * @param quizzDTO the quizzDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizzDTO,
     * or with status {@code 400 (Bad Request)} if the quizzDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizzDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quizzes/{id}")
    public ResponseEntity<QuizzDTO> updateQuizz(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuizzRequest request
    ) throws URISyntaxException {
        log.debug("REST request to update Quizz : {}, {}", id, request);
        if (request.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, request.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizzRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        QuizzDTO result = quizzService.update(request);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, request.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /quizzes/:id} : Partial updates given fields of an existing quizz, field will ignore if it is null
     *
     * @param id the id of the quizzDTO to save.
     * @param quizzDTO the quizzDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizzDTO,
     * or with status {@code 400 (Bad Request)} if the quizzDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quizzDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizzDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/quizzes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuizzDTO> partialUpdateQuizz(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuizzDTO quizzDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Quizz partially : {}, {}", id, quizzDTO);
        if (quizzDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizzDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizzRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizzDTO> result = quizzService.partialUpdate(quizzDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizzDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quizzes} : get all the quizzes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizzes in body.
     */
    @GetMapping("/quizzes")
    public ResponseEntity<List<QuizzDTO>> getAllQuizzes(
        @RequestParam(value = "type", defaultValue = "") QuizzTypeEnum type,
        @RequestParam(value = "search", defaultValue = "") String search,
        @RequestParam(value = "category_id", defaultValue = "") Long categoryId,
        @RequestParam(value = "quizz_ids", required = false) List<Long> quizzIds,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of Quizzes");
        if (quizzIds == null) quizzIds = new ArrayList<>();
        QuizzSearchRequest request = new QuizzSearchRequest();
        request.setSearch(search);
        request.setType(type);
        request.setCategoryId(categoryId);
        request.setQuizzIds(quizzIds);
        Page<QuizzDTO> page = quizzService.findAll(pageable, request);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quizzes/:id} : get the "id" quizz.
     *
     * @param id the id of the quizzDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizzDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quizzes/{id}")
    public ResponseEntity<QuizzDTO> getQuizz(@PathVariable Long id) {
        log.debug("REST request to get Quizz : {}", id);
        QuizzDTO quizzDTO = quizzService.findOne(id);
        return ResponseEntity.ok().body(quizzDTO);
    }

    /**
     * {@code DELETE  /quizzes/:id} : delete the "id" quizz.
     *
     * @param id the id of the quizzDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Void> deleteQuizz(@PathVariable Long id) {
        log.debug("REST request to delete Quizz : {}", id);
        quizzService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
