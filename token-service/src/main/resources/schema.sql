drop table if exists authentications;
drop table if exists tokens;

create table if not exists tokens
(
    id    bigserial
        primary key,
    token varchar(255) not null
);

create table if not exists authentications
(
    id       bigserial
        primary key,
    name     varchar(50)  not null
        constraint uk_auth_name
            unique,
    password varchar(50) not null,
    token_id bigint
        constraint fk_auths_tokens
            references tokens
);