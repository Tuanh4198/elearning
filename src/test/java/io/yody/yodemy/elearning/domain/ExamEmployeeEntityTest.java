package io.yody.yodemy.elearning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExamEmployeeEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamEmployeeEntity.class);
        ExamEmployeeEntity examEmployeeEntity1 = new ExamEmployeeEntity();
        examEmployeeEntity1.setId(1L);
        ExamEmployeeEntity examEmployeeEntity2 = new ExamEmployeeEntity();
        examEmployeeEntity2.setId(examEmployeeEntity1.getId());
        assertThat(examEmployeeEntity1).isEqualTo(examEmployeeEntity2);
        examEmployeeEntity2.setId(2L);
        assertThat(examEmployeeEntity1).isNotEqualTo(examEmployeeEntity2);
        examEmployeeEntity1.setId(null);
        assertThat(examEmployeeEntity1).isNotEqualTo(examEmployeeEntity2);
    }
}
