package ru.otus.services;


import ru.otus.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<Author> findAll();

    Optional<Author> findById(String authorId);

}
