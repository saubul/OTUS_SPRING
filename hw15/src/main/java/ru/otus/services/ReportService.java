package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.models.Account;
import ru.otus.models.Report;

import java.util.stream.Collectors;

@Service("reportService")
@RequiredArgsConstructor
public class ReportService {

    private final AccountService accountService;

    private final TransferService transferService;

    public Report fillClient(Long clientId) {
        Report report = new Report();
        report.setClientId(clientId);
        return report;
    }

    public Report fillAccounts(Report report) {
        report.setClientAccounts(accountService.getAllAccountsByClientId(report.getClientId()));
        return report;
    }

    public Report fillTransfers(Report report) {
        report.setClientTransfers(transferService.getAllTransfersByAccountIdFromList(report.getClientAccounts()
                .stream().map(Account::getId).collect(Collectors.toList())));
        return report;
    }

}
