package com.habin.sakila;

import com.habin.sakila.film.FilmPriceSummary;
import com.habin.sakila.film.FilmRentalSummary;
import com.habin.sakila.film.FilmRepositoryHasA;
import org.jooq.generated.tables.pojos.Film;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqSubqueryTest {

    @Autowired
    private FilmRepositoryHasA filmRepository;

    @Test
    @DisplayName("""
            영화별 대여료가
            1.0 이하면 'Cheap',
            3.0 이하면 'Moderate',
            그 이상이면 'Expensive'로 분류하고,
            각 영화의 총 재고 수를 조회한다.
            """)
    void scalaSubQueryTest() {
        // given
        String filmTitle = "EGG";

        // when
        List<FilmPriceSummary> summaries = filmRepository.findFilmPriceSummaryByFilmTitleLike(filmTitle);

        // then
        assertThat(summaries).isNotEmpty();

    }

    @Test
    @DisplayName("평균 대여 기간이 가장 긴 영화부터 정렬해서 조회한다.")
    void fromClauseSubqueryTest() {
        // given
        String filmTitle = "EGG";

        List<FilmRentalSummary> summaries = filmRepository.findFilmRentalSummaryByFilmTitleLike(filmTitle);

        // then
        assertThat(summaries).isNotEmpty();
    }

    @Test
    @DisplayName("대여된 기록이 있는 영화만 조회")
    void whereClauseSubqueryTest() {
        // given
        String filmTitle = "EGG";

        // when
        List<Film> summaries = filmRepository.findRentedFilmByTitle(filmTitle);

        // then
        assertThat(summaries).isNotEmpty();
    }

}
