package io.yody.yodemy.elearning.web.rest.vm.response;

import io.yody.yodemy.domain.enumaration.CapacityTypeEnum;

public class EmployeeCapacityBehaviorDTO {

    private Long id;
    private String code;
    private String level;
    private String criteria;
    private String behavior;
    private Double realCapacity;
    private Double targetCapacity;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public Double getRealCapacity() {
        return realCapacity;
    }

    public void setRealCapacity(Double realCapacity) {
        this.realCapacity = realCapacity;
    }

    public Double getTargetCapacity() {
        return targetCapacity;
    }

    public void setTargetCapacity(Double targetCapacity) {
        this.targetCapacity = targetCapacity;
    }

    public CapacityTypeEnum getType() {
        return type;
    }

    public void setType(CapacityTypeEnum type) {
        this.type = type;
    }
}
