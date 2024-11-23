package com.habin.sakila;

import com.habin.sakila.actor.ActorFilmography;
import com.habin.sakila.actor.ActorFilmographySearchOption;
import com.habin.sakila.actor.ActorRepository;
import org.jooq.generated.tables.pojos.Actor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqConditionTest {

    @Autowired
    private ActorRepository actorRepository;

    @Test
    @DisplayName("and 조건 검색 - firstName과 lastName이 일치하는 배우 조회")
    void andCondition1() {
        // given
        String firstName = "ED";
        String lastName = "CHASE";

        // when
        List<Actor> actors = actorRepository.findByFirstNameAndLastName(firstName, lastName);

        // then
        assertThat(actors).hasSize(1);
    }

    @Test
    @DisplayName("or 조건 검색 - firstName이 일치하거나 lastName이 일치하는 배우 조회")
    void orCondition1() {
        // given
        String firstName = "ED";
        String lastName = "CHASE";

        // when
        List<Actor> actors = actorRepository.findByFirstNameOrLastName(firstName, lastName);

        // then
        assertThat(actors).hasSize(4);
    }

    @Test
    @DisplayName("in 절 - 동적 조건 검색")
    void inCondition1() {
        // when
        List<Actor> actors = actorRepository.findByActorIdIn(List.of(1L));

        // then
        assertThat(actors).hasSize(1);
    }

    @Test
    @DisplayName("in절 - 동적 조건 검색 - empty list시 제외")
    void inCondition2() {
        // when
        List<Actor> actorList = actorRepository.findByActorIdIn(Collections.emptyList());

        // then
        assertThat(actorList).hasSizeGreaterThan(1);
    }

    @Test
    @DisplayName("다중 조건 검색 - 배우 이름으로 조회")
    void multipleCondition1() {
        // given
        var searchOption = ActorFilmographySearchOption.builder()
                .actorName("LOLLOBRIGIDA")
                .build();
        // when
        List<ActorFilmography> actorFilmographies = actorRepository.findActorFilmography(searchOption);

        // then
        assertThat(actorFilmographies).hasSize(1);
    }

    @Test
    @DisplayName("다중 조건 검색 - 배우 이름과 영화 제목으로 조회")
    void multipleCondition2() {
        // given
        var searchOption = ActorFilmographySearchOption.builder()
                .actorName("LOLLOBRIGIDA")
                .filmTitle("COMMANDMENTS EXPRESS")
                .build();

        // when
        List<ActorFilmography> actorFilmographies = actorRepository.findActorFilmography(searchOption);

        // then
        assertThat(actorFilmographies).hasSize(1);
        assertThat(actorFilmographies.getFirst().films()).hasSize(1);
    }

}
