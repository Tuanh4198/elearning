package io.yody.yodemy.elearning.domain.enumeration;

import java.util.regex.Pattern;

public enum ExamMetafieldEnum {
    WORKING_TIME("working_time", "Thời gian làm bài"),
    MAX_NUMBER_OF_TEST("max_number_of_test", "Số lần làm bài");

    private final String key;
    private final String value;

    ExamMetafieldEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static boolean inValidKey(String key) {
        for (ExamMetafieldEnum temp : values()) {
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
