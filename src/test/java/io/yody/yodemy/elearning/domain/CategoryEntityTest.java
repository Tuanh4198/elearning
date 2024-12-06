package io.yody.yodemy.elearning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoryEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryEntity.class);
        CategoryEntity categoryEntity1 = new CategoryEntity();
        categoryEntity1.setId(1L);
        CategoryEntity categoryEntity2 = new CategoryEntity();
        categoryEntity2.setId(categoryEntity1.getId());
        assertThat(categoryEntity1).isEqualTo(categoryEntity2);
        categoryEntity2.setId(2L);
        assertThat(categoryEntity1).isNotEqualTo(categoryEntity2);
        categoryEntity1.setId(null);
        assertThat(categoryEntity1).isNotEqualTo(categoryEntity2);
    }
}
