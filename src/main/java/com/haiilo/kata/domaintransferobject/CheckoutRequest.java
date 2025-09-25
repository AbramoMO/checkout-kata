package com.haiilo.kata.domaintransferobject;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CheckoutRequest(@NotNull @NotEmpty List<String> items)
{
}
