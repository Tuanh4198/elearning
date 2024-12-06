package io.yody.yodemy.elearning.service.business;

import io.yody.yodemy.elearning.domain.enumeration.CourseEmployeeStatusEnum;
import io.yody.yodemy.elearning.service.dto.CourseDTO;

public class CourseEmployeeBO {

    private Long id;
    private String code;
    private String name;
    private String rootId;
    private CourseEmployeeStatusEnum status;

    public CourseEmployeeBO(
        Long id,
        String code,
        String name,
        String rootId,
        CourseEmployeeStatusEnum status
    ) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.rootId = rootId;
        this.status = status;
    }

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

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public CourseEmployeeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CourseEmployeeStatusEnum status) {
        this.status = status;
    }
}
