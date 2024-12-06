package io.yody.yodemy.elearning.web.rest.vm.request;

import io.yody.yodemy.elearning.service.dto.MetafieldDTO;
import java.util.List;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class ExamQuizzPoolRequest {

    private Long id;
    private Long rootId;
    private Long sourceId;
    private List<MetafieldDTO> metafields;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public List<MetafieldDTO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldDTO> metafields) {
        this.metafields = metafields;
    }
}
