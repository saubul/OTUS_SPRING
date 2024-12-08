package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    private final GenreRepository genreRepository;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(namedParameterJdbcOperations.query(
                "select b.id as book_id, b.title as book_title, b.author_id, " +
                        "a.full_name as author_full_name, " +
                        "g.id as genre_id, g.name as genre_name " +
                        "from books b " +
                        "join authors a on b.author_id = a.id " +
                        "join books_genres bg on b.id = bg.book_id " +
                        "join genres g on g.id = bg.genre_id " +
                        "where b.id = :id",
                Map.of("id", id),
                new BookResultSetExtractor())
        );
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcOperations.update("delete from books where id = :id",
                Map.of("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        return namedParameterJdbcOperations.query(
                "select b.id, b.title, b.author_id, a.full_name " +
                        "from books b " +
                        "join authors a on b.author_id = a.id", new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return namedParameterJdbcOperations.query(
                "select book_id, genre_id from books_genres",
                new BookGenreRelationRowMapper());
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Book> bookMap = booksWithoutGenres.stream()
                .collect(Collectors.toMap(Book::getId, Function.identity()));
        Map<Long, Genre> genreMap = genres.stream()
                .collect(Collectors.toMap(Genre::getId, Function.identity()));
        relations.forEach(relation -> {
            bookMap.get(relation.bookId).getGenres().add(genreMap.get(relation.genreId));
        });
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcOperations.update("insert into books(title, author_id) values(:title, :authorId)",
                new MapSqlParameterSource(Map.of("title", book.getTitle(), "authorId", book.getAuthor().getId())),
                keyHolder);

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        int rowsAffectedCount = namedParameterJdbcOperations.update(
                "update books set title = :title, author_id = :authorId where id = :id",
                Map.of("title", book.getTitle(),
                        "authorId", book.getAuthor().getId(),
                        "id", book.getId()));

        if (rowsAffectedCount == 0) {
            throw new EntityNotFoundException("Book with id %d not found.".formatted(book.getId()));
        }
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        namedParameterJdbcOperations.batchUpdate(
                "insert into books_genres(book_id, genre_id) values (:bookId, :genreId)",
                SqlParameterSourceUtils.createBatch(
                        book.getGenres().stream()
                                .map(genre -> new BookGenreRelation(book.getId(), genre.getId()))
                                .collect(Collectors.toList())
                ));
    }

    private void removeGenresRelationsFor(Book book) {
        namedParameterJdbcOperations.update("delete from books_genres where book_id = :bookId",
                Map.of("bookId", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("id");
            String title = rs.getString("title");
            long authorId = rs.getLong("author_id");
            String authorFullName = rs.getString("full_name");
            return new Book(bookId, title, new Author(authorId, authorFullName), new ArrayList<>());
        }
    }

    // Использовать для findById
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (!rs.isBeforeFirst()) {
                return null;
            }
            long bookId = 0;
            String bookTitle = null;
            Author author = null;
            List<Genre> genres = new ArrayList<>();
            while (rs.next()) {
                if (bookId == 0) {
                    bookId = rs.getLong("book_id");
                }
                if (bookTitle == null) {
                    bookTitle = rs.getString("book_title");
                }
                if (author == null) {
                    author = new Author(rs.getLong("author_id"), rs.getString("author_full_name"));
                }
                genres.add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
            }
            return new Book(bookId, bookTitle, author, genres);
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }

    private static class BookGenreRelationRowMapper implements RowMapper<BookGenreRelation> {
        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            long bookId = rs.getLong("book_id");
            long genreId = rs.getLong("genre_id");
            return new BookGenreRelation(bookId, genreId);
        }
    }
}
