package ru.otus.hw.controllers.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<BookDto> findAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public BookDto findById(@PathVariable("id") Long bookId) {
        return bookService.findById(bookId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public BookDto createBook(@RequestBody BookCreateDto bookDto) {
        return bookService.create(bookDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public BookDto updateBook(@RequestBody @Valid BookUpdateDto bookDto) {
        return bookService.update(bookDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }
}
