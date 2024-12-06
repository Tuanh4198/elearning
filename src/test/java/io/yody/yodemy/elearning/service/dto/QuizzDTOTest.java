package io.yody.yodemy.elearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizzDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizzDTO.class);
        QuizzDTO quizzDTO1 = new QuizzDTO();
        quizzDTO1.setId(1L);
        QuizzDTO quizzDTO2 = new QuizzDTO();
        assertThat(quizzDTO1).isNotEqualTo(quizzDTO2);
        quizzDTO2.setId(quizzDTO1.getId());
        assertThat(quizzDTO1).isEqualTo(quizzDTO2);
        quizzDTO2.setId(2L);
        assertThat(quizzDTO1).isNotEqualTo(quizzDTO2);
        quizzDTO1.setId(null);
        assertThat(quizzDTO1).isNotEqualTo(quizzDTO2);
    }
}
