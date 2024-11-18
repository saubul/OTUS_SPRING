package ru.otus.hw.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({JpaBookRepository.class, JpaAuthorRepository.class})
class JpaBookRepositoryTest {

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findById() {
        SessionFactory sessionFactory = testEntityManager.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        Optional<Book> book = jpaBookRepository.findById(1L);
        Book book1 = testEntityManager.find(Book.class, 1L);

        assertThat(book).isPresent().get().usingRecursiveComparison().isEqualTo(book1);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);

        sessionFactory.getStatistics().clear();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findAll() {
        SessionFactory sessionFactory = testEntityManager.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        List<Book> bookList = jpaBookRepository.findAll();
        assertThat(bookList).hasSize(4)
                .allMatch(b -> !b.getTitle().equals(""))
                .allMatch(b -> b.getAuthor() != null && b.getId() != null);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);

        sessionFactory.getStatistics().clear();
    }

    @Test
    void save() {
        Book book = new Book();
        book.setTitle("TEST TITLE");
        Author author = testEntityManager.find(Author.class, 1L);
        Genre genre = testEntityManager.find(Genre.class, 1L);
        book.setAuthor(author);
        book.setGenres(List.of(genre));
        jpaBookRepository.save(book);

        assertThat(book.getId()).isGreaterThan(0);
        Book actualBook = testEntityManager.find(Book.class, book.getId());
        assertThat(actualBook).isNotNull()
                .matches(s -> s.getTitle().equals("TEST TITLE"))
                .matches(s -> s.getAuthor() != null && s.getAuthor().getId().equals(1L)); // и так далее

    }

    @Test
    void deleteById() {
        Book before = testEntityManager.find(Book.class, 1L);
        assertThat(before).isNotNull();

        jpaBookRepository.deleteById(1L);

        testEntityManager.flush();
        testEntityManager.clear();


        Book after = testEntityManager.find(Book.class, 1L);
        assertThat(after).isNull();
    }
}