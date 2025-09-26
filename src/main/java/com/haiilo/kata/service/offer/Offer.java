package com.haiilo.kata.service.offer;

import com.haiilo.kata.exception.ItemNotFoundException;
import com.haiilo.kata.service.item.ItemContainer;
import java.util.List;
import lombok.NoArgsConstructor;

/**
 * Base class for Offers to make it extendable by different types of offers
 */
@NoArgsConstructor
public abstract class Offer
{
    protected String itemName;

    public Offer(String itemName, String metadata)
    {
        this.itemName = itemName;
        parseMetadata(metadata);
    }

    public abstract boolean isApplicableFor(List<ItemContainer> itemContainers);

    public abstract int calculateDiscountedAmount(List<ItemContainer> itemContainers);

    public abstract void applyOffer(String itemName, List<ItemContainer> itemContainers);

    abstract void parseMetadata(String metadata);

    protected ItemContainer getItemContainer(String itemName, List<ItemContainer> itemContainers)
    {
        return itemContainers.stream()
            .filter(x -> x.getItemName().equals(itemName))
            .findFirst()
            .orElseThrow(() -> new ItemNotFoundException(String.format("Item %s not presented in the list for strategy", itemName)));
    }
}
