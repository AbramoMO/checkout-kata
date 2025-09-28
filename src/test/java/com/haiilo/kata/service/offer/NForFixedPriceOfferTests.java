package com.haiilo.kata.service.offer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.kata.service.item.ItemContainer;
import java.util.List;
import org.junit.jupiter.api.Test;

class NForFixedPriceOfferTests {

    private static ItemContainer makeItemContainer(int count) {
        return new ItemContainer("APPLE", 30, count);
    }
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void isApplicableFor_trueWhenEnoughItems_falseOtherwise() {
        Offer offer = new NForFixedPriceOffer(objectMapper, "APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");
        assertFalse(offer.isApplicableFor(List.of(makeItemContainer(1))));
        assertTrue(offer.isApplicableFor(List.of(makeItemContainer(2))));
        assertTrue(offer.isApplicableFor(List.of(makeItemContainer(3))));
    }

    @Test
    void calculateDiscountedAmount_correctForBundle() {
        Offer offer = new NForFixedPriceOffer(objectMapper, "APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");
        int discount = offer.calculateDiscountedAmount(List.of(makeItemContainer(2)));
        assertEquals(15, discount);
    }

    @Test
    void applyOffer_increasesEvaluatedCount_andTotalPrice() {
        Offer offer = new NForFixedPriceOffer(objectMapper, "APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");
        ItemContainer itemContainer = makeItemContainer(3);
        offer.applyOffer(itemContainer.getItemName(), List.of(itemContainer));
        assertEquals(2, itemContainer.getEvaluatedCount());
        assertEquals(45, itemContainer.getTotalPrice());
    }

    @Test
    void applyOffer_throws_whenInsufficientItems() {
        Offer offer = new NForFixedPriceOffer(objectMapper, "APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");
        ItemContainer itemContainer = makeItemContainer(1);
        assertThrows(IllegalArgumentException.class, () -> offer.applyOffer(itemContainer.getItemName(), List.of(itemContainer)));
    }

    @Test
    void parseMetadata_throws_forInvalidJson() {
        assertThrows(IllegalArgumentException.class, () -> new NForFixedPriceOffer(objectMapper, "APPLE", "not a json"));
    }
}


