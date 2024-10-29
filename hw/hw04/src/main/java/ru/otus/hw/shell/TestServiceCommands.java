package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.CommandAvailability;
import ru.otus.hw.security.StudentLoginContext;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;

@Command(group = "OTUS Test")
@RequiredArgsConstructor
public class TestServiceCommands {

    private final TestRunnerService testRunnerService;

    private final StudentLoginContext studentLoginContext;

    private final StudentService studentService;

    private final LocalizedIOService localizedIOService;

    @Command(command = "start", alias = "s")
    @CommandAvailability(provider = "runTestCommandAvailabilityProvider")
    public String start() {
        testRunnerService.run();
        return localizedIOService.getMessage("Shell.Test.completed");
    }

    @Command(command = "login", alias = "l")
    public String login() {
        studentLoginContext.login(studentService.determineCurrentStudent());
        return localizedIOService.getMessage("Shell.Login.success", studentLoginContext.getCurrentStudent().getFullName());
    }

}
