package com.haiilo.kata.service.item;

import lombok.Data;

/**
 * Container for item data, which we need to count progression over item price calculation e.g. storing price after applying offers
 */
@Data
public class ItemContainer {

    private final String itemName;
    private final int priceInMinor;
    private final int count;
    private int evaluatedCount;
    private long totalPrice;

    public long getCountToEvaluate() {
        return count - evaluatedCount;
    }

    public void increaseEvaluatedCount(int count) {
        if (getCountToEvaluate() < count) {
            throw new IllegalArgumentException("Not enough items to evaluate");
        }
        evaluatedCount += count;
    }

    public void increaseTotalPrice(long priceInMinor) {
        totalPrice += priceInMinor;
    }
}

