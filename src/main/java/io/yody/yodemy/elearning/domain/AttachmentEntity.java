package io.yody.yodemy.elearning.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nentangso.core.domain.AbstractAuditingEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "attachment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttachmentEntity extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * source of attachment
     */
    @NotNull
    @Column(name = "source", nullable = false)
    private String source;

    /**
     * url of attachment
     */
    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    /**
     * attachment type
     */
    @NotNull
    @Column(name = "attachment_type", nullable = false)
    private String attachmentType;

    /**
     * attachment name
     */
    @NotNull
    @Column(name = "attachment_name", nullable = false)
    private String attachmentName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AttachmentEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttachmentEntity source(String source) {
        this.setSource(source);
        return this;
    }

    public @NotNull String getSource() {
        return source;
    }

    public void setSource(@NotNull String source) {
        this.source = source;
    }

    public String getUrl() {
        return this.url;
    }

    public AttachmentEntity url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAttachmentType() {
        return this.attachmentType;
    }

    public AttachmentEntity attachmentType(String attachmentType) {
        this.setAttachmentType(attachmentType);
        return this;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public String getAttachmentName() {
        return this.attachmentName;
    }

    public AttachmentEntity attachmentName(String attachmentName) {
        this.setAttachmentName(attachmentName);
        return this;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttachmentEntity)) {
            return false;
        }
        return id != null && id.equals(((AttachmentEntity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttachmentEntity{" +
            "id=" + getId() +
            "}";
    }
}
