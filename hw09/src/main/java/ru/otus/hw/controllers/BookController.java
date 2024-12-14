package ru.otus.hw.controllers;

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
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSaveDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    public String findAllBooks(Model model) {
        List<BookDto> bookDtos = bookService.findAll();
        model.addAttribute("books", bookDtos);
        return "bookList";
    }

    @GetMapping("/create")
    public String createBookPage(Model model) {
        model.addAttribute("book", new BookDto());
        return "createBook";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute("book") BookSaveDto bookDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/book/create?id=" + bookDto.getId();
        }
        bookService.create(bookDto);

        return "redirect:/book/";
    }

    @GetMapping("/update")
    public String updateBookPage(@RequestParam("id") Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "updateBook";
    }

    @PostMapping("/update")
    public String updateBook(@Valid @ModelAttribute("book") BookSaveDto bookDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "updateBook";
        }
        bookService.update(bookDto);
        return "redirect:/book/";
    }

    @PostMapping("/delete")
    public String deleteBook(@RequestParam("id") Long id) {
        bookService.deleteById(id);
        return "redirect:/book/";
    }
}
