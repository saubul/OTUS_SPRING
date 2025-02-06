package ru.otus.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;

    private BigDecimal balance;

    // Для "усложнения" скажем, что есть некое разделение на модуль клиентов и счетов
    // Соответственно модель данных может быть у них "разделена" и использовать ссылочный тип нельзя
    @Column(name = "client_id")
    private Long clientId;

}
