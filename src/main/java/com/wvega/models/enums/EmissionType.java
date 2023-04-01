package com.wvega.models.enums;

public enum EmissionType {
    DIRECT("Direct"),
    INDIRECT("Indirect"),
    OTHER("Other");

    private final String value;

    EmissionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
