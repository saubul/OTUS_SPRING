package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id, Map.of(FETCH.getKey(),
                entityManager.getEntityGraph("book-author-entity-graph"))));
    }

    @Override
    public List<Book> findAll() {
        return entityManager.createQuery("select b from Book b", Book.class)
                .setHint(FETCH.getKey(), entityManager.getEntityGraph("book-author-entity-graph"))
                .getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void deleteById(Long id) {
        Book book = entityManager.find(Book.class, id);
        entityManager.remove(book);
    }

}
