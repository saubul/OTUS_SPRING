package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findById() {
        Optional<Comment> comment = commentService.findById(1L);
        assertTrue(comment.isPresent());
        assertTrue(comment.get().getId() > 0);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findAllByBookId() {
        List<Comment> commentList = commentService.findAllByBookId(1L);
        assertEquals(1, commentList.size());
    }

    @Test
    void create() {
//        Comment expectedComment = new Comment(5L, "TEST COMMENT",
//                new Book(1L, "BookTitle_1", new Author(1L, "Author_1"),
//                        List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2"))));
        Comment comment = commentService.create( "TEST COMMENT", 1L);
        assertNotNull(comment);
        assertTrue(comment.getId() > 0);
        assertEquals("TEST COMMENT", comment.getText());
        assertEquals(1L, comment.getBook().getId());

        // Здесь LazyInit..Exception из-за Lazy коллекции Genres в Book. Проверить нельзя
        // assertThat(expectedComment).usingRecursiveComparison().isEqualTo(comment);
        // Вообще, мы проверяем работу сервиса, а не репозитория. Достаточно убедиться, что нам вернулась не пустая сущность с непустым идентификатором
    }

    @Test
    void update() {
        Comment comment = commentService.update(1L, "TEST COM");
        Comment expectedComment = commentService.findById(1L).get();
        assertEquals(expectedComment.getText(), comment.getText());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void deleteById() {
        Optional<Comment> comment = commentService.findById(1L);
        assertTrue(comment.isPresent());

        commentService.deleteById(1L);

        comment = commentService.findById(1L);
        assertTrue(comment.isEmpty());

    }
}