package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.repository.EdgeRepository;
import io.yody.yodemy.elearning.service.EdgeQueryService;
import io.yody.yodemy.elearning.service.EdgeService;
import io.yody.yodemy.elearning.service.dto.EdgeDTO;
import io.yody.yodemy.elearning.service.dto.NodeDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.EdgeRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.NodeRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
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

@RestController
@RequestMapping("/api")
public class EdgeResource {

    private final Logger log = LoggerFactory.getLogger(EdgeResource.class);

    private static final String ENTITY_NAME = "edge";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private EdgeRepository edgeRepository;
    private EdgeService edgeService;
    private EdgeQueryService edgeQueryService;

    public EdgeResource(EdgeRepository edgeRepository, EdgeService edgeService, EdgeQueryService edgeQueryService) {
        this.edgeRepository = edgeRepository;
        this.edgeService = edgeService;
        this.edgeQueryService = edgeQueryService;
    }

    @PostMapping("/edges")
    public ResponseEntity<EdgeDTO> createNode(@Valid @RequestBody EdgeRequest request) throws URISyntaxException {
        log.debug("REST request to save Edge : {}", request);
        if (request.getId() != null) {
            throw new BadRequestAlertException("A new quizz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EdgeDTO result = edgeService.save(request);
        return ResponseEntity
            .created(new URI("/api/edges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/edges/{id}")
    public ResponseEntity<EdgeDTO> updateNode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EdgeRequest request
    ) throws URISyntaxException {
        log.debug("REST request to update request : {}, {}", id, request);
        if (request.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, request.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!edgeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EdgeDTO result = edgeService.update(request);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, request.getId().toString()))
            .body(result);
    }

    @GetMapping("/edges")
    public ResponseEntity<List<EdgeDTO>> getAllEdges(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of edges");
        List<EdgeDTO> page = edgeQueryService.findAll(pageable);
        //        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/edges/{id}")
    public ResponseEntity<EdgeDTO> getNode(@PathVariable Long id) {
        log.debug("REST request to get edge : {}", id);
        EdgeDTO edgeDTO = edgeQueryService.findOne(id);
        return ResponseEntity.ok().body(edgeDTO);
    }

    @DeleteMapping("/edges/{id}")
    public ResponseEntity<Boolean> deleteNode(@PathVariable Long id) {
        log.debug("REST request to delete Edge : {}", id);
        edgeService.delete(id);
        return ResponseEntity.ok().body(true);
    }
}
