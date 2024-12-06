package io.yody.yodemy.elearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseEmployeeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseEmployeeDTO.class);
        CourseEmployeeDTO courseEmployeeDTO1 = new CourseEmployeeDTO();
        courseEmployeeDTO1.setId(1L);
        CourseEmployeeDTO courseEmployeeDTO2 = new CourseEmployeeDTO();
        assertThat(courseEmployeeDTO1).isNotEqualTo(courseEmployeeDTO2);
        courseEmployeeDTO2.setId(courseEmployeeDTO1.getId());
        assertThat(courseEmployeeDTO1).isEqualTo(courseEmployeeDTO2);
        courseEmployeeDTO2.setId(2L);
        assertThat(courseEmployeeDTO1).isNotEqualTo(courseEmployeeDTO2);
        courseEmployeeDTO1.setId(null);
        assertThat(courseEmployeeDTO1).isNotEqualTo(courseEmployeeDTO2);
    }
}
