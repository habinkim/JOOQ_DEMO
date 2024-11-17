package com.habin.sakila.web;

import java.util.List;

public record FilmWithActorPagedResponse(

        PagedResponse page,
        List<FilmActorResponse> filmActorList

) {

    public record FilmActorResponse(
            String filmTitle,
            String actorFullName,
            Long filmId
    ) {
    }

}
