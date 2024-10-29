package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.security.StudentLoginContext;

@Component
@RequiredArgsConstructor
public class TestRunnerServiceImpl implements TestRunnerService {

    private final TestService testService;

    private final ResultService resultService;

    private final StudentLoginContext studentLoginContext;

    public void run() {
        var testResult = testService.executeTestFor(studentLoginContext.getCurrentStudent());
        resultService.showResult(testResult);
    }

}
