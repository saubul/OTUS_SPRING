package ru.otus.hw.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.dto.ItemDto;
import ru.otus.hw.models.Author;

@Component
public class AuthorMapper implements ItemMapper<Author> {
    public ru.otus.hw.dto.AuthorDto toDto(Author author) {
        return new ru.otus.hw.dto.AuthorDto(author.getId(), author.getFullName());
    }

    @Override
    public ItemDto toItem(Author author) {
        if (author == null) {
            return null;
        }
        return new ItemDto(author.getId(), author.getFullName());
    }
}
