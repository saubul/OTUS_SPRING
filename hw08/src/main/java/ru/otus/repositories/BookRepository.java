package ru.otus.repositories;

import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    @NonNull
    List<Book> findAll();

    @NonNull
    Optional<Book> findById(@NonNull String id);
}
