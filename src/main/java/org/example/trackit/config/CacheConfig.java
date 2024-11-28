package org.example.trackit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(List.of(partNumberCache().getName(), userDetailsCache().getName()));
        return cacheManager;
    }

    @Bean
    public CaffeineCache partNumberCache() {
        return new CaffeineCache("partNumberCache", Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterAccess(3, TimeUnit.DAYS)
                .build());
    }

    @Bean
    public CaffeineCache userDetailsCache() {
        return new CaffeineCache("userDetailsCache", Caffeine.newBuilder()
                .maximumSize(10)
                .expireAfterAccess(1, TimeUnit.DAYS)
                .build());
    }
}
