package io.yody.yodemy.elearning.domain;

import io.yody.yodemy.elearning.domain.enumeration.QuizzTypeEnum;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nentangso.core.domain.AbstractAuditingEntity;

/**
 * Câu hỏi
 */
@Entity
@Table(name = "quizz")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizzEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Id
     */
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Nội dung
     */
    @NotNull
    @Size(min = 1)
    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    /**
     * Id danh mục
     */
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * Loại câu hỏi
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private QuizzTypeEnum type;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuizzEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public QuizzEntity content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public QuizzEntity categoryId(Long categoryId) {
        this.setCategoryId(categoryId);
        return this;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public QuizzTypeEnum getType() {
        return this.type;
    }

    public QuizzEntity type(QuizzTypeEnum type) {
        this.setType(type);
        return this;
    }

    public void setType(QuizzTypeEnum type) {
        this.type = type;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizzEntity)) {
            return false;
        }
        return id != null && id.equals(((QuizzEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizzEntity{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", categoryId=" + getCategoryId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
