package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
@Import({BookConverter.class, GenreConverter.class, AuthorConverter.class})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookConverter bookConverter;

    @MockBean
    private BookService bookService;

    @Test
    void findAllBooks() throws Exception {
        BookDto bookDto = new BookDto(1L, "Test", new AuthorDto(1L, "Test"), List.of(new GenreDto(1L, "TEst")));
        when(bookService.findAll()).thenReturn(List.of(bookDto));
        this.mockMvc.perform(get("/book/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", List.of(bookDto)));
    }

    @Test
    void createBookPage() throws Exception {
        this.mockMvc.perform(get("/book/create"))
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name("createBook"));
    }

    @Test
    void createBook() throws Exception {
        BookEditDto bookEditDto = new BookEditDto(null, "Test", 1L, "1");
        this.mockMvc.perform(post("/book/create").flashAttr("book", bookEditDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/"));
        verify(bookService, times(1)).create("Test", 1L, List.of(1L));

    }

    @Test
    void editBookPage() throws Exception {
        Optional<BookDto> bookDtoOptional = Optional.of(
                new BookDto(1L, "Test", new AuthorDto(1L, "Test"), List.of(new GenreDto(1L, "Test")))
        );
        when(bookService.findById(1L)).thenReturn(bookDtoOptional);
        this.mockMvc.perform(get("/book/edit").param("id", "1"))
                .andExpect(model().attribute("book", bookConverter.toEditDto(bookDtoOptional.get())))
                .andExpect(view().name("editBook"));
    }

    @Test
    void editBook() throws Exception {
        BookEditDto bookEditDto = new BookEditDto(1L, "Test", 1L, "1");
        this.mockMvc.perform(post("/book/edit").flashAttr("book", bookEditDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/"));
        verify(bookService, times(1)).update(1L, "Test", 1L, List.of(1L));

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void deleteBook() throws Exception {
        this.mockMvc.perform(post("/book/delete").param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/"));
        verify(bookService, times(1)).deleteById(1L);
    }
}