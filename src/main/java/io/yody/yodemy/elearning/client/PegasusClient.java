package io.yody.yodemy.elearning.client;

import feign.Body;
import io.yody.yodemy.elearning.client.request.NotifyExamRequest;
import io.yody.yodemy.elearning.client.request.NotifyTrainingRequest;
import io.yody.yodemy.elearning.web.rest.vm.response.*;
import java.util.List;
import org.nentangso.core.client.NtsAuthorizedFeignClient;
import org.springframework.web.bind.annotation.*;

@NtsAuthorizedFeignClient(name = "pegasus-client", url = "${pegasus.client.url}")
public interface PegasusClient {
    @GetMapping("/api/positions")
    List<PositionDTO> getAllPositions(
        @RequestParam(name = "name.contains") String name,
        @RequestParam(name = "code.in") List<String> codes,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "30") int limit,
        @RequestParam(name = "sort", defaultValue = "id,asc") String sort
    );

    @GetMapping("/api/departments")
    List<DepartmentDTO> getAllDepartments(
        @RequestParam(name = "code.notEquals") String codeNotEquals,
        @RequestParam(name = "code.contains") String code,
        @RequestParam(name = "name.contains") String name,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "30") int limit,
        @RequestParam(name = "sort", defaultValue = "id,asc") String sort
    );

    @GetMapping("/api/employees/search")
    Result<PageableDto<EmployeeDTO>> getAllEmployees(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "limit", defaultValue = "30") int limit,
        @RequestParam(name = "info") String name,
        @RequestParam(name = "departments") String departmentNames,
        @RequestParam(name = "positions") String positionNames,
        @RequestParam(name = "filter_represent", defaultValue = "false") Boolean filterRepresent,
        @RequestParam(name = "status", defaultValue = "WORKING") String status
    );

    @GetMapping("/api/employees/list")
    Result<List<EmployeeDTO>> getEmployeeByCodeIn(@RequestParam(name = "codes") List<String> codes);

    @GetMapping("/api/employee-capacities/get-by-code/{code}")
    List<EmployeeCapacityDTO> getEmployeeCapacities(@PathVariable(name = "code") String code);

    @GetMapping("/api/employee-behavior-capacities/get-by-code/{code}")
    List<EmployeeCapacityBehaviorDTO> getEmployeeBehaviors(@PathVariable(name = "code") String code);

    @PostMapping("/api/external-notifications/upcoming-training")
    void sendUpcomingTraining(@RequestBody NotifyTrainingRequest request);

    @PostMapping("/api/external-notifications/assign-training")
    void sendAssignTraining(@RequestBody NotifyTrainingRequest request);

    @PostMapping("/api/external-notifications/assign-exam")
    void sendAssignExam(@RequestBody NotifyExamRequest request);
}
