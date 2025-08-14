package com.loopers.support.seed;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

@Component
@Profile("local")
@RequiredArgsConstructor
public class LocalJpaSeeder implements CommandLineRunner {

    private final LocalJpaSeedService seedService;

    @Override
    public void run(String... args) {
        seedService.seedAll(); // 프록시 통해 @Transactional 적용됨
    }
}
