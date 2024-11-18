package ru.otus.hw.test;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Import;
import ru.otus.hw.service.TestServiceImpl;

// Специальное "усложнение жизни", чтобы лучше вникнуть в тему. Можно было обойтись и без этого
// Для себя - тесты с аннотацией @SpringBootTest ищут вверх(!) класс с аннотацией @SpringBootConfiguration
// Также важно, чтобы пакет, в котором лежит класс с @SpringBootConfiguration для тестов был "ниже", чем основной с @SpringBootApplication
// Иначе - конфликт
@Import(value = {TestServiceImpl.class})
@SpringBootConfiguration
public class TestApplicationConfiguration {
}
