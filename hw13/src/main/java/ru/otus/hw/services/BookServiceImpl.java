package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    @Transactional(readOnly = true)
    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id \"" + id + "\" not found"));
        return bookMapper.toDto(book);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream().map(bookMapper::toDto).toList();
    }

    @Transactional
    @Override
    public BookDto create(BookCreateDto bookCreateDto) {
        Book book = new Book();
        book.setTitle(bookCreateDto.getTitle());
        book.setAuthor(authorRepository.findById(bookCreateDto.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author with id \"" +
                        bookCreateDto.getAuthorId() + "\" not found")));
        book.setGenres(genreRepository.findAllByIdIn(bookCreateDto.getGenresIdList()));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public BookDto update(BookUpdateDto bookUpdateDto) {
        Book book = bookRepository.findById(bookUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id \"" + bookUpdateDto.getId() + "\" not found"));
        // Проверка допускается, поскольку действительно могут прислать пустой заголовок.
        // А заставлять пользователя в обязательном порядке устанавливать поле заголовка,
        // как и других полей, не корректно
        if (!"".equals(bookUpdateDto.getTitle())) {
            book.setTitle(bookUpdateDto.getTitle());
        }
        if (bookUpdateDto.getAuthorId() != null) {
            book.setAuthor(authorRepository.findById(bookUpdateDto.getAuthorId())
                    .orElseThrow(() -> new NotFoundException("Author with id \"" +
                            bookUpdateDto.getAuthorId() + "\" not found")));
        }
        List<Long> genreIds = bookUpdateDto.getGenreIdList();
        if (genreIds.size() != 0) {
            book.setGenres(genreRepository.findAllByIdIn(genreIds));
        }
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

}
