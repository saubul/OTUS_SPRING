package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        questionDao.findAll().forEach(question -> {
            List<Answer> questionAnswers = question.answers();
            if (questionAnswers != null) {
                questionAnswers = questionAnswers.stream().filter(Objects::nonNull).toList();
            }
            AtomicInteger counter = new AtomicInteger(1);
            ioService.printLine(String.format("Question: %s%s", question.text(),
                    CollectionUtils.isNotEmpty(questionAnswers) ? " Possible answers: \n" +
                            questionAnswers.stream()
                                    .map(Answer::text)
                                    .filter(StringUtils::isNotBlank)
                                    .map(answerText -> counter.getAndIncrement() + ")" + answerText)
                                    .collect(Collectors.joining("\n")) : ""));
        });

    }
}
