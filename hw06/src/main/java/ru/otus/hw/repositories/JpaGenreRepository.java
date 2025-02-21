package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JpaGenreRepository implements GenreRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Genre> findAll() {
        return entityManager.createQuery("select g from Genre g", Genre.class).getResultList();
    }

    @Override
    public List<Genre> findAllByIds(List<Long> ids) {
        return entityManager.createQuery("select g from Genre g where g.id in (:ids)", Genre.class)
                .setParameter("ids", ids)
                .getResultList();
    }

}
