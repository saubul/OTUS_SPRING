package ru.otus.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    private Long clientId;

    private List<Account> clientAccounts;

    private List<Transfer> clientTransfers;

}
