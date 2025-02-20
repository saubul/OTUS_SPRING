package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.models.Transfer;
import ru.otus.repositories.TransferRepository;

import java.util.List;

@Service("transferService")
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;

    public List<Transfer> getAllTransfersByAccountIdFromList(List<Long> accountIdFromList) {
        return transferRepository.findByAccountIdFromIn(accountIdFromList);
    }

}
