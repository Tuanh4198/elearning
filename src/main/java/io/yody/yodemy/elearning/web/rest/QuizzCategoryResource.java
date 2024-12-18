package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.repository.QuizzCategoryRepository;
import io.yody.yodemy.elearning.service.QuizzCategoryService;
import io.yody.yodemy.elearning.service.dto.QuizzCategoryDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzCategoryRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzSearchRequest;
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
 * REST controller for managing {@link io.yody.yodemy.elearning.domain.QuizzCategoryEntity}.
 */
@RestController
@RequestMapping("/api")
public class QuizzCategoryResource {

    private final Logger log = LoggerFactory.getLogger(QuizzCategoryResource.class);

    private static final String ENTITY_NAME = "quizzCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuizzCategoryService quizzCategoryService;

    private final QuizzCategoryRepository quizzCategoryRepository;

    public QuizzCategoryResource(QuizzCategoryService quizzCategoryService, QuizzCategoryRepository quizzCategoryRepository) {
        this.quizzCategoryService = quizzCategoryService;
        this.quizzCategoryRepository = quizzCategoryRepository;
    }

    /**
     * {@code POST  /quizz-categories} : Create a new quizzCategory.
     *
     * @param quizzCategoryDTO the quizzCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new quizzCategoryDTO, or with status {@code 400 (Bad Request)} if the quizzCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quizz-categories")
    public ResponseEntity<QuizzCategoryDTO> createQuizzCategory(@Valid @RequestBody QuizzCategoryRequest quizzCategoryRequest)
        throws URISyntaxException {
        log.debug("REST request to save QuizzCategory : {}", quizzCategoryRequest);
        if (quizzCategoryRequest.getId() != null) {
            throw new BadRequestAlertException("A new quizzCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        try {
            QuizzCategoryDTO result = quizzCategoryService.save(quizzCategoryRequest);
            return ResponseEntity
                .created(new URI("/api/quizz-categories/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * {@code PUT  /quizz-categories/:id} : Updates an existing quizzCategory.
     *
     * @param id the id of the quizzCategoryDTO to save.
     * @param quizzCategoryDTO the quizzCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizzCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the quizzCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the quizzCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quizz-categories/{id}")
    public ResponseEntity<QuizzCategoryDTO> updateQuizzCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody QuizzCategoryRequest quizzCategoryRequest
    ) throws URISyntaxException {
        log.debug("REST request to update QuizzCategory : {}, {}", id, quizzCategoryRequest);
        if (quizzCategoryRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizzCategoryRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizzCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        try {
            QuizzCategoryDTO result = quizzCategoryService.update(quizzCategoryRequest);
            return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizzCategoryRequest.getId().toString()))
                .body(result);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * {@code PATCH  /quizz-categories/:id} : Partial updates given fields of an existing quizzCategory, field will ignore if it is null
     *
     * @param id the id of the quizzCategoryDTO to save.
     * @param quizzCategoryDTO the quizzCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated quizzCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the quizzCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the quizzCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the quizzCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/quizz-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<QuizzCategoryDTO> partialUpdateQuizzCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody QuizzCategoryDTO quizzCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update QuizzCategory partially : {}, {}", id, quizzCategoryDTO);
        if (quizzCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, quizzCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!quizzCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<QuizzCategoryDTO> result = quizzCategoryService.partialUpdate(quizzCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, quizzCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /quizz-categories} : get all the quizzCategories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of quizzCategories in body.
     */
    @GetMapping("/quizz-categories")
    public ResponseEntity<List<QuizzCategoryDTO>> getAllQuizzCategories(
        @RequestParam(value = "search", defaultValue = "") String search,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of QuizzCategories");
        QuizzSearchRequest request = new QuizzSearchRequest();
        request.setSearch(search);
        Page<QuizzCategoryDTO> page = quizzCategoryService.findAll(request, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /quizz-categories/:id} : get the "id" quizzCategory.
     *
     * @param id the id of the quizzCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the quizzCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/quizz-categories/{id}")
    public ResponseEntity<QuizzCategoryDTO> getQuizzCategory(@PathVariable Long id) {
        log.debug("REST request to get QuizzCategory : {}", id);
        Optional<QuizzCategoryDTO> quizzCategoryDTO = quizzCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(quizzCategoryDTO);
    }

    /**
     * {@code DELETE  /quizz-categories/:id} : delete the "id" quizzCategory.
     *
     * @param id the id of the quizzCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/quizz-categories/{id}")
    public ResponseEntity<Void> deleteQuizzCategory(@PathVariable Long id) {
        log.debug("REST request to delete QuizzCategory : {}", id);
        quizzCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
