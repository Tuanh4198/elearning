package io.yody.yodemy.elearning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.yody.yodemy.IntegrationTest;
import io.yody.yodemy.elearning.domain.QuizzEntity;
import io.yody.yodemy.elearning.domain.enumeration.QuizzTypeEnum;
import io.yody.yodemy.elearning.repository.QuizzRepository;
import io.yody.yodemy.elearning.service.dto.QuizzDTO;
import io.yody.yodemy.elearning.service.mapper.QuizzMapper;
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
 * Integration tests for the {@link QuizzResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizzResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final QuizzTypeEnum DEFAULT_TYPE = QuizzTypeEnum.ALL;
    private static final QuizzTypeEnum UPDATED_TYPE = QuizzTypeEnum.MULTIPLE_CHOICE;

    private static final String ENTITY_API_URL = "/api/quizzes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuizzRepository quizzRepository;

    @Autowired
    private QuizzMapper quizzMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizzMockMvc;

    private QuizzEntity quizzEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizzEntity createEntity(EntityManager em) {
        QuizzEntity quizzEntity = new QuizzEntity().content(DEFAULT_CONTENT).categoryId(DEFAULT_CATEGORY_ID).type(DEFAULT_TYPE);
        return quizzEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizzEntity createUpdatedEntity(EntityManager em) {
        QuizzEntity quizzEntity = new QuizzEntity().content(UPDATED_CONTENT).categoryId(UPDATED_CATEGORY_ID).type(UPDATED_TYPE);
        return quizzEntity;
    }

    @BeforeEach
    public void initTest() {
        quizzEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createQuizz() throws Exception {
        int databaseSizeBeforeCreate = quizzRepository.findAll().size();
        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizzEntity);
        restQuizzMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Quizz in the database
        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeCreate + 1);
        QuizzEntity testQuizz = quizzList.get(quizzList.size() - 1);
        assertThat(testQuizz.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testQuizz.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testQuizz.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createQuizzWithExistingId() throws Exception {
        // Create the Quizz with an existing ID
        quizzEntity.setId(1L);
        QuizzDTO quizzDTO = quizzMapper.toDto(quizzEntity);

        int databaseSizeBeforeCreate = quizzRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizzMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quizz in the database
        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzRepository.findAll().size();
        // set the field null
        quizzEntity.setContent(null);

        // Create the Quizz, which fails.
        QuizzDTO quizzDTO = quizzMapper.toDto(quizzEntity);

        restQuizzMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isBadRequest());

        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuizzes() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizzEntity);

        // Get all the quizzList
        restQuizzMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizzEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getQuizz() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizzEntity);

        // Get the quizz
        restQuizzMockMvc
            .perform(get(ENTITY_API_URL_ID, quizzEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizzEntity.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQuizz() throws Exception {
        // Get the quizz
        restQuizzMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuizz() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizzEntity);

        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();

        // Update the quizz
        QuizzEntity updatedQuizzEntity = quizzRepository.findById(quizzEntity.getId()).get();
        // Disconnect from session so that the updates on updatedQuizzEntity are not directly saved in db
        em.detach(updatedQuizzEntity);
        updatedQuizzEntity.content(UPDATED_CONTENT).categoryId(UPDATED_CATEGORY_ID).type(UPDATED_TYPE);
        QuizzDTO quizzDTO = quizzMapper.toDto(updatedQuizzEntity);

        restQuizzMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizzDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isOk());

        // Validate the Quizz in the database
        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
        QuizzEntity testQuizz = quizzList.get(quizzList.size() - 1);
        assertThat(testQuizz.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testQuizz.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testQuizz.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();
        quizzEntity.setId(count.incrementAndGet());

        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizzEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizzMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizzDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quizz in the database
        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();
        quizzEntity.setId(count.incrementAndGet());

        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizzEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quizz in the database
        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();
        quizzEntity.setId(count.incrementAndGet());

        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizzEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quizz in the database
        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizzWithPatch() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizzEntity);

        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();

        // Update the quizz using partial update
        QuizzEntity partialUpdatedQuizzEntity = new QuizzEntity();
        partialUpdatedQuizzEntity.setId(quizzEntity.getId());

        partialUpdatedQuizzEntity.type(UPDATED_TYPE);

        restQuizzMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizzEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizzEntity))
            )
            .andExpect(status().isOk());

        // Validate the Quizz in the database
        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
        QuizzEntity testQuizz = quizzList.get(quizzList.size() - 1);
        assertThat(testQuizz.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testQuizz.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testQuizz.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateQuizzWithPatch() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizzEntity);

        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();

        // Update the quizz using partial update
        QuizzEntity partialUpdatedQuizzEntity = new QuizzEntity();
        partialUpdatedQuizzEntity.setId(quizzEntity.getId());

        partialUpdatedQuizzEntity.content(UPDATED_CONTENT).categoryId(UPDATED_CATEGORY_ID).type(UPDATED_TYPE);

        restQuizzMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizzEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizzEntity))
            )
            .andExpect(status().isOk());

        // Validate the Quizz in the database
        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
        QuizzEntity testQuizz = quizzList.get(quizzList.size() - 1);
        assertThat(testQuizz.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testQuizz.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testQuizz.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();
        quizzEntity.setId(count.incrementAndGet());

        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizzEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizzMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizzDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quizz in the database
        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();
        quizzEntity.setId(count.incrementAndGet());

        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizzEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Quizz in the database
        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizz() throws Exception {
        int databaseSizeBeforeUpdate = quizzRepository.findAll().size();
        quizzEntity.setId(count.incrementAndGet());

        // Create the Quizz
        QuizzDTO quizzDTO = quizzMapper.toDto(quizzEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizzDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Quizz in the database
        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuizz() throws Exception {
        // Initialize the database
        quizzRepository.saveAndFlush(quizzEntity);

        int databaseSizeBeforeDelete = quizzRepository.findAll().size();

        // Delete the quizz
        restQuizzMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizzEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuizzEntity> quizzList = quizzRepository.findAll();
        assertThat(quizzList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
