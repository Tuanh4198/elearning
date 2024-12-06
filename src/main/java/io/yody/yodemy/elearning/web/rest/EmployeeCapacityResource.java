package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.service.EmployeeCapacityService;
import io.yody.yodemy.elearning.web.rest.vm.response.EmployeeCapacityBehaviorDTO;
import io.yody.yodemy.elearning.web.rest.vm.response.EmployeeCapacityDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmployeeCapacityResource {

    private final Logger log = LoggerFactory.getLogger(EmployeeCapacityResource.class);

    private static final String ENTITY_NAME = "employee_capacity";

    private final EmployeeCapacityService employeeCapacityService;

    public EmployeeCapacityResource(EmployeeCapacityService employeeCapacityService) {
        this.employeeCapacityService = employeeCapacityService;
    }

    @GetMapping("/employee-capacities")
    public ResponseEntity<List<EmployeeCapacityDTO>> getEmployeeCapacities() {
        log.debug("REST request to get Employees by code");
        return ResponseEntity.ok(employeeCapacityService.getEmployeeCapacities());
    }

    @GetMapping("/employee-behavior-capacities")
    public ResponseEntity<List<EmployeeCapacityBehaviorDTO>> getEmployeeBehaviors() {
        log.debug("REST request to get Employees by code");
        return ResponseEntity.ok(employeeCapacityService.getEmployeeBehaviors());
    }
}
