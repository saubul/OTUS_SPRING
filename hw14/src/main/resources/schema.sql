create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table comments (
    id bigserial,
    text varchar(255),
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);

create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

create table users (
    id bigserial,
    username varchar(255) not null,
    password varchar(255) not null,
    enabled int,
    primary key (id)
);

create table roles (
    id bigserial,
    name varchar(255) not null unique,
    primary key (id)
);

create table users_roles (
    user_id bigint,
    role_id bigint,
    primary key (user_id, role_id)
);