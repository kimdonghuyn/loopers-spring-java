package com.loopers.domain.brand;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BrandServiceIntegrationTest {

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandRepository brandRepository;

    @Test
    @DisplayName("productId로 브랜드르 조회하면 브랜드 정보를 반환한다.")
    void findBrandByProductId() {
        // arrange
        BrandEntity brand = brandRepository.save(new BrandEntity("아디다스", "아디다스 입니다."));

        // act
        Optional<BrandInfo> brandInfo = brandService.getBrandInfo(brand.getId());

        // assert
        assertAll(
                () -> assertThat(brandInfo).isPresent(),
                () -> assertThat(brandInfo.get().id()).isEqualTo(brand.getId()),
                () -> assertThat(brandInfo.get().name()).isEqualTo("아디다스"),
                () -> assertThat(brandInfo.get().description()).isEqualTo("아디다스 입니다.")
        );
    }

}
