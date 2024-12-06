package io.yody.yodemy.elearning.service.business;

import io.yody.yodemy.elearning.web.rest.vm.request.QuizzAnswerRequest;
import org.nentangso.core.service.errors.NtsValidationException;

public class QuizzAnswerBO {

    private Long id;
    private Long rootId;
    private String title;
    private String content;

    public QuizzAnswerBO() {
    }

    public QuizzAnswerBO(Long id, Long rootId, String title, String content) {
        this.id = id;
        this.rootId = rootId;
        this.title = title;
        this.content = content;
    }

    public QuizzAnswerBO(QuizzBO quizz, QuizzAnswerRequest request) {
        setRootId(quizz.getId());
        setTitle(request.getTitle());
        setContent(request.getContent());
    }

    public void update(QuizzBO quizz, QuizzAnswerRequest request) {
        setRootId(quizz.getId());
        setTitle(request.getTitle());
        setContent(request.getContent());
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private void validateAnswer() {
        if (title.isEmpty() || !title.startsWith("option")) {
            throw new NtsValidationException("message", String.format("Tiêu đề không hợp lệ %s", getTitle()));
        }
    }

    public void validate() {
        validateAnswer();
    }
}
