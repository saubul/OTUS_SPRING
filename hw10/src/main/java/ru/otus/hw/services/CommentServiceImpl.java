package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    public CommentDto findById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id \"" + id + "\" not found"));
        return commentMapper.toDto(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findAllByBookId(Long id) {
        return commentRepository.findAllByBookId(id).stream().map(commentMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto create(CommentDto commentDto) {
        Comment comment = new Comment();
        if (!"".equals(commentDto.text())) {
            comment.setText(commentDto.text());
        }
        Long bookId = commentDto.book().getId();
        if (bookId != null) {
            comment.setBook(bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("Book with id \"" + bookId + "\" not found")));
        }
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto update(CommentDto commentDto) {
        Long commentId = commentDto.id();
        if (commentId == null) {
            throw new IllegalArgumentException("Updatable comment id not provided");
        }
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id \"" + commentId + "\" not found"));
        if (!"".equals(commentDto.text())) {
            comment.setText(commentDto.text());
        }
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
