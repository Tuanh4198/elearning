package io.yody.yodemy.domain.enumaration;

public enum CapacityTypeEnum {
    SPECIALTY,
    LEADERSHIP,
    CORE_COMPETENCY;

    public static CapacityTypeEnum fromString(String value) {
        for (CapacityTypeEnum capacity : CapacityTypeEnum.values()) {
            if (capacity.name().equalsIgnoreCase(value)) {
                return capacity;
            }
        }
        throw new IllegalArgumentException("No enum constant " + CapacityTypeEnum.class.getCanonicalName() + " with value " + value);
    }

    public static boolean isValid(String value) {
        for (CapacityTypeEnum capacity : CapacityTypeEnum.values()) {
            if (capacity.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
