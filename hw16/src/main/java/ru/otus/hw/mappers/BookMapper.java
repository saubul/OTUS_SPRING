package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final GenreMapper genreMapper;

    private final AuthorMapper authorMapper;

    public BookDto toDto(Book book) {
        return new BookDto(book.getId(),
                book.getTitle(),
                authorMapper.toDto(book.getAuthor()),
                book.getGenres() == null ? Collections.emptyList() :
                        book.getGenres().stream().map(genreMapper::toDto).collect(Collectors.toList()));
    }

}
