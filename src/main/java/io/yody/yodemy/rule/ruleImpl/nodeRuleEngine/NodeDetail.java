package io.yody.yodemy.rule.ruleImpl.nodeRuleEngine;

public class NodeDetail {
    private Long startTime;
    private Long endTime;
    private String department;
    private String role;
    private String code;
    private Long id;

    public NodeDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public NodeDetail startTime(Long startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public NodeDetail endTime(Long endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public NodeDetail department(String department) {
        this.setDepartment(department);
        return this;
    }

    public NodeDetail role(String role) {
        this.setRole(role);
        return this;
    }

    public NodeDetail code(String code) {
        this.setCode(code);
        return this;
    }

    public NodeDetail id(Long id) {
        this.setId(id);
        return this;
    }

}
