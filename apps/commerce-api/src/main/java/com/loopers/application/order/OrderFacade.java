package com.loopers.application.order;

import com.loopers.application.user.UserResult;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderInfo;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.payment.PaymentService;
import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserService;
import com.loopers.support.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderFacade {

    private final UserService userService;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final ProductService productService;
    private final PointService pointService;

    @Transactional
    public OrderResult order(OrderCriteria.Order orderCriteria) {
        UserResult userResult = UserResult.from(userService.getUserInfo(orderCriteria.loginId()));

        List<ProductInfo> productInfos = productService.getProductsByProductId(
                orderCriteria.orderItems().stream()
                        .map(OrderCriteria.OrderItem::productId)
                        .toList()
        );

        // 결제 처리
        OrderStatus orderStatus = paymentService.pay(userResult.id(), productInfos);

        OrderCommand.Order orderCommand = orderCriteria.toCommand(productInfos);
        OrderInfo orderInfo = orderService.order(orderCommand);

        // 포인트 차감
        pointService.use(new PointCommand.Use(orderCriteria.loginId(), (long) orderInfo.totalPrice()));

        // 재고 차감
        orderCommand.orderItems().forEach(item ->
                productService.consume(new ProductCommand.Consume(item.productId(), item.quantity()))
        );

        return OrderResult.from(orderInfo);
    }
}
