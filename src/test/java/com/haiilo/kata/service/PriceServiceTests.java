package com.haiilo.kata.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haiilo.kata.domainobject.ItemDO;
import com.haiilo.kata.domainobject.OfferDO;
import com.haiilo.kata.domaintransferobject.CheckoutRequest;
import com.haiilo.kata.exception.ItemNotFoundException;
import com.haiilo.kata.repository.ItemRepository;
import com.haiilo.kata.repository.OfferRepository;
import com.haiilo.kata.service.offer.OfferFactory;
import com.haiilo.kata.service.offer.OfferFactory.OfferType;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PriceServiceTests {

    @Mock
    ItemRepository itemRepository;
    @Mock
    OfferRepository offerRepository;

    private PriceService priceService;

    private static final String APPLE = "APPLE";
    private static final String BANANA = "BANANA";
    private static final String PEACH = "PEACH";
    private static final String KIWI = "KIWI";

    private static final int APPLE_PRICE = 30;
    private static final int BANANA_PRICE = 50;
    private static final int PEACH_PRICE = 60;
    private static final int KIWI_PRICE = 20;

    private static final int APPLE_2_BUNDLE_PRICE = 45;
    private static final int BANANA_2_BUNDLE_PRICE = 130;
    private static final int BANANA_3_BUNDLE_PRICE = 90;

    private final List<ItemDO> defaultItems = List.of(new ItemDO(APPLE, APPLE_PRICE), new ItemDO(BANANA, BANANA_PRICE),
        new ItemDO(PEACH, PEACH_PRICE), new ItemDO(KIWI, KIWI_PRICE));

    private final List<OfferDO> defaultOffers = List.of(
        new OfferDO(1, OfferType.N_FOR_FIXED.toString(), APPLE, "{\"n\":2,\"bundlePriceInMinor\":%s}".formatted(APPLE_2_BUNDLE_PRICE), null),
        new OfferDO(2, OfferType.N_FOR_FIXED.toString(), BANANA, "{\"n\":3,\"bundlePriceInMinor\":%s}".formatted(BANANA_3_BUNDLE_PRICE), 1),
        new OfferDO(3, OfferType.N_FOR_FIXED.toString(), BANANA, "{\"n\":2,\"bundlePriceInMinor\":%s}".formatted(BANANA_2_BUNDLE_PRICE), 1));


    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        OfferFactory offerFactory = new OfferFactory(objectMapper);
        priceService = new PriceService(itemRepository, offerRepository, offerFactory);

        lenient().when(itemRepository.findAllById(anySet())).thenReturn(defaultItems);
        lenient().when(offerRepository.findByItemIn(anySet())).thenReturn(defaultOffers);
    }


    private static CheckoutRequest makeRequest(List<String> items) {
        return new CheckoutRequest(items);
    }

    // ---------- Tests ----------
    @Test
    void calculateTotalPrice_zeroForEmptyBasket() {
        long total = priceService.calculateTotalPrice(makeRequest(Collections.emptyList()));
        assertEquals(0, total);
    }

    @Test
    void calculateTotalPrice_appliesAppleTwoFor45() {
        List<String> items = Arrays.asList(APPLE, APPLE);
        HashSet<String> itemsSet = new HashSet<>(items);

        when(itemRepository.findAllById(anySet())).thenReturn(defaultItems.stream().filter(x -> items.contains(x.getName())).toList());
        when(offerRepository.findByItemIn(anySet())).thenReturn(defaultOffers.stream().filter(x -> itemsSet.contains(x.getItem())).toList());

        long total = priceService.calculateTotalPrice(makeRequest(items));
        assertEquals(45, total);
    }

    @Test
    void calculateTotalPrice_offersUsageLimits() {
        long total1 = priceService.calculateTotalPrice(makeRequest(Arrays.asList(APPLE, APPLE, APPLE)));
        long total2 = priceService.calculateTotalPrice(makeRequest(Arrays.asList(APPLE, APPLE, APPLE, APPLE)));  // 2*45
        long total3 = priceService.calculateTotalPrice(makeRequest(Arrays.asList(BANANA, BANANA, BANANA, BANANA, BANANA, BANANA, BANANA, BANANA)));
        assertEquals(APPLE_2_BUNDLE_PRICE + APPLE_PRICE, total1);
        assertEquals(APPLE_2_BUNDLE_PRICE * 2, total2);
        assertEquals(BANANA_PRICE * 3 + BANANA_2_BUNDLE_PRICE + BANANA_3_BUNDLE_PRICE, total3);
    }

    @Test
    void calculateTotalPrice_mixedBasket() {
        long total = priceService.calculateTotalPrice(makeRequest(Arrays.asList(APPLE, BANANA, PEACH, PEACH, KIWI)));
        assertEquals(APPLE_PRICE + BANANA_PRICE + PEACH_PRICE * 2 + KIWI_PRICE, total); // 2 apples -> 45; 1 banana -> 50
    }

    @Test
    void calculateTotalPrice_isOrderIndependent() {
        List<String> a = Arrays.asList(APPLE, APPLE, BANANA, BANANA, PEACH, BANANA, BANANA, BANANA);
        List<String> b = Arrays.asList(BANANA, APPLE, BANANA, APPLE, BANANA, BANANA, BANANA, PEACH);

        long totalA = priceService.calculateTotalPrice(makeRequest(a));
        long totalB = priceService.calculateTotalPrice(makeRequest(b));
        assertEquals(totalA, totalB);
        assertEquals(APPLE_2_BUNDLE_PRICE + BANANA_2_BUNDLE_PRICE + BANANA_3_BUNDLE_PRICE + PEACH_PRICE, totalA); // 5 bananas (220) + 2 apples (45)
    }

    @Test
    void calculateTotalPrice_throwsItemNotFoundException() {
        List<String> items = Arrays.asList(APPLE, "PINEAPPLE");

        when(itemRepository.findAllById(new HashSet<>(items))).thenReturn(defaultItems.stream().filter(x -> items.contains(x.getName())).toList());

        assertThrows(ItemNotFoundException.class, () -> priceService.calculateTotalPrice(makeRequest(items)));
    }

    @Test
    void calculateTotalPrice_acceptsCaseInsensitiveItemNames() {
        long apples = priceService.calculateTotalPrice(makeRequest(Arrays.asList(APPLE.toLowerCase(), APPLE, APPLE.toUpperCase())));   // 3 apples -> 75
        long bananas = priceService.calculateTotalPrice(makeRequest(Arrays.asList("BaNaNa", BANANA, BANANA))); // 3 bananas -> 130
        assertEquals(APPLE_2_BUNDLE_PRICE + APPLE_PRICE, apples);
        assertEquals(BANANA_3_BUNDLE_PRICE, bananas);
    }
}