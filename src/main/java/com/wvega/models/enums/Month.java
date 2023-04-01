package com.wvega.models.enums;

public enum Month {
    JANUARY("January", 1),
    FEBRUARY("February", 2),
    MARCH("March", 3),
    APRIL("April", 4),
    MAY("May", 5),
    JUNE("June", 6),
    JULY("July", 7),
    AUGUST("August", 8),
    SEPTEMBER("September", 9),
    OCTOBER("October", 10),
    NOVEMBER("November", 11),
    DECEMBER("December", 12);

    private final String value;
    private final int numericValue;

    Month(String value, int numericValue) {
        this.value = value;
        this.numericValue = numericValue;
    }

    public String getValue() {
        return value;
    }

    public int getNumericValue() {
        return numericValue;
    }
}

