package com.loopers.domain.product;

import com.loopers.support.enums.SortType;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;


    // 저장/수정 시 상세 + 목록 무효화
    @Caching(evict = {
            @CacheEvict(value = "productDetail", key = "#result.id", condition = "#result != null"),
            @CacheEvict(value = "productList", allEntries = true)
    })
    public ProductEntity save(final ProductCommand.Create command) {
        var product = new ProductEntity(
                command.name(), command.description(),
                command.price(), command.stock(), command.brandId()
        );
        return productRepository.save(product);
    }

    public List<ProductInfo> getProducts(SortType sortType) {
        List<ProductWithLikeCount> products = productRepository.findAllOrderBySortType(sortType.name());
        return products.stream().map(ProductInfo::from).collect(Collectors.toList());
    }

    @Cacheable(
            value = "productList",
            key = "'brand:' + #brandId + ':p:' + #pageable.pageNumber + ':s:' + #pageable.pageSize"
    )
    public List<ProductWithBrand> getProductsByBrandId(Long brandId, Pageable pageable) {
        List<ProductWithBrand> products = productRepository.findAllByBrandId(brandId, pageable);
        if (products.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND, "해당 브랜드의 상품이 존재하지 않습니다.");
        }
        return products;
    }

    @Cacheable(value = "productDetail", key = "#productId")
    public ProductInfo getProduct(final Long productId) {
        var found = Optional.ofNullable(productRepository.findDetailById(productId))
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 상품이 존재하지 않습니다."));
        return ProductInfo.from(found);
    }

    public List<ProductInfo> getProductsByProductId(List<Long> productIds) {
        List<ProductWithLikeCount> products = productRepository.findAllById(productIds);
        if (products.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND, "해당 상품이 존재하지 않습니다.");
        }
        return products.stream().map(ProductInfo::from).collect(Collectors.toList());
    }

    // 재고 변경 → 상세 + 목록 무효화
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "productDetail", key = "#command.productId()"),
            @CacheEvict(value = "productList", allEntries = true)
    })
    public void consume(ProductCommand.Consume command) {
        var product = productRepository.findByIdForUpdate(command.productId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 상품이 존재하지 않습니다."));
        product.decreaseStock(command.quantity().getQuantity());
        productRepository.save(product);
    }

    public List<ProductInfo> getLikedProducts(Long userId) {
        List<ProductWithLikeCount> likedProducts = productRepository.findLikedProductsByUserId(userId);
        return likedProducts.stream().map(ProductInfo::from).collect(Collectors.toList());
    }

    @Transactional
    public void updateLikeCount(Long productId, boolean isLike) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "해당 상품이 존재하지 않습니다."));

        if (isLike) {
            product.increaseLikeCount();
        } else {
            product.decreaseLikeCount();
        }
        
        productRepository.save(product);
    }
}
