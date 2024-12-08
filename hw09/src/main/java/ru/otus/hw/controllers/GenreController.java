package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.services.GenreService;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/genre")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @GetMapping("/")
    public String findAllGenres(Model model) {
        model.addAttribute("genres", genreService.findAll().stream()
                .map(genreConverter::convertToDto).collect(Collectors.toList()));
        return "genreList";
    }

}
