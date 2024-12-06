package io.yody.yodemy.elearning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.yody.yodemy.IntegrationTest;
import io.yody.yodemy.elearning.domain.ExamEmployeeEntity;
import io.yody.yodemy.elearning.domain.enumeration.ExamEmployeeStatusEnum;
import io.yody.yodemy.elearning.repository.ExamEmployeeRepository;
import io.yody.yodemy.elearning.service.dto.ExamEmployeeDTO;
import io.yody.yodemy.elearning.service.mapper.ExamEmployeeMapper;
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
 * Integration tests for the {@link ExamEmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamEmployeeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ROOT_ID = 1L;
    private static final Long UPDATED_ROOT_ID = 2L;

    private static final ExamEmployeeStatusEnum DEFAULT_STATUS = ExamEmployeeStatusEnum.NOT_ATTENDED;
    private static final ExamEmployeeStatusEnum UPDATED_STATUS = ExamEmployeeStatusEnum.PASS;

    private static final String ENTITY_API_URL = "/api/exam-employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExamEmployeeRepository examEmployeeRepository;

    @Autowired
    private ExamEmployeeMapper examEmployeeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamEmployeeMockMvc;

    private ExamEmployeeEntity examEmployeeEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamEmployeeEntity createEntity(EntityManager em) {
        ExamEmployeeEntity examEmployeeEntity = new ExamEmployeeEntity()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .rootId(DEFAULT_ROOT_ID)
            .status(DEFAULT_STATUS);
        return examEmployeeEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamEmployeeEntity createUpdatedEntity(EntityManager em) {
        ExamEmployeeEntity examEmployeeEntity = new ExamEmployeeEntity()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .rootId(UPDATED_ROOT_ID)
            .status(UPDATED_STATUS);
        return examEmployeeEntity;
    }

    @BeforeEach
    public void initTest() {
        examEmployeeEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createExamEmployee() throws Exception {
        int databaseSizeBeforeCreate = examEmployeeRepository.findAll().size();
        // Create the ExamEmployee
        ExamEmployeeDTO examEmployeeDTO = examEmployeeMapper.toDto(examEmployeeEntity);
        restExamEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ExamEmployee in the database
        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeCreate + 1);
        ExamEmployeeEntity testExamEmployee = examEmployeeList.get(examEmployeeList.size() - 1);
        assertThat(testExamEmployee.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testExamEmployee.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExamEmployee.getRootId()).isEqualTo(DEFAULT_ROOT_ID);
        assertThat(testExamEmployee.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createExamEmployeeWithExistingId() throws Exception {
        // Create the ExamEmployee with an existing ID
        examEmployeeEntity.setId(1L);
        ExamEmployeeDTO examEmployeeDTO = examEmployeeMapper.toDto(examEmployeeEntity);

        int databaseSizeBeforeCreate = examEmployeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamEmployee in the database
        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = examEmployeeRepository.findAll().size();
        // set the field null
        examEmployeeEntity.setCode(null);

        // Create the ExamEmployee, which fails.
        ExamEmployeeDTO examEmployeeDTO = examEmployeeMapper.toDto(examEmployeeEntity);

        restExamEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = examEmployeeRepository.findAll().size();
        // set the field null
        examEmployeeEntity.setName(null);

        // Create the ExamEmployee, which fails.
        ExamEmployeeDTO examEmployeeDTO = examEmployeeMapper.toDto(examEmployeeEntity);

        restExamEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRootIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = examEmployeeRepository.findAll().size();
        // set the field null
        examEmployeeEntity.setRootId(null);

        // Create the ExamEmployee, which fails.
        ExamEmployeeDTO examEmployeeDTO = examEmployeeMapper.toDto(examEmployeeEntity);

        restExamEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExamEmployees() throws Exception {
        // Initialize the database
        examEmployeeRepository.saveAndFlush(examEmployeeEntity);

        // Get all the examEmployeeList
        restExamEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examEmployeeEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rootId").value(hasItem(DEFAULT_ROOT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getExamEmployee() throws Exception {
        // Initialize the database
        examEmployeeRepository.saveAndFlush(examEmployeeEntity);

        // Get the examEmployee
        restExamEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, examEmployeeEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examEmployeeEntity.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.rootId").value(DEFAULT_ROOT_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExamEmployee() throws Exception {
        // Get the examEmployee
        restExamEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExamEmployee() throws Exception {
        // Initialize the database
        examEmployeeRepository.saveAndFlush(examEmployeeEntity);

        int databaseSizeBeforeUpdate = examEmployeeRepository.findAll().size();

        // Update the examEmployee
        ExamEmployeeEntity updatedExamEmployeeEntity = examEmployeeRepository.findById(examEmployeeEntity.getId()).get();
        // Disconnect from session so that the updates on updatedExamEmployeeEntity are not directly saved in db
        em.detach(updatedExamEmployeeEntity);
        updatedExamEmployeeEntity.code(UPDATED_CODE).name(UPDATED_NAME).rootId(UPDATED_ROOT_ID).status(UPDATED_STATUS);
        ExamEmployeeDTO examEmployeeDTO = examEmployeeMapper.toDto(updatedExamEmployeeEntity);

        restExamEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examEmployeeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExamEmployee in the database
        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeUpdate);
        ExamEmployeeEntity testExamEmployee = examEmployeeList.get(examEmployeeList.size() - 1);
        assertThat(testExamEmployee.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testExamEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExamEmployee.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testExamEmployee.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingExamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = examEmployeeRepository.findAll().size();
        examEmployeeEntity.setId(count.incrementAndGet());

        // Create the ExamEmployee
        ExamEmployeeDTO examEmployeeDTO = examEmployeeMapper.toDto(examEmployeeEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examEmployeeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamEmployee in the database
        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = examEmployeeRepository.findAll().size();
        examEmployeeEntity.setId(count.incrementAndGet());

        // Create the ExamEmployee
        ExamEmployeeDTO examEmployeeDTO = examEmployeeMapper.toDto(examEmployeeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamEmployee in the database
        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = examEmployeeRepository.findAll().size();
        examEmployeeEntity.setId(count.incrementAndGet());

        // Create the ExamEmployee
        ExamEmployeeDTO examEmployeeDTO = examEmployeeMapper.toDto(examEmployeeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamEmployee in the database
        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamEmployeeWithPatch() throws Exception {
        // Initialize the database
        examEmployeeRepository.saveAndFlush(examEmployeeEntity);

        int databaseSizeBeforeUpdate = examEmployeeRepository.findAll().size();

        // Update the examEmployee using partial update
        ExamEmployeeEntity partialUpdatedExamEmployeeEntity = new ExamEmployeeEntity();
        partialUpdatedExamEmployeeEntity.setId(examEmployeeEntity.getId());

        partialUpdatedExamEmployeeEntity.name(UPDATED_NAME);

        restExamEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamEmployeeEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamEmployeeEntity))
            )
            .andExpect(status().isOk());

        // Validate the ExamEmployee in the database
        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeUpdate);
        ExamEmployeeEntity testExamEmployee = examEmployeeList.get(examEmployeeList.size() - 1);
        assertThat(testExamEmployee.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testExamEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExamEmployee.getRootId()).isEqualTo(DEFAULT_ROOT_ID);
        assertThat(testExamEmployee.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateExamEmployeeWithPatch() throws Exception {
        // Initialize the database
        examEmployeeRepository.saveAndFlush(examEmployeeEntity);

        int databaseSizeBeforeUpdate = examEmployeeRepository.findAll().size();

        // Update the examEmployee using partial update
        ExamEmployeeEntity partialUpdatedExamEmployeeEntity = new ExamEmployeeEntity();
        partialUpdatedExamEmployeeEntity.setId(examEmployeeEntity.getId());

        partialUpdatedExamEmployeeEntity.code(UPDATED_CODE).name(UPDATED_NAME).rootId(UPDATED_ROOT_ID).status(UPDATED_STATUS);

        restExamEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamEmployeeEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamEmployeeEntity))
            )
            .andExpect(status().isOk());

        // Validate the ExamEmployee in the database
        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeUpdate);
        ExamEmployeeEntity testExamEmployee = examEmployeeList.get(examEmployeeList.size() - 1);
        assertThat(testExamEmployee.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testExamEmployee.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExamEmployee.getRootId()).isEqualTo(UPDATED_ROOT_ID);
        assertThat(testExamEmployee.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingExamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = examEmployeeRepository.findAll().size();
        examEmployeeEntity.setId(count.incrementAndGet());

        // Create the ExamEmployee
        ExamEmployeeDTO examEmployeeDTO = examEmployeeMapper.toDto(examEmployeeEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examEmployeeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamEmployee in the database
        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = examEmployeeRepository.findAll().size();
        examEmployeeEntity.setId(count.incrementAndGet());

        // Create the ExamEmployee
        ExamEmployeeDTO examEmployeeDTO = examEmployeeMapper.toDto(examEmployeeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExamEmployee in the database
        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = examEmployeeRepository.findAll().size();
        examEmployeeEntity.setId(count.incrementAndGet());

        // Create the ExamEmployee
        ExamEmployeeDTO examEmployeeDTO = examEmployeeMapper.toDto(examEmployeeEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examEmployeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExamEmployee in the database
        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExamEmployee() throws Exception {
        // Initialize the database
        examEmployeeRepository.saveAndFlush(examEmployeeEntity);

        int databaseSizeBeforeDelete = examEmployeeRepository.findAll().size();

        // Delete the examEmployee
        restExamEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, examEmployeeEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExamEmployeeEntity> examEmployeeList = examEmployeeRepository.findAll();
        assertThat(examEmployeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
