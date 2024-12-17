package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.models.Book;
import ru.otus.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Test
    void findAllTest() {

        List<Book> bookList = bookRepository.findAll();
        List<Book> expectedBookList = mongoTemplate.findAll(Book.class);

        assertThat(bookList).usingRecursiveComparison().isEqualTo(expectedBookList);

    }

    @Test
    void findByIdTest() {
        Optional<Book> book = bookRepository.findById("1");
        Book expectedBook = mongoTemplate.findById("1", Book.class);

        assertThat(book).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

}