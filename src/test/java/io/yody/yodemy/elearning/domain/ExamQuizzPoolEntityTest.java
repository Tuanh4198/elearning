package io.yody.yodemy.elearning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExamQuizzPoolEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamQuizzPoolEntity.class);
        ExamQuizzPoolEntity examQuizzPoolEntity1 = new ExamQuizzPoolEntity();
        examQuizzPoolEntity1.setId(1L);
        ExamQuizzPoolEntity examQuizzPoolEntity2 = new ExamQuizzPoolEntity();
        examQuizzPoolEntity2.setId(examQuizzPoolEntity1.getId());
        assertThat(examQuizzPoolEntity1).isEqualTo(examQuizzPoolEntity2);
        examQuizzPoolEntity2.setId(2L);
        assertThat(examQuizzPoolEntity1).isNotEqualTo(examQuizzPoolEntity2);
        examQuizzPoolEntity1.setId(null);
        assertThat(examQuizzPoolEntity1).isNotEqualTo(examQuizzPoolEntity2);
    }
}
