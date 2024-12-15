package ru.otus.hw.controllers.rest;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // В REST не должно быть глаголов, поэтому как бы "создаем" (POST)
    // фильтр и возвращаем данные, отфильтрованные с помощью него
    @PostMapping("/filters")
    public ResponseEntity<List<CommentDto>> findAllByBookId(@RequestBody BookIdFilter bookIdFilter) {
        List<CommentDto> commentDtos = commentService.findAllByBookId(bookIdFilter.getBookId());
        return ResponseEntity.ok(commentDtos);
    }

    @Data
    private static class BookIdFilter {
        private Long bookId;
    }

}
