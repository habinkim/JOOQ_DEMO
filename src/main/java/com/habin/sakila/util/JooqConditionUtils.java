package com.habin.sakila.util;

import lombok.experimental.UtilityClass;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.jooq.tools.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

@UtilityClass
public class JooqConditionUtils {

    public static Condition containsIfNotBlank(Field<String> field, String value) {
        if (StringUtils.isBlank(value)) {
            return DSL.noCondition();
        }

        return field.like("%" + value + "%");
    }

    public static Condition eqIfNotBlank(Field<String> field, String value) {
        if (StringUtils.isBlank(value)) {
            return DSL.noCondition();
        }

        return field.eq(value);
    }

    public static <T> Condition inIfNotEmpty(Field<T> field, List<T> values) {
        if (CollectionUtils.isEmpty(values)) {
            return DSL.noCondition();
        }

        return field.in(values);
    }

}
