package ru.otus.hw;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.models.Account;
import ru.otus.models.Report;
import ru.otus.models.Transfer;
import ru.otus.services.ReportGateway;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class IntegrationTest {

    @Autowired
    ReportGateway reportGateway;

    @Test
    void integrationTest() {
        List<Report> reportList = reportGateway.getReportByClient(List.of(1L));
        assertEquals(1, reportList.size());
        Report report = reportList.get(0);
        Report expectReport = new Report();
        expectReport.setClientId(1L);
        expectReport.setClientAccounts(List.of(new Account(1L, "40817810100010000001", new BigDecimal("100"), 1L)));
        expectReport.setClientTransfers(List.of(new Transfer(1L, 1L, 2L, new BigDecimal("100"))));
        assertEquals(expectReport, report);
    }

}
