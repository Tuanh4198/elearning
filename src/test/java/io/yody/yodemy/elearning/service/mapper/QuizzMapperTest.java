package io.yody.yodemy.elearning.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuizzMapperTest {

    private QuizzMapper quizzMapper;

    @BeforeEach
    public void setUp() {
        quizzMapper = new QuizzMapperImpl();
    }
}
