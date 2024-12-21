package ru.otus.hw.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookUpdateDto {

    @NotNull
    private Long id;

    private String title;

    private Long authorId;

    private List<Long> genreIdList;

}
