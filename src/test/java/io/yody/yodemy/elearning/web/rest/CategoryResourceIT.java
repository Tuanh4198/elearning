package io.yody.yodemy.elearning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.yody.yodemy.IntegrationTest;
import io.yody.yodemy.elearning.domain.CategoryEntity;
import io.yody.yodemy.elearning.repository.CategoryRepository;
import io.yody.yodemy.elearning.service.dto.CategoryDTO;
import io.yody.yodemy.elearning.service.mapper.CategoryMapper;
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
 * Integration tests for the {@link CategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryMockMvc;

    private CategoryEntity categoryEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryEntity createEntity(EntityManager em) {
        CategoryEntity categoryEntity = new CategoryEntity().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return categoryEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryEntity createUpdatedEntity(EntityManager em) {
        CategoryEntity categoryEntity = new CategoryEntity().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return categoryEntity;
    }

    @BeforeEach
    public void initTest() {
        categoryEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createCategory() throws Exception {
        int databaseSizeBeforeCreate = categoryRepository.findAll().size();
        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(categoryEntity);
        restCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Category in the database
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate + 1);
        CategoryEntity testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCategoryWithExistingId() throws Exception {
        // Create the Category with an existing ID
        categoryEntity.setId(1L);
        CategoryDTO categoryDTO = categoryMapper.toDto(categoryEntity);

        int databaseSizeBeforeCreate = categoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCategories() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(categoryEntity);

        // Get all the categoryList
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(categoryEntity);

        // Get the category
        restCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, categoryEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCategory() throws Exception {
        // Get the category
        restCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(categoryEntity);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category
        CategoryEntity updatedCategoryEntity = categoryRepository.findById(categoryEntity.getId()).get();
        // Disconnect from session so that the updates on updatedCategoryEntity are not directly saved in db
        em.detach(updatedCategoryEntity);
        updatedCategoryEntity.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        CategoryDTO categoryDTO = categoryMapper.toDto(updatedCategoryEntity);

        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        CategoryEntity testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        categoryEntity.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(categoryEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        categoryEntity.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(categoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        categoryEntity.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(categoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Category in the database
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(categoryEntity);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category using partial update
        CategoryEntity partialUpdatedCategoryEntity = new CategoryEntity();
        partialUpdatedCategoryEntity.setId(categoryEntity.getId());

        partialUpdatedCategoryEntity.title(UPDATED_TITLE);

        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoryEntity))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        CategoryEntity testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCategoryWithPatch() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(categoryEntity);

        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();

        // Update the category using partial update
        CategoryEntity partialUpdatedCategoryEntity = new CategoryEntity();
        partialUpdatedCategoryEntity.setId(categoryEntity.getId());

        partialUpdatedCategoryEntity.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoryEntity))
            )
            .andExpect(status().isOk());

        // Validate the Category in the database
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
        CategoryEntity testCategory = categoryList.get(categoryList.size() - 1);
        assertThat(testCategory.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        categoryEntity.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(categoryEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        categoryEntity.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(categoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Category in the database
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategory() throws Exception {
        int databaseSizeBeforeUpdate = categoryRepository.findAll().size();
        categoryEntity.setId(count.incrementAndGet());

        // Create the Category
        CategoryDTO categoryDTO = categoryMapper.toDto(categoryEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Category in the database
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategory() throws Exception {
        // Initialize the database
        categoryRepository.saveAndFlush(categoryEntity);

        int databaseSizeBeforeDelete = categoryRepository.findAll().size();

        // Delete the category
        restCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoryEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategoryEntity> categoryList = categoryRepository.findAll();
        assertThat(categoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
