package io.yody.yodemy.elearning.domain.enumeration;

public enum NodeMetafieldEnum {
    DESCRIPTION("description", "Mô tả node"),
    POSITION("position", "Thứ tự node");

    private final String key;
    private final String description;

    NodeMetafieldEnum(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public static boolean inValidKey(String key) {
        for (NodeMetafieldEnum temp : values()) {
            if (temp.key.equalsIgnoreCase(key)) return false;
        }
        return true;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
