package com.loopers.domain.product;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.support.SortType;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandJpaRepository brandJpaRepository;

    public ProductEntity save(final ProductCommand.Create command) {
        Optional<BrandEntity> brand = brandJpaRepository.findById(command.brandId());

        final ProductEntity product = new ProductEntity(
                command.name(),
                command.description(),
                command.price(),
                command.stock(),
                brand.get()
        );

        return productRepository.save(product);
    }

    public List<ProductInfo> getProducts(SortType sortType) {
        List<ProductWithLikeCount> products = productRepository.findAllOrderBySortType(sortType.name());
        return products.stream().map(ProductInfo::from).collect(Collectors.toList());
    }

    public ProductInfo getProduct(final Long productId) {
        Optional<ProductWithLikeCount> product = Optional.ofNullable(productRepository.findDetailById(productId));
        if (product.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND, "해당 상품이 존재하지 않습니다.");
        }
        return ProductInfo.from(product.get());
    }

    public List<ProductInfo> getProductsByProductId(List<Long> productIds) {
        List<ProductWithLikeCount> products = productRepository.findAllById(productIds);
        if (products.isEmpty()) {
            throw new CoreException(ErrorType.NOT_FOUND, "해당 상품이 존재하지 않습니다.");
        }
        return products.stream().map(ProductInfo::from).collect(Collectors.toList());
    }

    public void consume(ProductCommand.Consume command) {
        Optional<ProductEntity> product = productRepository.findById(command.productId());

        if (product.isPresent()) {
            product.get().decreaseStock(command.quantity().getQuantity());
            productRepository.save(product.get());
        } else {
            throw new CoreException(ErrorType.NOT_FOUND, "해당 상품이 존재하지 않습니다.");
        }
    }

    public List<ProductInfo> getLikedProducts(Long userId) {
        List<ProductWithLikeCount> likedProducts = productRepository.findLikedProductsByUserId(userId);
        return likedProducts.stream().map(ProductInfo::from).collect(Collectors.toList());
    }
}
