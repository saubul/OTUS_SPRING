package ru.otus.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.converters.CommentConverter;
import ru.otus.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findById(String id) {
        return commentService.findById(id).map(commentConverter::commentToString)
                .orElse("Comment with id %s not found".formatted(id));
    }

    @ShellMethod(value = "Find comments by book id", key = "cbbid")
    public String findAllByBookId(String id) {
        return commentService.findAllByBookId(id).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Insert comment to book", key = "cins")
    public String createCommentToBook(String text, String bookId) {
        return commentConverter.commentToString(commentService.create(text, bookId));
    }

    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateCommentToBook(String id, String text) {
        return commentConverter.commentToString(commentService.update(id, text));
    }

    @ShellMethod(value = "Delete comment", key = "cdel")
    public String deleteCommentById(String id) {
        commentService.deleteById(id);
        return "Comment with id %s deleted".formatted(id);
    }


}
