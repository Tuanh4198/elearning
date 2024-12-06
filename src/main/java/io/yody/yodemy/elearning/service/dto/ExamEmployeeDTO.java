package io.yody.yodemy.elearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.yody.yodemy.elearning.domain.enumeration.ExamEmployeeStatusEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link io.yody.yodemy.elearning.domain.ExamEmployeeEntity} entity.
 */
@Schema(description = "Bài thi của từng user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamEmployeeDTO implements Serializable {

    /**
     * id
     */
    @NotNull
    @Schema(description = "id", required = true)
    private Long id;

    /**
     * Mã nhân viên
     */
    @NotNull
    @Size(min = 3, max = 255)
    @Schema(description = "Mã nhân viên", required = true)
    private String code;

    /**
     * Tên nhân viên
     */
    @NotNull
    @Size(min = 1, max = 255)
    @Schema(description = "Tên nhân viên", required = true)
    private String name;

    /**
     * Id kỳ thi
     */
    @NotNull
    @Schema(description = "Id kỳ thi", required = true)
    private Long rootId;

    /**
     * status
     */
    @Schema(description = "status")
    private ExamEmployeeStatusEnum status;

    private ExamDTO exam;
    private List<QuizzDTO> quizzs;
    private Long nodeId;
    private Long rootNodeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public ExamEmployeeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ExamEmployeeStatusEnum status) {
        this.status = status;
    }

    public List<QuizzDTO> getQuizzs() {
        return quizzs;
    }

    public void setQuizzs(List<QuizzDTO> quizzs) {
        this.quizzs = quizzs;
    }

    public ExamDTO getExam() {
        return exam;
    }

    public void setExam(ExamDTO exam) {
        this.exam = exam;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getRootNodeId() {
        return rootNodeId;
    }

    public void setRootNodeId(Long rootNodeId) {
        this.rootNodeId = rootNodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamEmployeeDTO)) {
            return false;
        }

        ExamEmployeeDTO examEmployeeDTO = (ExamEmployeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examEmployeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamEmployeeDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", rootId=" + getRootId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
