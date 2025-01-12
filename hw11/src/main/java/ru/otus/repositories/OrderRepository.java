package ru.otus.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.entities.Order;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, String> {

}
