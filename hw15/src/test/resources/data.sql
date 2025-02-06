insert into clients(name)
values ('John Smith'), ('Brad Pitt');

insert into accounts(number, balance, client_id)
values('40817810100010000001', 100.0, 1),
('40817810100010000002', 300.0, 2);

insert into transfers(account_id_from, account_id_to, amount)
values (1, 2, 100.0);
