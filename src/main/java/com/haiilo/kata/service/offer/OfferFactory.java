package com.haiilo.kata.service.offer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.kata.domainobject.OfferDO;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@CacheConfig(cacheNames = "offers")
public class OfferFactory {

    public OfferFactory(ObjectMapper objectMapper) {
        registry = Map.of(
            OfferType.N_FOR_FIXED.toString(),
            offerDO -> new NForFixedPriceOffer(objectMapper, offerDO.getItem(), offerDO.getMetadata())
        );
    }

    public enum OfferType {
        N_FOR_FIXED
    }

    private final Map<String, Function<OfferDO, Offer>> registry;

    @Cacheable(key = "#offerDO.id", unless = "#result instanceof T(com.haiilo.kata.service.offer.DefaultOffer)")
    public Offer getOrCreate(OfferDO offerDO) {
        Function<OfferDO, Offer> builder = registry.get(offerDO.getType());
        if (builder == null) {
            log.error("Undefined strategy [{}], using default instead", offerDO.getType());
            return DefaultOffer.DEFAULT_INSTANCE;
        }

        return builder.apply(offerDO);
    }
}



