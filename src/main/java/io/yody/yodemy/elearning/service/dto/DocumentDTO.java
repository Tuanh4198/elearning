package io.yody.yodemy.elearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.yody.yodemy.elearning.domain.enumeration.DocumentEmployeeStatusEnum;
import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link io.yody.yodemy.elearning.domain.DocumentEntity} entity.
 */
@Schema(description = "Tài liệu")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentDTO implements Serializable {

    /**
     * id
     */
    @Schema(description = "id", required = true)
    private Long id;

    /**
     * Id khóa học
     */
    @NotNull
    @Schema(description = "Id khóa học", required = true)
    private Long rootId;

    /**
     * content
     */
    @Schema(description = "content")
    private String content;
    private String name;
    private String type;

    private List<MetafieldDTO> metafields;
    private List<RuleDTO> rules;
    DocumentEmployeeStatusEnum status = DocumentEmployeeStatusEnum.NOT_LEARNED;
    private String erContent;

    public List<MetafieldDTO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldDTO> metafields) {
        this.metafields = metafields;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<RuleDTO> getRules() {
        return rules;
    }

    public void setRules(List<RuleDTO> rules) {
        this.rules = rules;
    }

    public DocumentEmployeeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(DocumentEmployeeStatusEnum status) {
        this.status = status;
    }

    public String getErContent() {
        return erContent;
    }

    public void setErContent(String erContent) {
        this.erContent = erContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDTO)) {
            return false;
        }

        DocumentDTO documentDTO = (DocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentDTO{" +
            "id=" + getId() +
            ", rootId=" + getRootId() +
            ", content='" + getContent() + "'" +
            "}";
    }
}
