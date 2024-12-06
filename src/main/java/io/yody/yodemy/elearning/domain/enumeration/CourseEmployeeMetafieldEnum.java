package io.yody.yodemy.elearning.domain.enumeration;

public enum CourseEmployeeMetafieldEnum {
    LEARN_INFO("learn_info", "Thông tin về việc học"),
    ATTEND_AT("attend_at", "Thời gian điểm danh"),
    LEARN_START_AT("learn_start_at", "Thông tin về bắt đầu việc học"),
    LEARN_FINISH_AT("learn_finish_at", "Thông tin về kết thúc việc học");

    private final String key;
    private final String value;

    CourseEmployeeMetafieldEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static boolean inValidKey(String key) {
        for (CourseEmployeeMetafieldEnum temp : values()) {
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
