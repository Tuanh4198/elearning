package io.yody.yodemy.elearning.web.rest.vm.request;

import io.yody.yodemy.elearning.domain.enumeration.ExamPointStrategyEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamQuizzPoolStrategyEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamStrategyEnum;
import io.yody.yodemy.rule.knowledgeBase.request.RuleRequest;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public class ExamRequest {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Long categoryId;

    private Boolean requireJoin;

    @NotNull
    private Instant applyTime;

    private Instant expireTime;

    @NotNull
    private ExamPointStrategyEnum pointStrategy;

    @NotNull
    private Long minPointToPass;

    private String description;
    private String thumbUrl;
    private Long numberOfQuestion;
    private ExamQuizzPoolStrategyEnum poolStrategy;
    private ExamStrategyEnum examStrategy;
    private List<ExamQuizzPoolRequest> quizzPools;
    private Long nodeId;
    private List<RuleRequest> rules;
    private List<DocumentRequest> documents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getRequireJoin() {
        return requireJoin;
    }

    public void setRequireJoin(Boolean requireJoin) {
        this.requireJoin = requireJoin;
    }

    public Instant getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Instant applyTime) {
        this.applyTime = applyTime;
    }

    public Instant getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Instant expireTime) {
        this.expireTime = expireTime;
    }

    public ExamPointStrategyEnum getPointStrategy() {
        return pointStrategy;
    }

    public void setPointStrategy(ExamPointStrategyEnum pointStrategy) {
        this.pointStrategy = pointStrategy;
    }

    public Long getMinPointToPass() {
        return minPointToPass;
    }

    public void setMinPointToPass(Long minPointToPass) {
        this.minPointToPass = minPointToPass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Long getNumberOfQuestion() {
        return numberOfQuestion;
    }

    public void setNumberOfQuestion(Long numberOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
    }

    public ExamStrategyEnum getExamStrategy() {
        return examStrategy;
    }

    public void setExamStrategy(ExamStrategyEnum examStrategy) {
        this.examStrategy = examStrategy;
    }

    public ExamQuizzPoolStrategyEnum getPoolStrategy() {
        return poolStrategy;
    }

    public void setPoolStrategy(ExamQuizzPoolStrategyEnum poolStrategy) {
        this.poolStrategy = poolStrategy;
    }

    public List<ExamQuizzPoolRequest> getQuizzPools() {
        return quizzPools;
    }

    public void setQuizzPools(List<ExamQuizzPoolRequest> quizzPools) {
        this.quizzPools = quizzPools;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public List<RuleRequest> getRules() {
        return rules;
    }

    public void setRules(List<RuleRequest> rules) {
        this.rules = rules;
    }

    public List<DocumentRequest> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentRequest> documents) {
        this.documents = documents;
    }
}
