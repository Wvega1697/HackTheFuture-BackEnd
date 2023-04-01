package com.wvega.models;

public enum SubType {
    PACKAGING_PLANT("Packaging plant"),
    OIL("Oil"),
    COOLANT("Coolant"),
    SHEETS("Sheets");

    private final String value;

    SubType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
