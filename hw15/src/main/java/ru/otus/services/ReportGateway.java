package ru.otus.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.models.Report;

@MessagingGateway
public interface ReportGateway {
    @Gateway(requestChannel = "clientChannel", replyChannel = "reportOutputChannel")
    Report getReportByClient(Long clientId);
}
