package com.haiilo.kata.service.offer;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.kata.domainobject.OfferDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(classes = {OfferFactoryCachingTest.TestConfig.class})
class OfferFactoryCachingTest {

    @Configuration
    @EnableCaching
    static class TestConfig {

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("offers");
        }

        @Bean
        OfferFactory offerFactory(ObjectMapper om) {
            return new OfferFactory(om);
        }
    }

    @Autowired
    OfferFactory factory;

    private OfferDO offer(int id, String type, String item, String metadata) {
        return new OfferDO(id, type, item, metadata, null);
    }

    @Test
    void returnsSameInstance_forSameOfferId() {
        var a = offer(1, OfferFactory.OfferType.N_FOR_FIXED.toString(), "APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");

        Offer o1 = factory.getOrCreate(a);
        Offer o2 = factory.getOrCreate(a);

        assertSame(o1, o2);
    }

    @Test
    void returnsDifferentInstances_forDifferentOfferIds() {
        var a = offer(1, OfferFactory.OfferType.N_FOR_FIXED.toString(), "BANANA", "{\"n\":2,\"bundlePriceInMinor\":45}");
        var b = offer(2, OfferFactory.OfferType.N_FOR_FIXED.toString(), "APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");

        Offer o1 = factory.getOrCreate(a);
        Offer o2 = factory.getOrCreate(b);

        assertNotSame(o1, o2);
    }

    @Test
    void unknownType_returnsDefaultOffer_nonCached_dueToUnless() {
        var bad = offer(3, "UNKNOWN", "APPLE", "{}");
        assertInstanceOf(DefaultOffer.class, factory.getOrCreate(bad));
    }
}
