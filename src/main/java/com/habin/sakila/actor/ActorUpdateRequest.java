package com.habin.sakila.actor;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActorUpdateRequest {
    private String firstName;
    private String lastName;

}
