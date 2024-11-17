package com.habin.sakila.film;

public record SimpleFilmInfo(
        Long filmId,
        String title,
        String description
) {
}
