package io.yody.yodemy.elearning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.yody.yodemy.IntegrationTest;
import io.yody.yodemy.elearning.domain.DocumentEntity;
import io.yody.yodemy.elearning.repository.DocumentRepository;
import io.yody.yodemy.elearning.service.dto.DocumentDTO;
import io.yody.yodemy.elearning.service.mapper.DocumentMapper;
import io.yody.yodemy.web.rest.TestUtil;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentResourceIT {

    private static final Long DEFAULT_ROOT_ID = 1L;
    private static final Long UPDATED_ROOT_ID = 2L;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentMockMvc;

    private DocumentEntity documentEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentEntity createEntity(EntityManager em) {
        DocumentEntity documentEntity = new DocumentEntity().rootId(DEFAULT_ROOT_ID).content(DEFAULT_CONTENT);
        return documentEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentEntity createUpdatedEntity(EntityManager em) {
        DocumentEntity documentEntity = new DocumentEntity().rootId(UPDATED_ROOT_ID).content(UPDATED_CONTENT);
        return documentEntity;
    }

    @BeforeEach
    public void initTest() {
        documentEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();
        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(documentEntity);
        restDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
        DocumentEntity testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getRootId()).isEqualTo(DEFAULT_ROOT_ID);
        assertThat(testDocument.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void createDocumentWithExistingId() throws Exception {
        // Create the Document with an existing ID
        documentEntity.setId(1L);
        DocumentDTO documentDTO = documentMapper.toDto(documentEntity);

        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRootIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        documentEntity.setRootId(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(documentEntity);

        restDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(documentEntity);

        // Get all the documentList
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].rootId").value(hasItem(DEFAULT_ROOT_ID.intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(documentEntity);

        // Get the document
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, documentEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentEntity.getId().intValue()))
            .andExpect(jsonPath("$.rootId").value(DEFAULT_ROOT_ID.intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(documentEntity);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        DocumentEntity updatedDocumentEntity = documentRepository.findById(documentEntity.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentEntity are not directly saved in db
        em.detach(updatedDocumentEntity);
        updatedDocumentEntity.rootId(UPDATED_ROOT_ID).content(UPDATED_CONTENT);
        DocumentDTO documentDTO = documentMapper.toDto(updatedDocumentEntity);

        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        DocumentEntity testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testDocument.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void putNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        documentEntity.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(documentEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        documentEntity.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(documentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        documentEntity.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(documentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Document in the database
        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(documentEntity);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document using partial update
        DocumentEntity partialUpdatedDocumentEntity = new DocumentEntity();
        partialUpdatedDocumentEntity.setId(documentEntity.getId());

        partialUpdatedDocumentEntity.rootId(UPDATED_ROOT_ID).content(UPDATED_CONTENT);

        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentEntity))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        DocumentEntity testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testDocument.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void fullUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(documentEntity);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document using partial update
        DocumentEntity partialUpdatedDocumentEntity = new DocumentEntity();
        partialUpdatedDocumentEntity.setId(documentEntity.getId());

        partialUpdatedDocumentEntity.rootId(UPDATED_ROOT_ID).content(UPDATED_CONTENT);

        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentEntity))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        DocumentEntity testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testDocument.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void patchNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        documentEntity.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(documentEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        documentEntity.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(documentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        documentEntity.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(documentEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Document in the database
        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(documentEntity);

        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Delete the document
        restDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DocumentEntity> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
