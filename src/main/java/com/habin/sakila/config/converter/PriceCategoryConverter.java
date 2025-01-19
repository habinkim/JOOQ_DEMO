package com.habin.sakila.config.converter;

import com.habin.sakila.film.FilmPriceSummary;
import org.jooq.impl.EnumConverter;

public class PriceCategoryConverter extends EnumConverter<String, FilmPriceSummary.PriceCategory> {
    public PriceCategoryConverter() {
        super(String.class, FilmPriceSummary.PriceCategory.class, FilmPriceSummary.PriceCategory::getCode);
    }
}
