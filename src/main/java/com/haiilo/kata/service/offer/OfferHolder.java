package com.haiilo.kata.service.offer;

import com.haiilo.kata.service.item.ItemContainer;
import java.util.List;
import lombok.Data;

@Data
public final class OfferHolder
{
    private final Offer offer;
    private Integer remaining; // null = unlimited


    public OfferHolder(Offer offer, Integer remaining)
    {
        this.offer = offer;
        this.remaining = remaining;
    }


    public boolean hasRemaining()
    {
        return remaining == null || remaining > 0;
    }


    public void applyOffer(String itemName, List<ItemContainer> itemContainers)
    {
        offer.applyOffer(itemName, itemContainers);

        if (remaining != null)
        {
            remaining--;
        }
    }
}
