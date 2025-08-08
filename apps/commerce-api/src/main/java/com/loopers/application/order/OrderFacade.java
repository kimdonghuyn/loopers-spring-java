package com.loopers.application.order;

import com.loopers.application.user.UserResult;
import com.loopers.domain.coupon.CouponInfo;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserCouponCommand;
import com.loopers.domain.user.UserCouponInfo;
import com.loopers.domain.user.UserCouponService;
import com.loopers.domain.user.UserService;
import com.loopers.support.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final UserService userService;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final ProductService productService;
    private final PointService pointService;
    private final UserCouponService userCouponService;
    private final CouponService couponService;

    @Transactional
    public OrderResult order(OrderCriteria.Order orderCriteria) {
        UserResult userResult = UserResult.from(userService.getUserInfo(orderCriteria.loginId()));

        List<Long> productIds = orderCriteria.orderItems().stream()
                .map(OrderCriteria.OrderItem::productId)
                .toList();

        List<ProductInfo> productInfos = productService.getProductsByProductId(productIds);

        // 쿠폰 적용
        List<Integer> productPrices = productInfos.stream()
                .map(ProductInfo::price)
                .toList();

        UserCouponInfo userCouponInfo = userCouponService.getUserCoupon(new UserCouponCommand.GetUserCoupon(userResult.id()));
        Optional<CouponInfo> couponInfo = couponService.getCoupon(userCouponInfo.couponId());

        // TODO: 추후 쿠폰 적용 로직 개선하기
        int disCountedTotalPrice = couponService.applyCoupon(productPrices, couponInfo.get().status(), couponInfo.get().expiredAt(),
                couponInfo.get().discountPolicy(), couponInfo.get().discountRate(), couponInfo.get().discountAmount());

        // 결제 처리 (외부 결제 시스템)
        OrderStatus orderStatus = paymentService.pay(OrderCriteria.Payment.toCommand(userResult.id(), disCountedTotalPrice));

        OrderCommand.Order orderCommand = orderCriteria.toCommand(productInfos);
        OrderInfo orderInfo = orderService.order(orderCommand);

        // 포인트 차감
        pointService.use(new PointCommand.Use(orderCriteria.loginId(), (long) disCountedTotalPrice));

        // 재고 차감
        orderCommand.orderItems().forEach(item ->
                productService.consume(new ProductCommand.Consume(item.productId(), item.quantity()))
        );

        return OrderResult.from(orderInfo);
    }
}
