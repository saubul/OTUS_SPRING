package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Comment> findAllByBookId(Long id) {
        return commentRepository.findAllByBookId(id);
    }

    @Transactional
    @Override
    public Comment create(String text, Long bookId) {
        Comment comment = new Comment();
        if (!"".equals(text)) {
            comment.setText(text);
        }
        if (bookId != null) {
            comment.setBook(bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId))));
        }
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment update(Long id, String text) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        if (!"".equals(text)) {
            comment.setText(text);
        }
        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
