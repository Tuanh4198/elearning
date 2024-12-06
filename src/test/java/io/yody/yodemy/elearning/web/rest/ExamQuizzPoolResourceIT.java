package io.yody.yodemy.elearning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.yody.yodemy.IntegrationTest;
import io.yody.yodemy.elearning.domain.ExamQuizzPoolEntity;
import io.yody.yodemy.elearning.repository.ExamQuizzPoolRepository;
import io.yody.yodemy.elearning.service.dto.ExamQuizzPoolDTO;
import io.yody.yodemy.elearning.service.mapper.ExamQuizzPoolMapper;
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

/**
 * Integration tests for the {@link ExamQuizzPoolResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamQuizzPoolResourceIT {

    private static final Long DEFAULT_ROOT_ID = 1L;
    private static final Long UPDATED_ROOT_ID = 2L;

    private static final Long DEFAULT_SOURCE_ID = 1L;
    private static final Long UPDATED_SOURCE_ID = 2L;

    private static final String ENTITY_API_URL = "/api/exam-quizz-pools";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExamQuizzPoolRepository examQuizzPoolRepository;

    @Autowired
    private ExamQuizzPoolMapper examQuizzPoolMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamQuizzPoolMockMvc;

    private ExamQuizzPoolEntity examQuizzPoolEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamQuizzPoolEntity createEntity(EntityManager em) {
        ExamQuizzPoolEntity examQuizzPoolEntity = new ExamQuizzPoolEntity().rootId(DEFAULT_ROOT_ID).sourceId(DEFAULT_SOURCE_ID);
        return examQuizzPoolEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamQuizzPoolEntity createUpdatedEntity(EntityManager em) {
        ExamQuizzPoolEntity examQuizzPoolEntity = new ExamQuizzPoolEntity().rootId(UPDATED_ROOT_ID).sourceId(UPDATED_SOURCE_ID);
        return examQuizzPoolEntity;
    }

    @BeforeEach
    public void initTest() {
        examQuizzPoolEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createExamQuizzPool() throws Exception {
        int databaseSizeBeforeCreate = examQuizzPoolRepository.findAll().size();
        // Create the ExamQuizzPool
        ExamQuizzPoolDTO examQuizzPoolDTO = examQuizzPoolMapper.toDto(examQuizzPoolEntity);
        restExamQuizzPoolMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examQuizzPoolDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExamQuizzPool in the database
        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeCreate + 1);
        ExamQuizzPoolEntity testExamQuizzPool = examQuizzPoolList.get(examQuizzPoolList.size() - 1);
        assertThat(testExamQuizzPool.getRootId()).isEqualTo(DEFAULT_ROOT_ID);
        assertThat(testExamQuizzPool.getSourceId()).isEqualTo(DEFAULT_SOURCE_ID);
    }

    @Test
    @Transactional
    void createExamQuizzPoolWithExistingId() throws Exception {
        // Create the ExamQuizzPool with an existing ID
        examQuizzPoolEntity.setId(1L);
        ExamQuizzPoolDTO examQuizzPoolDTO = examQuizzPoolMapper.toDto(examQuizzPoolEntity);

        int databaseSizeBeforeCreate = examQuizzPoolRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamQuizzPoolMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examQuizzPoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamQuizzPool in the database
        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRootIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = examQuizzPoolRepository.findAll().size();
        // set the field null
        examQuizzPoolEntity.setRootId(null);

        // Create the ExamQuizzPool, which fails.
        ExamQuizzPoolDTO examQuizzPoolDTO = examQuizzPoolMapper.toDto(examQuizzPoolEntity);

        restExamQuizzPoolMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examQuizzPoolDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = examQuizzPoolRepository.findAll().size();
        // set the field null
        examQuizzPoolEntity.setSourceId(null);

        // Create the ExamQuizzPool, which fails.
        ExamQuizzPoolDTO examQuizzPoolDTO = examQuizzPoolMapper.toDto(examQuizzPoolEntity);

        restExamQuizzPoolMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examQuizzPoolDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExamQuizzPools() throws Exception {
        // Initialize the database
        examQuizzPoolRepository.saveAndFlush(examQuizzPoolEntity);

        // Get all the examQuizzPoolList
        restExamQuizzPoolMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examQuizzPoolEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].rootId").value(hasItem(DEFAULT_ROOT_ID.intValue())))
            .andExpect(jsonPath("$.[*].sourceId").value(hasItem(DEFAULT_SOURCE_ID.intValue())));
    }

    @Test
    @Transactional
    void getExamQuizzPool() throws Exception {
        // Initialize the database
        examQuizzPoolRepository.saveAndFlush(examQuizzPoolEntity);

        // Get the examQuizzPool
        restExamQuizzPoolMockMvc
            .perform(get(ENTITY_API_URL_ID, examQuizzPoolEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examQuizzPoolEntity.getId().intValue()))
            .andExpect(jsonPath("$.rootId").value(DEFAULT_ROOT_ID.intValue()))
            .andExpect(jsonPath("$.sourceId").value(DEFAULT_SOURCE_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingExamQuizzPool() throws Exception {
        // Get the examQuizzPool
        restExamQuizzPoolMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExamQuizzPool() throws Exception {
        // Initialize the database
        examQuizzPoolRepository.saveAndFlush(examQuizzPoolEntity);

        int databaseSizeBeforeUpdate = examQuizzPoolRepository.findAll().size();

        // Update the examQuizzPool
        ExamQuizzPoolEntity updatedExamQuizzPoolEntity = examQuizzPoolRepository.findById(examQuizzPoolEntity.getId()).get();
        // Disconnect from session so that the updates on updatedExamQuizzPoolEntity are not directly saved in db
        em.detach(updatedExamQuizzPoolEntity);
        updatedExamQuizzPoolEntity.rootId(UPDATED_ROOT_ID).sourceId(UPDATED_SOURCE_ID);
        ExamQuizzPoolDTO examQuizzPoolDTO = examQuizzPoolMapper.toDto(updatedExamQuizzPoolEntity);

        restExamQuizzPoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examQuizzPoolDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examQuizzPoolDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExamQuizzPool in the database
        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeUpdate);
        ExamQuizzPoolEntity testExamQuizzPool = examQuizzPoolList.get(examQuizzPoolList.size() - 1);
        assertThat(testExamQuizzPool.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testExamQuizzPool.getSourceId()).isEqualTo(UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    void putNonExistingExamQuizzPool() throws Exception {
        int databaseSizeBeforeUpdate = examQuizzPoolRepository.findAll().size();
        examQuizzPoolEntity.setId(count.incrementAndGet());

        // Create the ExamQuizzPool
        ExamQuizzPoolDTO examQuizzPoolDTO = examQuizzPoolMapper.toDto(examQuizzPoolEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamQuizzPoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examQuizzPoolDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examQuizzPoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamQuizzPool in the database
        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExamQuizzPool() throws Exception {
        int databaseSizeBeforeUpdate = examQuizzPoolRepository.findAll().size();
        examQuizzPoolEntity.setId(count.incrementAndGet());

        // Create the ExamQuizzPool
        ExamQuizzPoolDTO examQuizzPoolDTO = examQuizzPoolMapper.toDto(examQuizzPoolEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamQuizzPoolMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examQuizzPoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamQuizzPool in the database
        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExamQuizzPool() throws Exception {
        int databaseSizeBeforeUpdate = examQuizzPoolRepository.findAll().size();
        examQuizzPoolEntity.setId(count.incrementAndGet());

        // Create the ExamQuizzPool
        ExamQuizzPoolDTO examQuizzPoolDTO = examQuizzPoolMapper.toDto(examQuizzPoolEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamQuizzPoolMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examQuizzPoolDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamQuizzPool in the database
        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamQuizzPoolWithPatch() throws Exception {
        // Initialize the database
        examQuizzPoolRepository.saveAndFlush(examQuizzPoolEntity);

        int databaseSizeBeforeUpdate = examQuizzPoolRepository.findAll().size();

        // Update the examQuizzPool using partial update
        ExamQuizzPoolEntity partialUpdatedExamQuizzPoolEntity = new ExamQuizzPoolEntity();
        partialUpdatedExamQuizzPoolEntity.setId(examQuizzPoolEntity.getId());

        restExamQuizzPoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamQuizzPoolEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamQuizzPoolEntity))
            )
            .andExpect(status().isOk());

        // Validate the ExamQuizzPool in the database
        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeUpdate);
        ExamQuizzPoolEntity testExamQuizzPool = examQuizzPoolList.get(examQuizzPoolList.size() - 1);
        assertThat(testExamQuizzPool.getRootId()).isEqualTo(DEFAULT_ROOT_ID);
        assertThat(testExamQuizzPool.getSourceId()).isEqualTo(DEFAULT_SOURCE_ID);
    }

    @Test
    @Transactional
    void fullUpdateExamQuizzPoolWithPatch() throws Exception {
        // Initialize the database
        examQuizzPoolRepository.saveAndFlush(examQuizzPoolEntity);

        int databaseSizeBeforeUpdate = examQuizzPoolRepository.findAll().size();

        // Update the examQuizzPool using partial update
        ExamQuizzPoolEntity partialUpdatedExamQuizzPoolEntity = new ExamQuizzPoolEntity();
        partialUpdatedExamQuizzPoolEntity.setId(examQuizzPoolEntity.getId());

        partialUpdatedExamQuizzPoolEntity.rootId(UPDATED_ROOT_ID).sourceId(UPDATED_SOURCE_ID);

        restExamQuizzPoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamQuizzPoolEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamQuizzPoolEntity))
            )
            .andExpect(status().isOk());

        // Validate the ExamQuizzPool in the database
        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeUpdate);
        ExamQuizzPoolEntity testExamQuizzPool = examQuizzPoolList.get(examQuizzPoolList.size() - 1);
        assertThat(testExamQuizzPool.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testExamQuizzPool.getSourceId()).isEqualTo(UPDATED_SOURCE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingExamQuizzPool() throws Exception {
        int databaseSizeBeforeUpdate = examQuizzPoolRepository.findAll().size();
        examQuizzPoolEntity.setId(count.incrementAndGet());

        // Create the ExamQuizzPool
        ExamQuizzPoolDTO examQuizzPoolDTO = examQuizzPoolMapper.toDto(examQuizzPoolEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamQuizzPoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examQuizzPoolDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examQuizzPoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamQuizzPool in the database
        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExamQuizzPool() throws Exception {
        int databaseSizeBeforeUpdate = examQuizzPoolRepository.findAll().size();
        examQuizzPoolEntity.setId(count.incrementAndGet());

        // Create the ExamQuizzPool
        ExamQuizzPoolDTO examQuizzPoolDTO = examQuizzPoolMapper.toDto(examQuizzPoolEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamQuizzPoolMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examQuizzPoolDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamQuizzPool in the database
        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExamQuizzPool() throws Exception {
        int databaseSizeBeforeUpdate = examQuizzPoolRepository.findAll().size();
        examQuizzPoolEntity.setId(count.incrementAndGet());

        // Create the ExamQuizzPool
        ExamQuizzPoolDTO examQuizzPoolDTO = examQuizzPoolMapper.toDto(examQuizzPoolEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamQuizzPoolMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examQuizzPoolDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamQuizzPool in the database
        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExamQuizzPool() throws Exception {
        // Initialize the database
        examQuizzPoolRepository.saveAndFlush(examQuizzPoolEntity);

        int databaseSizeBeforeDelete = examQuizzPoolRepository.findAll().size();

        // Delete the examQuizzPool
        restExamQuizzPoolMockMvc
            .perform(delete(ENTITY_API_URL_ID, examQuizzPoolEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExamQuizzPoolEntity> examQuizzPoolList = examQuizzPoolRepository.findAll();
        assertThat(examQuizzPoolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
