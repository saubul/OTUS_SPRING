package ru.otus.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.models.Report;

import java.util.List;

@MessagingGateway
public interface ReportGateway {
    @Gateway(requestChannel = "clientChannel", replyChannel = "reportOutputChannel")
    List<Report> getReportByClient(List<Long> clientIdList);
}
