package io.yody.yodemy.elearning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseEntity.class);
        CourseEntity courseEntity1 = new CourseEntity();
        courseEntity1.setId(1L);
        CourseEntity courseEntity2 = new CourseEntity();
        courseEntity2.setId(courseEntity1.getId());
        assertThat(courseEntity1).isEqualTo(courseEntity2);
        courseEntity2.setId(2L);
        assertThat(courseEntity1).isNotEqualTo(courseEntity2);
        courseEntity1.setId(null);
        assertThat(courseEntity1).isNotEqualTo(courseEntity2);
    }
}
