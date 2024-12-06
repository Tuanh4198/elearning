package io.yody.yodemy.elearning.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseEmployeeMapperTest {

    private CourseEmployeeMapper courseEmployeeMapper;

    @BeforeEach
    public void setUp() {
        courseEmployeeMapper = new CourseEmployeeMapperImpl();
    }
}
