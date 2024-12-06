package io.yody.yodemy.elearning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.yody.yodemy.IntegrationTest;
import io.yody.yodemy.elearning.domain.ExamEmployeeResultEntity;
import io.yody.yodemy.elearning.repository.ExamEmployeeResultRepository;
import io.yody.yodemy.elearning.service.dto.ExamEmployeeResultDTO;
import io.yody.yodemy.elearning.service.mapper.ExamEmployeeResultMapper;
import io.yody.yodemy.web.rest.TestUtil;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ExamEmployeeResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamEmployeeResultResourceIT {

    private static final Long DEFAULT_ROOT_ID = 1L;
    private static final Long UPDATED_ROOT_ID = 2L;

    private static final Instant DEFAULT_START_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_FINISHED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FINISHED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_NUMBER_OF_CORRECT = 1L;
    private static final Long UPDATED_NUMBER_OF_CORRECT = 2L;

    private static final Long DEFAULT_NUMBER_OF_QUESTION = 1L;
    private static final Long UPDATED_NUMBER_OF_QUESTION = 2L;

    private static final String ENTITY_API_URL = "/api/exam-employee-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExamEmployeeResultRepository examEmployeeResultRepository;

    @Autowired
    private ExamEmployeeResultMapper examEmployeeResultMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamEmployeeResultMockMvc;

    private ExamEmployeeResultEntity examEmployeeResultEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamEmployeeResultEntity createEntity(EntityManager em) {
        ExamEmployeeResultEntity examEmployeeResultEntity = new ExamEmployeeResultEntity()
            .rootId(DEFAULT_ROOT_ID)
            .startAt(DEFAULT_START_AT)
            .finishedAt(DEFAULT_FINISHED_AT)
            .numberOfCorrect(DEFAULT_NUMBER_OF_CORRECT)
            .numberOfQuestion(DEFAULT_NUMBER_OF_QUESTION);
        return examEmployeeResultEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamEmployeeResultEntity createUpdatedEntity(EntityManager em) {
        ExamEmployeeResultEntity examEmployeeResultEntity = new ExamEmployeeResultEntity()
            .rootId(UPDATED_ROOT_ID)
            .startAt(UPDATED_START_AT)
            .finishedAt(UPDATED_FINISHED_AT)
            .numberOfCorrect(UPDATED_NUMBER_OF_CORRECT)
            .numberOfQuestion(UPDATED_NUMBER_OF_QUESTION);
        return examEmployeeResultEntity;
    }

    @BeforeEach
    public void initTest() {
        examEmployeeResultEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createExamEmployeeResult() throws Exception {
        int databaseSizeBeforeCreate = examEmployeeResultRepository.findAll().size();
        // Create the ExamEmployeeResult
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);
        restExamEmployeeResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExamEmployeeResult in the database
        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeCreate + 1);
        ExamEmployeeResultEntity testExamEmployeeResult = examEmployeeResultList.get(examEmployeeResultList.size() - 1);
        assertThat(testExamEmployeeResult.getRootId()).isEqualTo(DEFAULT_ROOT_ID);
        assertThat(testExamEmployeeResult.getStartAt()).isEqualTo(DEFAULT_START_AT);
        assertThat(testExamEmployeeResult.getFinishedAt()).isEqualTo(DEFAULT_FINISHED_AT);
        assertThat(testExamEmployeeResult.getNumberOfCorrect()).isEqualTo(DEFAULT_NUMBER_OF_CORRECT);
        assertThat(testExamEmployeeResult.getNumberOfQuestion()).isEqualTo(DEFAULT_NUMBER_OF_QUESTION);
    }

    @Test
    @Transactional
    void createExamEmployeeResultWithExistingId() throws Exception {
        // Create the ExamEmployeeResult with an existing ID
        examEmployeeResultEntity.setId(1L);
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);

        int databaseSizeBeforeCreate = examEmployeeResultRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamEmployeeResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamEmployeeResult in the database
        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRootIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = examEmployeeResultRepository.findAll().size();
        // set the field null
        examEmployeeResultEntity.setRootId(null);

        // Create the ExamEmployeeResult, which fails.
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);

        restExamEmployeeResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = examEmployeeResultRepository.findAll().size();
        // set the field null
        examEmployeeResultEntity.setStartAt(null);

        // Create the ExamEmployeeResult, which fails.
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);

        restExamEmployeeResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFinishedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = examEmployeeResultRepository.findAll().size();
        // set the field null
        examEmployeeResultEntity.setFinishedAt(null);

        // Create the ExamEmployeeResult, which fails.
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);

        restExamEmployeeResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfCorrectIsRequired() throws Exception {
        int databaseSizeBeforeTest = examEmployeeResultRepository.findAll().size();
        // set the field null
        examEmployeeResultEntity.setNumberOfCorrect(null);

        // Create the ExamEmployeeResult, which fails.
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);

        restExamEmployeeResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfQuestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = examEmployeeResultRepository.findAll().size();
        // set the field null
        examEmployeeResultEntity.setNumberOfQuestion(null);

        // Create the ExamEmployeeResult, which fails.
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);

        restExamEmployeeResultMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExamEmployeeResults() throws Exception {
        // Initialize the database
        examEmployeeResultRepository.saveAndFlush(examEmployeeResultEntity);

        // Get all the examEmployeeResultList
        restExamEmployeeResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examEmployeeResultEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].rootId").value(hasItem(DEFAULT_ROOT_ID.intValue())))
            .andExpect(jsonPath("$.[*].startAt").value(hasItem(DEFAULT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].finishedAt").value(hasItem(DEFAULT_FINISHED_AT.toString())))
            .andExpect(jsonPath("$.[*].numberOfCorrect").value(hasItem(DEFAULT_NUMBER_OF_CORRECT.intValue())))
            .andExpect(jsonPath("$.[*].numberOfQuestion").value(hasItem(DEFAULT_NUMBER_OF_QUESTION.intValue())));
    }

    @Test
    @Transactional
    void getExamEmployeeResult() throws Exception {
        // Initialize the database
        examEmployeeResultRepository.saveAndFlush(examEmployeeResultEntity);

        // Get the examEmployeeResult
        restExamEmployeeResultMockMvc
            .perform(get(ENTITY_API_URL_ID, examEmployeeResultEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examEmployeeResultEntity.getId().intValue()))
            .andExpect(jsonPath("$.rootId").value(DEFAULT_ROOT_ID.intValue()))
            .andExpect(jsonPath("$.startAt").value(DEFAULT_START_AT.toString()))
            .andExpect(jsonPath("$.finishedAt").value(DEFAULT_FINISHED_AT.toString()))
            .andExpect(jsonPath("$.numberOfCorrect").value(DEFAULT_NUMBER_OF_CORRECT.intValue()))
            .andExpect(jsonPath("$.numberOfQuestion").value(DEFAULT_NUMBER_OF_QUESTION.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingExamEmployeeResult() throws Exception {
        // Get the examEmployeeResult
        restExamEmployeeResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExamEmployeeResult() throws Exception {
        // Initialize the database
        examEmployeeResultRepository.saveAndFlush(examEmployeeResultEntity);

        int databaseSizeBeforeUpdate = examEmployeeResultRepository.findAll().size();

        // Update the examEmployeeResult
        ExamEmployeeResultEntity updatedExamEmployeeResultEntity = examEmployeeResultRepository
            .findById(examEmployeeResultEntity.getId())
            .get();
        // Disconnect from session so that the updates on updatedExamEmployeeResultEntity are not directly saved in db
        em.detach(updatedExamEmployeeResultEntity);
        updatedExamEmployeeResultEntity
            .rootId(UPDATED_ROOT_ID)
            .startAt(UPDATED_START_AT)
            .finishedAt(UPDATED_FINISHED_AT)
            .numberOfCorrect(UPDATED_NUMBER_OF_CORRECT)
            .numberOfQuestion(UPDATED_NUMBER_OF_QUESTION);
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(updatedExamEmployeeResultEntity);

        restExamEmployeeResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examEmployeeResultDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExamEmployeeResult in the database
        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeUpdate);
        ExamEmployeeResultEntity testExamEmployeeResult = examEmployeeResultList.get(examEmployeeResultList.size() - 1);
        assertThat(testExamEmployeeResult.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testExamEmployeeResult.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testExamEmployeeResult.getFinishedAt()).isEqualTo(UPDATED_FINISHED_AT);
        assertThat(testExamEmployeeResult.getNumberOfCorrect()).isEqualTo(UPDATED_NUMBER_OF_CORRECT);
        assertThat(testExamEmployeeResult.getNumberOfQuestion()).isEqualTo(UPDATED_NUMBER_OF_QUESTION);
    }

    @Test
    @Transactional
    void putNonExistingExamEmployeeResult() throws Exception {
        int databaseSizeBeforeUpdate = examEmployeeResultRepository.findAll().size();
        examEmployeeResultEntity.setId(count.incrementAndGet());

        // Create the ExamEmployeeResult
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamEmployeeResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examEmployeeResultDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamEmployeeResult in the database
        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExamEmployeeResult() throws Exception {
        int databaseSizeBeforeUpdate = examEmployeeResultRepository.findAll().size();
        examEmployeeResultEntity.setId(count.incrementAndGet());

        // Create the ExamEmployeeResult
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamEmployeeResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamEmployeeResult in the database
        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExamEmployeeResult() throws Exception {
        int databaseSizeBeforeUpdate = examEmployeeResultRepository.findAll().size();
        examEmployeeResultEntity.setId(count.incrementAndGet());

        // Create the ExamEmployeeResult
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamEmployeeResultMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamEmployeeResult in the database
        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamEmployeeResultWithPatch() throws Exception {
        // Initialize the database
        examEmployeeResultRepository.saveAndFlush(examEmployeeResultEntity);

        int databaseSizeBeforeUpdate = examEmployeeResultRepository.findAll().size();

        // Update the examEmployeeResult using partial update
        ExamEmployeeResultEntity partialUpdatedExamEmployeeResultEntity = new ExamEmployeeResultEntity();
        partialUpdatedExamEmployeeResultEntity.setId(examEmployeeResultEntity.getId());

        partialUpdatedExamEmployeeResultEntity
            .startAt(UPDATED_START_AT)
            .finishedAt(UPDATED_FINISHED_AT)
            .numberOfCorrect(UPDATED_NUMBER_OF_CORRECT)
            .numberOfQuestion(UPDATED_NUMBER_OF_QUESTION);

        restExamEmployeeResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamEmployeeResultEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamEmployeeResultEntity))
            )
            .andExpect(status().isOk());

        // Validate the ExamEmployeeResult in the database
        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeUpdate);
        ExamEmployeeResultEntity testExamEmployeeResult = examEmployeeResultList.get(examEmployeeResultList.size() - 1);
        assertThat(testExamEmployeeResult.getRootId()).isEqualTo(DEFAULT_ROOT_ID);
        assertThat(testExamEmployeeResult.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testExamEmployeeResult.getFinishedAt()).isEqualTo(UPDATED_FINISHED_AT);
        assertThat(testExamEmployeeResult.getNumberOfCorrect()).isEqualTo(UPDATED_NUMBER_OF_CORRECT);
        assertThat(testExamEmployeeResult.getNumberOfQuestion()).isEqualTo(UPDATED_NUMBER_OF_QUESTION);
    }

    @Test
    @Transactional
    void fullUpdateExamEmployeeResultWithPatch() throws Exception {
        // Initialize the database
        examEmployeeResultRepository.saveAndFlush(examEmployeeResultEntity);

        int databaseSizeBeforeUpdate = examEmployeeResultRepository.findAll().size();

        // Update the examEmployeeResult using partial update
        ExamEmployeeResultEntity partialUpdatedExamEmployeeResultEntity = new ExamEmployeeResultEntity();
        partialUpdatedExamEmployeeResultEntity.setId(examEmployeeResultEntity.getId());

        partialUpdatedExamEmployeeResultEntity
            .rootId(UPDATED_ROOT_ID)
            .startAt(UPDATED_START_AT)
            .finishedAt(UPDATED_FINISHED_AT)
            .numberOfCorrect(UPDATED_NUMBER_OF_CORRECT)
            .numberOfQuestion(UPDATED_NUMBER_OF_QUESTION);

        restExamEmployeeResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamEmployeeResultEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamEmployeeResultEntity))
            )
            .andExpect(status().isOk());

        // Validate the ExamEmployeeResult in the database
        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeUpdate);
        ExamEmployeeResultEntity testExamEmployeeResult = examEmployeeResultList.get(examEmployeeResultList.size() - 1);
        assertThat(testExamEmployeeResult.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testExamEmployeeResult.getStartAt()).isEqualTo(UPDATED_START_AT);
        assertThat(testExamEmployeeResult.getFinishedAt()).isEqualTo(UPDATED_FINISHED_AT);
        assertThat(testExamEmployeeResult.getNumberOfCorrect()).isEqualTo(UPDATED_NUMBER_OF_CORRECT);
        assertThat(testExamEmployeeResult.getNumberOfQuestion()).isEqualTo(UPDATED_NUMBER_OF_QUESTION);
    }

    @Test
    @Transactional
    void patchNonExistingExamEmployeeResult() throws Exception {
        int databaseSizeBeforeUpdate = examEmployeeResultRepository.findAll().size();
        examEmployeeResultEntity.setId(count.incrementAndGet());

        // Create the ExamEmployeeResult
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamEmployeeResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examEmployeeResultDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamEmployeeResult in the database
        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExamEmployeeResult() throws Exception {
        int databaseSizeBeforeUpdate = examEmployeeResultRepository.findAll().size();
        examEmployeeResultEntity.setId(count.incrementAndGet());

        // Create the ExamEmployeeResult
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamEmployeeResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamEmployeeResult in the database
        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExamEmployeeResult() throws Exception {
        int databaseSizeBeforeUpdate = examEmployeeResultRepository.findAll().size();
        examEmployeeResultEntity.setId(count.incrementAndGet());

        // Create the ExamEmployeeResult
        ExamEmployeeResultDTO examEmployeeResultDTO = examEmployeeResultMapper.toDto(examEmployeeResultEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamEmployeeResultMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeResultDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamEmployeeResult in the database
        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExamEmployeeResult() throws Exception {
        // Initialize the database
        examEmployeeResultRepository.saveAndFlush(examEmployeeResultEntity);

        int databaseSizeBeforeDelete = examEmployeeResultRepository.findAll().size();

        // Delete the examEmployeeResult
        restExamEmployeeResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, examEmployeeResultEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExamEmployeeResultEntity> examEmployeeResultList = examEmployeeResultRepository.findAll();
        assertThat(examEmployeeResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
