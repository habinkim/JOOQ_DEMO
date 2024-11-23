package com.habin.sakila;

import com.habin.sakila.actor.ActorRepository;
import org.assertj.core.api.Assertions;
import org.jooq.generated.tables.pojos.Actor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        Assertions.assertThat(actors).hasSize(1);
    }

}
