package io.yody.yodemy.elearning.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.yody.yodemy.elearning.domain.enumeration.*;
import io.yody.yodemy.rule.knowledgeBase.dto.RuleDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link io.yody.yodemy.elearning.domain.ExamEntity} entity.
 */
@Schema(description = "Kỳ thi")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExamDTO implements Serializable {

    /**
     * id
     */
    @NotNull
    @Schema(description = "id", required = true)
    private Long id;

    /**
     * title
     */
    @NotNull
    @Size(min = 1, max = 255)
    @Schema(description = "title", required = true)
    private String title;

    /**
     * Id danh mục
     */
    @Schema(description = "Id danh mục")
    private Long categoryId;

    /**
     * Kỳ thi bắt buộc
     */
    @Schema(description = "Kỳ thi bắt buộc")
    private Boolean requireJoin;

    /**
     * Chiến thuật giao câu hỏi
     */
    @Schema(description = "Chiến thuật giao câu hỏi")
    private AssignStrategyEnum assignStrategy;

    /**
     * Danh sách đối tượng tham gia, ngăn cách bởi dấu ,
     */
    @Schema(description = "Danh sách đối tượng tham gia, ngăn cách bởi dấu ,")
    private String assignStrategyJson;

    /**
     * Thời gian bắt đầu
     */
    @NotNull
    @Schema(description = "Thời gian bắt đầu", required = true)
    private Instant applyTime;

    /**
     * Thời gian kết thúc
     */
    @Schema(description = "Thời gian kết thúc")
    private Instant expireTime;

    /**
     * Cách chấm điểm
     */
    @NotNull
    @Schema(description = "Cách chấm điểm", required = true)
    private ExamPointStrategyEnum pointStrategy;

    /**
     * Điểm tối thiểu cần đạt
     */
    @NotNull
    @Schema(description = "Điểm tối thiểu cần đạt", required = true)
    private Long minPointToPass;

    /**
     * Mô tả
     */
    @Schema(description = "Mô tả")
    private String description;

    /**
     * Ảnh bìa
     */
    @Schema(description = "Ảnh bìa")
    private String thumbUrl;

    /**
     * Id bài đào tạo
     */
    @Schema(description = "Id bài đào tạo")
    private Long courseId;

    /**
     * Tổng số lượng câu hỏi
     */
    @NotNull
    @Schema(description = "Tổng số lượng câu hỏi", required = true)
    private Long numberOfQuestion;

    /**
     * Chiến thuật chọn pool câu hỏi
     */
    @Schema(description = "Chiến thuật chọn pool câu hỏi")
    private ExamQuizzPoolStrategyEnum poolStrategy;

    @Schema(description = "Chiến thuật ra đề thi")
    private ExamStrategyEnum examStrategy;

    /**
     * Danh sách metafields
     */
    @Schema(description = "metafields")
    private List<MetafieldDTO> metafields;

    private List<ExamQuizzPoolDTO> quizzPools;
    private List<DocumentDTO> documents;
    private List<RuleDTO> rules;

    private CategoryDTO category;
    private ExamEmployeeStatusEnum status;
    private Long nodeId;

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

    public AssignStrategyEnum getAssignStrategy() {
        return assignStrategy;
    }

    public void setAssignStrategy(AssignStrategyEnum assignStrategy) {
        this.assignStrategy = assignStrategy;
    }

    public String getAssignStrategyJson() {
        return assignStrategyJson;
    }

    public void setAssignStrategyJson(String assignStrategyJson) {
        this.assignStrategyJson = assignStrategyJson;
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

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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

    public List<MetafieldDTO> getMetafields() {
        return metafields;
    }

    public void setMetafields(List<MetafieldDTO> metafields) {
        this.metafields = metafields;
    }

    public List<ExamQuizzPoolDTO> getQuizzPools() {
        return quizzPools;
    }

    public void setQuizzPools(List<ExamQuizzPoolDTO> quizzPools) {
        this.quizzPools = quizzPools;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public List<DocumentDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentDTO> documents) {
        this.documents = documents;
    }

    public List<RuleDTO> getRules() {
        return rules;
    }

    public void setRules(List<RuleDTO> rules) {
        this.rules = rules;
    }

    public ExamEmployeeStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ExamEmployeeStatusEnum status) {
        this.status = status;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExamDTO)) {
            return false;
        }

        ExamDTO examDTO = (ExamDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, examDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExamDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", categoryId=" + getCategoryId() +
            ", requireJoin='" + getRequireJoin() + "'" +
            ", assignStrategy='" + getAssignStrategy() + "'" +
            ", assignStrategyJson='" + getAssignStrategyJson() + "'" +
            ", applyTime='" + getApplyTime() + "'" +
            ", expireTime='" + getExpireTime() + "'" +
            ", pointStrategy='" + getPointStrategy() + "'" +
            ", minPointToPass=" + getMinPointToPass() +
            ", description='" + getDescription() + "'" +
            ", thumbUrl='" + getThumbUrl() + "'" +
            ", courseId=" + getCourseId() +
            ", numberOfQuestion=" + getNumberOfQuestion() +
            ", strategy='" + getPoolStrategy() + "'" +
            "}";
    }
}
