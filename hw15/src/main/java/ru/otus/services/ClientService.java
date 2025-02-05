package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.repositories.ClientRepository;

@Service("clientService")
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

}
