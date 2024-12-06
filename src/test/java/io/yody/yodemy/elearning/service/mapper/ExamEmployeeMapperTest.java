package io.yody.yodemy.elearning.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExamEmployeeMapperTest {

    private ExamEmployeeMapper examEmployeeMapper;

    @BeforeEach
    public void setUp() {
        examEmployeeMapper = new ExamEmployeeMapperImpl();
    }
}
