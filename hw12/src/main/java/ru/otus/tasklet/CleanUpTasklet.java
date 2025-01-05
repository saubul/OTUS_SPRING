package ru.otus.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.mongodb.core.MongoOperations;

public class CleanUpTasklet implements Tasklet {

    private final MongoOperations mongoOperations;

    public CleanUpTasklet(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        mongoOperations.getCollection("bookIdMappings").drop();
        return RepeatStatus.FINISHED;
    }

}
