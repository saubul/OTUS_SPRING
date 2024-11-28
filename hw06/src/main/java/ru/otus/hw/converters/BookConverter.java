package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;

import java.util.ArrayList;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookConverter {

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public String bookToString(BookDto book) {
        var genresString = book.genreList().stream()
                .map(genreConverter::genreToString)
                .map("{%s}"::formatted)
                .collect(Collectors.joining(", "));
        return "Id: %d, title: %s, author: {%s}, genres: [%s]".formatted(
                book.id(),
                book.title(),
                authorConverter.authorToString(book.author()),
                genresString);
    }

    public BookDto toDTO(Book book) {
        // Для Lazy-инициализация new ArrayList(..)
        return new BookDto(book.getId(), book.getTitle(), book.getAuthor(), new ArrayList<>(book.getGenres()));
    }
}
