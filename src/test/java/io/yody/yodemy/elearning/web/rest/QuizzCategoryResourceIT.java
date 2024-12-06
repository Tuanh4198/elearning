package io.yody.yodemy.elearning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.yody.yodemy.IntegrationTest;
import io.yody.yodemy.elearning.domain.QuizzCategoryEntity;
import io.yody.yodemy.elearning.repository.QuizzCategoryRepository;
import io.yody.yodemy.elearning.service.dto.QuizzCategoryDTO;
import io.yody.yodemy.elearning.service.mapper.QuizzCategoryMapper;
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
 * Integration tests for the {@link QuizzCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class QuizzCategoryResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/quizz-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private QuizzCategoryRepository quizzCategoryRepository;

    @Autowired
    private QuizzCategoryMapper quizzCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuizzCategoryMockMvc;

    private QuizzCategoryEntity quizzCategoryEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizzCategoryEntity createEntity(EntityManager em) {
        QuizzCategoryEntity quizzCategoryEntity = new QuizzCategoryEntity().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return quizzCategoryEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuizzCategoryEntity createUpdatedEntity(EntityManager em) {
        QuizzCategoryEntity quizzCategoryEntity = new QuizzCategoryEntity().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return quizzCategoryEntity;
    }

    @BeforeEach
    public void initTest() {
        quizzCategoryEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createQuizzCategory() throws Exception {
        int databaseSizeBeforeCreate = quizzCategoryRepository.findAll().size();
        // Create the QuizzCategory
        QuizzCategoryDTO quizzCategoryDTO = quizzCategoryMapper.toDto(quizzCategoryEntity);
        restQuizzCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzCategoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the QuizzCategory in the database
        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        QuizzCategoryEntity testQuizzCategory = quizzCategoryList.get(quizzCategoryList.size() - 1);
        assertThat(testQuizzCategory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testQuizzCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createQuizzCategoryWithExistingId() throws Exception {
        // Create the QuizzCategory with an existing ID
        quizzCategoryEntity.setId(1L);
        QuizzCategoryDTO quizzCategoryDTO = quizzCategoryMapper.toDto(quizzCategoryEntity);

        int databaseSizeBeforeCreate = quizzCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizzCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizzCategory in the database
        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = quizzCategoryRepository.findAll().size();
        // set the field null
        quizzCategoryEntity.setTitle(null);

        // Create the QuizzCategory, which fails.
        QuizzCategoryDTO quizzCategoryDTO = quizzCategoryMapper.toDto(quizzCategoryEntity);

        restQuizzCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllQuizzCategories() throws Exception {
        // Initialize the database
        quizzCategoryRepository.saveAndFlush(quizzCategoryEntity);

        // Get all the quizzCategoryList
        restQuizzCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quizzCategoryEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getQuizzCategory() throws Exception {
        // Initialize the database
        quizzCategoryRepository.saveAndFlush(quizzCategoryEntity);

        // Get the quizzCategory
        restQuizzCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, quizzCategoryEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quizzCategoryEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingQuizzCategory() throws Exception {
        // Get the quizzCategory
        restQuizzCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingQuizzCategory() throws Exception {
        // Initialize the database
        quizzCategoryRepository.saveAndFlush(quizzCategoryEntity);

        int databaseSizeBeforeUpdate = quizzCategoryRepository.findAll().size();

        // Update the quizzCategory
        QuizzCategoryEntity updatedQuizzCategoryEntity = quizzCategoryRepository.findById(quizzCategoryEntity.getId()).get();
        // Disconnect from session so that the updates on updatedQuizzCategoryEntity are not directly saved in db
        em.detach(updatedQuizzCategoryEntity);
        updatedQuizzCategoryEntity.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        QuizzCategoryDTO quizzCategoryDTO = quizzCategoryMapper.toDto(updatedQuizzCategoryEntity);

        restQuizzCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizzCategoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the QuizzCategory in the database
        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeUpdate);
        QuizzCategoryEntity testQuizzCategory = quizzCategoryList.get(quizzCategoryList.size() - 1);
        assertThat(testQuizzCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuizzCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingQuizzCategory() throws Exception {
        int databaseSizeBeforeUpdate = quizzCategoryRepository.findAll().size();
        quizzCategoryEntity.setId(count.incrementAndGet());

        // Create the QuizzCategory
        QuizzCategoryDTO quizzCategoryDTO = quizzCategoryMapper.toDto(quizzCategoryEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizzCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, quizzCategoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizzCategory in the database
        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchQuizzCategory() throws Exception {
        int databaseSizeBeforeUpdate = quizzCategoryRepository.findAll().size();
        quizzCategoryEntity.setId(count.incrementAndGet());

        // Create the QuizzCategory
        QuizzCategoryDTO quizzCategoryDTO = quizzCategoryMapper.toDto(quizzCategoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizzCategory in the database
        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamQuizzCategory() throws Exception {
        int databaseSizeBeforeUpdate = quizzCategoryRepository.findAll().size();
        quizzCategoryEntity.setId(count.incrementAndGet());

        // Create the QuizzCategory
        QuizzCategoryDTO quizzCategoryDTO = quizzCategoryMapper.toDto(quizzCategoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(quizzCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizzCategory in the database
        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateQuizzCategoryWithPatch() throws Exception {
        // Initialize the database
        quizzCategoryRepository.saveAndFlush(quizzCategoryEntity);

        int databaseSizeBeforeUpdate = quizzCategoryRepository.findAll().size();

        // Update the quizzCategory using partial update
        QuizzCategoryEntity partialUpdatedQuizzCategoryEntity = new QuizzCategoryEntity();
        partialUpdatedQuizzCategoryEntity.setId(quizzCategoryEntity.getId());

        partialUpdatedQuizzCategoryEntity.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restQuizzCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizzCategoryEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizzCategoryEntity))
            )
            .andExpect(status().isOk());

        // Validate the QuizzCategory in the database
        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeUpdate);
        QuizzCategoryEntity testQuizzCategory = quizzCategoryList.get(quizzCategoryList.size() - 1);
        assertThat(testQuizzCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuizzCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateQuizzCategoryWithPatch() throws Exception {
        // Initialize the database
        quizzCategoryRepository.saveAndFlush(quizzCategoryEntity);

        int databaseSizeBeforeUpdate = quizzCategoryRepository.findAll().size();

        // Update the quizzCategory using partial update
        QuizzCategoryEntity partialUpdatedQuizzCategoryEntity = new QuizzCategoryEntity();
        partialUpdatedQuizzCategoryEntity.setId(quizzCategoryEntity.getId());

        partialUpdatedQuizzCategoryEntity.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restQuizzCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedQuizzCategoryEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedQuizzCategoryEntity))
            )
            .andExpect(status().isOk());

        // Validate the QuizzCategory in the database
        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeUpdate);
        QuizzCategoryEntity testQuizzCategory = quizzCategoryList.get(quizzCategoryList.size() - 1);
        assertThat(testQuizzCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testQuizzCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingQuizzCategory() throws Exception {
        int databaseSizeBeforeUpdate = quizzCategoryRepository.findAll().size();
        quizzCategoryEntity.setId(count.incrementAndGet());

        // Create the QuizzCategory
        QuizzCategoryDTO quizzCategoryDTO = quizzCategoryMapper.toDto(quizzCategoryEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizzCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, quizzCategoryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizzCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizzCategory in the database
        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchQuizzCategory() throws Exception {
        int databaseSizeBeforeUpdate = quizzCategoryRepository.findAll().size();
        quizzCategoryEntity.setId(count.incrementAndGet());

        // Create the QuizzCategory
        QuizzCategoryDTO quizzCategoryDTO = quizzCategoryMapper.toDto(quizzCategoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizzCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the QuizzCategory in the database
        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamQuizzCategory() throws Exception {
        int databaseSizeBeforeUpdate = quizzCategoryRepository.findAll().size();
        quizzCategoryEntity.setId(count.incrementAndGet());

        // Create the QuizzCategory
        QuizzCategoryDTO quizzCategoryDTO = quizzCategoryMapper.toDto(quizzCategoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restQuizzCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(quizzCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the QuizzCategory in the database
        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteQuizzCategory() throws Exception {
        // Initialize the database
        quizzCategoryRepository.saveAndFlush(quizzCategoryEntity);

        int databaseSizeBeforeDelete = quizzCategoryRepository.findAll().size();

        // Delete the quizzCategory
        restQuizzCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, quizzCategoryEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuizzCategoryEntity> quizzCategoryList = quizzCategoryRepository.findAll();
        assertThat(quizzCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
