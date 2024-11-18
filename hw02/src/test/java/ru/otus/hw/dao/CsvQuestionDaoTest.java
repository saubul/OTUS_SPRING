package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    @InjectMocks
    CsvQuestionDao csvQuestionDao;

    @Mock
    TestFileNameProvider testFileNameProvider;

    @Test
    void findAllSuccess() {
        when(testFileNameProvider.getTestFileName()).thenReturn("testQuestions.csv");
        List<Question> questionList = csvQuestionDao.findAll();
        assertEquals(3, questionList.size());
        assertEquals("Is there life on Mars?", questionList.get(0).text());
        assertEquals("How should resources be loaded form jar in Java?", questionList.get(1).text());
        assertEquals("Which option is a good way to handle the exception?", questionList.get(2).text());
    }

    @Test
    void findAllFailWithNonExistingResource() {
        when(testFileNameProvider.getTestFileName()).thenReturn("qwerty.csv");
        assertThrows(QuestionReadException.class, () -> csvQuestionDao.findAll());
    }
}