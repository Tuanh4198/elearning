package io.yody.yodemy.elearning.web.rest;

import io.yody.yodemy.elearning.service.StatisticService;
import io.yody.yodemy.elearning.service.dto.CategoryDTO;
import io.yody.yodemy.elearning.service.dto.GeneralStatisticDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StatisticResource {

    private final Logger log = LoggerFactory.getLogger(StatisticResource.class);

    private static final String ENTITY_NAME = "statistic";

    private final StatisticService statisticService;

    public StatisticResource(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<GeneralStatisticDTO> getStatistics() {
        log.debug("Rest request to get categories");
        GeneralStatisticDTO result = statisticService.getGeneralStatistic();

        return ResponseEntity.ok().body(result);
    }
}
