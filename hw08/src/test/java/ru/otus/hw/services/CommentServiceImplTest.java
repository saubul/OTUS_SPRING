package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.models.Book;
import ru.otus.models.Comment;
import ru.otus.services.CommentService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureDataMongo
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findById() {
        Optional<Comment> comment = commentService.findById("1");
        assertTrue(comment.isPresent());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findAllByBookId() {
        List<Comment> commentList = commentService.findAllByBookId("1");
        assertEquals(2, commentList.size()); // Можно сделать как в BookServiceImplTest
    }

    @Test
    void create() {

        Comment comment = commentService.create("Comment_10", "1");
        assertThat(comment).matches(c -> c.getText().equals(comment.getText())); // и т.д. рекурсия не используется из-за неоднозначности ID
    }

    @Test
    void update() {
        Comment comment = commentService.update("1", "TEST COM");
        Comment expectedComment = commentService.findById("1").get();
        assertEquals(expectedComment.getText(), comment.getText());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void deleteById() {
        Optional<Comment> comment = commentService.findById("1");
        assertTrue(comment.isPresent());

        commentService.deleteById("1");

        comment = commentService.findById("1");
        assertTrue(comment.isEmpty());

    }
}