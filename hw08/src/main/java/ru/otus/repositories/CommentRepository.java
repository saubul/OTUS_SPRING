package ru.otus.repositories;


import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.models.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    @NonNull
    List<Comment> findAllByBookId(@NonNull String book);

}
