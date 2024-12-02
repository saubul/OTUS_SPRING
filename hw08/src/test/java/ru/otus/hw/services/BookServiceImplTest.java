package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.services.BookService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureDataMongo
class BookServiceImplTest {

    @Autowired
    private BookService bookService;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findById() {
        Book expect = new Book("1", "Book_1", new Author("2", "Author_2"), List.of(new Genre("2", "Genre_2"), new Genre("1", "Genre_1")));
        Book book = bookService.findById("1").get();

        assertThat(expect).usingRecursiveComparison().isEqualTo(book);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findAll() {

        Book book = new Book("1", "Book_1", new Author("2", "Author_2"),
                List.of(new Genre("2", "Genre_2"), new Genre("1", "Genre_1")));

        Book book2 = new Book("2", "Book_2", new Author("1", "Author_1"),
                List.of(new Genre("3", "Genre_3"), new Genre("2", "Genre_2")));

        Book book3 = new Book("3", "Book_3", new Author("1", "Author_1"),
                List.of(new Genre("1", "Genre_1")));

        Book book4 = new Book("4", "Book_4", new Author("3", "Author_3"),
                List.of(new Genre("3", "Genre_3"), new Genre("1", "Genre_1")));

        List<Book> books = bookService.findAll();
        assertThat(List.of(book, book2, book3, book4)).usingRecursiveComparison().isEqualTo(books);

    }

    @Test
    void create() {

        Book book = bookService.create("Book_10", "1", List.of("1"));
        assertThat(book).matches(b -> b.getTitle().equals(book.getTitle())); // и т.д. рекурсия не используется из-за неоднозначности ID
    }

    @Test
    void update() {

        Book book = bookService.update("1", "Book_TEST", "1", List.of("1"));
        assertThat(book).matches(b -> b.getTitle().equals("Book_TEST")); // и т.д. рекурсия не используется из-за неоднозначности ID

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void deleteById() {
        Optional<Book> book = bookService.findById("1");
        assertTrue(book.isPresent());

        bookService.deleteById("1");

        book = bookService.findById("1");
        assertTrue(book.isEmpty());

    }
}