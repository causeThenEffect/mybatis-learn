drop table if exists users;

create table users (
  id int,
  name varchar(20),
  PRIMARY KEY (`id`)
);

insert into users values(1, 'User1');
insert into users values(2, 'User2');
insert into users values(3, 'User3');
insert into users values(4, 'User4');
insert into users values(5, 'User5');