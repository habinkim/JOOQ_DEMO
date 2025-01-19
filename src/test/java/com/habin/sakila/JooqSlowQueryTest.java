package com.habin.sakila;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.jooq.impl.DSL.dual;

@SpringBootTest
public class JooqSlowQueryTest {

    @Autowired
    private DSLContext dslContext;

    @Test
    @DisplayName("SLOW 쿼리 감지테스트")
    void detectSlowQueryTest() {
        dslContext.select(DSL.field("SLEEP(4)")).from(dual()).execute();
    }

}
