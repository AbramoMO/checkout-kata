package com.haiilo.kata.service.offer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.haiilo.kata.service.item.ItemContainer;
import java.util.List;
import org.junit.jupiter.api.Test;

class NForFixedPriceOfferTests {

    private static ItemContainer container(String name, int priceInMinor, int count) {
        return new ItemContainer(name, priceInMinor, count);
    }

    @Test
    void isApplicableFor_trueWhenEnoughItems_falseOtherwise() {
        Offer offer = new NForFixedPriceOffer("APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");
        assertFalse(offer.isApplicableFor(List.of(container("APPLE", 30, 1))));
        assertTrue(offer.isApplicableFor(List.of(container("APPLE", 30, 2))));
        assertTrue(offer.isApplicableFor(List.of(container("APPLE", 30, 3))));
    }

    @Test
    void calculateDiscountedAmount_correctForBundle() {
        Offer offer = new NForFixedPriceOffer("APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");
        int discount = offer.calculateDiscountedAmount(List.of(container("APPLE", 30, 2)));
        assertEquals(15, discount);
    }

    @Test
    void applyOffer_increasesEvaluatedCount_andTotalPrice() {
        Offer offer = new NForFixedPriceOffer("APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");
        ItemContainer itemContainer = container("APPLE", 30, 3);
        offer.applyOffer(itemContainer.getItemName(), List.of(itemContainer));
        assertEquals(2, itemContainer.getEvaluatedCount());
        assertEquals(45, itemContainer.getTotalPrice());
    }

    @Test
    void applyOffer_throws_whenInsufficientItems() {
        Offer offer = new NForFixedPriceOffer("APPLE", "{\"n\":2,\"bundlePriceInMinor\":45}");
        ItemContainer itemContainer = container("APPLE", 30, 1);
        assertThrows(IllegalArgumentException.class, () -> offer.applyOffer(itemContainer.getItemName(), List.of(itemContainer)));
    }

    @Test
    void parseMetadata_throws_forInvalidJson() {
        assertThrows(IllegalArgumentException.class, () -> new NForFixedPriceOffer("APPLE", "not a json"));
    }
}


