package io.yody.yodemy.elearning.service.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class EdgeDTO implements Serializable {
    private Long id;
    private Long source;
    private Long target;
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
