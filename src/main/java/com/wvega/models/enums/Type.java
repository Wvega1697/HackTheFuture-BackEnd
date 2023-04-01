package com.wvega.models.enums;

public enum Type {
    FUEL("Fuel"),
    ELECTRICITY("Electricity"),
    PETROLEUM_DERIVED_PRODUCT("Petroleum-derived product"),
    TRAVEL("Travel"),
    SUPPLY("Supply");

    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

