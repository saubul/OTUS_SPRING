package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.models.Comment;

@Component
@RequiredArgsConstructor
public class CommentConverter {

    private final BookConverter bookConverter;

    public String commentToString(CommentDto comment) {
        return "Id: %s, Text: %s, Book: %d".formatted(comment.id(), comment.text(), comment.book().getId());
    }

    public CommentDto convertToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(), bookConverter.toDTO(comment.getBook()));
    }

}
