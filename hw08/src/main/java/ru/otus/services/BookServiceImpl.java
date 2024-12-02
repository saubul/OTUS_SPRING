package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.exceptions.EntityNotFoundException;
import ru.otus.models.Book;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

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

}
