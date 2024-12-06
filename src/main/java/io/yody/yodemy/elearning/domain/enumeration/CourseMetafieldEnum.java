package io.yody.yodemy.elearning.domain.enumeration;

public enum CourseMetafieldEnum {
    MINIMUM_STUDY_TIME("minimum_study_time", "Thời gian học tối thiểu");

    private final String key;
    private final String value;

    CourseMetafieldEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static boolean inValidKey(String key) {
        for (CourseMetafieldEnum temp : values()) {
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
