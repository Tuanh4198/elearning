package io.yody.yodemy.elearning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizzEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizzEntity.class);
        QuizzEntity quizzEntity1 = new QuizzEntity();
        quizzEntity1.setId(1L);
        QuizzEntity quizzEntity2 = new QuizzEntity();
        quizzEntity2.setId(quizzEntity1.getId());
        assertThat(quizzEntity1).isEqualTo(quizzEntity2);
        quizzEntity2.setId(2L);
        assertThat(quizzEntity1).isNotEqualTo(quizzEntity2);
        quizzEntity1.setId(null);
        assertThat(quizzEntity1).isNotEqualTo(quizzEntity2);
    }
}
