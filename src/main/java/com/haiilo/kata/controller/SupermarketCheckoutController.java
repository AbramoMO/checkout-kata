package com.haiilo.kata.controller;

import com.haiilo.kata.domaintransferobject.CheckoutRequest;
import com.haiilo.kata.domaintransferobject.CheckoutResponse;
import com.haiilo.kata.service.PriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SupermarketCheckoutController {

    private final PriceService priceService;

    @PostMapping("checkout")
    public ResponseEntity<CheckoutResponse> checkout(@Valid @RequestBody CheckoutRequest checkoutRequest) {
        int totalPriceInMinor = priceService.calculateTotalPrice(checkoutRequest);
        return ResponseEntity.ok(new CheckoutResponse(totalPriceInMinor));
    }
}
