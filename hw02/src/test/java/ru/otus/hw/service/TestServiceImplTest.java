package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @InjectMocks
    TestServiceImpl testService;

    @Mock
    StreamsIOService ioService;

    @Mock
    CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void init() {
        when(csvQuestionDao.findAll()).thenReturn(
                List.of(new Question("Test question",
                        List.of(
                                new Answer("right answer", true),
                                new Answer("wrong answer", false)
                        )))
        );
    }

    @Test
    void executeTestWithSuccess() {

        when(ioService.readStringWithPrompt(anyString())).thenReturn("right answer");

        Assertions.assertEquals(1, testService.executeTestFor(new Student("test","test")).getRightAnswersCount());

    }

    @Test
    void executeTestWithFail() {

        when(ioService.readStringWithPrompt(anyString())).thenReturn("wrong answer");

        Assertions.assertEquals(0, testService.executeTestFor(new Student("test","test")).getRightAnswersCount());

    }
}