package ru.otus.hw.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.Availability;
import org.springframework.shell.AvailabilityProvider;
import ru.otus.hw.security.StudentLoginContext;
import ru.otus.hw.service.LocalizedIOService;

@Configuration
public class ShellConfiguration {

    @Autowired
    private LocalizedIOService localizedIOService;

    @Bean
    public AvailabilityProvider runTestCommandAvailabilityProvider(StudentLoginContext studentLoginContext) {
        return () -> studentLoginContext.isStudentLoggedIn() ? Availability.available() :
                Availability.unavailable(localizedIOService.getMessage("Shell.Unavailable.reason"));
    }

}
