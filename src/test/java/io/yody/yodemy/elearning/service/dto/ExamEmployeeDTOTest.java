package io.yody.yodemy.elearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExamEmployeeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamEmployeeDTO.class);
        ExamEmployeeDTO examEmployeeDTO1 = new ExamEmployeeDTO();
        examEmployeeDTO1.setId(1L);
        ExamEmployeeDTO examEmployeeDTO2 = new ExamEmployeeDTO();
        assertThat(examEmployeeDTO1).isNotEqualTo(examEmployeeDTO2);
        examEmployeeDTO2.setId(examEmployeeDTO1.getId());
        assertThat(examEmployeeDTO1).isEqualTo(examEmployeeDTO2);
        examEmployeeDTO2.setId(2L);
        assertThat(examEmployeeDTO1).isNotEqualTo(examEmployeeDTO2);
        examEmployeeDTO1.setId(null);
        assertThat(examEmployeeDTO1).isNotEqualTo(examEmployeeDTO2);
    }
}
