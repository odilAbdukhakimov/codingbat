create table if not exists role_cb(
    id serial primary key,
    name varchar not null
);
create table if not exists permission_cb(
    id serial primary key,
    name varchar not null
);

insert into role_cb(name) values ('ADMIN');
insert into role_cb(name) values ('USER');
insert into role_cb(name) values ('SUPER_ADMIN');

insert into permission_cb(name) values ('ADD');
insert into permission_cb(name) values ('DELETE');
insert into permission_cb(name) values ('EDIT');
insert into permission_cb(name) values ('READ');



