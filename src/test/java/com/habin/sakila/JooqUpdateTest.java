package com.habin.sakila;

import com.habin.sakila.actor.ActorRepository;
import com.habin.sakila.actor.ActorUpdateRequest;
import org.jooq.generated.tables.pojos.Actor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class JooqUpdateTest {

    @Autowired
    private ActorRepository actorRepository;

    @Test
    @DisplayName("POJO를 사용한 update")
    @Transactional
    void updateWithPojo() {
        // given
        Actor newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Actor actor = actorRepository.saveWithReturning(newActor);

        // when
        actor.setFirstName("Suri");
        actorRepository.update(actor);

        // then
        Actor updatedActor = actorRepository.findByActorId(actor.getActorId());
        assertThat(updatedActor.getFirstName()).isEqualTo("Suri");

    }

    @Test
    @DisplayName("일부 필드만 update - DTO 활용")
    @Transactional
    void updatePartialFields() {
        // given
        Actor newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Long newActorId = actorRepository.saveWithReturningPkOnly(newActor);
        ActorUpdateRequest request = ActorUpdateRequest.builder()
                .firstName("Suri")
                .build();

        // when
        actorRepository.updateWithDto(newActorId, request);

        // then
        Actor updatedActor = actorRepository.findByActorId(newActorId);
        assertThat(updatedActor.getFirstName()).isEqualTo("Suri");
    }

    @Test
    @DisplayName("일부 필드만 update - Record 활용")
    @Transactional
    void updatePartialFieldsWithRecord() {
        // given
        Actor newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Long newActorId = actorRepository.saveWithReturningPkOnly(newActor);
        ActorUpdateRequest request = ActorUpdateRequest.builder()
                .firstName("Suri")
                .build();

        // when
        actorRepository.updateWithRecord(newActorId, request);

        // then
        Actor updatedActor = actorRepository.findByActorId(newActorId);
        assertThat(updatedActor.getFirstName()).isEqualTo("Suri");
    }

    @Test
    @DisplayName("delete")
    @Transactional
    void delete() {
        // given
        Actor newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Long newActorId = actorRepository.saveWithReturningPkOnly(newActor);

        // when
        actorRepository.delete(newActorId);

        // then
        Actor deletedActor = actorRepository.findByActorId(newActorId);
        assertThat(deletedActor).isNull();
    }

    @Test
    @DisplayName("delete with Record")
    @Transactional
    void deleteWithActiveRecord() {
        // given
        Actor newActor = new Actor();
        newActor.setFirstName("Tom");
        newActor.setLastName("Cruise");

        Long newActorId = actorRepository.saveWithReturningPkOnly(newActor);

        // when
        int delete = actorRepository.deleteWithActiveRecord(newActorId);

        // then
        assertThat(delete).isEqualTo(1);
    }
}
