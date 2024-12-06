package io.yody.yodemy.elearning.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizzAnswerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizzAnswerDTO.class);
        QuizzAnswerDTO quizzAnswerDTO1 = new QuizzAnswerDTO();
        quizzAnswerDTO1.setId(1L);
        QuizzAnswerDTO quizzAnswerDTO2 = new QuizzAnswerDTO();
        assertThat(quizzAnswerDTO1).isNotEqualTo(quizzAnswerDTO2);
        quizzAnswerDTO2.setId(quizzAnswerDTO1.getId());
        assertThat(quizzAnswerDTO1).isEqualTo(quizzAnswerDTO2);
        quizzAnswerDTO2.setId(2L);
        assertThat(quizzAnswerDTO1).isNotEqualTo(quizzAnswerDTO2);
        quizzAnswerDTO1.setId(null);
        assertThat(quizzAnswerDTO1).isNotEqualTo(quizzAnswerDTO2);
    }
}
