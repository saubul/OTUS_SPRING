package ru.otus.hw.controllers.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/genres")
public class PageGenreController {

    @GetMapping
    public String genres() {
        return "genreList";
    }

}
