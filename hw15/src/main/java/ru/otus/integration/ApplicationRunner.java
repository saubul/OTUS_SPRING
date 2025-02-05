package ru.otus.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.otus.models.Report;
import ru.otus.services.ReportGateway;

@Component
@RequiredArgsConstructor
public class ApplicationRunner implements CommandLineRunner {

    private final ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        Report report = applicationContext.getBean(ReportGateway.class).getReportByClient(1L);
        System.out.println(report);
    }

}
