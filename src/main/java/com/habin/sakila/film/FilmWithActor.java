package com.habin.sakila.film;

import com.habin.sakila.tables.pojos.Actor;
import com.habin.sakila.tables.pojos.Film;
import com.habin.sakila.tables.pojos.FilmActor;

public record FilmWithActor(
        Film film,
        FilmActor filmActor,
        Actor actor
) {
}
