package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @InjectMocks
    TestServiceImpl testService;

    @Mock
    StreamsIOService ioService;

    @Mock
    CsvQuestionDao csvQuestionDao;


    @Test
    void executeTest() {
        when(csvQuestionDao.findAll()).thenReturn(
                List.of(new Question("Test question",
                        List.of(
                                new Answer("right answer", true),
                                new Answer("wrong answer", false)
                        )))
        );

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        assertDoesNotThrow(() -> testService.executeTest());

        verify(ioService, times(2)).printLine(stringArgumentCaptor.capture());
        assertEquals("""
                Question: Test question Possible answers:\s
                1)right answer
                2)wrong answer""", stringArgumentCaptor.getAllValues().get(1));
    }
}