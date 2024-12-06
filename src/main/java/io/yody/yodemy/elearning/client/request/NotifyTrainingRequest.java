package io.yody.yodemy.elearning.client.request;

import java.util.List;

public class NotifyTrainingRequest {

    NotifyRequest notify;
    List<String> codes;

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
}
