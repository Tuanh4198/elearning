package io.yody.yodemy.elearning.web.rest.vm.request;
import javax.validation.constraints.NotNull;

public class QuizzAnswerRequest {
     /**
     * id
     */
    private Long id;

    /**
     * Quizz id
     */
    private Long rootId;

    /**
     * Tên đáp án
     */
    @NotNull
    private String title;

    /**
     * Nội dung đáp án
     */
    @NotNull
    private String content;

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
}
