package com.habin.sakila.film;

import org.jooq.*;
import org.jooq.generated.tables.*;
import org.jooq.generated.tables.daos.FilmDao;
import org.jooq.generated.tables.pojos.Film;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.habin.sakila.util.JooqConditionUtils.containsIfNotBlank;
import static org.jooq.impl.DSL.*;

/**
 * 컴포지트를 통해서 사용
 */
@Repository
public class FilmRepositoryHasA {

    private final DSLContext dslContext;
    private final FilmDao dao;

    public static final JFilm FILM = JFilm.FILM;
    public static final JFilmActor FILM_ACTOR = JFilmActor.FILM_ACTOR;
    public static final JActor ACTOR = JActor.ACTOR;
    public static final JInventory INVENTORY = JInventory.INVENTORY;
    public static final JRental RENTAL = JRental.RENTAL;

    public FilmRepositoryHasA(DSLContext dslContext, Configuration configuration) {
        this.dslContext = dslContext;
        this.dao = new FilmDao(configuration);
    }

    public Film findById(Long id) {
        return dao.fetchOneByJFilmId(id);
    }

    public List<Film> findByRangeBetween(Integer from, Integer to) {
        return dao.fetchRangeOfJLength(from, to);
    }

    public SimpleFilmInfo findSimpleFilmInfoById(Long id) {
        return dslContext.select(FILM.FILM_ID, FILM.TITLE, FILM.DESCRIPTION)
                .from(FILM)
                .where(FILM.FILM_ID.eq(id))
                .fetchOneInto(SimpleFilmInfo.class);
    }

    public List<FilmWithActor> findFilmWithActorList(Long page, Long pageSize) {
        return dslContext.select(
                        DSL.row(FILM.fields()),
                        DSL.row(FILM_ACTOR.fields()),
                        DSL.row(ACTOR.fields())
                )
                .from(FILM)
                .join(FILM_ACTOR).on(FILM.FILM_ID.eq(FILM_ACTOR.FILM_ID))
                .join(ACTOR).on(FILM_ACTOR.ACTOR_ID.eq(ACTOR.ACTOR_ID))
                .offset((page - 1) * pageSize)
                .limit(pageSize)
                .fetchInto(FilmWithActor.class);
    }

    public List<FilmPriceSummary> findFilmPriceSummaryByFilmTitleLike(String filmTitle) {
        return dslContext.select(
                        FILM.FILM_ID,
                        FILM.TITLE,
                        FILM.RENTAL_RATE,
                        case_()
                                .when(FILM.RENTAL_RATE.le(BigDecimal.valueOf(1.0)), "Cheap")
                                .when(FILM.RENTAL_RATE.le(BigDecimal.valueOf(3.0)), "Moderate")
                                .otherwise("Expensive").as("priceCategory"),
                        DSL.selectCount().from(INVENTORY).where(INVENTORY.FILM_ID.eq(FILM.FILM_ID)).asField("total_inventory")
                )
                .from(FILM)
                .where(containsIfNotBlank(FILM.TITLE, filmTitle))
                .fetchInto(FilmPriceSummary.class);
    }

    public List<FilmRentalSummary> findFilmRentalSummaryByFilmTitleLike(String filmTitle) {
        Table<Record2<Long, BigDecimal>> rentalDurationInfoSubquery = dslContext.select(
                        INVENTORY.FILM_ID,
                        avg(localDateTimeDiff(DatePart.DAY, RENTAL.RENTAL_DATE, RENTAL.RETURN_DATE)).as("average_rental_duration"))
                .from(RENTAL)
                .join(INVENTORY).on(RENTAL.INVENTORY_ID.eq(INVENTORY.INVENTORY_ID))
                .where(RENTAL.RETURN_DATE.isNotNull())
                .groupBy(INVENTORY.FILM_ID)
                .asTable("rental_duration_info");

        return dslContext.select(FILM.FILM_ID, FILM.TITLE, rentalDurationInfoSubquery.field("average_rental_duration"))
                .from(FILM)
                .join(rentalDurationInfoSubquery).on(FILM.FILM_ID.eq(rentalDurationInfoSubquery.field(INVENTORY.FILM_ID)))
                .where(containsIfNotBlank(FILM.TITLE, filmTitle))
                .orderBy(field(name("average_rental_duration")).desc())
                .fetchInto(FilmRentalSummary.class);
    }

    public List<Film> findRentedFilmByTitle(String filmTitle) {
        return dslContext.selectFrom(FILM)
                .whereExists(
                        selectOne()
                                .from(INVENTORY)
                                .join(RENTAL).on(INVENTORY.INVENTORY_ID.eq(RENTAL.INVENTORY_ID))
                                .where(INVENTORY.FILM_ID.eq(FILM.FILM_ID))
                )
                .and(containsIfNotBlank(FILM.TITLE, filmTitle))
                .fetchInto(Film.class);
    }


}
