package com.habin.sakila;

import com.habin.sakila.film.FilmRepository;
import com.habin.sakila.film.FilmService;
import com.habin.sakila.web.FilmWithActorPagedResponse;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class JooqStartSelectTest {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmService filmService;

    @Test
    @DisplayName("1) 영화정보 조회")
    void test() {
        Film film = filmRepository.findById(1L);
        Assertions.assertThat(film).isNotNull();
        log.info("film: {}", film);
    }

    @Test
    @DisplayName("2) 간단한 영화정보 조회")
    void test2() {
        var simpleFilmInfo = filmRepository.findSimpleFilmInfoById(1L);
        Assertions.assertThat(simpleFilmInfo).isNotNull();
        log.info("simpleFilmInfo: {}", simpleFilmInfo);
    }

    @Test
    @DisplayName("3) 영화와 배우정보 페이징 조회")
    void test3() {
        FilmWithActorPagedResponse filmWithActorList = filmService.getFilmWithActorList(1L, 20L);
        Assertions.assertThat(filmWithActorList).isNotNull();
        Assertions.assertThat(filmWithActorList.filmActorList()).hasSize(20);
        log.info("filmWithActorList: {}", filmWithActorList);
    }

}
