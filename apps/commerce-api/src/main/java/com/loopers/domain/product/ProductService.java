package com.loopers.domain.product;

import com.loopers.domain.brand.BrandEntity;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.support.SortType;
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
        return  products.stream().map(ProductInfo::from).collect(Collectors.toList());
    }

    public Optional<ProductEntity> getProductById(final Long productId) {
        return productRepository.findById(productId);
    }
}
