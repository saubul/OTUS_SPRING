package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    private String label;

    public ItemDto(Long id) {
        this(id, null);
    }

}
