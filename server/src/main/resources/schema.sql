drop table if exists messages;

create table if not exists messages
(
    id      bigserial
        primary key,
    message varchar(1024) not null,
    name    varchar(50)   not null
);