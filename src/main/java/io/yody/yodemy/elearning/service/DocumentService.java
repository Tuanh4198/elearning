package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.domain.DocumentEntity;
import io.yody.yodemy.elearning.repository.DocumentRepository;
import io.yody.yodemy.elearning.service.dto.DocumentDTO;
import io.yody.yodemy.elearning.service.mapper.DocumentMapper;
import io.yody.yodemy.elearning.service.specification.DocumentSpecification;
import io.yody.yodemy.elearning.web.rest.vm.request.DocumentSearchRequest;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DocumentEntity}.
 */
@Service
@Transactional
public class DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    public DocumentService(DocumentRepository documentRepository, DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    /**
     * Save a document.
     *
     * @param documentDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentDTO save(DocumentDTO documentDTO) {
        log.debug("Request to save Document : {}", documentDTO);
        DocumentEntity documentEntity = documentMapper.toEntity(documentDTO);
        documentEntity = documentRepository.save(documentEntity);
        return documentMapper.toDto(documentEntity);
    }

    /**
     * Update a document.
     *
     * @param documentDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentDTO update(DocumentDTO documentDTO) {
        log.debug("Request to update Document : {}", documentDTO);
        DocumentEntity documentEntity = documentMapper.toEntity(documentDTO);
        documentEntity = documentRepository.save(documentEntity);
        return documentMapper.toDto(documentEntity);
    }

    /**
     * Partially update a document.
     *
     * @param documentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentDTO> partialUpdate(DocumentDTO documentDTO) {
        log.debug("Request to partially update Document : {}", documentDTO);

        return documentRepository
            .findById(documentDTO.getId())
            .map(existingDocument -> {
                documentMapper.partialUpdate(existingDocument, documentDTO);

                return existingDocument;
            })
            .map(documentRepository::save)
            .map(documentMapper::toDto);
    }

    /**
     * Get all the documents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentDTO> findAll(Pageable pageable, DocumentSearchRequest request) {
        log.debug("Request to get all Documents with rootId = -1");
        DocumentSpecification specification = new DocumentSpecification().search(request.getSearch(), -1L);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));
        Page<DocumentDTO> documentDTOS = documentRepository.findAll(specification, sortedPageable).map(documentMapper::toDto);
        return documentDTOS;
    }

    /**
     * Get one document by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentDTO> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentRepository.findById(id).map(documentMapper::toDto);
    }

    /**
     * Delete the document by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.deleteById(id);
    }
}
