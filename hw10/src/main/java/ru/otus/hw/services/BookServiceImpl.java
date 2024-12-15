package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.ItemDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

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
    public BookDto create(BookDto bookDto) {
        Book book = new Book();
        if (!"".equals(bookDto.getTitle())) {
            book.setTitle(bookDto.getTitle());
        }
        Long authorId = bookDto.getAuthor().getId();
        if (authorId != null) {
            book.setAuthor(authorRepository.findById(authorId)
                    .orElseThrow(() -> new NotFoundException("Author with id \"" + authorId + "\" not found")));
        }
        List<Long> genreIds = bookDto.getGenres().stream().map(ItemDto::getId).collect(Collectors.toList());
        if (genreIds.size() != 0) {
            book.setGenres(genreRepository.findAllByIdIn(genreIds));
        }
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional
    @Override
    public BookDto update(BookDto bookDto) {
        Book book = bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id \"" + bookDto.getId() + "\" not found"));
        if (!"".equals(bookDto.getTitle())) {
            book.setTitle(bookDto.getTitle());
        }
        ItemDto author = bookDto.getAuthor();
        if (author != null && author.getId() != null) {
            book.setAuthor(authorRepository.findById(author.getId())
                    .orElseThrow(() -> new NotFoundException("Author with id \"" + author.getId() + "\" not found")));
        }
        List<Long> genreIds = bookDto.getGenres().stream().map(ItemDto::getId).collect(Collectors.toList());
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
