package ru.otus.hw.services;


import ru.otus.hw.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto findById(Long id);

    List<CommentDto> findAllByBookId(Long id);

    CommentDto create(CommentDto commentDto);

    CommentDto update(CommentDto commentDto);

    void deleteById(Long id);

}
