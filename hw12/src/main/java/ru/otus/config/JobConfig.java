package ru.otus.config;

import jakarta.persistence.EntityManagerFactory;
import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.models.relational.Author;
import ru.otus.models.relational.Book;
import ru.otus.models.relational.Comment;
import ru.otus.models.relational.Genre;
import ru.otus.tasklet.CleanUpTasklet;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class JobConfig {

    public static final String IMPORT_AUTHOR_JOB = "importAuthorJob";

    // Авторов тоже не должно быть так много, вдобавок храним только маппинг идентификаторов
    private final Map<Long, String> authorIdMap = new HashMap<>();

    // Можем позволить хранить в значение объект Genre,
    // так как таблица жанров скорее справочник
    private final Map<Long, ru.otus.models.nosql.Genre> genreMap = new HashMap<>();

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job importJob() {
        return new JobBuilder(IMPORT_AUTHOR_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(importAuthorsStep())
                .next(importGenresStep())
                .next(importBooksStep())
                .next(importCommentsStep())
                .next(cleanUpStep())
                .end()
                .build();
    }

    // Authors step

    @Bean
    public Step importAuthorsStep() {
        return new StepBuilder("importAuthorsStep", jobRepository)
                .<Author, ru.otus.models.nosql.Author>chunk(10, platformTransactionManager)
                .reader(authorItemReader())
                .processor(authorItemProcessor())
                .writer(authorItemWriter())
                .build();
    }

    @Bean
    public JpaCursorItemReader<Author> authorItemReader() {
        return new JpaCursorItemReaderBuilder<Author>()
                .name("AuthorJpaReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from Author a")
                .build();
    }

    @Bean
    public ItemProcessor<Author, ru.otus.models.nosql.Author> authorItemProcessor() {
        return inAuthor -> {
            ru.otus.models.nosql.Author outAuthor = new ru.otus.models.nosql.Author();

            String outId = UUID.randomUUID().toString();
            outAuthor.setId(outId);

            outAuthor.setFullName(inAuthor.getFullName());

            authorIdMap.put(inAuthor.getId(), outId);

            return outAuthor;
        };
    }

    @Bean
    public MongoItemWriter<ru.otus.models.nosql.Author> authorItemWriter() {
        return new MongoItemWriterBuilder<ru.otus.models.nosql.Author>()
                .collection("authors")
                .template(mongoOperations)
                .build();
    }

    // Genres step

    @Bean
    public Step importGenresStep() {
        return new StepBuilder("importGenresStep", jobRepository)
                .<Genre, ru.otus.models.nosql.Genre>chunk(10, platformTransactionManager)
                .reader(genreItemReader())
                .processor(genreItemProcessor())
                .writer(genreItemWriter())
                .build();
    }

    @Bean
    public JpaCursorItemReader<Genre> genreItemReader() {
        return new JpaCursorItemReaderBuilder<Genre>()
                .name("GenreJpaReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select g from Genre g")
                .build();
    }

    @Bean
    public ItemProcessor<Genre, ru.otus.models.nosql.Genre> genreItemProcessor() {
        return inGenre -> {
            ru.otus.models.nosql.Genre outGenre = new ru.otus.models.nosql.Genre();

            String outId = UUID.randomUUID().toString();
            outGenre.setId(outId);

            outGenre.setName(inGenre.getName());

            genreMap.put(inGenre.getId(), outGenre);

            return outGenre;
        };
    }

    @Bean
    public MongoItemWriter<ru.otus.models.nosql.Genre> genreItemWriter() {
        return new MongoItemWriterBuilder<ru.otus.models.nosql.Genre>()
                .collection("genres")
                .template(mongoOperations)
                .build();
    }

    // Books step
    @Bean
    public Step importBooksStep() {
        return new StepBuilder("importBooksStep", jobRepository)
                .<Book, ru.otus.models.nosql.Book>chunk(10, platformTransactionManager)
                .reader(bookItemReader())
                .processor(bookItemProcessor())
                .writer(bookItemWriter())
                .build();
    }

    @Bean
    public JpaCursorItemReader<Book> bookItemReader() {
        return new JpaCursorItemReaderBuilder<Book>()
                .name("BookJpaReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from Book b join fetch b.genres g")
                .build();
    }

    @Bean
    public ItemProcessor<Book, ru.otus.models.nosql.Book> bookItemProcessor() {
        return inBook -> {
            ru.otus.models.nosql.Book outBook = new ru.otus.models.nosql.Book();
            String outId = UUID.randomUUID().toString();
            outBook.setId(outId);
            outBook.setTitle(inBook.getTitle());
            outBook.setAuthor(
                    new ru.otus.models.nosql.Author(
                            authorIdMap.get(inBook.getAuthor().getId()),
                            inBook.getAuthor().getFullName())
            );
            outBook.setGenres(
                    inBook.getGenres().stream()
                            .map(genre -> genreMap.get(genre.getId()))
                            .collect(Collectors.toList())
            );
            // знаю, что коряво делать каждый раз insert
            mongoOperations.getCollection("bookIdMappings")
                    .insertOne(new Document("oldId", inBook.getId()).append("newId", outId));
            return outBook;
        };
    }

    @Bean
    public MongoItemWriter<ru.otus.models.nosql.Book> bookItemWriter() {
        return new MongoItemWriterBuilder<ru.otus.models.nosql.Book>()
                .collection("books")
                .template(mongoOperations)
                .build();
    }

    // Comments step
    @Bean
    public Step importCommentsStep() {
        return new StepBuilder("importCommentsStep", jobRepository)
                .<Comment, ru.otus.models.nosql.Comment>chunk(10, platformTransactionManager)
                .reader(commentItemReader())
                .processor(commentItemProcessor())
                .writer(commentItemWriter())
                .build();
    }

    @Bean
    public JpaCursorItemReader<Comment> commentItemReader() {
        return new JpaCursorItemReaderBuilder<Comment>()
                .name("CommentJpaReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select c from Comment c")
                .build();
    }

    @Bean
    public ItemProcessor<Comment, ru.otus.models.nosql.Comment> commentItemProcessor() {
        return inComment -> {
            ru.otus.models.nosql.Comment outComment = new ru.otus.models.nosql.Comment();

            String outId = UUID.randomUUID().toString();
            outComment.setId(outId);

            ru.otus.models.nosql.Book book = new ru.otus.models.nosql.Book();
            Document mapping = mongoOperations.getCollection("bookIdMappings")
                    .find(new Document("oldId", inComment.getBook().getId()))
                    .first();
            if (mapping != null) {
                String newBookId = mapping.getString("newId");
                book.setId(newBookId);
                outComment.setBook(book);
            }

            return outComment;
        };
    }

    @Bean
    public MongoItemWriter<ru.otus.models.nosql.Comment> commentItemWriter() {
        return new MongoItemWriterBuilder<ru.otus.models.nosql.Comment>()
                .collection("comments")
                .template(mongoOperations)
                .build();
    }

    @Bean
    public Step cleanUpStep() {
        return new StepBuilder("cleanUpStep", jobRepository)
                .tasklet(cleanUpTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet cleanUpTasklet() {
        return new CleanUpTasklet(mongoOperations);
    }

}
