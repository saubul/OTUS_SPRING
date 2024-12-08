package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.converters.AuthorConverter;
import ru.otus.hw.services.AuthorService;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private final AuthorConverter authorConverter;

    @GetMapping("/")
    public String findAllAuthors(Model model) {
        model.addAttribute("authors", authorService.findAll().stream()
                .map(authorConverter::convertToDto).collect(Collectors.toList()));
        return "authorList";
    }
}
