package ru.otus.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import ru.otus.services.ReportService;

@Configuration
@EnableIntegration
public class IntegrationConfiguration {

    @Autowired
    private ReportService reportService;

    @Bean
    DirectChannel reportOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow getReportsByClients() {
        return flow -> flow
                .channel("clientChannel")
                // Намеренное усложнение
                .split()
                .handle(reportService, "fillClient")
                .handle(reportService, "fillAccounts")
                .handle(reportService, "fillTransfers")
                .aggregate();
    }

}
