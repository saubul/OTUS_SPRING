package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.exceptions.EntityNotFoundException;
import ru.otus.models.Comment;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> findAllByBookId(String id) {
        return commentRepository.findAllByBookId(id);
    }

    @Override
    public Comment create(String text, String bookId) {
        Comment comment = new Comment();
        if (!"".equals(text)) {
            comment.setText(text);
        }
        if (bookId != null) {
            comment.setBook(bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId))));
        }
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(String id, String text) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
        if (!"".equals(text)) {
            comment.setText(text);
        }
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}
