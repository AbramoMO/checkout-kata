package com.haiilo.kata.service.offer;

import com.haiilo.kata.service.item.ItemContainer;
import java.util.List;

public class DefaultOffer extends Offer {

    @Override
    public boolean isApplicableFor(List<ItemContainer> itemContainers) {
        return true;
    }

    @Override
    public int calculateDiscountedAmount(List<ItemContainer> itemContainers) {
        return 0;
    }

    @Override
    public void applyOffer(String itemName, List<ItemContainer> itemContainers) {
        ItemContainer itemContainer = getItemContainer(itemName, itemContainers);

        itemContainer.increaseTotalPrice(itemContainer.getPriceInMinor());
        itemContainer.increaseEvaluatedCount(1);
    }

    @Override
    void parseMetadata(String metadata) {
    }
}

