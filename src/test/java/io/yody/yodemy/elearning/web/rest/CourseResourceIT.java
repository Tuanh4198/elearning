package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.IntegrationTest;
import io.yody.yodemy.elearning.domain.CourseEntity;
import io.yody.yodemy.elearning.domain.enumeration.AssignStrategyEnum;
import io.yody.yodemy.elearning.repository.CourseRepository;
import io.yody.yodemy.elearning.service.dto.CourseDTO;
import io.yody.yodemy.elearning.service.mapper.CourseMapper;
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
 * Integration tests for the {@link CourseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Long DEFAULT_CATEGORY_ID = 1L;
    private static final Long UPDATED_CATEGORY_ID = 2L;

    private static final Boolean DEFAULT_REQUIRE_JOIN = false;
    private static final Boolean UPDATED_REQUIRE_JOIN = true;

    private static final Boolean DEFAULT_REQUIRE_ATTEND = false;
    private static final Boolean UPDATED_REQUIRE_ATTEND = true;

    private static final String DEFAULT_MEETING_URL = "AAAAAAAAAA";
    private static final String UPDATED_MEETING_URL = "BBBBBBBBBB";

    private static final Long DEFAULT_EXAM_ID = 1L;
    private static final Long UPDATED_EXAM_ID = 2L;

    private static final Instant DEFAULT_APPLY_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPLY_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final AssignStrategyEnum DEFAULT_ASSIGN_STRATEGY = AssignStrategyEnum.ANY_USERS;
    private static final AssignStrategyEnum UPDATED_ASSIGN_STRATEGY = AssignStrategyEnum.SPEC_POSITIONS;

    private static final String DEFAULT_ASSIGN_STRATEGY_JSON = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGN_STRATEGY_JSON = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_THUMB_URL = "AAAAAAAAAA";
    private static final String UPDATED_THUMB_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/courses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseMockMvc;

    private CourseEntity courseEntity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseEntity createEntity(EntityManager em) {
        CourseEntity courseEntity = new CourseEntity()
            .title(DEFAULT_TITLE)
            .categoryId(DEFAULT_CATEGORY_ID)
            .requireJoin(DEFAULT_REQUIRE_JOIN)
            .requireAttend(DEFAULT_REQUIRE_ATTEND)
            .meetingUrl(DEFAULT_MEETING_URL)
            .examId(DEFAULT_EXAM_ID)
            .applyTime(DEFAULT_APPLY_TIME)
            .expireTime(DEFAULT_EXPIRE_TIME)
            .description(DEFAULT_DESCRIPTION)
            .thumbUrl(DEFAULT_THUMB_URL);
        return courseEntity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseEntity createUpdatedEntity(EntityManager em) {
        CourseEntity courseEntity = new CourseEntity()
            .title(UPDATED_TITLE)
            .categoryId(UPDATED_CATEGORY_ID)
            .requireJoin(UPDATED_REQUIRE_JOIN)
            .requireAttend(UPDATED_REQUIRE_ATTEND)
            .meetingUrl(UPDATED_MEETING_URL)
            .examId(UPDATED_EXAM_ID)
            .applyTime(UPDATED_APPLY_TIME)
            .expireTime(UPDATED_EXPIRE_TIME)
            .description(UPDATED_DESCRIPTION)
            .thumbUrl(UPDATED_THUMB_URL);
        return courseEntity;
    }

    @BeforeEach
    public void initTest() {
        courseEntity = createEntity(em);
    }

    @Test
    @Transactional
    void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();
        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(courseEntity);
        restCourseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        CourseEntity testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCourse.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testCourse.getRequireJoin()).isEqualTo(DEFAULT_REQUIRE_JOIN);
        assertThat(testCourse.getRequireAttend()).isEqualTo(DEFAULT_REQUIRE_ATTEND);
        assertThat(testCourse.getMeetingUrl()).isEqualTo(DEFAULT_MEETING_URL);
        assertThat(testCourse.getExamId()).isEqualTo(DEFAULT_EXAM_ID);
        assertThat(testCourse.getApplyTime()).isEqualTo(DEFAULT_APPLY_TIME);
        assertThat(testCourse.getExpireTime()).isEqualTo(DEFAULT_EXPIRE_TIME);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getThumbUrl()).isEqualTo(DEFAULT_THUMB_URL);
    }

    @Test
    @Transactional
    void createCourseWithExistingId() throws Exception {
        // Create the Course with an existing ID
        courseEntity.setId(1L);
        CourseDTO courseDTO = courseMapper.toDto(courseEntity);

        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        courseEntity.setTitle(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(courseEntity);

        restCourseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseDTO))
            )
            .andExpect(status().isBadRequest());

        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoryIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        courseEntity.setCategoryId(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(courseEntity);

        restCourseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseDTO))
            )
            .andExpect(status().isBadRequest());

        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkApplyTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        courseEntity.setApplyTime(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(courseEntity);

        restCourseMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseDTO))
            )
            .andExpect(status().isBadRequest());

        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(courseEntity);

        // Get all the courseList
        restCourseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID.intValue())))
            .andExpect(jsonPath("$.[*].requireJoin").value(hasItem(DEFAULT_REQUIRE_JOIN.booleanValue())))
            .andExpect(jsonPath("$.[*].requireAttend").value(hasItem(DEFAULT_REQUIRE_ATTEND.booleanValue())))
            .andExpect(jsonPath("$.[*].meetingUrl").value(hasItem(DEFAULT_MEETING_URL.toString())))
            .andExpect(jsonPath("$.[*].examId").value(hasItem(DEFAULT_EXAM_ID.intValue())))
            .andExpect(jsonPath("$.[*].applyTime").value(hasItem(DEFAULT_APPLY_TIME.toString())))
            .andExpect(jsonPath("$.[*].expireTime").value(hasItem(DEFAULT_EXPIRE_TIME.toString())))
            .andExpect(jsonPath("$.[*].assignStrategy").value(hasItem(DEFAULT_ASSIGN_STRATEGY.toString())))
            .andExpect(jsonPath("$.[*].assignStrategyJson").value(hasItem(DEFAULT_ASSIGN_STRATEGY_JSON.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].thumbUrl").value(hasItem(DEFAULT_THUMB_URL.toString())));
    }

    @Test
    @Transactional
    void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(courseEntity);

        // Get the course
        restCourseMockMvc
            .perform(get(ENTITY_API_URL_ID, courseEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseEntity.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID.intValue()))
            .andExpect(jsonPath("$.requireJoin").value(DEFAULT_REQUIRE_JOIN.booleanValue()))
            .andExpect(jsonPath("$.requireAttend").value(DEFAULT_REQUIRE_ATTEND.booleanValue()))
            .andExpect(jsonPath("$.meetingUrl").value(DEFAULT_MEETING_URL.toString()))
            .andExpect(jsonPath("$.examId").value(DEFAULT_EXAM_ID.intValue()))
            .andExpect(jsonPath("$.applyTime").value(DEFAULT_APPLY_TIME.toString()))
            .andExpect(jsonPath("$.expireTime").value(DEFAULT_EXPIRE_TIME.toString()))
            .andExpect(jsonPath("$.assignStrategy").value(DEFAULT_ASSIGN_STRATEGY.toString()))
            .andExpect(jsonPath("$.assignStrategyJson").value(DEFAULT_ASSIGN_STRATEGY_JSON.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.thumbUrl").value(DEFAULT_THUMB_URL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(courseEntity);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        CourseEntity updatedCourseEntity = courseRepository.findById(courseEntity.getId()).get();
        // Disconnect from session so that the updates on updatedCourseEntity are not directly saved in db
        em.detach(updatedCourseEntity);
        updatedCourseEntity
            .title(UPDATED_TITLE)
            .categoryId(UPDATED_CATEGORY_ID)
            .requireJoin(UPDATED_REQUIRE_JOIN)
            .requireAttend(UPDATED_REQUIRE_ATTEND)
            .meetingUrl(UPDATED_MEETING_URL)
            .examId(UPDATED_EXAM_ID)
            .applyTime(UPDATED_APPLY_TIME)
            .expireTime(UPDATED_EXPIRE_TIME)
            .description(UPDATED_DESCRIPTION)
            .thumbUrl(UPDATED_THUMB_URL);
        CourseDTO courseDTO = courseMapper.toDto(updatedCourseEntity);

        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        CourseEntity testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCourse.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testCourse.getRequireJoin()).isEqualTo(UPDATED_REQUIRE_JOIN);
        assertThat(testCourse.getRequireAttend()).isEqualTo(UPDATED_REQUIRE_ATTEND);
        assertThat(testCourse.getMeetingUrl()).isEqualTo(UPDATED_MEETING_URL);
        assertThat(testCourse.getExamId()).isEqualTo(UPDATED_EXAM_ID);
        assertThat(testCourse.getApplyTime()).isEqualTo(UPDATED_APPLY_TIME);
        assertThat(testCourse.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getThumbUrl()).isEqualTo(UPDATED_THUMB_URL);
    }

    @Test
    @Transactional
    void putNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        courseEntity.setId(count.incrementAndGet());

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(courseEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        courseEntity.setId(count.incrementAndGet());

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(courseEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        courseEntity.setId(count.incrementAndGet());

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(courseEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Course in the database
        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseWithPatch() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(courseEntity);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course using partial update
        CourseEntity partialUpdatedCourseEntity = new CourseEntity();
        partialUpdatedCourseEntity.setId(courseEntity.getId());

        partialUpdatedCourseEntity
            .requireJoin(UPDATED_REQUIRE_JOIN)
            .meetingUrl(UPDATED_MEETING_URL)
            .examId(UPDATED_EXAM_ID)
            .expireTime(UPDATED_EXPIRE_TIME)
            .description(UPDATED_DESCRIPTION)
            .thumbUrl(UPDATED_THUMB_URL);

        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseEntity))
            )
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        CourseEntity testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCourse.getCategoryId()).isEqualTo(DEFAULT_CATEGORY_ID);
        assertThat(testCourse.getRequireJoin()).isEqualTo(UPDATED_REQUIRE_JOIN);
        assertThat(testCourse.getRequireAttend()).isEqualTo(DEFAULT_REQUIRE_ATTEND);
        assertThat(testCourse.getMeetingUrl()).isEqualTo(UPDATED_MEETING_URL);
        assertThat(testCourse.getExamId()).isEqualTo(UPDATED_EXAM_ID);
        assertThat(testCourse.getApplyTime()).isEqualTo(DEFAULT_APPLY_TIME);
        assertThat(testCourse.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getThumbUrl()).isEqualTo(UPDATED_THUMB_URL);
    }

    @Test
    @Transactional
    void fullUpdateCourseWithPatch() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(courseEntity);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course using partial update
        CourseEntity partialUpdatedCourseEntity = new CourseEntity();
        partialUpdatedCourseEntity.setId(courseEntity.getId());

        partialUpdatedCourseEntity
            .title(UPDATED_TITLE)
            .categoryId(UPDATED_CATEGORY_ID)
            .requireJoin(UPDATED_REQUIRE_JOIN)
            .requireAttend(UPDATED_REQUIRE_ATTEND)
            .meetingUrl(UPDATED_MEETING_URL)
            .examId(UPDATED_EXAM_ID)
            .applyTime(UPDATED_APPLY_TIME)
            .expireTime(UPDATED_EXPIRE_TIME)
            .description(UPDATED_DESCRIPTION)
            .thumbUrl(UPDATED_THUMB_URL);

        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseEntity.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseEntity))
            )
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        CourseEntity testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCourse.getCategoryId()).isEqualTo(UPDATED_CATEGORY_ID);
        assertThat(testCourse.getRequireJoin()).isEqualTo(UPDATED_REQUIRE_JOIN);
        assertThat(testCourse.getRequireAttend()).isEqualTo(UPDATED_REQUIRE_ATTEND);
        assertThat(testCourse.getMeetingUrl()).isEqualTo(UPDATED_MEETING_URL);
        assertThat(testCourse.getExamId()).isEqualTo(UPDATED_EXAM_ID);
        assertThat(testCourse.getApplyTime()).isEqualTo(UPDATED_APPLY_TIME);
        assertThat(testCourse.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getThumbUrl()).isEqualTo(UPDATED_THUMB_URL);
    }

    @Test
    @Transactional
    void patchNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        courseEntity.setId(count.incrementAndGet());

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(courseEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        courseEntity.setId(count.incrementAndGet());

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(courseEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();
        courseEntity.setId(count.incrementAndGet());

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(courseEntity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Course in the database
        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(courseEntity);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        restCourseMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseEntity.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseEntity> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
