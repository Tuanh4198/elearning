package io.yody.yodemy.elearning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.yody.yodemy.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentEntityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentEntity.class);
        DocumentEntity documentEntity1 = new DocumentEntity();
        documentEntity1.setId(1L);
        DocumentEntity documentEntity2 = new DocumentEntity();
        documentEntity2.setId(documentEntity1.getId());
        assertThat(documentEntity1).isEqualTo(documentEntity2);
        documentEntity2.setId(2L);
        assertThat(documentEntity1).isNotEqualTo(documentEntity2);
        documentEntity1.setId(null);
        assertThat(documentEntity1).isNotEqualTo(documentEntity2);
    }
}
