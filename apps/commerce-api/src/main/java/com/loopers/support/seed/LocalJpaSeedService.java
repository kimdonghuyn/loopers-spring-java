package com.loopers.support.seed;

import com.loopers.domain.product.ProductEntity;
import com.loopers.domain.brand.BrandEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional // <-- 전체 시드 작업을 하나의 트랜잭션으로
public class LocalJpaSeedService {

    @PersistenceContext
    private final EntityManager em;

    @Value("${seed.enabled:false}")       private boolean enabled;
    @Value("${seed.drop-and-reseed:false}") private boolean dropAndReseed;

    @Value("${seed.brands:100}")        private int brands;
    @Value("${seed.products:100000}")   private int products;
    @Value("${seed.batch-size:1000}")   private int batchSize;

    private final Faker faker = new Faker(new Locale("ko", "KR"));

    public void seedAll() {
        if (!enabled) return;

        if (dropAndReseed) {
            // FK 고려: product -> brand 순서로 비우기
            exec("SET FOREIGN_KEY_CHECKS = 0");
            exec("TRUNCATE TABLE product");
            exec("TRUNCATE TABLE brand");
            exec("SET FOREIGN_KEY_CHECKS = 1");
        }

        if (count("brand") == 0) insertBrands();
        if (count("product") == 0) insertProducts();
    }

    private long count(String table) {
        return ((Number) em.createNativeQuery("SELECT COUNT(*) FROM " + table)
                .getSingleResult()).longValue();
    }

    private void exec(String sql) {
        em.createNativeQuery(sql).executeUpdate();
    }

    private void insertBrands() {
        for (int i = 1; i <= brands; i++) {
            var brand = new BrandEntity(
                    faker.company().name(),
                    faker.lorem().sentence()
            );
            em.persist(brand);
            if (i % batchSize == 0) { em.flush(); em.clear(); }
        }
    }

    private void insertProducts() {
        for (int i = 1; i <= products; i++) {
            var price = BigDecimal.valueOf(1000 + ThreadLocalRandom.current().nextInt(100_000 - 1000 + 1));
            var stock = ThreadLocalRandom.current().nextInt(0, 101);
            var brandId = (long) ThreadLocalRandom.current().nextInt(1, brands + 1);
            var likes = ThreadLocalRandom.current().nextInt(1, 2001); // 1~2000

            var p = ProductEntity.create(
                    faker.commerce().productName(),
                    faker.lorem().paragraph(),
                    price,
                    stock,
                    brandId,
                    likes
            );

            em.persist(p);
            if (i % batchSize == 0) { em.flush(); em.clear(); }
        }
    }
}
