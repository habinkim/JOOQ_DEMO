package com.habin.sakila.web;

import com.habin.sakila.film.FilmWithActor;
import com.habin.sakila.tables.pojos.Actor;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class FilmMapper {

    @IterableMapping(qualifiedByName = "toFilmWithActorResponse")
    public abstract List<FilmWithActorPagedResponse.FilmActorResponse>
        toFilmWithActorPagedResponses(List<FilmWithActor> filmWithActorList);

    @Named("toFilmWithActorResponse")
    @Mapping(target = "filmTitle", source = "film.title")
    @Mapping(target = "actorFullName", source = "actor", qualifiedByName = "toFullName")
    @Mapping(target = "filmId", source = "film.filmId")
    public abstract FilmWithActorPagedResponse.FilmActorResponse toFilmWithActorResponse(FilmWithActor filmWithActor);

    @Named("toFullName")
    public String toFullName(Actor actor) {
        return actor.getFirstName() + " " + actor.getLastName();
    }

}
