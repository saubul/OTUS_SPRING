package ru.otus.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;

@Configuration
@EnableIntegration
public class IntegrationConfiguration {

    @Bean
    DirectChannel reportOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow getReportByClient() {
        return flow -> flow
                .channel("clientChannel")
                // Намеренное усложнение
                .handle("reportService", "fillClient")
                .handle("reportService", "fillAccounts")
                .handle("reportService", "fillTransfers");
    }

}
