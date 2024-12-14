package ru.otus.hw.dto;

public record CommentDto(Long id, String text, ItemDto book) {
    public CommentDto(String text, Long bookId) {
        this(null, text, new ItemDto(bookId, null));
    }

    public CommentDto(Long id, String text) {
        this(id, text, null);
    }
}
