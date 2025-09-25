package com.haiilo.kata.service;

import com.haiilo.kata.domainobject.Item;
import com.haiilo.kata.domaintransferobject.CheckoutRequest;
import com.haiilo.kata.exception.ItemNotFoundException;
import com.haiilo.kata.repository.ItemRepository;
import com.haiilo.kata.repository.OfferRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService
{
    private final ItemRepository itemRepository;
    private final OfferRepository offerRepository;

    public int calculateTotalPrice(CheckoutRequest checkoutRequest)
    {
        List<String> items = checkoutRequest.items();

        return items.stream()
                .map(itemName -> itemRepository.findById(itemName)
                        .orElseThrow(() -> new ItemNotFoundException("Item not found in the repo: " + itemName)))
                .mapToInt(Item::getPriceInMinor)
                .sum();
    }
}
