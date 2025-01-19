package com.habin.sakila.film;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

public record FilmPriceSummary(
        Long filmId,
        String title,
        BigDecimal rentalRate,
        PriceCategory priceCategory,
        Long totalInventory
) {

    @Getter
    @AllArgsConstructor
    public enum PriceCategory {
        CHEAP("Cheap"), Moderate("Moderate"), EXPENSIVE("Expensive");

        private final String code;

        public static PriceCategory findByCode(String code) {
            for (PriceCategory value : values()) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
            return null;
        }
    }
}
