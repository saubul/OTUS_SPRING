package ru.otus.services;


import ru.otus.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(String id);

    List<Comment> findAllByBookId(String id);

    Comment create(String text, String bookId);

    Comment update(String id, String text);

    void deleteById(String id);

}
