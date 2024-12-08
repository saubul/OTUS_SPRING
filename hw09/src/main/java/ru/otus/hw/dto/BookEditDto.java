package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookEditDto {

    private Long id;

    @NotBlank(message = "Title can't be empty")
    @Size(min = 3, message = "Title size must be more than 3")
    private String title;

    private Long authorId;

    private String genresIds;

}
