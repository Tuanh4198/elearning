package io.yody.yodemy.elearning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.yody.yodemy.IntegrationTest;
import io.yody.yodemy.elearning.domain.QuizzAnswerEntity;
import io.yody.yodemy.elearning.repository.QuizzAnswerRepository;
import io.yody.yodemy.elearning.service.dto.QuizzAnswerDTO;
import io.yody.yodemy.elearning.service.mapper.QuizzAnswerMapper;
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
 * Integration tests for the {@link QuizzAnswerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizzAnswerResourceIT {

    private static final Long DEFAULT_ROOT_ID = 1L;
    private static final Long UPDATED_ROOT_ID = 2L;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quizz-answers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuizzAnswerRepository quizzAnswerRepository;

    @Autowired
    private QuizzAnswerMapper quizzAnswerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizzAnswerMockMvc;

    private QuizzAnswerEntity quizzAnswerEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizzAnswerEntity createEntity(EntityManager em) {
        QuizzAnswerEntity quizzAnswerEntity = new QuizzAnswerEntity().rootId(DEFAULT_ROOT_ID).title(DEFAULT_TITLE).content(DEFAULT_CONTENT);
        return quizzAnswerEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizzAnswerEntity createUpdatedEntity(EntityManager em) {
        QuizzAnswerEntity quizzAnswerEntity = new QuizzAnswerEntity().rootId(UPDATED_ROOT_ID).title(UPDATED_TITLE).content(UPDATED_CONTENT);
        return quizzAnswerEntity;
    }

    @BeforeEach
    public void initTest() {
        quizzAnswerEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createQuizzAnswer() throws Exception {
        int databaseSizeBeforeCreate = quizzAnswerRepository.findAll().size();
        // Create the QuizzAnswer
        QuizzAnswerDTO quizzAnswerDTO = quizzAnswerMapper.toDto(quizzAnswerEntity);
        restQuizzAnswerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzAnswerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the QuizzAnswer in the database
        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        QuizzAnswerEntity testQuizzAnswer = quizzAnswerList.get(quizzAnswerList.size() - 1);
        assertThat(testQuizzAnswer.getRootId()).isEqualTo(DEFAULT_ROOT_ID);
        assertThat(testQuizzAnswer.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuizzAnswer.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void createQuizzAnswerWithExistingId() throws Exception {
        // Create the QuizzAnswer with an existing ID
        quizzAnswerEntity.setId(1L);
        QuizzAnswerDTO quizzAnswerDTO = quizzAnswerMapper.toDto(quizzAnswerEntity);

        int databaseSizeBeforeCreate = quizzAnswerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizzAnswerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizzAnswer in the database
        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzAnswerRepository.findAll().size();
        // set the field null
        quizzAnswerEntity.setContent(null);

        // Create the QuizzAnswer, which fails.
        QuizzAnswerDTO quizzAnswerDTO = quizzAnswerMapper.toDto(quizzAnswerEntity);

        restQuizzAnswerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuizzAnswers() throws Exception {
        // Initialize the database
        quizzAnswerRepository.saveAndFlush(quizzAnswerEntity);

        // Get all the quizzAnswerList
        restQuizzAnswerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizzAnswerEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].rootId").value(hasItem(DEFAULT_ROOT_ID.intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }

    @Test
    @Transactional
    void getQuizzAnswer() throws Exception {
        // Initialize the database
        quizzAnswerRepository.saveAndFlush(quizzAnswerEntity);

        // Get the quizzAnswer
        restQuizzAnswerMockMvc
            .perform(get(ENTITY_API_URL_ID, quizzAnswerEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizzAnswerEntity.getId().intValue()))
            .andExpect(jsonPath("$.rootId").value(DEFAULT_ROOT_ID.intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }

    @Test
    @Transactional
    void getNonExistingQuizzAnswer() throws Exception {
        // Get the quizzAnswer
        restQuizzAnswerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuizzAnswer() throws Exception {
        // Initialize the database
        quizzAnswerRepository.saveAndFlush(quizzAnswerEntity);

        int databaseSizeBeforeUpdate = quizzAnswerRepository.findAll().size();

        // Update the quizzAnswer
        QuizzAnswerEntity updatedQuizzAnswerEntity = quizzAnswerRepository.findById(quizzAnswerEntity.getId()).get();
        // Disconnect from session so that the updates on updatedQuizzAnswerEntity are not directly saved in db
        em.detach(updatedQuizzAnswerEntity);
        updatedQuizzAnswerEntity.rootId(UPDATED_ROOT_ID).title(UPDATED_TITLE).content(UPDATED_CONTENT);
        QuizzAnswerDTO quizzAnswerDTO = quizzAnswerMapper.toDto(updatedQuizzAnswerEntity);

        restQuizzAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizzAnswerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzAnswerDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuizzAnswer in the database
        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeUpdate);
        QuizzAnswerEntity testQuizzAnswer = quizzAnswerList.get(quizzAnswerList.size() - 1);
        assertThat(testQuizzAnswer.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testQuizzAnswer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuizzAnswer.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void putNonExistingQuizzAnswer() throws Exception {
        int databaseSizeBeforeUpdate = quizzAnswerRepository.findAll().size();
        quizzAnswerEntity.setId(count.incrementAndGet());

        // Create the QuizzAnswer
        QuizzAnswerDTO quizzAnswerDTO = quizzAnswerMapper.toDto(quizzAnswerEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizzAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizzAnswerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizzAnswer in the database
        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizzAnswer() throws Exception {
        int databaseSizeBeforeUpdate = quizzAnswerRepository.findAll().size();
        quizzAnswerEntity.setId(count.incrementAndGet());

        // Create the QuizzAnswer
        QuizzAnswerDTO quizzAnswerDTO = quizzAnswerMapper.toDto(quizzAnswerEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzAnswerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizzAnswer in the database
        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizzAnswer() throws Exception {
        int databaseSizeBeforeUpdate = quizzAnswerRepository.findAll().size();
        quizzAnswerEntity.setId(count.incrementAndGet());

        // Create the QuizzAnswer
        QuizzAnswerDTO quizzAnswerDTO = quizzAnswerMapper.toDto(quizzAnswerEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzAnswerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzAnswerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizzAnswer in the database
        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizzAnswerWithPatch() throws Exception {
        // Initialize the database
        quizzAnswerRepository.saveAndFlush(quizzAnswerEntity);

        int databaseSizeBeforeUpdate = quizzAnswerRepository.findAll().size();

        // Update the quizzAnswer using partial update
        QuizzAnswerEntity partialUpdatedQuizzAnswerEntity = new QuizzAnswerEntity();
        partialUpdatedQuizzAnswerEntity.setId(quizzAnswerEntity.getId());

        partialUpdatedQuizzAnswerEntity.title(UPDATED_TITLE);

        restQuizzAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizzAnswerEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizzAnswerEntity))
            )
            .andExpect(status().isOk());

        // Validate the QuizzAnswer in the database
        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeUpdate);
        QuizzAnswerEntity testQuizzAnswer = quizzAnswerList.get(quizzAnswerList.size() - 1);
        assertThat(testQuizzAnswer.getRootId()).isEqualTo(DEFAULT_ROOT_ID);
        assertThat(testQuizzAnswer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuizzAnswer.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void fullUpdateQuizzAnswerWithPatch() throws Exception {
        // Initialize the database
        quizzAnswerRepository.saveAndFlush(quizzAnswerEntity);

        int databaseSizeBeforeUpdate = quizzAnswerRepository.findAll().size();

        // Update the quizzAnswer using partial update
        QuizzAnswerEntity partialUpdatedQuizzAnswerEntity = new QuizzAnswerEntity();
        partialUpdatedQuizzAnswerEntity.setId(quizzAnswerEntity.getId());

        partialUpdatedQuizzAnswerEntity.rootId(UPDATED_ROOT_ID).title(UPDATED_TITLE).content(UPDATED_CONTENT);

        restQuizzAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizzAnswerEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizzAnswerEntity))
            )
            .andExpect(status().isOk());

        // Validate the QuizzAnswer in the database
        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeUpdate);
        QuizzAnswerEntity testQuizzAnswer = quizzAnswerList.get(quizzAnswerList.size() - 1);
        assertThat(testQuizzAnswer.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testQuizzAnswer.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuizzAnswer.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void patchNonExistingQuizzAnswer() throws Exception {
        int databaseSizeBeforeUpdate = quizzAnswerRepository.findAll().size();
        quizzAnswerEntity.setId(count.incrementAndGet());

        // Create the QuizzAnswer
        QuizzAnswerDTO quizzAnswerDTO = quizzAnswerMapper.toDto(quizzAnswerEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizzAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizzAnswerDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizzAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizzAnswer in the database
        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizzAnswer() throws Exception {
        int databaseSizeBeforeUpdate = quizzAnswerRepository.findAll().size();
        quizzAnswerEntity.setId(count.incrementAndGet());

        // Create the QuizzAnswer
        QuizzAnswerDTO quizzAnswerDTO = quizzAnswerMapper.toDto(quizzAnswerEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizzAnswerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizzAnswer in the database
        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizzAnswer() throws Exception {
        int databaseSizeBeforeUpdate = quizzAnswerRepository.findAll().size();
        quizzAnswerEntity.setId(count.incrementAndGet());

        // Create the QuizzAnswer
        QuizzAnswerDTO quizzAnswerDTO = quizzAnswerMapper.toDto(quizzAnswerEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzAnswerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizzAnswerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizzAnswer in the database
        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuizzAnswer() throws Exception {
        // Initialize the database
        quizzAnswerRepository.saveAndFlush(quizzAnswerEntity);

        int databaseSizeBeforeDelete = quizzAnswerRepository.findAll().size();

        // Delete the quizzAnswer
        restQuizzAnswerMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizzAnswerEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuizzAnswerEntity> quizzAnswerList = quizzAnswerRepository.findAll();
        assertThat(quizzAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
