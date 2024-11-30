package ru.otus.hw.repositories;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(JpaCommentRepository.class)
class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void create() {
        Author author = new Author(null, "TEST AUTHOR");
        testEntityManager.persistAndFlush(author);
        Book book = new Book(null, "TEST TITLE", author, new ArrayList<>());
        testEntityManager.persistAndFlush(book);

        Comment comment = new Comment(null, "TEST TEXT", book);
        jpaCommentRepository.save(comment);

        assertThat(comment.getId()).isGreaterThan(0);

        Comment actualComment = testEntityManager.find(Comment.class, comment.getId());
        assertThat(actualComment).isNotNull()
                .matches(s -> s.getText().equals("TEST TEXT"))
                .matches(s -> s.getBook() != null && s.getBook().getId() > 0); // и так далее
    }

    @Test
    void deleteById() {
        Comment before = testEntityManager.find(Comment.class, 1L);
        assertThat(before).isNotNull();

        jpaCommentRepository.deleteById(1L);

        testEntityManager.flush();
        testEntityManager.clear();

        Comment after = testEntityManager.find(Comment.class, 1L);
        assertThat(after).isNull();
    }

    @Test
    void findById() {
        Optional<Comment> comment = jpaCommentRepository.findById(1L);
        Comment expectedComment = testEntityManager.find(Comment.class, 1L);
        assertThat(comment).isPresent().get().usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findAllByBookId() {
        SessionFactory sessionFactory = testEntityManager.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        List<Comment> commentList = jpaCommentRepository.findAllByBookId(1L);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);

        List<Comment> expectedCommentList = List.of(jpaCommentRepository.findById(1L).get());
        assertThat(commentList).isNotNull().hasSize(1)
                .usingRecursiveComparison().isEqualTo(expectedCommentList);

        sessionFactory.getStatistics().clear();
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findAllByIds() {
        SessionFactory sessionFactory = testEntityManager.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);

        List<Comment> commentList = jpaCommentRepository.findAllByIds(List.of(1L, 2L));
        System.out.println(commentList);
        assertThat(commentList).isNotNull().hasSize(2)
                .allMatch(c -> !c.getText().equals(""))
                .allMatch(c -> c.getBook() != null);
        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(1);
        sessionFactory.getStatistics().clear();
    }
}