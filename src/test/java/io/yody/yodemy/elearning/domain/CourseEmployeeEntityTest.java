package io.yody.yodemy.elearning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseEmployeeEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseEmployeeEntity.class);
        CourseEmployeeEntity courseEmployeeEntity1 = new CourseEmployeeEntity();
        courseEmployeeEntity1.setId(1L);
        CourseEmployeeEntity courseEmployeeEntity2 = new CourseEmployeeEntity();
        courseEmployeeEntity2.setId(courseEmployeeEntity1.getId());
        assertThat(courseEmployeeEntity1).isEqualTo(courseEmployeeEntity2);
        courseEmployeeEntity2.setId(2L);
        assertThat(courseEmployeeEntity1).isNotEqualTo(courseEmployeeEntity2);
        courseEmployeeEntity1.setId(null);
        assertThat(courseEmployeeEntity1).isNotEqualTo(courseEmployeeEntity2);
    }
}
