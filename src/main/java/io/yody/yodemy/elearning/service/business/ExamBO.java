package io.yody.yodemy.elearning.service.business;

import io.yody.yodemy.elearning.domain.enumeration.ExamPointStrategyEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamQuizzPoolStrategyEnum;
import io.yody.yodemy.elearning.domain.enumeration.ExamStrategyEnum;
import io.yody.yodemy.elearning.service.helpers.UpdateListHelper;
import io.yody.yodemy.elearning.web.rest.vm.request.DocumentRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.ExamQuizzPoolRequest;
import io.yody.yodemy.elearning.web.rest.vm.request.ExamRequest;
import io.yody.yodemy.rule.knowledgeBase.request.RuleRequest;
import org.nentangso.core.service.errors.NtsValidationException;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class ExamBO {

    private static final String ENTITY_NAME = "exam";
    private Long id;
    private String title;
    private Long categoryId;
    private Boolean requireJoin;
    private Instant applyTime;
    private Instant expireTime;
    private ExamPointStrategyEnum pointStrategy;
    private Long minPointToPass;
    private String description;
    private String thumbUrl;
    private Long numberOfQuestion;
    private ExamQuizzPoolStrategyEnum poolStrategy;
    private ExamStrategyEnum examStrategy;
    private List<ExamQuizzPoolBO> quizzPools;
    private List<DocumentBO> documents;
    private List<ExamRuleBO> rules;
    private Long nodeId;

    public ExamBO() {
    }

    private void validateTime() {
        if (Objects.isNull(applyTime)) {
            throw new NtsValidationException("message", "Phải có thời gian bắt đầu");
        }
        if (!Objects.isNull(expireTime)) {
            if (applyTime.isAfter(expireTime)) {
                throw new NtsValidationException("message", "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc");
            }
        }
    }

    public void validate() {
        validateTime();
    }

    public void update(ExamRequest request) {
        setTitle(request.getTitle());
        setCategoryId(request.getCategoryId());
        setRequireJoin(request.getRequireJoin());
        setApplyTime(request.getApplyTime());
        setExpireTime(request.getExpireTime());
        setPointStrategy(request.getPointStrategy());
        setMinPointToPass(request.getMinPointToPass());
        setDescription(request.getDescription());
        setThumbUrl(request.getThumbUrl());
        setNumberOfQuestion(request.getNumberOfQuestion());
        setPoolStrategy(request.getPoolStrategy());
        setExamStrategy(request.getExamStrategy());

        List<ExamQuizzPoolRequest> quizzPoolRequests = request.getQuizzPools();
        UpdateListHelper.updateListBO(this, quizzPools, quizzPoolRequests, ExamQuizzPoolBO::getId, ExamQuizzPoolRequest::getId,
            ExamQuizzPoolBO::update, ExamQuizzPoolBO::new);

        List<DocumentRequest> documentRequests = request.getDocuments();
        UpdateListHelper.updateListBO(this, documents, documentRequests, DocumentBO::getId, DocumentRequest::getId,
            DocumentBO::update, DocumentBO::new);

        List<RuleRequest> ruleRequests = request.getRules();
        UpdateListHelper.updateListBO(this, rules, ruleRequests, ExamRuleBO::getId, RuleRequest::getId,
            ExamRuleBO::update, ExamRuleBO::new);
    }

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

    public ExamQuizzPoolStrategyEnum getPoolStrategy() {
        return poolStrategy;
    }

    public void setPoolStrategy(ExamQuizzPoolStrategyEnum poolStrategy) {
        this.poolStrategy = poolStrategy;
    }

    public ExamStrategyEnum getExamStrategy() {
        return examStrategy;
    }

    public void setExamStrategy(ExamStrategyEnum examStrategy) {
        this.examStrategy = examStrategy;
    }

    public List<ExamQuizzPoolBO> getQuizzPools() {
        return quizzPools;
    }

    public void setQuizzPools(List<ExamQuizzPoolBO> quizzPools) {
        this.quizzPools = quizzPools;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public List<DocumentBO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentBO> documents) {
        this.documents = documents;
    }

    public List<ExamRuleBO> getRules() {
        return rules;
    }

    public void setRules(List<ExamRuleBO> rules) {
        this.rules = rules;
    }
}
