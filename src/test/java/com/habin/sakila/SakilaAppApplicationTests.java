package com.habin.sakila;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.generated.tables.JActor;
import org.jooq.generated.tables.records.ActorRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SakilaAppApplicationTests {

    @Autowired
    DSLContext dslContext;

    @Test
    void test() {
        Result<ActorRecord> fetched = dslContext.selectFrom(JActor.ACTOR)
                .limit(10)
                .fetch();
    }

}
