package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.ItemDto;
import ru.otus.hw.models.Genre;

@Component
public class GenreMapper implements ItemMapper<Genre> {

    public GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    @Override
    public ItemDto toItem(Genre genre) {
        if (genre == null) {
            return null;
        }
        return new ItemDto(genre.getId(), genre.getName());
    }
}
