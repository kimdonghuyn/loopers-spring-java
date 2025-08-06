package com.loopers.support.enums;

import lombok.Getter;

@Getter
public enum CouponStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화");

    private final String description;

    CouponStatus(String description) {
        this.description = description;
    }
}
