package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void findAllTest() {

        List<Book> bookList = bookRepository.findAll();
        List<Book> expectedBookList = testEntityManager.getEntityManager().createQuery("select b Book from Book b", Book.class).getResultList();

        assertThat(bookList).usingRecursiveComparison().isEqualTo(expectedBookList);

    }

    @Test
    void findByIdTest() {
        Optional<Book> book = bookRepository.findById(1L);
        Book expectedBook = testEntityManager.find(Book.class, 1L);

        assertThat(book).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

}