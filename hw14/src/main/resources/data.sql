insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3), ('BookTitle_4', 3);

insert into comments(text, book_id)
values ('This book is really good!', 1),
       ('This book is awful!', 2),
       ('This book is ok.', 3),
       ('I dont want to comment that book', 4);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);


insert into users(username, password, enabled)
values ('admin', 'admin', 1),
('user', 'user', 1);

insert into roles(name) values ('admin'), ('user');

insert into users_roles(user_id, role_id)
values (1 ,1),
(2, 2)