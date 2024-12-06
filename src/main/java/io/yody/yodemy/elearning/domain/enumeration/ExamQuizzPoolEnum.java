package io.yody.yodemy.elearning.domain.enumeration;

public enum ExamQuizzPoolEnum {
    WEIGHT("weight", "Tỉ trọng câu hỏi trong danh mục");

    private final String key;
    private final String value;

    ExamQuizzPoolEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static boolean inValidKey(String key) {
        for (ExamQuizzPoolEnum temp : values()) {
            if (temp.key.equalsIgnoreCase(key)) return false;
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
