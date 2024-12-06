package io.yody.yodemy.elearning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExamEmployeeResultEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamEmployeeResultEntity.class);
        ExamEmployeeResultEntity examEmployeeResultEntity1 = new ExamEmployeeResultEntity();
        examEmployeeResultEntity1.setId(1L);
        ExamEmployeeResultEntity examEmployeeResultEntity2 = new ExamEmployeeResultEntity();
        examEmployeeResultEntity2.setId(examEmployeeResultEntity1.getId());
        assertThat(examEmployeeResultEntity1).isEqualTo(examEmployeeResultEntity2);
        examEmployeeResultEntity2.setId(2L);
        assertThat(examEmployeeResultEntity1).isNotEqualTo(examEmployeeResultEntity2);
        examEmployeeResultEntity1.setId(null);
        assertThat(examEmployeeResultEntity1).isNotEqualTo(examEmployeeResultEntity2);
    }
}
