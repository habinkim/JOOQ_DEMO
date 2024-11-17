package com.habin.sakila.web;

public record PagedResponse(
        Long page,
        Long pageSize
) {
}
