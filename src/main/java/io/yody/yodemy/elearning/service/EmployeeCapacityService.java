package io.yody.yodemy.elearning.service;

import io.yody.yodemy.elearning.client.PegasusClient;
import io.yody.yodemy.elearning.service.helpers.Helper;
import io.yody.yodemy.elearning.web.rest.vm.response.EmployeeCapacityBehaviorDTO;
import io.yody.yodemy.elearning.web.rest.vm.response.EmployeeCapacityDTO;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmployeeCapacityService {

    private final PegasusClient pegasusClient;

    public EmployeeCapacityService(PegasusClient pegasusClient) {
        this.pegasusClient = pegasusClient;
    }

    public List<EmployeeCapacityDTO> getEmployeeCapacities() {
        String userCode = Helper.getUserCodeUpperCase();
        return pegasusClient.getEmployeeCapacities(userCode);
    }

    public List<EmployeeCapacityBehaviorDTO> getEmployeeBehaviors() {
        String userCode = Helper.getUserCodeUpperCase();
        return pegasusClient.getEmployeeBehaviors(userCode);
    }
}
