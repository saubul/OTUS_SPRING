package ru.otus.hw.test.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
class TestServiceImplTest {

    @Autowired
    TestServiceImpl testService;

    @MockBean
    LocalizedIOService ioService;

    @MockBean
    QuestionDao questionDao;

    @BeforeEach
    void init() {
        when(questionDao.findAll()).thenReturn(
                List.of(new Question("Test question",
                        List.of(
                                new Answer("right answer", true),
                                new Answer("wrong answer", false)
                        )))
        );
    }

    @Test
    void executeTestWithSuccess() {
        when(ioService.getMessage(anyString(), anyInt(), anyInt())).thenReturn("");
        when(ioService.readIntForRange(anyInt(), anyInt(), anyString())).thenReturn(1);

        Assertions.assertEquals(1, testService.executeTestFor(new Student("test", "test")).getRightAnswersCount());

    }

    @Test
    void executeTestWithFail() {

        when(ioService.getMessage(anyString(), anyInt(), anyInt())).thenReturn("");
        when(ioService.readIntForRange(anyInt(), anyInt(), anyString())).thenReturn(2);

        Assertions.assertEquals(0, testService.executeTestFor(new Student("test", "test")).getRightAnswersCount());

    }
}