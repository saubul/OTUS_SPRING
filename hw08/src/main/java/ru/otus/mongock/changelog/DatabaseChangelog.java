package ru.otus.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.models.Book;
import ru.otus.models.Comment;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.CommentRepository;
import ru.otus.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "000", id = "dropDB", author = "nrr", runAlways = true)
    public void dropDB(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "001", id = "insertGenres", author = "nrr")
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> genres = db.getCollection("genres");
        Document genre1 = new Document().append("_id", "1").append("name", "Genre_1");
        Document genre2 = new Document().append("_id", "2").append("name", "Genre_2");
        Document genre3 = new Document().append("_id", "3").append("name", "Genre_3");
        genres.insertMany(List.of(genre1, genre2, genre3));
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "nrr")
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> genres = db.getCollection("authors");
        Document author1 = new Document().append("_id", "1").append("fullName", "Author_1");
        Document author2 = new Document().append("_id", "2").append("fullName", "Author_2");
        Document author3 = new Document().append("_id", "3").append("fullName", "Author_3");
        genres.insertMany(List.of(author1, author2, author3));
    }

    @ChangeSet(order = "003", id = "insertBooks", author = "nrr")
    public void insertBooks(BookRepository bookRepository,
                            AuthorRepository authorRepository,
                            GenreRepository genreRepository) {
        Book book = new Book("1", "Book_1", authorRepository.findById("2").get(),
                List.of(genreRepository.findById("2").get(), genreRepository.findById("1").get()));
        Book book2 = new Book("2", "Book_2", authorRepository.findById("1").get(),
                List.of(genreRepository.findById("3").get(), genreRepository.findById("2").get()));
        Book book3 = new Book("3", "Book_3", authorRepository.findById("1").get(),
                List.of(genreRepository.findById("1").get()));
        Book book4 = new Book("4", "Book_4", authorRepository.findById("3").get(),
                List.of(genreRepository.findById("3").get(), genreRepository.findById("1").get()));


        bookRepository.insert(List.of(book, book2, book3, book4));
    }

    @ChangeSet(order = "004", id = "insertComments", author = "nrr")
    public void insertComments(CommentRepository commentRepository, BookRepository bookRepository) {

        Comment comment1 = new Comment("1", "Comment_1", bookRepository.findById("1").get());
        Comment comment2 = new Comment("2", "Comment_2", bookRepository.findById("2").get());
        Comment comment3 = new Comment("3", "Comment_3", bookRepository.findById("3").get());
        Comment comment4 = new Comment("4", "Comment_4", bookRepository.findById("4").get());
        Comment comment5 = new Comment("5", "Comment_5", bookRepository.findById("1").get());
        Comment comment6 = new Comment("6", "Comment_6", bookRepository.findById("2").get());
        Comment comment7 = new Comment("7", "Comment_7", bookRepository.findById("3").get());

        commentRepository.insert(List.of(comment1, comment2, comment3, comment4, comment5, comment6, comment7));
    }

}
