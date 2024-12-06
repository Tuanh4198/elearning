package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.IntegrationTest;
import io.yody.yodemy.elearning.domain.ExamEntity;
import io.yody.yodemy.elearning.domain.enumeration.AssignStrategyEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamPointStrategyEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamQuizzPoolStrategyEnum;
import io.yody.yodemy.elearning.repository.ExamRepository;
import io.yody.yodemy.elearning.service.dto.ExamDTO;
import io.yody.yodemy.elearning.service.mapper.ExamMapper;
import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ExamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExamResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final Boolean DEFAULT_REQUIRE_JOIN = false;
    private static final Boolean UPDATED_REQUIRE_JOIN = true;

    private static final AssignStrategyEnum DEFAULT_ASSIGN_STRATEGY = AssignStrategyEnum.ANY_USERS;
    private static final AssignStrategyEnum UPDATED_ASSIGN_STRATEGY = AssignStrategyEnum.SPEC_POSITIONS;

    private static final String DEFAULT_ASSIGN_STRATEGY_JSON = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGN_STRATEGY_JSON = "BBBBBBBBBB";

    private static final Instant DEFAULT_APPLY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPLY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ExamPointStrategyEnum DEFAULT_POINT_STRATEGY = ExamPointStrategyEnum.PERCENTAGE;
    private static final ExamPointStrategyEnum UPDATED_POINT_STRATEGY = ExamPointStrategyEnum.PERCENTAGE;

    private static final Long DEFAULT_MIN_POINT_TO_PASS = 1L;
    private static final Long UPDATED_MIN_POINT_TO_PASS = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_THUMB_URL = "AAAAAAAAAA";
    private static final String UPDATED_THUMB_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_COURSE_ID = 1L;
    private static final Long UPDATED_COURSE_ID = 2L;

    private static final Long DEFAULT_NUMBER_OF_QUESTION = 1L;
    private static final Long UPDATED_NUMBER_OF_QUESTION = 2L;

    private static final ExamQuizzPoolStrategyEnum DEFAULT_STRATEGY = ExamQuizzPoolStrategyEnum.WEIGHT;
    private static final ExamQuizzPoolStrategyEnum UPDATED_STRATEGY = ExamQuizzPoolStrategyEnum.MANUAL;

    private static final String ENTITY_API_URL = "/api/exams";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamMapper examMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExamMockMvc;

    private ExamEntity examEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamEntity createEntity(EntityManager em) {
        ExamEntity examEntity = new ExamEntity()
            .title(DEFAULT_TITLE)
            .categoryId(DEFAULT_CATEGORY_ID)
            .requireJoin(DEFAULT_REQUIRE_JOIN)
            .applyTime(DEFAULT_APPLY_TIME)
            .expireTime(DEFAULT_EXPIRE_TIME)
            .pointStrategy(DEFAULT_POINT_STRATEGY)
            .minPointToPass(DEFAULT_MIN_POINT_TO_PASS)
            .description(DEFAULT_DESCRIPTION)
            .thumbUrl(DEFAULT_THUMB_URL)
            .courseId(DEFAULT_COURSE_ID)
            .numberOfQuestion(DEFAULT_NUMBER_OF_QUESTION)
            .poolStrategy(DEFAULT_STRATEGY);
        return examEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExamEntity createUpdatedEntity(EntityManager em) {
        ExamEntity examEntity = new ExamEntity()
            .title(UPDATED_TITLE)
            .categoryId(UPDATED_CATEGORY_ID)
            .requireJoin(UPDATED_REQUIRE_JOIN)
            .applyTime(UPDATED_APPLY_TIME)
            .expireTime(UPDATED_EXPIRE_TIME)
            .pointStrategy(UPDATED_POINT_STRATEGY)
            .minPointToPass(UPDATED_MIN_POINT_TO_PASS)
            .description(UPDATED_DESCRIPTION)
            .thumbUrl(UPDATED_THUMB_URL)
            .courseId(UPDATED_COURSE_ID)
            .numberOfQuestion(UPDATED_NUMBER_OF_QUESTION)
            .poolStrategy(UPDATED_STRATEGY);
        return examEntity;
    }

    @BeforeEach
    public void initTest() {
        examEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createExam() throws Exception {
        int databaseSizeBeforeCreate = examRepository.findAll().size();
        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(examEntity);
        restExamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Exam in the database
        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeCreate + 1);
        ExamEntity testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testExam.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testExam.getRequireJoin()).isEqualTo(DEFAULT_REQUIRE_JOIN);
        assertThat(testExam.getApplyTime()).isEqualTo(DEFAULT_APPLY_TIME);
        assertThat(testExam.getExpireTime()).isEqualTo(DEFAULT_EXPIRE_TIME);
        assertThat(testExam.getPointStrategy()).isEqualTo(DEFAULT_POINT_STRATEGY);
        assertThat(testExam.getMinPointToPass()).isEqualTo(DEFAULT_MIN_POINT_TO_PASS);
        assertThat(testExam.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExam.getThumbUrl()).isEqualTo(DEFAULT_THUMB_URL);
        assertThat(testExam.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testExam.getNumberOfQuestion()).isEqualTo(DEFAULT_NUMBER_OF_QUESTION);
        assertThat(testExam.getPoolStrategy()).isEqualTo(DEFAULT_STRATEGY);
    }

    @Test
    @Transactional
    void createExamWithExistingId() throws Exception {
        // Create the Exam with an existing ID
        examEntity.setId(1L);
        ExamDTO examDTO = examMapper.toDto(examEntity);

        int databaseSizeBeforeCreate = examRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        examEntity.setTitle(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(examEntity);

        restExamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApplyTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        examEntity.setApplyTime(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(examEntity);

        restExamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointStrategyIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        examEntity.setPointStrategy(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(examEntity);

        restExamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMinPointToPassIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        examEntity.setMinPointToPass(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(examEntity);

        restExamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfQuestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        examEntity.setNumberOfQuestion(null);

        // Create the Exam, which fails.
        ExamDTO examDTO = examMapper.toDto(examEntity);

        restExamMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExams() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(examEntity);

        // Get all the examList
        restExamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(examEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].requireJoin").value(hasItem(DEFAULT_REQUIRE_JOIN.booleanValue())))
            .andExpect(jsonPath("$.[*].assignStrategy").value(hasItem(DEFAULT_ASSIGN_STRATEGY.toString())))
            .andExpect(jsonPath("$.[*].assignStrategyJson").value(hasItem(DEFAULT_ASSIGN_STRATEGY_JSON.toString())))
            .andExpect(jsonPath("$.[*].applyTime").value(hasItem(DEFAULT_APPLY_TIME.toString())))
            .andExpect(jsonPath("$.[*].expireTime").value(hasItem(DEFAULT_EXPIRE_TIME.toString())))
            .andExpect(jsonPath("$.[*].pointStrategy").value(hasItem(DEFAULT_POINT_STRATEGY.toString())))
            .andExpect(jsonPath("$.[*].minPointToPass").value(hasItem(DEFAULT_MIN_POINT_TO_PASS.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].thumbUrl").value(hasItem(DEFAULT_THUMB_URL.toString())))
            .andExpect(jsonPath("$.[*].courseId").value(hasItem(DEFAULT_COURSE_ID.intValue())))
            .andExpect(jsonPath("$.[*].numberOfQuestion").value(hasItem(DEFAULT_NUMBER_OF_QUESTION.intValue())))
            .andExpect(jsonPath("$.[*].strategy").value(hasItem(DEFAULT_STRATEGY.toString())));
    }

    @Test
    @Transactional
    void getExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(examEntity);

        // Get the exam
        restExamMockMvc
            .perform(get(ENTITY_API_URL_ID, examEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(examEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.requireJoin").value(DEFAULT_REQUIRE_JOIN.booleanValue()))
            .andExpect(jsonPath("$.assignStrategy").value(DEFAULT_ASSIGN_STRATEGY.toString()))
            .andExpect(jsonPath("$.assignStrategyJson").value(DEFAULT_ASSIGN_STRATEGY_JSON.toString()))
            .andExpect(jsonPath("$.applyTime").value(DEFAULT_APPLY_TIME.toString()))
            .andExpect(jsonPath("$.expireTime").value(DEFAULT_EXPIRE_TIME.toString()))
            .andExpect(jsonPath("$.pointStrategy").value(DEFAULT_POINT_STRATEGY.toString()))
            .andExpect(jsonPath("$.minPointToPass").value(DEFAULT_MIN_POINT_TO_PASS.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.thumbUrl").value(DEFAULT_THUMB_URL.toString()))
            .andExpect(jsonPath("$.courseId").value(DEFAULT_COURSE_ID.intValue()))
            .andExpect(jsonPath("$.numberOfQuestion").value(DEFAULT_NUMBER_OF_QUESTION.intValue()))
            .andExpect(jsonPath("$.strategy").value(DEFAULT_STRATEGY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExam() throws Exception {
        // Get the exam
        restExamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(examEntity);

        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Update the exam
        ExamEntity updatedExamEntity = examRepository.findById(examEntity.getId()).get();
        // Disconnect from session so that the updates on updatedExamEntity are not directly saved in db
        em.detach(updatedExamEntity);
        updatedExamEntity
            .title(UPDATED_TITLE)
            .categoryId(UPDATED_CATEGORY_ID)
            .requireJoin(UPDATED_REQUIRE_JOIN)
            .applyTime(UPDATED_APPLY_TIME)
            .expireTime(UPDATED_EXPIRE_TIME)
            .pointStrategy(UPDATED_POINT_STRATEGY)
            .minPointToPass(UPDATED_MIN_POINT_TO_PASS)
            .description(UPDATED_DESCRIPTION)
            .thumbUrl(UPDATED_THUMB_URL)
            .courseId(UPDATED_COURSE_ID)
            .numberOfQuestion(UPDATED_NUMBER_OF_QUESTION)
            .poolStrategy(UPDATED_STRATEGY);
        ExamDTO examDTO = examMapper.toDto(updatedExamEntity);

        restExamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isOk());

        // Validate the Exam in the database
        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
        ExamEntity testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testExam.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testExam.getRequireJoin()).isEqualTo(UPDATED_REQUIRE_JOIN);
        assertThat(testExam.getApplyTime()).isEqualTo(UPDATED_APPLY_TIME);
        assertThat(testExam.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testExam.getPointStrategy()).isEqualTo(UPDATED_POINT_STRATEGY);
        assertThat(testExam.getMinPointToPass()).isEqualTo(UPDATED_MIN_POINT_TO_PASS);
        assertThat(testExam.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExam.getThumbUrl()).isEqualTo(UPDATED_THUMB_URL);
        assertThat(testExam.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testExam.getNumberOfQuestion()).isEqualTo(UPDATED_NUMBER_OF_QUESTION);
        assertThat(testExam.getPoolStrategy()).isEqualTo(UPDATED_STRATEGY);
    }

    @Test
    @Transactional
    void putNonExistingExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();
        examEntity.setId(count.incrementAndGet());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(examEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, examDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();
        examEntity.setId(count.incrementAndGet());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(examEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();
        examEntity.setId(count.incrementAndGet());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(examEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exam in the database
        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExamWithPatch() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(examEntity);

        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Update the exam using partial update
        ExamEntity partialUpdatedExamEntity = new ExamEntity();
        partialUpdatedExamEntity.setId(examEntity.getId());

        partialUpdatedExamEntity
            .title(UPDATED_TITLE)
            .categoryId(UPDATED_CATEGORY_ID)
            .requireJoin(UPDATED_REQUIRE_JOIN)
            .applyTime(UPDATED_APPLY_TIME)
            .expireTime(UPDATED_EXPIRE_TIME)
            .pointStrategy(UPDATED_POINT_STRATEGY)
            .description(UPDATED_DESCRIPTION)
            .numberOfQuestion(UPDATED_NUMBER_OF_QUESTION)
            .poolStrategy(UPDATED_STRATEGY);

        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamEntity))
            )
            .andExpect(status().isOk());

        // Validate the Exam in the database
        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
        ExamEntity testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testExam.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testExam.getRequireJoin()).isEqualTo(UPDATED_REQUIRE_JOIN);
        assertThat(testExam.getApplyTime()).isEqualTo(UPDATED_APPLY_TIME);
        assertThat(testExam.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testExam.getPointStrategy()).isEqualTo(UPDATED_POINT_STRATEGY);
        assertThat(testExam.getMinPointToPass()).isEqualTo(DEFAULT_MIN_POINT_TO_PASS);
        assertThat(testExam.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExam.getThumbUrl()).isEqualTo(DEFAULT_THUMB_URL);
        assertThat(testExam.getCourseId()).isEqualTo(DEFAULT_COURSE_ID);
        assertThat(testExam.getNumberOfQuestion()).isEqualTo(UPDATED_NUMBER_OF_QUESTION);
        assertThat(testExam.getPoolStrategy()).isEqualTo(UPDATED_STRATEGY);
    }

    @Test
    @Transactional
    void fullUpdateExamWithPatch() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(examEntity);

        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Update the exam using partial update
        ExamEntity partialUpdatedExamEntity = new ExamEntity();
        partialUpdatedExamEntity.setId(examEntity.getId());

        partialUpdatedExamEntity
            .title(UPDATED_TITLE)
            .categoryId(UPDATED_CATEGORY_ID)
            .requireJoin(UPDATED_REQUIRE_JOIN)
            .applyTime(UPDATED_APPLY_TIME)
            .expireTime(UPDATED_EXPIRE_TIME)
            .pointStrategy(UPDATED_POINT_STRATEGY)
            .minPointToPass(UPDATED_MIN_POINT_TO_PASS)
            .description(UPDATED_DESCRIPTION)
            .thumbUrl(UPDATED_THUMB_URL)
            .courseId(UPDATED_COURSE_ID)
            .numberOfQuestion(UPDATED_NUMBER_OF_QUESTION)
            .poolStrategy(UPDATED_STRATEGY);

        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExamEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedExamEntity))
            )
            .andExpect(status().isOk());

        // Validate the Exam in the database
        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
        ExamEntity testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testExam.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testExam.getRequireJoin()).isEqualTo(UPDATED_REQUIRE_JOIN);
        assertThat(testExam.getApplyTime()).isEqualTo(UPDATED_APPLY_TIME);
        assertThat(testExam.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testExam.getPointStrategy()).isEqualTo(UPDATED_POINT_STRATEGY);
        assertThat(testExam.getMinPointToPass()).isEqualTo(UPDATED_MIN_POINT_TO_PASS);
        assertThat(testExam.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExam.getThumbUrl()).isEqualTo(UPDATED_THUMB_URL);
        assertThat(testExam.getCourseId()).isEqualTo(UPDATED_COURSE_ID);
        assertThat(testExam.getNumberOfQuestion()).isEqualTo(UPDATED_NUMBER_OF_QUESTION);
        assertThat(testExam.getPoolStrategy()).isEqualTo(UPDATED_STRATEGY);
    }

    @Test
    @Transactional
    void patchNonExistingExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();
        examEntity.setId(count.incrementAndGet());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(examEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, examDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();
        examEntity.setId(count.incrementAndGet());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(examEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();
        examEntity.setId(count.incrementAndGet());

        // Create the Exam
        ExamDTO examDTO = examMapper.toDto(examEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExamMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(examDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Exam in the database
        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(examEntity);

        int databaseSizeBeforeDelete = examRepository.findAll().size();

        // Delete the exam
        restExamMockMvc
            .perform(delete(ENTITY_API_URL_ID, examEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExamEntity> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
