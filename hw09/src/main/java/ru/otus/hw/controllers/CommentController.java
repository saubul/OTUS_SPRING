package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @GetMapping("")
    public String findAllByBookId(@RequestParam("bookId") Long bookId, Model model) {
        List<CommentDto> commentDtos = commentService.findAllByBookId(bookId).stream()
                .map(commentConverter::convertToDto).toList();
        model.addAttribute("comments", commentDtos);
        return "commentList";
    }

}
