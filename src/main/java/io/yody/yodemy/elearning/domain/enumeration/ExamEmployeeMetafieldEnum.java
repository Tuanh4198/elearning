package io.yody.yodemy.elearning.domain.enumeration;

public enum ExamEmployeeMetafieldEnum {
    QUIZZ_ID("quizz_id", "Id câu hỏi"),
    ATTEND_AT("attend_at", "Thời gian tham gia kì thi");

    private final String key;
    private final String value;

    ExamEmployeeMetafieldEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
