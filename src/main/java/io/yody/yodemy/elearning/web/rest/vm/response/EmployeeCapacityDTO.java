package io.yody.yodemy.elearning.web.rest.vm.response;

import io.yody.yodemy.domain.enumaration.CapacityTypeEnum;

public class EmployeeCapacityDTO {

    private Long id;
    private String code;
    private String criteria;
    private Double realCapacity;
    private Integer targetCapacity;
    private CapacityTypeEnum type;

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

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public Double getRealCapacity() {
        return realCapacity;
    }

    public void setRealCapacity(Double realCapacity) {
        this.realCapacity = realCapacity;
    }

    public Integer getTargetCapacity() {
        return targetCapacity;
    }

    public void setTargetCapacity(Integer targetCapacity) {
        this.targetCapacity = targetCapacity;
    }

    public CapacityTypeEnum getType() {
        return type;
    }

    public void setType(CapacityTypeEnum type) {
        this.type = type;
    }
}
