insert into authors(full_name)
values ('Author_1'), ('Author_2');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 2);

insert into comments(text, book_id)
values ('This book is really good!', 1),
       ('This book is awful!', 2),
       ('This book is ok.', 3),
       ('I dont want to comment that book', 2);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (3, 2);
