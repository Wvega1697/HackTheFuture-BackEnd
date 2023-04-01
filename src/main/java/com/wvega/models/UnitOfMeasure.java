package com.wvega.models;

public enum UnitOfMeasure {
    GALLON("Gallon"),
    SHEETS("Sheets"),
    KW("Kw"),
    TRAVEL("Travel");

    private final String value;

    UnitOfMeasure(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

