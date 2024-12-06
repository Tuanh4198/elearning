package io.yody.yodemy.elearning.service.business;

import io.yody.yodemy.elearning.domain.enumeration.ExamEmployeeStatusEnum;

public class ExamEmployeeBO {

    private Long id;
    private String code;
    private String name;
    private Long rootId;
    private ExamEmployeeStatusEnum status;

    public ExamEmployeeBO(Long id, String code, String name, Long rootId, ExamEmployeeStatusEnum status) {
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
}
