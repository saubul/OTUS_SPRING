package ru.otus.hw.controllers.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class PageBookController {

    @GetMapping
    public String books() {
        return "bookList";
    }

    @GetMapping("/create")
    public String createBookPage() {
        return "createBook";
    }

    @GetMapping("/update/{id}")
    public String updateBookPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("bookId", id);
        return "updateBook";
    }

}
