package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.EdgeEntity;
import io.yody.yodemy.elearning.domain.NodeEntity;
import io.yody.yodemy.elearning.domain.NodeMetafieldEntity;
import io.yody.yodemy.elearning.repository.EdgeRepository;
import io.yody.yodemy.elearning.repository.NodeMetafieldRepository;
import io.yody.yodemy.elearning.repository.NodeRepository;
import io.yody.yodemy.elearning.service.business.NodeAggregate;
import io.yody.yodemy.elearning.service.mapper.EdgeMapper;
import io.yody.yodemy.elearning.service.mapper.NodeMapper;
import io.yody.yodemy.elearning.service.mapper.NodeMetafieldMapper;
import io.yody.yodemy.elearning.web.rest.vm.request.EdgeRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.NodeRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FlowDomainService {

    private final Logger log = LoggerFactory.getLogger(FlowDomainService.class);

    private EdgeRepository edgeRepository;
    private EdgeMapper edgeMapper;
    private NodeRepository nodeRepository;
    private NodeMapper nodeMapper;
    private NodeMetafieldRepository nodeMetafieldRepository;
    private NodeMetafieldMapper nodeMetafieldMapper;
    private NodeService nodeService;

    public FlowDomainService(
        EdgeRepository edgeRepository,
        EdgeMapper edgeMapper,
        NodeRepository nodeRepository,
        NodeMapper nodeMapper,
        NodeMetafieldRepository nodeMetafieldRepository,
        NodeMetafieldMapper nodeMetafieldMapper,
        NodeService nodeService
    ) {
        this.edgeRepository = edgeRepository;
        this.edgeMapper = edgeMapper;
        this.nodeRepository = nodeRepository;
        this.nodeMapper = nodeMapper;
        this.nodeMetafieldRepository = nodeMetafieldRepository;
        this.nodeMetafieldMapper = nodeMetafieldMapper;
        this.nodeService = nodeService;
    }

    @Transactional
    public void updateMultiple(List<NodeRequest> nodesRequest, List<EdgeRequest> edgesRequest) {
        log.debug("Request to update multiple Nodes and Edges : {}", nodesRequest, edgesRequest);
        if (edgesRequest != null && !edgesRequest.isEmpty()) {
            List<EdgeEntity> edgeEntities = edgesRequest.stream().map(edgeMapper::requestToEntity).collect(Collectors.toList());
            edgeRepository.saveAll(edgeEntities);
        }
        nodeService.updateMultiple(nodesRequest);
    }
}
