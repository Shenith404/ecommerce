package com.example.ecommerce.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Cache names used across the application.
     */
    public static final String PRODUCTS_FILTER_CACHE  = "productsFilter";
    public static final String PRODUCT_BY_SLUG_CACHE  = "productBySlug";
    public static final String PRODUCT_BY_ID_CACHE    = "productById";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager(
                PRODUCTS_FILTER_CACHE,
                PRODUCT_BY_SLUG_CACHE,
                PRODUCT_BY_ID_CACHE
        );
        manager.setCaffeine(caffeineCacheBuilder());
        return manager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .recordStats();
    }
}

