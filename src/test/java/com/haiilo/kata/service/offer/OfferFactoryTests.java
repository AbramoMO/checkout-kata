package com.haiilo.kata.service.offer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.haiilo.kata.domainobject.OfferDO;
import org.junit.jupiter.api.Test;

class OfferFactoryTests {

    private OfferDO offer(int id, String type, String item, String metadata) {
        return new OfferDO(id, type, item, metadata, null);
    }

    @Test
    void getOrCreate_returnsSameInstance_forSameOfferId() {
        OfferFactory factory = new OfferFactory();

        OfferDO a = offer(1, OfferFactory.OfferType.N_FOR_FIXED.toString(), "APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");
        OfferDO b = offer(1, OfferFactory.OfferType.N_FOR_FIXED.toString(), "APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");

        Offer o1 = factory.getOrCreate(a);
        Offer o2 = factory.getOrCreate(b);

        assertSame(o1, o2);
    }

    @Test
    void getOrCreate_returnsDifferentInstances_forDifferentOfferIds() {
        OfferFactory factory = new OfferFactory();

        OfferDO a = offer(1, OfferFactory.OfferType.N_FOR_FIXED.toString(), "APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");
        OfferDO b = offer(2, OfferFactory.OfferType.N_FOR_FIXED.toString(), "APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");

        Offer o1 = factory.getOrCreate(a);
        Offer o2 = factory.getOrCreate(b);

        assertNotSame(o1, o2);
    }

    @Test
    void getOrCreate_throwsIllegalArgument_forUnknownType() {
        OfferFactory factory = new OfferFactory();
        OfferDO bad = offer(3, "UNKNOWN", "APPLE", "{}");

        assertInstanceOf(DefaultOffer.class, factory.getOrCreate(bad));
    }
}


