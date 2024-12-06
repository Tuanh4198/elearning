package io.yody.yodemy.elearning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizzAnswerEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizzAnswerEntity.class);
        QuizzAnswerEntity quizzAnswerEntity1 = new QuizzAnswerEntity();
        quizzAnswerEntity1.setId(1L);
        QuizzAnswerEntity quizzAnswerEntity2 = new QuizzAnswerEntity();
        quizzAnswerEntity2.setId(quizzAnswerEntity1.getId());
        assertThat(quizzAnswerEntity1).isEqualTo(quizzAnswerEntity2);
        quizzAnswerEntity2.setId(2L);
        assertThat(quizzAnswerEntity1).isNotEqualTo(quizzAnswerEntity2);
        quizzAnswerEntity1.setId(null);
        assertThat(quizzAnswerEntity1).isNotEqualTo(quizzAnswerEntity2);
    }
}
