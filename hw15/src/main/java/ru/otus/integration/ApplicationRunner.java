package ru.otus.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.models.Report;
import ru.otus.services.ReportGateway;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {

    private final ReportGateway reportGateway;

    @Override
    public void run(String... args) {
        List<Report> reportList = reportGateway.getReportByClient(List.of(1L, 2L));
        System.out.println(reportList);
    }

}
