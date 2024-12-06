package io.yody.yodemy.elearning.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.nentangso.core.domain.AbstractAuditingEntity;

/**
 * Danh mục câu hỏi
 */
@Entity
@Table(name = "quizz_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Where(clause = "deleted = false")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizzCategoryEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Title
     */
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title", length = 255, nullable = false)
    private String title;

    /**
     * Mô tả
     */
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuizzCategoryEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public QuizzCategoryEntity title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public QuizzCategoryEntity description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(o instanceof QuizzCategoryEntity)) {
            return false;
        }
        return id != null && id.equals(((QuizzCategoryEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizzCategoryEntity{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
