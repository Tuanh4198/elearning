package io.yody.yodemy.elearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExamEmployeeResultDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamEmployeeResultDTO.class);
        ExamEmployeeResultDTO examEmployeeResultDTO1 = new ExamEmployeeResultDTO();
        examEmployeeResultDTO1.setId(1L);
        ExamEmployeeResultDTO examEmployeeResultDTO2 = new ExamEmployeeResultDTO();
        assertThat(examEmployeeResultDTO1).isNotEqualTo(examEmployeeResultDTO2);
        examEmployeeResultDTO2.setId(examEmployeeResultDTO1.getId());
        assertThat(examEmployeeResultDTO1).isEqualTo(examEmployeeResultDTO2);
        examEmployeeResultDTO2.setId(2L);
        assertThat(examEmployeeResultDTO1).isNotEqualTo(examEmployeeResultDTO2);
        examEmployeeResultDTO1.setId(null);
        assertThat(examEmployeeResultDTO1).isNotEqualTo(examEmployeeResultDTO2);
    }
}
