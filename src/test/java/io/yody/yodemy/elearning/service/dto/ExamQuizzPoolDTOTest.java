package io.yody.yodemy.elearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExamQuizzPoolDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamQuizzPoolDTO.class);
        ExamQuizzPoolDTO examQuizzPoolDTO1 = new ExamQuizzPoolDTO();
        examQuizzPoolDTO1.setId(1L);
        ExamQuizzPoolDTO examQuizzPoolDTO2 = new ExamQuizzPoolDTO();
        assertThat(examQuizzPoolDTO1).isNotEqualTo(examQuizzPoolDTO2);
        examQuizzPoolDTO2.setId(examQuizzPoolDTO1.getId());
        assertThat(examQuizzPoolDTO1).isEqualTo(examQuizzPoolDTO2);
        examQuizzPoolDTO2.setId(2L);
        assertThat(examQuizzPoolDTO1).isNotEqualTo(examQuizzPoolDTO2);
        examQuizzPoolDTO1.setId(null);
        assertThat(examQuizzPoolDTO1).isNotEqualTo(examQuizzPoolDTO2);
    }
}
