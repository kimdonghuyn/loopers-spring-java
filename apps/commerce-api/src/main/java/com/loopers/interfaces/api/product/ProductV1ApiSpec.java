package com.loopers.interfaces.api.product;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Tag(name = "Product V1 API", description = "상품 관련 API")
public interface ProductV1ApiSpec {
    @Operation(
            summary = "상품 목록 조회",
            description = "상품 목록을 조회합니다."
    )
    ApiResponse<List<ProductV1Dto.GetProductsResponse>> getProductsByBrandId(
            Long brandId,
            @Schema(name = "페이징 정보", description = "페이지네이션을 위한 정보입니다. 기본값은 20개씩 내림차순 정렬입니다.")
            Pageable pageable
    );
}
