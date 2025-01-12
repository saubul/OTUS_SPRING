package ru.otus.runners;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Properties;

import static ru.otus.config.JobConfig.IMPORT_AUTHOR_JOB;

@Component
@RequiredArgsConstructor
public class JobRunner implements ApplicationRunner {

    private final JobOperator jobOperator;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Properties properties = new Properties();
        properties.put("time", (new Date()).toString());
        jobOperator.start(IMPORT_AUTHOR_JOB, properties);
    }

}
