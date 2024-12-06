package io.yody.yodemy.elearning.domain.enumeration;

public enum ExamQuizzMetafieldEnum {
    ANSWER("answer", "Đáp án đúng");

    private final String key;
    private final String value;

    ExamQuizzMetafieldEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static boolean inValidKey(String key) {
        for (ExamQuizzMetafieldEnum temp : values()) {
            if (temp.key.equals(key))
                return false;
        }
        return true;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
