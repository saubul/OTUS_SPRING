package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto findById(Long id);

    List<BookDto> findAll();

    BookDto create(BookDto bookDto);

    BookDto update(BookDto bookDto);

    void deleteById(Long id);
}
