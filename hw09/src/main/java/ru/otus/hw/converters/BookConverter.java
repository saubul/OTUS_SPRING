package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Book;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {

    private final GenreConverter genreConverter;

    private final AuthorConverter authorConverter;

    public BookDto toDTO(Book book) {
        // Для Lazy-инициализация new ArrayList(..)
        return new BookDto(book.getId(), book.getTitle(), authorConverter.convertToDto(book.getAuthor()),
                book.getGenres().stream().map(genreConverter::convertToDto).collect(Collectors.toList()));
    }

    public BookEditDto toEditDto(BookDto bookDto) {
        return new BookEditDto(bookDto.getId(), bookDto.getTitle(),
                bookDto.getAuthor().id(), bookDto.getGenreList()
                .stream()
                .map(GenreDto::id).map(String::valueOf)
                .collect(Collectors.joining(",")));
    }
}
