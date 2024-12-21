package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.*;
import ru.otus.hw.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findById() {
        BookDto expectedBook = new BookDto(1L, "BookTitle_1", new AuthorDto(1L, "Author_1"),
                List.of(new GenreDto(1L, "Genre_1"), new GenreDto(2L, "Genre_2")));
        assertDoesNotThrow(() -> bookService.findById(1L));
        BookDto bookDto = bookService.findById(1L);
        assertThat(expectedBook).usingRecursiveComparison().isEqualTo(bookDto);
    }

    @Test
    void findAll() {
        List<BookDto> bookDTOList = bookService.findAll();
        assertFalse(bookDTOList.isEmpty());
    }

    @Test
    void create() {
        BookCreateDto bookCreateDto = new BookCreateDto("TEST BOOK", 1L, Collections.emptyList());
        BookDto expectedBook = new BookDto(5L, "TEST BOOK", new AuthorDto(1L, "Author_1"), Collections.emptyList());
        BookDto book = bookService.create(bookCreateDto);
        assertThat(expectedBook).usingRecursiveComparison().isEqualTo(book);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void update() {
        BookUpdateDto bookUpdateDto = new BookUpdateDto(1L, "TEST BOOK2",1L, List.of());
        BookDto book = bookService.update(bookUpdateDto);
        BookDto expect = new BookDto(1L, "TEST BOOK2", new AuthorDto(1L, "Author_1"),
                List.of(new GenreDto(1L,  "Genre_1"), new GenreDto(2L, "Genre_2")));
        assertThat(expect).usingRecursiveComparison().isEqualTo(book);
    }

    @Test
    void deleteById() {

        assertDoesNotThrow(() -> bookService.findById(1L));

        bookService.deleteById(1L);

        assertThrows(NotFoundException.class, () -> bookService.findById(1L));
    }
}