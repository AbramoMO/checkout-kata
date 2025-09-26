package com.haiilo.kata.service.offer;

import com.haiilo.kata.domainobject.OfferDO;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OfferFactory {

    private final Map<Integer, Offer> cache = new ConcurrentHashMap<>();

    public enum OfferType {
        N_FOR_FIXED
    }

    private final DefaultOffer defaultOffer = new DefaultOffer();

    private final Map<String, Function<OfferDO, Offer>> registry = Map.of(
        OfferType.N_FOR_FIXED.toString(),
        offerDO -> new NForFixedPriceOffer(offerDO.getItem(), offerDO.getMetadata())
    );

    public Offer getOrCreate(OfferDO offerDO) {
        return cache.computeIfAbsent(
            offerDO.getId(), id -> {
                Function<OfferDO, Offer> builder = registry.get(offerDO.getType());
                if (builder == null) {
                    log.error("Undefined strategy [{}], using default instead", offerDO.getType());
                    return defaultOffer;
                }

                return builder.apply(offerDO);
            });
    }
}



