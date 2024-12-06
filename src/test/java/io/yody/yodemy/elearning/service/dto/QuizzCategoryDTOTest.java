package io.yody.yodemy.elearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizzCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizzCategoryDTO.class);
        QuizzCategoryDTO quizzCategoryDTO1 = new QuizzCategoryDTO();
        quizzCategoryDTO1.setId(1L);
        QuizzCategoryDTO quizzCategoryDTO2 = new QuizzCategoryDTO();
        assertThat(quizzCategoryDTO1).isNotEqualTo(quizzCategoryDTO2);
        quizzCategoryDTO2.setId(quizzCategoryDTO1.getId());
        assertThat(quizzCategoryDTO1).isEqualTo(quizzCategoryDTO2);
        quizzCategoryDTO2.setId(2L);
        assertThat(quizzCategoryDTO1).isNotEqualTo(quizzCategoryDTO2);
        quizzCategoryDTO1.setId(null);
        assertThat(quizzCategoryDTO1).isNotEqualTo(quizzCategoryDTO2);
    }
}
