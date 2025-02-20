package ru.otus.hw.services;


import ru.otus.hw.dto.GenreDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();

    List<GenreDto> findAllByIds(List<Long> ids);
}
