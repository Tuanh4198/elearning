package io.yody.yodemy.elearning.web.rest.vm.response;

import java.io.Serializable;

public class EmployeeDTO implements Serializable {

    private String code;
    private String displayName;
    private String role;
    private String departmentName;
    private String roleId;
    private String departmentCode;

    public EmployeeDTO() {}

    public EmployeeDTO(String code, String name) {
        this.code = code;
        this.displayName = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }
}
