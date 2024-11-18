package ru.otus.hw.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    private final BookConverter bookConverter;

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> findById(Long id) {
        return bookRepository.findById(id).map(bookConverter::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookConverter::toDTO).toList();
    }

    @Transactional
    @Override
    public BookDto create(String title, Long authorId, List<Long> genreIds) {
        Book book = new Book();
        if (!"".equals(title)) {
            book.setTitle(title);
        }
        if (authorId != null) {
            book.setAuthor(authorRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId))));
        }
        if (genreIds != null) {
            book.setGenres(genreRepository.findAllByIds(genreIds));
        }
        return bookConverter.toDTO(bookRepository.save(book));
    }

    @Transactional
    @Override
    public BookDto update(Long id, String title, Long authorId, List<Long> genreIds) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        if (!"".equals(title)) {
            book.setTitle(title);
        }
        if (authorId != null) {
            book.setAuthor(authorRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId))));
        }
        if (genreIds != null) {
            book.setGenres(genreRepository.findAllByIds(genreIds));
        }
        return bookConverter.toDTO(bookRepository.save(book));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

}
