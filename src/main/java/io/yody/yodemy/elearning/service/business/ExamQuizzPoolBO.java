package io.yody.yodemy.elearning.service.business;

import io.yody.yodemy.elearning.service.mapper.MetafieldMapper;
import io.yody.yodemy.elearning.web.rest.vm.request.ExamQuizzPoolRequest;
import java.util.List;

public class ExamQuizzPoolBO {

    private Long id;
    private Long rootId;
    private Long sourceId;
    private List<MetafieldBO> metafields;

    public ExamQuizzPoolBO() {}

    public ExamQuizzPoolBO(Long id, Long rootId, Long sourceId, List<MetafieldBO> metafields) {
        this.id = id;
        this.rootId = rootId;
        this.sourceId = sourceId;
        this.metafields = metafields;
    }

    public ExamQuizzPoolBO(ExamBO root, ExamQuizzPoolRequest request) {
    }

    public void update(ExamBO root, ExamQuizzPoolRequest request) {
        setRootId(root.getId());
        setSourceId(request.getSourceId());
        setMetafields(MetafieldMapper.INSTANCE.dtosToBos(request.getMetafields()));
    }

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

    public List<MetafieldBO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldBO> metafields) {
        this.metafields = metafields;
    }
}
