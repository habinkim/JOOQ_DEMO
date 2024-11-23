package com.habin.sakila.film;


import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.pojos.Film;
import org.jooq.generated.tables.pojos.FilmActor;

public record FilmWithActor(
        Film film,
        FilmActor filmActor,
        Actor actor
) {
}
