package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            ioService.printLineLocalized("TestService.question");
            ioService.printLine(question.text());
            ioService.printLineLocalized("TestService.answer.possible");
            AtomicInteger counter = new AtomicInteger(1);
            List<Answer> questionAnswers = question.answers();
            ioService.printLine(questionAnswers.stream()
                    .map(Answer::text)
                    .filter(StringUtils::isNotBlank)
                    .map(answerText -> counter.getAndIncrement() + ")" + answerText)
                    .collect(Collectors.joining("\n")));
            int studentAnswerNumber = ioService.readIntForRange(1, questionAnswers.size(),
                    ioService.getMessage("TestService.answer.wrong.number", 1, questionAnswers.size()));
            boolean isAnswerValid = questionAnswers.get(studentAnswerNumber - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

}
