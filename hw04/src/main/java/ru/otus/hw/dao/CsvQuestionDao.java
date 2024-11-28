package ru.otus.hw.dao;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName())) {
            Objects.requireNonNull(in);
            return new CsvToBeanBuilder<QuestionDto>(
                    new CSVReaderBuilder(new InputStreamReader(in))
                            .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                            .withSkipLines(1)
                            .build())
                    .withType(QuestionDto.class)
                    .build()
                    .parse()
                    .stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();
        } catch (Exception e) {
            throw new QuestionReadException(
                    String.format("При попытке прочитать данные из CSV-файла \"%s\" произошла ошибка: %s",
                            fileNameProvider.getTestFileName(), e.getMessage()), e);
        }
    }
}
