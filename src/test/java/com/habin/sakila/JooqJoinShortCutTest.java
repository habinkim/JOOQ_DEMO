package com.habin.sakila;

import com.habin.sakila.film.FilmRepository;
import com.habin.sakila.film.FilmWithActor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqJoinShortCutTest {

    @Autowired
    FilmRepository filmRepository;

    @Test
    @DisplayName("implicitPathJoin 테스트")
    void implicitPathJoinTest() {
        List<FilmWithActor> original = filmRepository.findFilmWithActorList(1L, 10L);
        List<FilmWithActor> implicit = filmRepository.findFilmWithActorListImplicitPathJoin(1L, 10L);

        assertThat(original)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(implicit);
    }

    @Test
    @DisplayName("explicitPathJoin 테스트")
    void explicitPathJoinTest() {
        List<FilmWithActor> original = filmRepository.findFilmWithActorList(1L, 10L);
        List<FilmWithActor> implicit = filmRepository.findFilmWithActorListExplicitPathJoin(1L, 10L);

        assertThat(original)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(implicit);
    }

}
