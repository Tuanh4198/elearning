package io.yody.yodemy.elearning.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuizzAnswerMapperTest {

    private QuizzAnswerMapper quizzAnswerMapper;

    @BeforeEach
    public void setUp() {
        quizzAnswerMapper = new QuizzAnswerMapperImpl();
    }
}
