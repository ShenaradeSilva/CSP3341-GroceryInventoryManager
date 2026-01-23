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
        String s = name().replace("_", " ").toLowerCase();
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
