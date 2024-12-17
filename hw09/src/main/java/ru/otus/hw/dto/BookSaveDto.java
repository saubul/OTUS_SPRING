package ru.otus.hw.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BookSaveDto {

    private Long id;

    @NotNull
    @Size(min = 2)
    private String title;

    @NotNull
    private ItemDto author;

    @NotNull
    @NotEmpty
    private List<Long> genres;


    public BookSaveDto(String title, Long author, List<Long> genres) {
        this.title = title;
        this.author = new ItemDto(author, null);
        this.genres = genres;
    }

    public BookSaveDto() {
        this.author = new ItemDto();
        this.genres = new ArrayList<>();
    }

    public void setAuthor(Long id) {
        this.author = new ItemDto(id, null);
    }

    public void setGenres(String genres) {
        this.genres = Arrays.stream(genres.replaceAll("\\s+", "").split(","))
                .map(Long::valueOf).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ItemDto getAuthor() {
        return author;
    }

    public void setAuthor(ItemDto author) {
        this.author = author;
    }

    public List<Long> getGenres() {
        return genres;
    }

    public void setGenres(List<Long> genres) {
        this.genres = genres;
    }
}