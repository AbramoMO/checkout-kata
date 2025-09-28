package com.haiilo.kata.service;

import com.haiilo.kata.domainobject.ItemDO;
import com.haiilo.kata.domainobject.OfferDO;
import com.haiilo.kata.domaintransferobject.CheckoutRequest;
import com.haiilo.kata.exception.ItemNotFoundException;
import com.haiilo.kata.repository.ItemRepository;
import com.haiilo.kata.repository.OfferRepository;
import com.haiilo.kata.service.item.ItemContainer;
import com.haiilo.kata.service.offer.OfferFactory;
import com.haiilo.kata.service.offer.OfferHolder;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceService {

    private final ItemRepository itemRepository;
    private final OfferRepository offerRepository;
    private final OfferFactory offerFactory;


    @Transactional(readOnly = true)
    public long calculateTotalPrice(CheckoutRequest checkoutRequest) {
        List<String> items = checkoutRequest.items();
        if (items == null || items.isEmpty()) {
            log.info("No items in checkoutRequest");
            return 0;
        }

        Map<String, Long> itemToCount = items.stream()
            .collect(Collectors.groupingBy(String::toUpperCase, Collectors.counting()));

        Map<String, ItemDO> itemToDO = itemRepository.findAllById(itemToCount.keySet())
            .stream()
            .collect(Collectors.toMap(ItemDO::getName, Function.identity()));

        // filling out item containers. Each container presents unique item + it's data
        List<ItemContainer> itemContainers = itemToCount.entrySet().stream()
            .map(entry -> {
                String itemName = entry.getKey();
                int count = entry.getValue().intValue();
                ItemDO item = Optional.ofNullable(itemToDO.get(itemName))
                    .orElseThrow(() -> new ItemNotFoundException("Item not found: " + itemName));
                return new ItemContainer(itemName, item.getPriceInMinor(), count);
            })
            .toList();

        // offers which could be applied to given items
        List<OfferDO> availableOffers = offerRepository.findByItemIn(
            new HashSet<>(itemToCount.keySet()));
        Map<String, List<OfferHolder>> offersByItem = availableOffers.stream()
            .collect(Collectors.groupingBy(
                OfferDO::getItem,
                Collectors.mapping(offerDo -> new OfferHolder(offerFactory.getOrCreate(offerDo),
                    offerDo.getUsageLimit()), Collectors.toList())
            ));

        return calculateTotalPrice(itemContainers, offersByItem);
    }


    private static long calculateTotalPrice(List<ItemContainer> itemContainers,
        Map<String, List<OfferHolder>> offersByItem) {
        log.info("Calculating total price");
        long totalPrice = 0;

        for (ItemContainer itemContainer : itemContainers) {
            List<OfferHolder> availableOffersForItem = offersByItem.getOrDefault(
                itemContainer.getItemName(), List.of()).stream().toList();

            while (itemContainer.getCountToEvaluate() > 0) {
                // customer-oriented - we choose the best offer, which gives the biggest discount
                OfferHolder bestOffer = availableOffersForItem.stream()
                    .filter(offerHolder -> offerHolder.hasRemaining() && offerHolder.getOffer()
                        .isApplicableFor(itemContainers))
                    .max(Comparator.comparingInt(
                        offerHolder -> offerHolder.getOffer().calculateDiscountedAmount(itemContainers)))
                    .orElse(null);

                if (bestOffer == null) {
                    break;
                }

                bestOffer.applyOffer(itemContainer.getItemName(), itemContainers);
            }

            // counting leftover items without discount
            totalPrice += itemContainer.getTotalPrice()
                + itemContainer.getCountToEvaluate() * itemContainer.getPriceInMinor();
        }

        return totalPrice;
    }
}

