package io.yody.yodemy.elearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.yody.yodemy.elearning.domain.enumeration.CourseEmployeeStatusEnum;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.yody.yodemy.elearning.domain.CourseEmployeeEntity} entity.
 */
@Schema(description = "Khóa học giao cho user")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourseEmployeeDTO implements Serializable {

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
     * Id khóa học gốc
     */
    @NotNull
    @Schema(description = "Id khóa học gốc", required = true)
    private Long rootId;

    /**
     * status
     */
    @Schema(description = "status")
    private CourseEmployeeStatusEnum status;

    private CourseDTO course;

    private List<MetafieldDTO> metafields;

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

    public CourseEmployeeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CourseEmployeeStatusEnum status) {
        this.status = status;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    public List<MetafieldDTO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldDTO> metafields) {
        this.metafields = metafields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseEmployeeDTO)) {
            return false;
        }

        CourseEmployeeDTO courseEmployeeDTO = (CourseEmployeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, courseEmployeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseEmployeeDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", rootId=" + getRootId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
