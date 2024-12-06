package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.service.FlowQueryService;
import io.yody.yodemy.elearning.service.FlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FlowResource {
    private final Logger log = LoggerFactory.getLogger(FlowResource.class);

    private static final String ENTITY_NAME = "flow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlowService flowService;

    private final FlowQueryService flowQueryService;

    public FlowResource(FlowService flowService, FlowQueryService flowQueryService) {
        this.flowService = flowService;
        this.flowQueryService = flowQueryService;
    }
}
