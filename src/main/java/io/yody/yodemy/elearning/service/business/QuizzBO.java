package io.yody.yodemy.elearning.service.business;

import io.yody.yodemy.elearning.domain.enumeration.ExamQuizzMetafieldEnum;
import io.yody.yodemy.elearning.domain.enumeration.QuizzTypeEnum;
import io.yody.yodemy.elearning.service.dto.MetafieldDTO;
import io.yody.yodemy.elearning.service.helpers.UpdateListHelper;
import io.yody.yodemy.elearning.service.mapper.MetafieldMapper;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzAnswerRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.QuizzRequest;
import org.nentangso.core.service.errors.NtsValidationException;

import java.util.List;

public class QuizzBO {

    private Long id;
    private String content;
    private Long categoryId;
    private QuizzTypeEnum type;
    private List<QuizzAnswerBO> answers;
    private List<MetafieldBO> metafields;

    public QuizzBO() {
    }

    public QuizzBO(Long id, String content, Long categoryId, QuizzTypeEnum type, List<QuizzAnswerBO> answers, List<MetafieldBO> metafields) {
        this.id = id;
        this.content = content;
        this.categoryId = categoryId;
        this.type = type;
        this.answers = answers;
        this.metafields = metafields;
    }

    public QuizzBO(QuizzRequest request) {
        setId(request.getId());
        setContent(request.getContent());
        setCategoryId(request.getCategoryId());
        setType(request.getType());
        _setMetafields(request.getMetafields());
        _setAnswers(request.getAnswers());
    }

    public void update(QuizzRequest request) {
        setId(request.getId());
        setContent(request.getContent());
        setCategoryId(request.getCategoryId());
        setType(request.getType());
        _setMetafields(request.getMetafields());
        _setAnswers(request.getAnswers());
    }

    private void _setMetafields(List<MetafieldDTO> requests) {
        setMetafields(MetafieldMapper.INSTANCE.dtosToBos(requests));
    }

    private void _setAnswers(List<QuizzAnswerRequest> requests) {
        UpdateListHelper.updateListBO(this, answers, requests, QuizzAnswerBO::getId, QuizzAnswerRequest::getId,
            QuizzAnswerBO::update, QuizzAnswerBO::new);
    }

    private void validateMetafields() {
        for (MetafieldBO metafieldBO : metafields) {
            if (ExamQuizzMetafieldEnum.inValidKey(metafieldBO.getKey())) {
                throw new NtsValidationException("message", String.format("Metafield không hợp lệ %s", metafieldBO.getKey()));
            }
        }
    }

    private void validateAnswers() {
        if (type == QuizzTypeEnum.MULTIPLE_CHOICE && answers.isEmpty()) {
            throw new NtsValidationException("message", String.format("Answers không hợp lệ %s", "answers"));
        }
        if (type != QuizzTypeEnum.MULTIPLE_CHOICE && !answers.isEmpty()) {
            throw new NtsValidationException("message", String.format("Answers không hợp lệ %s", "answers"));
        }
        for (QuizzAnswerBO answer : answers) {
            answer.validate();
        }
    }

    public List<QuizzAnswerBO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizzAnswerBO> answers) {
        this.answers = answers;
    }

    public List<MetafieldBO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldBO> metafields) {
        this.metafields = metafields;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public QuizzTypeEnum getType() {
        return type;
    }

    public void setType(QuizzTypeEnum type) {
        this.type = type;
    }

    public void validate() {
        validateMetafields();
        validateAnswers();
    }
}
