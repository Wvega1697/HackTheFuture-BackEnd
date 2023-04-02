package com.wvega.models.enums;

public enum SubType {
    FUEL("Fuel"),
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
