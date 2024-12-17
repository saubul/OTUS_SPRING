package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.models.Comment;
import ru.otus.repositories.CommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void findAllByBookId() {
        List<Comment> commentList = commentRepository.findAllByBookId("1");
        List<Comment> expectedCommentList = List.of(commentRepository.findById("1").get(), commentRepository.findById("5").get());

        assertThat(commentList).usingRecursiveComparison().isEqualTo(expectedCommentList);
    }

}