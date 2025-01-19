package com.habin.sakila.actor;

import org.jooq.DSLContext;
import org.jooq.Row2;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.JFilm;
import org.jooq.generated.tables.JFilmActor;
import org.jooq.generated.tables.daos.ActorDao;
import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.pojos.Film;
import org.jooq.generated.tables.records.ActorRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.habin.sakila.util.JooqConditionUtils.containsIfNotBlank;
import static com.habin.sakila.util.JooqConditionUtils.inIfNotEmpty;

@Repository
public class ActorRepository {

    private final DSLContext dslContext;
    private final ActorDao actorDao;
    public static final JActor ACTOR = JActor.ACTOR;
    public static final JFilmActor FILM_ACTOR = JFilmActor.FILM_ACTOR;
    public static final JFilm FILM = JFilm.FILM;

    public ActorRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
        this.actorDao = new ActorDao(dslContext.configuration());
    }


    public List<Actor> findByFirstNameAndLastName(String firstName, String lastName) {
        return dslContext.selectFrom(ACTOR)
                .where(
                        ACTOR.FIRST_NAME.eq(firstName),
                        ACTOR.LAST_NAME.eq(lastName)
                ).fetchInto(Actor.class);
    }

    public List<Actor> findByFirstNameOrLastName(String firstName, String lastName) {
        return dslContext.selectFrom(ACTOR)
                .where(
                        ACTOR.FIRST_NAME.eq(firstName)
                                .or(ACTOR.LAST_NAME.eq(lastName))
                ).fetchInto(Actor.class);
    }

    public List<Actor> findByActorIdIn(List<Long> ids) {
        return dslContext.selectFrom(ACTOR)
                .where(inIfNotEmpty(ACTOR.ACTOR_ID, ids))
                .fetchInto(Actor.class);
    }

    public List<ActorFilmography> findActorFilmography(ActorFilmographySearchOption searchOption) {
        Map<Actor, List<Film>> actorsMap = dslContext.select(
                        DSL.row(ACTOR.fields()).as("actor"),
                        DSL.row(FILM.fields()).as("film")
                )
                .from(ACTOR)
                .join(FILM_ACTOR).on(ACTOR.ACTOR_ID.eq(FILM_ACTOR.ACTOR_ID))
                .join(FILM).on(FILM_ACTOR.FILM_ID.eq(FILM.FILM_ID))
                .where(
                        containsIfNotBlank(ACTOR.FIRST_NAME.concat(" ").concat(ACTOR.LAST_NAME), searchOption.getActorName()),
                        containsIfNotBlank(FILM.TITLE, searchOption.getFilmTitle())
                )
                .fetchGroups(
                        record -> record.get("actor", Actor.class),
                        record -> record.get("film", Film.class)
                );


        return actorsMap.entrySet().stream()
                .map(entry -> new ActorFilmography(entry.getKey(), entry.getValue()))
                .toList();
    }

    public Actor saveByDao(Actor actor) {
        // 이떄 PK (actorId)가 actor 객체에 추가됨
        actorDao.insert(actor);
        return actor;
    }

    public ActorRecord saveByRecord(Actor actor) {
        ActorRecord actorRecord = dslContext.newRecord(ACTOR, actor);
        actorRecord.insert();

        // 단 이 방식은 immutable pojo 에서 사용하기 어려울 수 있음
        return actorRecord;
    }

    public Actor saveWithReturning(Actor actor) {
        return dslContext.insertInto(ACTOR,
                        ACTOR.FIRST_NAME,
                        ACTOR.LAST_NAME
                )
                .values(
                        actor.getFirstName(),
                        actor.getLastName()
                )
                .returning(ACTOR.fields())
                .fetchOneInto(Actor.class);
    }

    public Long saveWithReturningPkOnly(Actor actor) {
        return dslContext.insertInto(ACTOR,
                        ACTOR.FIRST_NAME,
                        ACTOR.LAST_NAME
                )
                .values(
                        actor.getFirstName(),
                        actor.getLastName()
                )
                .returning(ACTOR.ACTOR_ID)
                .fetchOneInto(Long.class);
    }

    public void bulkInsertWithRows(List<Actor> actors) {
        List<Row2<String, String>> rows = actors.stream().map(actor -> DSL.row(actor.getFirstName(), actor.getLastName())).toList();

        dslContext.insertInto(ACTOR, ACTOR.FIRST_NAME, ACTOR.LAST_NAME)
                .valuesOfRows(rows)
                .execute();
    }
}
