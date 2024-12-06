package io.yody.yodemy.elearning.web.rest.vm.request;
import io.yody.yodemy.elearning.domain.enumeration.QuizzTypeEnum;
import io.yody.yodemy.elearning.service.dto.MetafieldDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

public class QuizzRequest {

    /**
     * Id
     */
    private Long id;

    /**
     * Nội dung
     */
    @NotNull
    private String content;


    /**
     * Id danh mục
     */
    @NotNull
    private Long categoryId;

    /**
     * Loại câu hỏi
     */
    @NotNull
    private QuizzTypeEnum type;

    /**
     * Câu trả lời
     */
    List<QuizzAnswerRequest> answers;

    List<MetafieldDTO> metafields;

    public List<MetafieldDTO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldDTO> metafields) {
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

    public List<QuizzAnswerRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizzAnswerRequest> answers) {
        this.answers = answers;
    }
}
