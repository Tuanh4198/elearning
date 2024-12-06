package io.yody.yodemy.elearning.service.helpers;

import io.yody.yodemy.elearning.domain.constant.NextIdConst;
import io.yody.yodemy.elearning.service.business.redis.IDGenerator;
import io.yody.yodemy.elearning.web.rest.vm.request.NodeRequest;
import io.yody.yodemy.rule.knowledgeBase.request.RuleRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class IdHelper {
    private final IDGenerator idGenerator;

    public IdHelper(IDGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public void processNodeId(List<NodeRequest> requests) {
        for (NodeRequest request:requests) {
            if (Objects.isNull(request.getId())) {
                request.setTempId(idGenerator.nextId(NextIdConst.NODE));
            } else {
                request.setTempId(request.getId());
            }
        }
        List<RuleRequest> ruleRequests = requests.stream()
            .flatMap(ruleRequest -> {
                List<RuleRequest> rules = new ArrayList<>();
                if (!ObjectUtils.isEmpty(ruleRequest.getRules())) {
                    rules = ruleRequest.getRules();
                }
                return rules.stream();
            })
            .collect(Collectors.toList());
        processRuleId(ruleRequests);
    }

    public void processRuleId(List<RuleRequest> requests) {
        for (RuleRequest request:requests) {
            if (Objects.isNull(request.getId())) {
                request.setTempId(idGenerator.nextId(NextIdConst.RULE));
            } else {
                request.setTempId(request.getId());
            }
        }
    }
}
