package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.*;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
@Import({BookMapper.class, GenreMapper.class, AuthorMapper.class})
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookMapper bookConverter;

    @MockBean
    private BookService bookService;

    @Test
    void findAllBooks() throws Exception {
        BookDto bookDto = new BookDto(1L, "Test", new ItemDto(1L, "Test"), List.of(new ItemDto(1L, "Test")));
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
        BookSaveDto bookDto = new BookSaveDto("Test", 1L, List.of(1L));
        this.mockMvc.perform(post("/book/create").flashAttr("book", bookDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/"));
        verify(bookService, times(1)).create(bookDto);

    }

    @Test
    void updateBookPage() throws Exception {
        BookDto bookDto = new BookDto(1L, "Test", new ItemDto(1L, "Test"),
                List.of(new ItemDto(1L, "Test")));
        when(bookService.findById(1L)).thenReturn(bookDto);
        this.mockMvc.perform(get("/book/update").param("id", "1"))
                .andExpect(model().attribute("book", bookDto))
                .andExpect(view().name("updateBook"));
    }

    @Test
    void updateBook() throws Exception {
        BookSaveDto bookDto = new BookSaveDto("Test", 1L, List.of(1L));
        this.mockMvc.perform(post("/book/update").flashAttr("book", bookDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/"));
        verify(bookService, times(1)).update(bookDto);

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