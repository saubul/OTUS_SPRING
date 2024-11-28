package ru.otus.hw.test.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

// Напоминание для себя: внутри @SpringBootTest - @ExtendWith({SpringExtension.class})!
// Без точного указания classes = {CsvQuestionDao.class} падает ошибка, что не найден бин для LocalizedIOService
// Это происходит потому, что здесь поднимается контекст с описание из класса TestApplicationConfiguration
// А там есть @Import(value = {TestServiceImpl.class}), который приводит к тому, что он пытается внедрить эту зависимость
// Этого опять-таки можно было избежать, но тогда бы я не так хорошо усвоил материал(!)
@SpringBootTest(classes = {CsvQuestionDao.class})
class CsvQuestionDaoTest {

    // Такое же специальное усложнения для более вдумчивого использования в будущем
//    @Import(value = {CsvQuestionDao.class})
//    @Configuration
//    static class TestConfiguration {
//    }

    @Autowired
    CsvQuestionDao csvQuestionDao;

    @MockBean
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