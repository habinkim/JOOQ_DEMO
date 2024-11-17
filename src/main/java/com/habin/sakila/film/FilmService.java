package com.habin.sakila.film;

import com.habin.sakila.web.FilmMapper;
import com.habin.sakila.web.FilmWithActorPagedResponse;
import com.habin.sakila.web.PagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public FilmWithActorPagedResponse getFilmWithActorList(Long page, Long pageSize) {
        List<FilmWithActor> filmWithActors = filmRepository.findFilmWithActorList(page, pageSize);
        List<FilmWithActorPagedResponse.FilmActorResponse> filmActorResponses = filmMapper.toFilmWithActorPagedResponses(filmWithActors);

        PagedResponse pagedResponse = new PagedResponse(page, pageSize);

        return new FilmWithActorPagedResponse(pagedResponse, filmActorResponses);
    }

}
