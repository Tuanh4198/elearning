package io.yody.yodemy.elearning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.yody.yodemy.IntegrationTest;
import io.yody.yodemy.elearning.domain.CourseEmployeeEntity;
import io.yody.yodemy.elearning.domain.enumeration.CourseEmployeeStatusEnum;
import io.yody.yodemy.elearning.repository.CourseEmployeeRepository;
import io.yody.yodemy.elearning.service.dto.CourseEmployeeDTO;
import io.yody.yodemy.elearning.service.mapper.CourseEmployeeMapper;
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
 * Integration tests for the {@link CourseEmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseEmployeeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ROOT_ID = 1L;
    private static final Long UPDATED_ROOT_ID = 2L;

    private static final CourseEmployeeStatusEnum DEFAULT_STATUS = CourseEmployeeStatusEnum.ATTENDED;
    private static final CourseEmployeeStatusEnum UPDATED_STATUS = CourseEmployeeStatusEnum.NOT_ATTENDED;

    private static final String ENTITY_API_URL = "/api/course-employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseEmployeeRepository courseEmployeeRepository;

    @Autowired
    private CourseEmployeeMapper courseEmployeeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseEmployeeMockMvc;

    private CourseEmployeeEntity courseEmployeeEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseEmployeeEntity createEntity(EntityManager em) {
        CourseEmployeeEntity courseEmployeeEntity = new CourseEmployeeEntity()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .rootId(DEFAULT_ROOT_ID)
            .status(DEFAULT_STATUS);
        return courseEmployeeEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseEmployeeEntity createUpdatedEntity(EntityManager em) {
        CourseEmployeeEntity courseEmployeeEntity = new CourseEmployeeEntity()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .rootId(UPDATED_ROOT_ID)
            .status(UPDATED_STATUS);
        return courseEmployeeEntity;
    }

    @BeforeEach
    public void initTest() {
        courseEmployeeEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseEmployee() throws Exception {
        int databaseSizeBeforeCreate = courseEmployeeRepository.findAll().size();
        // Create the CourseEmployee
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeMapper.toDto(courseEmployeeEntity);
        restCourseEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseEmployeeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CourseEmployee in the database
        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeCreate + 1);
        CourseEmployeeEntity testCourseEmployee = courseEmployeeList.get(courseEmployeeList.size() - 1);
        assertThat(testCourseEmployee.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCourseEmployee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourseEmployee.getRootId()).isEqualTo(DEFAULT_ROOT_ID);
        assertThat(testCourseEmployee.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createCourseEmployeeWithExistingId() throws Exception {
        // Create the CourseEmployee with an existing ID
        courseEmployeeEntity.setId(1L);
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeMapper.toDto(courseEmployeeEntity);

        int databaseSizeBeforeCreate = courseEmployeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEmployee in the database
        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseEmployeeRepository.findAll().size();
        // set the field null
        courseEmployeeEntity.setCode(null);

        // Create the CourseEmployee, which fails.
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeMapper.toDto(courseEmployeeEntity);

        restCourseEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseEmployeeRepository.findAll().size();
        // set the field null
        courseEmployeeEntity.setName(null);

        // Create the CourseEmployee, which fails.
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeMapper.toDto(courseEmployeeEntity);

        restCourseEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRootIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseEmployeeRepository.findAll().size();
        // set the field null
        courseEmployeeEntity.setRootId(null);

        // Create the CourseEmployee, which fails.
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeMapper.toDto(courseEmployeeEntity);

        restCourseEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourseEmployees() throws Exception {
        // Initialize the database
        courseEmployeeRepository.saveAndFlush(courseEmployeeEntity);

        // Get all the courseEmployeeList
        restCourseEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseEmployeeEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rootId").value(hasItem(DEFAULT_ROOT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getCourseEmployee() throws Exception {
        // Initialize the database
        courseEmployeeRepository.saveAndFlush(courseEmployeeEntity);

        // Get the courseEmployee
        restCourseEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, courseEmployeeEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseEmployeeEntity.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.rootId").value(DEFAULT_ROOT_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCourseEmployee() throws Exception {
        // Get the courseEmployee
        restCourseEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCourseEmployee() throws Exception {
        // Initialize the database
        courseEmployeeRepository.saveAndFlush(courseEmployeeEntity);

        int databaseSizeBeforeUpdate = courseEmployeeRepository.findAll().size();

        // Update the courseEmployee
        CourseEmployeeEntity updatedCourseEmployeeEntity = courseEmployeeRepository.findById(courseEmployeeEntity.getId()).get();
        // Disconnect from session so that the updates on updatedCourseEmployeeEntity are not directly saved in db
        em.detach(updatedCourseEmployeeEntity);
        updatedCourseEmployeeEntity.code(UPDATED_CODE).name(UPDATED_NAME).rootId(UPDATED_ROOT_ID).status(UPDATED_STATUS);
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeMapper.toDto(updatedCourseEmployeeEntity);

        restCourseEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseEmployeeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseEmployeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CourseEmployee in the database
        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeUpdate);
        CourseEmployeeEntity testCourseEmployee = courseEmployeeList.get(courseEmployeeList.size() - 1);
        assertThat(testCourseEmployee.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCourseEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourseEmployee.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testCourseEmployee.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingCourseEmployee() throws Exception {
        int databaseSizeBeforeUpdate = courseEmployeeRepository.findAll().size();
        courseEmployeeEntity.setId(count.incrementAndGet());

        // Create the CourseEmployee
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeMapper.toDto(courseEmployeeEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseEmployeeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEmployee in the database
        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseEmployee() throws Exception {
        int databaseSizeBeforeUpdate = courseEmployeeRepository.findAll().size();
        courseEmployeeEntity.setId(count.incrementAndGet());

        // Create the CourseEmployee
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeMapper.toDto(courseEmployeeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEmployee in the database
        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseEmployee() throws Exception {
        int databaseSizeBeforeUpdate = courseEmployeeRepository.findAll().size();
        courseEmployeeEntity.setId(count.incrementAndGet());

        // Create the CourseEmployee
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeMapper.toDto(courseEmployeeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseEmployeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseEmployee in the database
        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseEmployeeWithPatch() throws Exception {
        // Initialize the database
        courseEmployeeRepository.saveAndFlush(courseEmployeeEntity);

        int databaseSizeBeforeUpdate = courseEmployeeRepository.findAll().size();

        // Update the courseEmployee using partial update
        CourseEmployeeEntity partialUpdatedCourseEmployeeEntity = new CourseEmployeeEntity();
        partialUpdatedCourseEmployeeEntity.setId(courseEmployeeEntity.getId());

        partialUpdatedCourseEmployeeEntity.rootId(UPDATED_ROOT_ID).status(UPDATED_STATUS);

        restCourseEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseEmployeeEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseEmployeeEntity))
            )
            .andExpect(status().isOk());

        // Validate the CourseEmployee in the database
        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeUpdate);
        CourseEmployeeEntity testCourseEmployee = courseEmployeeList.get(courseEmployeeList.size() - 1);
        assertThat(testCourseEmployee.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCourseEmployee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourseEmployee.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testCourseEmployee.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateCourseEmployeeWithPatch() throws Exception {
        // Initialize the database
        courseEmployeeRepository.saveAndFlush(courseEmployeeEntity);

        int databaseSizeBeforeUpdate = courseEmployeeRepository.findAll().size();

        // Update the courseEmployee using partial update
        CourseEmployeeEntity partialUpdatedCourseEmployeeEntity = new CourseEmployeeEntity();
        partialUpdatedCourseEmployeeEntity.setId(courseEmployeeEntity.getId());

        partialUpdatedCourseEmployeeEntity.code(UPDATED_CODE).name(UPDATED_NAME).rootId(UPDATED_ROOT_ID).status(UPDATED_STATUS);

        restCourseEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseEmployeeEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseEmployeeEntity))
            )
            .andExpect(status().isOk());

        // Validate the CourseEmployee in the database
        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeUpdate);
        CourseEmployeeEntity testCourseEmployee = courseEmployeeList.get(courseEmployeeList.size() - 1);
        assertThat(testCourseEmployee.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCourseEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourseEmployee.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testCourseEmployee.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingCourseEmployee() throws Exception {
        int databaseSizeBeforeUpdate = courseEmployeeRepository.findAll().size();
        courseEmployeeEntity.setId(count.incrementAndGet());

        // Create the CourseEmployee
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeMapper.toDto(courseEmployeeEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseEmployeeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEmployee in the database
        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseEmployee() throws Exception {
        int databaseSizeBeforeUpdate = courseEmployeeRepository.findAll().size();
        courseEmployeeEntity.setId(count.incrementAndGet());

        // Create the CourseEmployee
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeMapper.toDto(courseEmployeeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseEmployee in the database
        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseEmployee() throws Exception {
        int databaseSizeBeforeUpdate = courseEmployeeRepository.findAll().size();
        courseEmployeeEntity.setId(count.incrementAndGet());

        // Create the CourseEmployee
        CourseEmployeeDTO courseEmployeeDTO = courseEmployeeMapper.toDto(courseEmployeeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseEmployeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseEmployee in the database
        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseEmployee() throws Exception {
        // Initialize the database
        courseEmployeeRepository.saveAndFlush(courseEmployeeEntity);

        int databaseSizeBeforeDelete = courseEmployeeRepository.findAll().size();

        // Delete the courseEmployee
        restCourseEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseEmployeeEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseEmployeeEntity> courseEmployeeList = courseEmployeeRepository.findAll();
        assertThat(courseEmployeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
