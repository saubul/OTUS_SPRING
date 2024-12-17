package ru.otus.hw.services;

import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSaveDto;

import java.util.List;

public interface BookService {
    BookDto findById(Long id);

    List<BookDto> findAll();

    BookDto create(BookSaveDto bookDto);

    BookDto update(BookSaveDto bookDto);

    void deleteById(Long id);
}
