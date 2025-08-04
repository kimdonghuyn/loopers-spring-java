package com.loopers.application.product;

import com.loopers.domain.brand.BrandInfo;
import com.loopers.domain.brand.BrandService;
import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.product.ProductInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.support.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ProductFacade {
    private final BrandService brandService;
    private final ProductService productService;

    @Transactional
    public ProductEntity create(final ProductCriteria.Create criteria) {
        return productService.save(criteria.toCommand());
    }

    @Transactional(readOnly = true)
    public List<ProductResult> getProducts(SortType sortType) {
        List<ProductInfo> products = productService.getProducts(sortType);

        Map<Long, Optional<BrandInfo>> brandMap = getBrandMap(products);

        List<ProductResult> productResult = mergeProductAndBrand(products, brandMap);

        return productResult;
    }

    @Transactional(readOnly = true)
    public ProductResult getProduct(Long productId) {
        ProductInfo product = productService.getProduct(productId);
        Optional<BrandInfo> brandInfo = brandService.getBrandInfo(product.brandId());
        return brandInfo.map(info -> ProductResult.from(product, info))
                        .orElseThrow(() -> new IllegalStateException("BrandInfo not found for productId: " + productId));
    }

    @Transactional(readOnly = true)
    public List<ProductResult> getLikedProducts(Long userId) {
        List<ProductInfo> likedProducts = productService.getLikedProducts(userId);
        Map<Long, Optional<BrandInfo>> brandMap = getBrandMap(likedProducts);
        return mergeProductAndBrand(likedProducts, brandMap);
    }

    private Map<Long, Optional<BrandInfo>> getBrandMap(List<ProductInfo> products) {
        Map<Long, Optional<BrandInfo>> brandMap = products.stream().map(ProductInfo::brandId)
                .distinct().collect(
                        Collectors.toMap(
                                brandId -> brandId,
                                brandService::getBrandInfo
                        )
                );
        return brandMap;
    }

    private List<ProductResult> mergeProductAndBrand(List<ProductInfo> products, Map<Long, Optional<BrandInfo>> brandMap) {
        return products.stream()
                .map(product -> {
                    Optional<BrandInfo> brandInfo = brandMap.get(product.brandId());
                    return brandInfo.map(info -> ProductResult.from(product, info)).orElse(null);
                })
                .collect(Collectors.toList());
    }
}
