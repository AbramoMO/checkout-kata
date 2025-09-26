package com.haiilo.kata.service.offer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.kata.service.item.ItemContainer;
import java.util.List;

public class NForFixedPriceOffer extends Offer
{
    private int n;                    // bundle size (e.g., 2)
    private int bundlePriceInMinor;   // total price for the bundle (e.g., 45)


    public NForFixedPriceOffer(String itemName, String metadata)
    {
        super(itemName, metadata);
    }


    @Override
    public boolean isApplicableFor(List<ItemContainer> itemContainers)
    {
        ItemContainer itemContainer = getItemContainer(itemName, itemContainers);

        return itemContainer.getCountToEvaluate() >= n;
    }


    @Override
    public int calculateDiscountedAmount(List<ItemContainer> itemContainers)
    {
        ItemContainer itemContainer = getItemContainer(itemName, itemContainers);
        int priceInMinor = itemContainer.getPriceInMinor();

        return n * priceInMinor - bundlePriceInMinor;
    }


    @Override
    public void applyOffer(String itemName, List<ItemContainer> itemContainers)
    {
        ItemContainer itemContainer = getItemContainer(this.itemName, itemContainers);

        itemContainer.increaseTotalPrice(bundlePriceInMinor);
        itemContainer.increaseEvaluatedCount(n);
    }


    @Override
    void parseMetadata(String metadata)
    {
        try
        {
            JsonNode meta = new ObjectMapper().readTree(metadata);
            n = meta.path("n").asInt();
            bundlePriceInMinor = meta.path("bundlePriceInMinor").asInt();
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Invalid offer metadata: " + metadata, e);
        }
    }
}

