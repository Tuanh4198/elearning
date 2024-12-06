package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.EdgeEntity;
import io.yody.yodemy.elearning.repository.EdgeRepository;
import io.yody.yodemy.elearning.repository.NodeRepository;
import io.yody.yodemy.elearning.service.dto.EdgeDTO;
import io.yody.yodemy.elearning.service.dto.NodeDTO;
import io.yody.yodemy.elearning.service.mapper.EdgeMapper;
import io.yody.yodemy.elearning.service.mapper.NodeMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EdgeQueryService {

    private static final String ENTITY_NAME = "edge";
    private final Logger log = LoggerFactory.getLogger(EdgeQueryService.class);
    private EdgeRepository repo;

    public EdgeQueryService(EdgeRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<EdgeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all edges");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        List<EdgeEntity> entities = repo.findAll();
        List<EdgeDTO> dtos = entities.stream().map(EdgeMapper.INSTANCE::toDto).collect(Collectors.toList());
        return dtos;
    }

    @Transactional(readOnly = true)
    public EdgeDTO findOne(Long id) {
        log.debug("Request to get Quizz : {}", id);
        Optional<EdgeDTO> dtoOptional = repo.findById(id).map(EdgeMapper.INSTANCE::toDto);
        if (!dtoOptional.isPresent()) {
            return null;
        }
        EdgeDTO dto = dtoOptional.get();
        return dto;
    }
}
