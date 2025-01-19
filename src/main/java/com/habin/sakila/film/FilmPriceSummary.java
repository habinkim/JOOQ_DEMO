package com.habin.sakila.film;

import java.math.BigDecimal;

public record FilmPriceSummary(
        Long filmId,
        String title,
        BigDecimal rentalRate,
        String priceCategory,
        Long totalInventory
) {
}
