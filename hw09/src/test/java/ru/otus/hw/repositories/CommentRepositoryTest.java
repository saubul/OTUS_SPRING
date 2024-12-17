package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void findAllByBookId() {
        List<Comment> commentList = commentRepository.findAllByBookId(1L);
        List<Comment> expectedCommentList = List.of(commentRepository.findById(1L).get());

        assertThat(commentList).isNotNull().hasSize(1)
                .usingRecursiveComparison().isEqualTo(expectedCommentList);
    }

    @Test
    void findAllByIds() {
        List<Comment> commentList = commentRepository.findAllByIdIn(List.of(1L, 2L));
        List<Comment> expectedCommentList = testEntityManager.getEntityManager()
                .createQuery("select c from Comment c where c.book.id in (:bookIds)", Comment.class)
                .setParameter("bookIds", List.of(1L, 2L))
                .getResultList();

        assertThat(commentList).usingRecursiveComparison().isEqualTo(expectedCommentList);
    }
}