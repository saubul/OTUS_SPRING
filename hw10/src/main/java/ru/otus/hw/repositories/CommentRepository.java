package ru.otus.hw.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @NonNull
    List<Comment> findAllByBookId(@NonNull Long book);

    @NonNull
    List<Comment> findAllByIdIn(@NonNull List<Long> ids);

}
