package com.loopers.application.product;

import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.UserService;
import com.loopers.support.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ProductFacade {

    private final ProductService productService;

    public ProductEntity create(final ProductCriteria.Create criteria) {
        return productService.save(criteria.toCommand());
    }

    @Transactional(readOnly = true)
    public List<ProductResult> getProducts(SortType sortType) {
        List<ProductInfo> products = productService.getProducts(sortType);

        return products.stream().map(ProductResult::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductResult getProduct(Long productId) {
        ProductInfo product = productService.getProduct(productId);
        return ProductResult.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResult> getLikedProducts(String loginId) {
        List<ProductInfo> likedProducts = productService.getLikedProducts(loginId);
        return likedProducts.stream().map(ProductResult::from).collect(Collectors.toList());

    }
}
