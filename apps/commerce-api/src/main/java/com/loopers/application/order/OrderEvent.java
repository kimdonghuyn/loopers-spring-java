package com.loopers.application.order;

import com.loopers.application.user.UserResult;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.user.UserCouponInfo;

public record OrderEvent(
        OrderCommand.Order orderCommand,
        UserResult userResult,
        UserCouponInfo userCouponInfo
) {}
