package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.rest.BookController;
import ru.otus.hw.dto.*;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAllBooksTest() throws Exception {
        BookDto bookDto = new BookDto(1L, "Test", new AuthorDto(1L, "Test"),
                List.of(new GenreDto(1L, "Test")));
        when(bookService.findAll()).thenReturn(List.of(bookDto));
        this.mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookDto))));
    }

    @Test
    void findByIdTest() throws Exception {
        BookDto bookDto = new BookDto(1L, "Test", new AuthorDto(1L, "Test"),
                List.of(new GenreDto(1L, "Test")));
        when(bookService.findById(1L)).thenReturn(bookDto);
        this.mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookDto)));

    }

    @Test
    void createBookTest() throws Exception {
        BookCreateDto bookCreateDto = new BookCreateDto("Test", 1L, List.of(1L));
        BookDto bookDto = new BookDto(5L, "Test", new AuthorDto(1L, "Author_1"), List.of(new GenreDto(1L, "Genre_1")));
        when(bookService.create(any())).thenReturn(bookDto);
        String bookDtoJson = objectMapper.writeValueAsString(bookDto);
        this.mockMvc.perform(post("/api/v1/books").contentType(APPLICATION_JSON)
                        .content(bookDtoJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(bookDtoJson));
    }

    @Test
    void updateBook() throws Exception {
        BookUpdateDto bookUpdateDto = new BookUpdateDto(1L, "Test", 1L, List.of(1L));
        BookDto bookDto = new BookDto(5L, "Test", new AuthorDto(1L, "Author_1"), List.of(new GenreDto(1L, "Genre_1")));
        when(bookService.update(any())).thenReturn(bookDto);
        String bookDtoJson = objectMapper.writeValueAsString(bookDto);
        this.mockMvc.perform(put("/api/v1/books/1")
                        .contentType(APPLICATION_JSON)
                        .content(bookDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(bookDtoJson));
    }

    @Test
    void deleteBook() throws Exception {
        doNothing().when(bookService).deleteById(any());
        this.mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().is2xxSuccessful());
    }
}