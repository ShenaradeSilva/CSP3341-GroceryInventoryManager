package com.csp3341.grocery;

public enum Category {
    DAIRY,
    PRODUCE,
    MEAT,
    BEVERAGES,
    CANNED_FOOD,
    DRIED_FOOD;

    @Override
    public String toString() {
        return name();
    }
}
