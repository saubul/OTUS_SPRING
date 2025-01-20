package ru.otus.hw.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.config.WebSecurityConfiguration;
import ru.otus.hw.controllers.rest.BookController;
import ru.otus.hw.services.BookService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(WebSecurityConfiguration.class)
public class BookControllerSecurityTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookService bookService;

    @Test
    void testAuthenticatedOnBookEndpoint() throws Exception {
        mockMvc.perform(
                        get("/api/v1/books").with(user("daemon").password("daemon"))
                )
                .andExpect(status().isOk());
        mockMvc.perform(
                        get("/api/v1/books/1").with(user("daemon").password("daemon"))
                )
                .andExpect(status().isOk());

        // И так далее?..
    }

    @Test
    void testUnauthenticatedOnBookEndpoint() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/books/1")
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testAuthorizedOnBookEndpoint() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/books/1").with(user("daemon").password("daemon").authorities(new SimpleGrantedAuthority("admin")))
                )
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(
                        delete("/api/v1/books/1").with(user("daemon").password("daemon").authorities(new SimpleGrantedAuthority("user")))
                )
                .andExpect(status().is4xxClientError());
    }

}
