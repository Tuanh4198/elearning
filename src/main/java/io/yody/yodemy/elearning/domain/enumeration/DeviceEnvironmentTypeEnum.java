package io.yody.yodemy.elearning.domain.enumeration;
public enum DeviceEnvironmentTypeEnum {
    MOBILE("mobile", "MOBILE"),
    WEB("web", "WEB");

    private final String key;
    private final String value;

    DeviceEnvironmentTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static boolean inValidKey(String key) {
        for (DeviceEnvironmentTypeEnum temp : values()) {
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
