package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.exceptions.EntityNotFoundException;
import ru.otus.models.Book;
import ru.otus.models.Comment;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.CommentRepository;
import ru.otus.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book create(String title, String authorId, List<String> genreIds) {
        Book book = new Book();
        if ("".equals(title)) {
            throw new IllegalArgumentException("Book title can't be empty");
        }
        if ("".equals(authorId)) {
            throw new IllegalArgumentException("Book author id can't be empty");
        }
        if (genreIds == null || genreIds.size() == 0) {
            throw new IllegalArgumentException("Book genres ids can't be empty");
        }
        book.setTitle(title);
        book.setAuthor(authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId))));
        book.setGenres(genreRepository.findAllByIdIn(genreIds));
        return bookRepository.save(book);
    }

    @Override
    public Book update(String id, String title, String authorId, List<String> genreIds) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        if (!"".equals(title)) {
            book.setTitle(title);
        }
        if (authorId != null) {
            book.setAuthor(authorRepository.findById(authorId)
                    .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId))));
        }
        if (genreIds != null) {
            book.setGenres(genreRepository.findAllByIdIn(genreIds));
        }
        return bookRepository.save(book);
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    @Component
    private static class BookCascadeDeleteMongoEventListener extends AbstractMongoEventListener<Book> {

        private final CommentRepository commentRepository;

        private BookCascadeDeleteMongoEventListener(CommentRepository commentRepository) {
            this.commentRepository = commentRepository;
        }

        @Override
        public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
            commentRepository.deleteAllById(
                    commentRepository.findAllByBookId((String) event.getSource().get("_id"))
                            .stream()
                            .map(Comment::getId)
                            .collect(Collectors.toList())
            );
        }
    }

}
