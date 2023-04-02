package com.wvega.models.enums;

public enum CategoryType {
    ADMIN("Admin"),
    INDIRECT("Indirect"),
    LOGISTIC("Logistic"),
    OPERATION("Operation"),
    DISTRIBUTION("Distribution"),
    SALES("Sales");

    private final String value;

    CategoryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
