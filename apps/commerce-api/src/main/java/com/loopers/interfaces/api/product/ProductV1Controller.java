package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductFacade;
import com.loopers.interfaces.api.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductV1Controller implements ProductV1ApiSpec{
    private final ProductFacade productFacade;

    public ProductV1Controller(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @Override
    @GetMapping("")
    public ApiResponse<List<ProductV1Dto.GetProductsResponse>> getProductsByBrandId(
            @RequestParam Long brandId,
            @PageableDefault(size = 20, sort = {"likeCount", "id", "price"}, direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        var query = ProductV1Dto.GetProductsRequest.toQuery(brandId, pageable);
        var response = ProductV1Dto.GetProductsResponse.from(productFacade.getProductsByBrandId(query));

        return ApiResponse.success(response);
    }
}
