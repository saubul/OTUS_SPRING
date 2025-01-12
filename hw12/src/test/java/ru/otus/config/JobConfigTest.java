package ru.otus.config;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.models.nosql.Author;
import ru.otus.models.nosql.Book;
import ru.otus.models.nosql.Comment;
import ru.otus.models.nosql.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.config.JobConfig.IMPORT_AUTHOR_JOB;

@SpringBootTest
@SpringBatchTest
class JobConfigTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testJob() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(IMPORT_AUTHOR_JOB);

        JobParameters parameters = new JobParametersBuilder()
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        List<Author> authors = mongoTemplate.findAll(Author.class);
        List<Book> books = mongoTemplate.findAll(Book.class);
        List<Genre> genres = mongoTemplate.findAll(Genre.class);
        List<Comment> comments = mongoTemplate.findAll(Comment.class);
        assertThat(authors).isNotEmpty();
        assertThat(books).isNotEmpty();
        assertThat(genres).isNotEmpty();
        assertThat(comments).isNotEmpty();
    }

}