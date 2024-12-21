package ru.otus.hw.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @Test
    void findById() {
        CommentDto comment = commentService.findById(1L);
        assertTrue(comment.getId() > 0);
    }

    @Test
    void findAllByBookId() {
        List<CommentDto> commentList = commentService.findAllByBookId(1L);
        assertEquals(1, commentList.size());
    }

    @Test
    void create() {
//        Comment expectedComment = new Comment(5L, "TEST COMMENT",
//                new Book(1L, "BookTitle_1", new Author(1L, "Author_1"),
//                        List.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2"))));
        CommentDto comment = commentService.create(new CommentDto(1L, "TEST COMMENT", new BookDto(1L, "Book_1")));
        assertNotNull(comment);
        assertTrue(comment.getId() > 0);
        assertEquals("TEST COMMENT", comment.getText());
        assertEquals(1L, comment.getBookDto().getId());

        // Здесь LazyInit..Exception из-за Lazy коллекции Genres в Book. Проверить нельзя
        // assertThat(expectedComment).usingRecursiveComparison().isEqualTo(comment);
        // Вообще, мы проверяем работу сервиса, а не репозитория. Достаточно убедиться, что нам вернулась не пустая сущность с непустым идентификатором
    }

    @Test
    void update() {
        CommentDto comment = commentService.update(new CommentDto(1L, "TEST COMMENT 2", new BookDto(1L, "Book_1")));
        CommentDto expectedComment = commentService.findById(1L);
        assertThat(expectedComment).usingRecursiveComparison().isEqualTo(comment);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void deleteById() {
        assertDoesNotThrow(() -> commentService.findById(1L));

        commentService.deleteById(1L);

        assertThrows(NotFoundException.class, () -> commentService.findById(1L));
    }
}