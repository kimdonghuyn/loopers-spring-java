package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderCriteria;
import com.loopers.domain.coupon.Coupon;
import com.loopers.domain.coupon.CouponRepository;
import com.loopers.domain.user.*;
import com.loopers.infrastructure.user.UserCouponJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.support.enums.DiscountPolicy;
import com.loopers.support.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderV1ApiE2ETest {
    private static final String ENDPOINT = "/api/v1/orders";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponJpaRepository userCouponJpaRepository;

    @BeforeEach
    void setUp() {
        // 유저 생성
        userRepository.save(new UserEntity(
                new LoginId("loopers123"),
                "test",
                new Email("test@naver.com"),
                new Birth("2000-01-01"),
                Gender.F
        ));

        // 쿠폰 생성
        couponRepository.save(new Coupon(
                "Test Coupon",
                DiscountPolicy.FIXED,
                1000,
                null,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                10
        ));

        // 사용자에게 쿠폰 발급
        UserCoupon userCoupon = UserCoupon.create(
                1L,
                1L
        );
        userCouponJpaRepository.save(userCoupon);
    }

    @Test
    @DisplayName("POST /api/v1/orders - 주문 생성에 성공할 경우, 주문 정보를 응답으로 반환한다.")
    void createsOrder_whenOrderCreationIsSuccessful() {
        String loginId = "loopers123";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", loginId);
        OrderV1Dto.OrderRequest orderRequest = new OrderV1Dto.OrderRequest(
                List.of(
                        new OrderCriteria.OrderItem(1L, 2)
                ),
                "CARD",
                "SAMSUNG",
                "1234-5678-9012-3456"
        );
        HttpEntity<OrderV1Dto.OrderRequest> requestEntity = new HttpEntity<>(orderRequest, headers);

        // act
        ParameterizedTypeReference<ApiResponse<OrderV1Dto.OrderResponse>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<ApiResponse<OrderV1Dto.OrderResponse>> response =
                testRestTemplate.exchange(ENDPOINT, HttpMethod.POST, requestEntity, responseType);

        System.out.println("response = " + response);
    }
}
