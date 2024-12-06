package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.client.PegasusClient;
import io.yody.yodemy.elearning.web.rest.vm.response.DepartmentDTO;
import io.yody.yodemy.elearning.web.rest.vm.response.PositionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PegasusResource {

    private final PegasusClient pegasusClient;

    public PegasusResource(PegasusClient pegasusClient) {
        this.pegasusClient = pegasusClient;
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDTO>> getDepartments(
        @RequestParam(name = "code.notEquals", defaultValue = "") String codeNotEquals,
        @RequestParam(name = "code.contains", required = false) String code,
        @RequestParam(name = "name.contains", required = false) String name,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "1000") int limit
    ) {
        List<DepartmentDTO> departments = pegasusClient.getAllDepartments(codeNotEquals, code, name, page, limit, null);
        return ResponseEntity.ok().body(departments);
    }

    @GetMapping("/positions")
    public ResponseEntity<List<PositionDTO>> getPositions(
        @RequestParam(name = "name.contains", required = false) String name,
        @RequestParam(name = "code.in", required = false) List<String> codes,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "1000") int limit
    ) {
        List<PositionDTO> departments = pegasusClient.getAllPositions(name, codes, page, limit, null);
        return ResponseEntity.ok().body(departments);
    }
}
