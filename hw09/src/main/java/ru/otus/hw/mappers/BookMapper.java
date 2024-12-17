package ru.otus.hw.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.ItemDto;
import ru.otus.hw.models.Book;

import java.util.Collections;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BookMapper implements ItemMapper<Book> {

    private final GenreMapper genreMapper;

    private final AuthorMapper authorMapper;

    public BookDto toDto(Book book) {
        // Для Lazy-инициализация new ArrayList(..)
        return new BookDto(book.getId(), book.getTitle(), authorMapper.toItem(book.getAuthor()),
                book.getGenres() == null ? Collections.emptyList() :
                        book.getGenres().stream().map(genreMapper::toItem).collect(Collectors.toList()));
    }

    @Override
    public ItemDto toItem(Book book) {
        if (book == null) {
            return null;
        }
        return new ItemDto(book.getId(), book.getTitle());
    }
}
