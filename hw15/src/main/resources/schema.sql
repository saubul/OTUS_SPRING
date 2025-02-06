create table clients (
    id bigserial,
    name varchar(127),
    primary key (id)
);

create table accounts (
    id bigserial,
    number varchar(20),
    balance numeric,
    client_id bigint references clients (id),
    primary key (id)
);

create table transfers (
    id bigserial,
    account_id_from bigint references accounts (id),
    account_id_to bigint references accounts (id),
    amount numeric,
    primary key (id)
);