package com.loopers.support;

public enum SortType {
    LATEST,
    PRICE_ASC,
    LIKES_DESC;

    public static SortType from(String value) {
        try {
            return SortType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return LATEST;
        }
    }
}
