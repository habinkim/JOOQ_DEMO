package com.habin.sakila.actor;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class ActorFilmographySearchOption {

    private final String actorName;
    private final String filmTitle;

}
