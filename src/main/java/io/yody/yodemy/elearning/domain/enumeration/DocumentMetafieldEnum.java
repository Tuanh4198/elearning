package io.yody.yodemy.elearning.domain.enumeration;

public enum DocumentMetafieldEnum {
    MIME_TYPE("mime_type", "Loại file"),
    TYPE("type", "Loại tài liệu trên môi trường nào");

    private final String key;
    private final String value;

    DocumentMetafieldEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static boolean inValidKey(String key) {
        for (DocumentMetafieldEnum temp : values()) {
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
