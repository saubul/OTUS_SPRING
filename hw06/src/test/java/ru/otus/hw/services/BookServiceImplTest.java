package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findById() {
        BookDto expectedBook = new BookDto(1L, "BookTitle_1", new Author(1L, "Author_1"),
                List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")));
        Optional<BookDto> book = bookService.findById(1L);
        assertTrue(book.isPresent());
        assertTrue(book.get().id() > 0);
        assertThat(expectedBook).usingRecursiveComparison().isEqualTo(book.get());
    }

    @Test
    void findAll() {
        List<BookDto> bookDTOList = bookService.findAll();
        assertFalse(bookDTOList.isEmpty());
    }

    @Test
    void create() {
        BookDto expectedBook = new BookDto(5L, "TEST BOOK", new Author(1L, "Author_1"), Collections.emptyList());
        BookDto book = bookService.create("TEST BOOK", 1L, new ArrayList<>());
        assertTrue(book.id() > 0); // и т.д.
        assertThat(expectedBook).usingRecursiveComparison().isEqualTo(book);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void update() {
        BookDto expectedBook = new BookDto(1L, "TEST BOOK2", new Author(1L, "Author_1"), Collections.emptyList());
        BookDto book = bookService.update(1L, "TEST BOOK2", 1L, new ArrayList<>());
        assertThat(expectedBook).usingRecursiveComparison().isEqualTo(book);
    }

    @Test
    void deleteById() {

        Optional<BookDto> book = bookService.findById(1L);
        assertTrue(book.isPresent());

        bookService.deleteById(1L);

        book = bookService.findById(1L);
        assertTrue(book.isEmpty());
    }
}