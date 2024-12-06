package io.yody.yodemy.elearning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizzCategoryEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizzCategoryEntity.class);
        QuizzCategoryEntity quizzCategoryEntity1 = new QuizzCategoryEntity();
        quizzCategoryEntity1.setId(1L);
        QuizzCategoryEntity quizzCategoryEntity2 = new QuizzCategoryEntity();
        quizzCategoryEntity2.setId(quizzCategoryEntity1.getId());
        assertThat(quizzCategoryEntity1).isEqualTo(quizzCategoryEntity2);
        quizzCategoryEntity2.setId(2L);
        assertThat(quizzCategoryEntity1).isNotEqualTo(quizzCategoryEntity2);
        quizzCategoryEntity1.setId(null);
        assertThat(quizzCategoryEntity1).isNotEqualTo(quizzCategoryEntity2);
    }
}
