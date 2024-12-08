package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findById(Long id) {
        return commentService.findById(id).map(commentConverter::commentToString)
                .orElse("Comment with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Find comments by book id", key = "cbbid")
    public String findAllByBookId(Long id) {
        return commentService.findAllByBookId(id).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert comment to book", key = "cins")
    public String createCommentToBook(String text, Long bookId) {
        return commentConverter.commentToString(commentService.create(text, bookId));
    }

    @ShellMethod(value = "Update comment", key = "cupd")
    public String createCommentToBook(Long id, String text, Long bookId) {
        return commentConverter.commentToString(commentService.update(id, text));
    }

    @ShellMethod(value = "Delete comment", key = "cdel")
    public String createCommentToBook(Long id) {
        commentService.deleteById(id);
        return "Comment with id %d deleted".formatted(id);
    }


}
