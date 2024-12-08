package ru.otus.hw.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookEditDto;
import ru.otus.hw.services.BookService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @GetMapping("/")
    public String findAllBooks(Model model) {
        List<BookDto> bookDtos = bookService.findAll();
        model.addAttribute("books", bookDtos);
        return "bookList";
    }

    @GetMapping("/create")
    public String createBookPage(Model model) {
        model.addAttribute("book", new BookEditDto());
        return "createBook";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute("book") BookEditDto bookEditDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/book/edit?id=" + bookEditDto.getId();
        }
        bookService.create(bookEditDto.getTitle(), bookEditDto.getAuthorId(),
                Arrays.stream(bookEditDto.getGenresIds()
                                .replaceAll("\\s+", "")
                                .split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList()));

        return "redirect:/book/";
    }

    @GetMapping("/edit")
    public String editBookPage(@RequestParam("id") Long id, Model model) {
        BookDto bookDto = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with %id not found".formatted(id)));
        model.addAttribute("book", bookConverter.toEditDto(bookDto));
        return "editBook";
    }

    @PostMapping("/edit")
    public String editBook(@Valid @ModelAttribute("book") BookEditDto bookEditDto,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editBook";
        }
        bookService.update(bookEditDto.getId(), bookEditDto.getTitle(), bookEditDto.getAuthorId(),
                bookEditDto.getGenresIds() != null && !"".equals(bookEditDto.getGenresIds()) ?
                Arrays.stream(bookEditDto.getGenresIds()
                                .replaceAll("\\s+", "")
                                .split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList()) : Collections.emptyList());
        return "redirect:/book/";
    }

    @PostMapping("/delete")
    public String deleteBook(@RequestParam("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/book/";
    }
}
