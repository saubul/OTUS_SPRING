package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.models.Account;
import ru.otus.repositories.AccountRepository;

import java.util.List;

@Service("accountService")
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> getAllAccountsByClientId(Long clientId) {
        return accountRepository.findByClientId(clientId);
    }

}
