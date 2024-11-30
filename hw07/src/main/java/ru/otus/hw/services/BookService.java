package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(Long id);

    List<BookDto> findAll();

    BookDto create(String title, Long authorId, List<Long> genreIds);

    BookDto update(Long id, String title, Long authorId, List<Long> genreIds);

    void deleteById(Long id);
}
