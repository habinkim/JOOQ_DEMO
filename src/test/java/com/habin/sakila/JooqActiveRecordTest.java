package com.habin.sakila;

import com.habin.sakila.actor.ActorRepository;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.records.ActorRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqActiveRecordTest {

    @Autowired
    ActorRepository actorRepository;

    @Autowired
    DSLContext dslContext;

    @Test
    @DisplayName("SELECT 절 예제")
    void activeRecordSelect() {
        // given
        Long actorId = 1L;

        // when
        ActorRecord actor = actorRepository.findRecordByActorId(actorId);

        // then
        assertThat(actor).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("activeRecord refresh 예제")
    void activeRecordRefresh() {
        // given
        Long actorId = 1L;
        ActorRecord actor = actorRepository.findRecordByActorId(actorId);
        actor.setFirstName(null);

        // when
        actor.refresh();

        // then
        assertThat(actor.getFirstName()).isNotBlank();
    }

    @Test
    @DisplayName("activeRecord store 예제 - insert")
    @Transactional
    void activeRecordInsert() {
        // given
        ActorRecord actorRecord = dslContext.newRecord(JActor.ACTOR);

        // when
        actorRecord.setFirstName("john");
        actorRecord.setLastName("doe");
        actorRecord.store();

//        혹은
//        actorRecord.insert();

        // then
        assertThat(actorRecord.getLastUpdate()).isNull();
    }

    @Test
    @DisplayName("activeRecord store 예제 - update")
    @Transactional
    void activeRecordUpdate() {
        // given
        Long actorId = 1L;
        String newName = "test";
        ActorRecord actor = actorRepository.findRecordByActorId(actorId);

        // when
        actor.setFirstName(newName);
        actor.store();

        // 혹은
        // actor.update();

        // then
        assertThat(actor.getFirstName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("activeRecord delete 예제")
    @Transactional
    void activeRecordDelete() {
        // given
        ActorRecord actorRecord = dslContext.newRecord(JActor.ACTOR);

        // when
        actorRecord.setFirstName("john");
        actorRecord.setLastName("doe");
        actorRecord.store();

        // when
        actorRecord.delete();

        // then
        assertThat(actorRecord).hasNoNullFieldsOrProperties();
    }

}
