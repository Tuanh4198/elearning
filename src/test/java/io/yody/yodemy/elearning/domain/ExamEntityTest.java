package io.yody.yodemy.elearning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExamEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamEntity.class);
        ExamEntity examEntity1 = new ExamEntity();
        examEntity1.setId(1L);
        ExamEntity examEntity2 = new ExamEntity();
        examEntity2.setId(examEntity1.getId());
        assertThat(examEntity1).isEqualTo(examEntity2);
        examEntity2.setId(2L);
        assertThat(examEntity1).isNotEqualTo(examEntity2);
        examEntity1.setId(null);
        assertThat(examEntity1).isNotEqualTo(examEntity2);
    }
}
