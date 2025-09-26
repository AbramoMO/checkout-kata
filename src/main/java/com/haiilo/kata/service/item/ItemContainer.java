package com.haiilo.kata.service.item;

import lombok.Data;

/**
 * Container for item data, which we need to count progression over item price calculation
 * e.g. storing price after applying offers
 */
@Data
public class ItemContainer
{
    private final String itemName;
    private final int priceInMinor;
    private final int count;
    private int evaluatedCount;
    private int totalPrice;


    public int getCountToEvaluate()
    {
        return count - evaluatedCount;
    }


    public void increaseEvaluatedCount(int cnt)
    {
        if (getCountToEvaluate() < cnt)
        {
            throw new IllegalArgumentException("Not enough items to evaluate");
        }
        evaluatedCount += cnt;
    }


    public void increaseTotalPrice(int priceInMinor)
    {
        totalPrice += priceInMinor;
    }
}

