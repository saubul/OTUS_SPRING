package ru.otus.hw.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDto>> findAllBooks() {
        List<BookDto> bookDtos = bookService.findAll();
        return ResponseEntity.ok(bookDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findById(@PathVariable("id") Long bookId) {
        BookDto bookDto = bookService.findById(bookId);
        return ResponseEntity.ok(bookDto);
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody BookDto bookDto) {
        return ResponseEntity.status(201).body(bookService.create(bookDto));
    }

    @PutMapping
    public ResponseEntity<BookDto> updateBook(@RequestBody BookDto bookDto) {
        return ResponseEntity.ok(bookService.update(bookDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok("OK");
    }
}
