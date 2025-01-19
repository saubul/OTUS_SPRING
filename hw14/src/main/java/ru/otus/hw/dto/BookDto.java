package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDto {

    private Long id;

    private String title;

    private AuthorDto author;

    private List<GenreDto> genres;

    public BookDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

}