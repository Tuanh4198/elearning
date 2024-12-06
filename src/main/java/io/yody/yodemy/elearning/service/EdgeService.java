package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.EdgeEntity;
import io.yody.yodemy.elearning.domain.NodeEntity;
import io.yody.yodemy.elearning.repository.EdgeRepository;
import io.yody.yodemy.elearning.repository.NodeRepository;
import io.yody.yodemy.elearning.service.dto.EdgeDTO;
import io.yody.yodemy.elearning.service.dto.NodeDTO;
import io.yody.yodemy.elearning.service.mapper.EdgeMapper;
import io.yody.yodemy.elearning.service.mapper.NodeMapper;
import io.yody.yodemy.elearning.web.rest.vm.request.EdgeRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.NodeRequest;
import org.nentangso.core.service.errors.NtsValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EdgeService {
    private static final String ENTITY_NAME = "edge";
    private final Logger log = LoggerFactory.getLogger(EdgeService.class);
    private EdgeRepository repo;

    public EdgeService(EdgeRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public EdgeDTO save(EdgeRequest request) {
        log.debug("Request to save Node : {}", request);
        EdgeEntity entity = EdgeMapper.INSTANCE.requestToEntity(request);
        entity = repo.save(entity);
        EdgeDTO dto = EdgeMapper.INSTANCE.toDto(entity);
        return dto;
    }

    public EdgeDTO update(EdgeRequest request) {
        log.debug("Request to update Node : {}", request);
        EdgeEntity entity = EdgeMapper.INSTANCE.requestToEntity(request);
        entity = repo.save(entity);
        EdgeDTO dto = EdgeMapper.INSTANCE.toDto(entity);
        return dto;
    }

    public void delete(Long id) {
        EdgeEntity entity = repo
            .findById(id)
            .orElseThrow(() -> new NtsValidationException("message", "Không tìm thấy edge"));
        entity.setDeleted(true);
        repo.save(entity);
    }
}
