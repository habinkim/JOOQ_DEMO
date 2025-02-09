package com.habin.sakila;

import com.habin.sakila.film.FilmRepositoryHasA;
import com.habin.sakila.film.FilmRepositoryIsA;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqDaoWrapperTest {

    @Autowired
    FilmRepositoryIsA filmRepositoryIsA;

    @Autowired
    FilmRepositoryHasA filmRepositoryHasA;

    @Test
    @DisplayName("""
            상속) 자동생성 DAO 사용
             - 영화 길이가 100 ~ 180 분 사이인 영화 조회
            """)
    void inheritanceDao() {
        // given
        Integer from = 100;
        Integer to = 180;

        // when
        List<Film> films = filmRepositoryIsA.fetchRangeOfJLength(from, to);

        // then
        assertThat(films).allSatisfy(film -> assertThat(film.getLength()).isBetween(from, to));

    }

    @Test
    @DisplayName("""
            컴포지트) 자동생성 DAO 사용
             - 영화 길이가 100 ~ 180 분 사이인 영화 조회
            """)
    void compositionDao() {
        // given
        Integer from = 100;
        Integer to = 180;

        // when
        List<Film> films = filmRepositoryHasA.findByRangeBetween(from, to);

        // then
        assertThat(films).allSatisfy(film -> assertThat(film.getLength()).isBetween(from, to));

    }


}
