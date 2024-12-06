package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.repository.NodeRepository;
import io.yody.yodemy.elearning.service.FlowDomainService;
import io.yody.yodemy.elearning.service.NodeQueryService;
import io.yody.yodemy.elearning.service.NodeService;
import io.yody.yodemy.elearning.service.criteria.SearchNodeCriteria;
import io.yody.yodemy.elearning.service.dto.NodeDTO;
import io.yody.yodemy.elearning.web.rest.vm.request.MultipleNodeRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.NodeCourseRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.NodeExamRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.NodeRequest;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class NodeResource {

    private final Logger log = LoggerFactory.getLogger(NodeResource.class);
    private static final String ENTITY_NAME = "node";
    private final NodeRepository nodeRepository;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NodeService nodeService;
    private final NodeQueryService nodeQueryService;
    private final FlowDomainService flowDomainService;

    public NodeResource(
        NodeService nodeService,
        NodeQueryService nodeQueryService,
        NodeRepository nodeRepository,
        FlowDomainService flowDomainService
    ) {
        this.nodeService = nodeService;
        this.nodeQueryService = nodeQueryService;
        this.nodeRepository = nodeRepository;
        this.flowDomainService = flowDomainService;
    }

    @PostMapping("/nodes")
    public ResponseEntity<NodeDTO> createNode(@Valid @RequestBody NodeRequest request) throws URISyntaxException {
        log.debug("REST request to save Node : {}", request);
        if (request.getId() != null) {
            throw new BadRequestAlertException("A new quizz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NodeDTO result = nodeService.save(request);
        return ResponseEntity
            .created(new URI("/api/nodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/node-course")
    public ResponseEntity<NodeDTO> createNode(@Valid @RequestBody NodeCourseRequest request) throws URISyntaxException {
        log.debug("REST request to save Node : {}", request);
        if (!Objects.isNull(request.getNode()) && request.getNode().getId() != null) {
            throw new BadRequestAlertException("A new quizz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NodeDTO result = nodeService.saveCourse(request);
        return ResponseEntity
            .created(new URI("/api/nodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/node-course/{id}")
    public ResponseEntity<NodeDTO> updateNodeCourse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NodeCourseRequest request
    ) throws URISyntaxException {
        log.debug("REST request to update Node : {}, {}", id, request);
        if (request.getNode().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, request.getNode().getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NodeDTO result = nodeService.updateCourse(request);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, request.getNode().getId().toString()))
            .body(result);
    }

    @PostMapping("/node-exam")
    public ResponseEntity<NodeDTO> createNode(@Valid @RequestBody NodeExamRequest request) throws URISyntaxException {
        log.debug("REST request to save Node : {}", request);
        if (!Objects.isNull(request.getNode()) && request.getNode().getId() != null) {
            throw new BadRequestAlertException("A new quizz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NodeDTO result = nodeService.saveExam(request);
        return ResponseEntity
            .created(new URI("/api/nodes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/node-exam/{id}")
    public ResponseEntity<NodeDTO> updateNodeExam(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NodeExamRequest request
    ) throws URISyntaxException {
        log.debug("REST request to update Node : {}, {}", id, request);
        if (request.getNode().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, request.getNode().getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NodeDTO result = nodeService.updateExam(request);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, request.getNode().getId().toString()))
            .body(result);
    }

    @PutMapping("/nodes/{id}")
    public ResponseEntity<NodeDTO> updateNode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NodeRequest request
    ) throws URISyntaxException {
        log.debug("REST request to update Node : {}, {}", id, request);
        if (request.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, request.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NodeDTO result = nodeService.update(request, true);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, request.getId().toString()))
            .body(result);
    }

    @GetMapping("/nodes")
    public ResponseEntity<List<NodeDTO>> getAllNodes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of nodes");
        List<NodeDTO> page = nodeQueryService.findAll(pageable);
        return ResponseEntity.ok().body(page);
    }

    @GetMapping("/nodes/{id}")
    public ResponseEntity<NodeDTO> getNode(@PathVariable Long id) {
        log.debug("REST request to get Node : {}", id);
        NodeDTO nodeDTO = nodeQueryService.findOne(id);
        return ResponseEntity.ok().body(nodeDTO);
    }

    @GetMapping("/my-nodes")
    public ResponseEntity<List<NodeDTO>> getMyNodes(
        @RequestParam(value = "root_id", required = false) Long rootId,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(value = "limit", required = false, defaultValue = "200") Integer limit,
        @RequestParam(value = "sort_type", required = false, defaultValue = "DESC") String sortType,
        @RequestParam(value = "sort_column", required = false, defaultValue = "updatedAt") String sortColumn) {
        log.debug("REST request to get my  : {}");
        SearchNodeCriteria criteria = new SearchNodeCriteria(rootId, type, page, limit, sortType, sortColumn);
        Page<NodeDTO> result = nodeQueryService.find(criteria);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }

    @DeleteMapping("/nodes/{id}")
    public ResponseEntity<Boolean> deleteNode(@PathVariable Long id) {
        log.debug("REST request to delete Node : {}", id);
        nodeService.delete(id);
        return ResponseEntity.ok().body(true);
    }

    @PutMapping("/nodes-multiple")
    public ResponseEntity<Boolean> updateNode(@Valid @RequestBody MultipleNodeRequest request) throws URISyntaxException {
        log.debug("REST request to update Node : {}, {}", request.getNodesrequest(), request.getEdgesRequest());
        flowDomainService.updateMultiple(request.getNodesrequest(), request.getEdgesRequest());
        return ResponseEntity.ok().body(true);
    }
}
