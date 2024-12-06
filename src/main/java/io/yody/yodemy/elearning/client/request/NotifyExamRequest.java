package io.yody.yodemy.elearning.client.request;

import java.util.List;
import java.util.Map;

public class NotifyExamRequest {

    NotifyRequest notify;
    List<String> codes;
    Map<String, Long> codeToTaskId;

    public NotifyRequest getNotify() {
        return notify;
    }

    public void setNotify(NotifyRequest notify) {
        this.notify = notify;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public Map<String, Long> getCodeToTaskId() {
        return codeToTaskId;
    }

    public void setCodeToTaskId(Map<String, Long> codeToTaskId) {
        this.codeToTaskId = codeToTaskId;
    }
}
