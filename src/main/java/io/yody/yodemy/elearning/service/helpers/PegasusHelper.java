package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.elearning.client.PegasusClient;
import io.yody.yodemy.elearning.client.request.NotifyExamRequest;
import io.yody.yodemy.elearning.client.request.NotifyRequest;
import io.yody.yodemy.elearning.client.request.NotifyTrainingRequest;
import io.yody.yodemy.elearning.domain.CourseEntity;
import io.yody.yodemy.elearning.domain.ExamEntity;
import io.yody.yodemy.elearning.web.rest.vm.response.EmployeeDTO;
import io.yody.yodemy.elearning.web.rest.vm.response.PageableDto;
import io.yody.yodemy.elearning.web.rest.vm.response.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PegasusHelper {

    private final PegasusClient pegasusClient;

    public PegasusHelper(PegasusClient pegasusClient) {
        this.pegasusClient = pegasusClient;
    }

    public List<EmployeeDTO> getEmployeeByCodesIn(List<String> employeeCodes) {
        List<EmployeeDTO> employeeInfos = new ArrayList<>();
        final int BATCH_SIZE = 100;
        for (int i = 0; i < employeeCodes.size(); i += BATCH_SIZE) {
            int endIndex = Math.min(i + BATCH_SIZE, employeeCodes.size());
            List<String> batch = employeeCodes.subList(i, endIndex);
            Result<List<EmployeeDTO>> employeeResult = pegasusClient.getEmployeeByCodeIn(batch);
            employeeInfos.addAll(employeeResult.getData());
        }

        return employeeInfos;
    }

    private List<EmployeeDTO> getEmployeeByBatch(int page, int limit, String positions) {
        Result<PageableDto<EmployeeDTO>> employeeResult = pegasusClient.getAllEmployees(page, limit, null, null, positions, false, null);
        PageableDto<EmployeeDTO> batch = employeeResult.getData();

        return batch.getItems();
    }

    public List<EmployeeDTO> getEmployeeByPositionsIn(String positions) {
        List<EmployeeDTO> employeeInfos = new ArrayList<>();
        int page = 1;
        int limit = 100;
        int maxUser = 10000;

        List<EmployeeDTO> batch = getEmployeeByBatch(page, limit, positions);
        while (page * limit < maxUser && !batch.isEmpty()) {
            employeeInfos.addAll(batch);
            page++;
            batch = getEmployeeByBatch(page, limit, positions);
        }

        return employeeInfos;
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeDTO> employeeInfos = new ArrayList<>();
        int page = 1;
        int limit = 100;
        int maxUser = 10000;
        List<EmployeeDTO> batch = getEmployeeByBatch(page, limit, null);

        while (page * limit < maxUser && !batch.isEmpty()) {
            employeeInfos.addAll(batch);
            page++;
            batch = getEmployeeByBatch(page, limit, null);
        }

        return employeeInfos;
    }

    public void sendNotifyTraining(CourseEntity course, List<String> codes) {
        NotifyRequest notifyRequest = new NotifyRequest();
        notifyRequest.setId(course.getId());
        notifyRequest.setName(course.getTitle());
        notifyRequest.setThumbUrl(course.getThumbUrl());
        NotifyTrainingRequest notifyTrainingRequest = new NotifyTrainingRequest();
        notifyTrainingRequest.setNotify(notifyRequest);
        notifyTrainingRequest.setCodes(codes);
        //        pegasusClient.sendAssignTraining(notifyTrainingRequest);
    }

    public void sendNotifyExam(ExamEntity exam, List<String> codes) {
        NotifyRequest notifyRequest = new NotifyRequest();
        notifyRequest.setId(exam.getId());
        notifyRequest.setName(exam.getTitle());

        NotifyExamRequest notifyExamRequest = new NotifyExamRequest();
        notifyExamRequest.setNotify(notifyRequest);
        notifyExamRequest.setCodes(codes);
        //        pegasusClient.sendAssignExam(notifyExamRequest);
    }
}
