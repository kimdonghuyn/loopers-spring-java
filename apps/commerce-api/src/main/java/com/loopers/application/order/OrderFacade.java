package com.loopers.application.order;

import com.loopers.application.payment.PaymentStrategy;
import com.loopers.application.user.UserResult;
import com.loopers.domain.coupon.CouponService;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentCommand;
import com.loopers.domain.payment.PaymentInfo;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.point.PointCommand;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserCouponCommand;
import com.loopers.domain.user.UserCouponInfo;
import com.loopers.domain.user.UserCouponService;
import com.loopers.domain.user.UserService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;
    private final UserCouponService userCouponService;
    private final CouponService couponService;
    private final Map<String, PaymentStrategy> paymentStrategies;

    private final Logger logger = LogManager.getLogger(OrderFacade.class);
    private final PaymentService paymentService;

    @Transactional
    public OrderResult order(OrderCriteria.Order orderCriteria) {
        UserResult userResult = UserResult.from(userService.getUserInfo(orderCriteria.loginId()));

        List<Long> productIds = orderCriteria.orderItems().stream()
                .map(OrderCriteria.OrderItem::productId)
                .toList();

        List<ProductInfo> productInfos = productService.getProductsByProductId(productIds);

        BigDecimal totalPrice = productInfos.stream()
                .map(ProductInfo::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(0, BigDecimal.ROUND_DOWN);

        UserCouponInfo userCouponInfo =
                userCouponService.getUserCoupon(new UserCouponCommand.GetUserCoupon(userResult.id()));

        BigDecimal discountedTotalPrice =
                couponService.calculateDiscountPrice(
                        productInfos.stream().map(ProductInfo::price).toList(),
                        userCouponInfo.couponId()
                );

        OrderCommand.Order orderCommand = orderCriteria.toCommand(userResult.id(), productInfos, discountedTotalPrice);
        OrderInfo orderInfo = orderService.order(orderCommand);

        PaymentStrategy strategy = paymentStrategies.get(orderCriteria.paymentMethod().name());
        if (strategy == null) {
            throw new CoreException(ErrorType.BAD_REQUEST, "지원하지 않는 결제 방식입니다: " + orderCriteria.paymentMethod());
        }

        strategy.processPayment(new PaymentCommand.Payment(
                        userResult.loginId(),
                        orderInfo.orderKey(),
                        discountedTotalPrice.longValue(),
                        orderCriteria.paymentMethod(),
                        orderCriteria.cardType(),
                        orderCriteria.cardNo()
                )
        );

        orderCommand.orderItems().forEach(item ->
                productService.consume(new ProductCommand.Consume(item.productId(), item.quantity()))
        );

        return OrderResult.from(orderInfo);
    }

    public void processPendingOrders() {
        // 주문 상태가 PENDING인 주문들 가져오기
        List<OrderInfo> pendingOrders = orderService.getOrdersByStatus("PENDING");

        // 결제 정보 가져오기 (PG)
        for (OrderInfo order : pendingOrders) {
            try {
                PaymentInfo.Card.Order paymentInfo = paymentService.findCardPaymentResult(order.orderKey());
                if (paymentInfo == null) {
                    logger.warn("No payment info found for orderKey: " + order.orderKey());
                    continue;
                }

                // 결제 상태에 따라 주문 상태 업데이트
                String newStatus;
                if (paymentInfo.paymentResults().stream().allMatch(p -> "SUCCESS".equals(p.status()))) {
                    newStatus = "COMPLETED";
                } else if (paymentInfo.paymentResults().stream().anyMatch(p -> "FAILED".equals(p.status()))) {
                    newStatus = "CANCELLED";
                } else {
                    newStatus = "PENDING";
                }
                orderService.updateOrderStatus(new OrderCommand.UpdateOrderStatus(order.orderKey(), newStatus));
            } catch (Exception e) {
                logger.error("Error processing order with orderKey: " + order.orderKey(), e);
            }
        }
    }
}
