package io.yody.yodemy.elearning.service.cache;

import io.yody.yodemy.elearning.client.PegasusClient;
import io.yody.yodemy.elearning.web.rest.vm.response.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EmployeeCache {

    private final PegasusClient pegasusClient;
    private Map<String, String> employeeCodeNameMap = new HashMap<>();
    private Map<String, String> employeeRoleIdMap = new HashMap<>();
    private Map<String, String> employeeDeparmentCodeMap = new HashMap<>();
    private List<EmployeeDTO> allEmployees = new ArrayList<>();

    public EmployeeCache(PegasusClient pegasusClient) {
        this.pegasusClient = pegasusClient;
    }

    private List<EmployeeDTO> getEmployeeByBatch(int page, int limit) {
        Result<PageableDto<EmployeeDTO>> employeeResult = pegasusClient.getAllEmployees(page, limit, null, null, null, false, "WORKING");
        PageableDto<EmployeeDTO> batch = employeeResult.getData();
        return batch.getItems();
    }

    private List<EmployeeDTO> getAllEmployees() {
        System.out.println("getAllEmployees");
        List<EmployeeDTO> employeeInfos = new ArrayList<>();
        int page = 1;
        int limit = 1000;
        int maxUser = 10000;
        List<EmployeeDTO> batch = getEmployeeByBatch(page, limit);

        while (page * limit < maxUser && !batch.isEmpty()) {
            employeeInfos.addAll(batch);
            page++;
            batch = getEmployeeByBatch(page, limit);
            System.out.println("fetched page " + page);
        }

        System.out.println("Total employees fetched: " + employeeInfos.size());
        return employeeInfos;
    }

    private Map<String, PositionDTO> fetchAllPositions() {
        List<PositionDTO> positions = pegasusClient.getAllPositions(null, null, 0, 2000, "id,asc");

        return positions.stream()
            .collect(Collectors.toMap(
                PositionDTO::getName,
                position -> position,
                (existing, replacement) -> {
                    return existing;
                }
            ));
    }


    private Map<String, DepartmentDTO> fetchAllDepartments() {
        List<DepartmentDTO> departments = pegasusClient.getAllDepartments(null, null, null, 0, 2000, "id,asc");

        return departments.stream()
            .collect(Collectors.toMap(
                DepartmentDTO::getName,
                department -> department,
                (existing, replacement) -> {
                    return existing;
                }
            ));
    }


    public void loadEmployeeCache() {
        try {
            // Fetch employee data
            List<EmployeeDTO> employeeData = getAllEmployees();

            // Fetch department and role data
            Map<String, PositionDTO> positionMap = fetchAllPositions();
            Map<String, DepartmentDTO> departmentMap = fetchAllDepartments();

            // Enrich employee data with roleId and departmentCode
            employeeData.forEach(employee -> {
                // Set roleId from positionMap
                if (positionMap.containsKey(employee.getRole())) {
                    employee.setRoleId(positionMap.get(employee.getRole()).getCode());
                }

                // Set departmentCode from departmentMap
                if (departmentMap.containsKey(employee.getDepartmentName())) {
                    employee.setDepartmentCode(departmentMap.get(employee.getDepartmentName()).getCode());
                }

                if (employeeCodeNameMap.containsKey(employee.getCode())) {
                    System.out.println("Duplicate entry for employee code in employeeCodeNameMap: " + employee.getCode());
                } else {
                    employeeCodeNameMap.put(employee.getCode(), employee.getDisplayName());
                }

                if (employeeRoleIdMap.containsKey(employee.getCode())) {
                    System.out.println("Duplicate entry for employee code in employeeRoleIdMap: " + employee.getCode());
                } else {
                    employeeRoleIdMap.put(employee.getCode(), employee.getRoleId());
                }

                if (employeeDeparmentCodeMap.containsKey(employee.getCode())) {
                    System.out.println("Duplicate entry for employee code in employeeDeparmentCodeMap: " + employee.getCode());
                } else {
                    employeeDeparmentCodeMap.put(employee.getCode(), employee.getDepartmentCode());
                }
            });

            allEmployees = employeeData;

        } catch (Exception e) {
            System.out.println("load Employee Cache error: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 1,6,8 * * *")
    public void scheduledCacheRefresh() {
        loadEmployeeCache();
    }

    public String getNameByCode(String code) {
        return employeeCodeNameMap.getOrDefault(code, "");
    }

    public Map<String, String> getEmployeeCodeNameMap() {
        return employeeCodeNameMap;
    }

    public List<EmployeeDTO> getAllEmployeesCache() {
        return allEmployees;
    }

    public String getDepartmentByCode(String code) {
        return employeeDeparmentCodeMap.getOrDefault(code, "");
    }

    public String getRoleByCode(String code) {
        return employeeRoleIdMap.getOrDefault(code, "");
    }
}


