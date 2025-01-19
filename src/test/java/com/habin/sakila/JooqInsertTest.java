package com.habin.sakila;

import com.habin.sakila.actor.ActorRepository;
import org.jooq.generated.tables.pojos.Actor;
import org.jooq.generated.tables.records.ActorRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqInsertTest {

    @Autowired
    private ActorRepository actorRepository;

    @Test
    @DisplayName("자동생성된 DAO를 통한 insert")
    @Transactional
    void insertDao() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        actorRepository.saveByDao(actor);

        // then
        assertThat(actor.getActorId()).isNotNull();
    }

    @Test
    @DisplayName("ActiveRecord 를 통한 insert")
    @Transactional
    void insertByRecord() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        ActorRecord newActorRecord = actorRepository.saveByRecord(actor);

        // then
        assertThat(actor.getActorId()).isNull();
        assertThat(newActorRecord.getActorId()).isNotNull();
    }

    @Test
    @DisplayName("SQL 실행 후 PK만 반환")
    @Transactional
    void insertByRecordReturnPK() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        Long newActorId = actorRepository.saveWithReturningPkOnly(actor);

        // then
        assertThat(newActorId).isNotNull();
    }

    @Test
    @DisplayName("SQL 실행 후 해당 ROW 전체 반환")
    @Transactional
    void insertByRecordReturnRow() {
        // given
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor.setLastUpdate(LocalDateTime.now());

        // when
        Actor newActor = actorRepository.saveWithReturning(actor);

        // then
        assertThat(newActor.getActorId()).isNotNull();
    }

    @Test
    @DisplayName("Bulk Insert")
    @Transactional
    void bulkInsert() {
        // given
        Actor actor1 = new Actor();
        actor1.setFirstName("John");
        actor1.setLastName("Doe");
        actor1.setLastUpdate(LocalDateTime.now());

        Actor actor2 = new Actor();
        actor2.setFirstName("Jane");
        actor2.setLastName("Doe");
        actor2.setLastUpdate(LocalDateTime.now());

        // when
        actorRepository.bulkInsertWithRows(List.of(actor1, actor2));

    }


}
