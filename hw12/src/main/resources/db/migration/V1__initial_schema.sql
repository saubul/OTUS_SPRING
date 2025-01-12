create table authors
(
    id         bigserial    not null primary key,
    full_name  varchar(50)  not null
);

create table genres
(
    id      bigserial   not null primary key,
    name    varchar(250) not null
);

create table books
(
    id        bigserial   not null primary key,
    title     varchar(250) not null,
    author_id bigint not null references authors
);

create table comments
(
    id       bigserial not null primary key,
    text     varchar(512) not null,
    book_id  bigint references books
);

create table books_genres
(
    book_id bigint not null references books,
    genre_id bigint not null references genres
);