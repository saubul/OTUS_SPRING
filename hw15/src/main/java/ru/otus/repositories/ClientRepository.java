package ru.otus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.models.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
