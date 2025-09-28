package com.haiilo.kata.service.offer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.kata.service.item.ItemContainer;
import java.util.List;

public class DefaultOffer extends Offer {
    public static DefaultOffer DEFAULT_INSTANCE = new DefaultOffer(null, "", "");

    public DefaultOffer(ObjectMapper objectMapper, String itemName, String metadata) {
        super(objectMapper, itemName, metadata);
    }

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

