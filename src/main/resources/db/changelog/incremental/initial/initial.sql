create sequence hibernate_sequence;
create table role
(
    id   bigint not null
        constraint role_pkey
            primary key,
    name varchar(255),
    role varchar(255)
);

create table users
(
    id          bigint  not null
        constraint users_pkey
            primary key,
    email       varchar(255),
    full_name   varchar(255),
    is_active   boolean not null,
    password    varchar(255),
    username    varchar(255),
    strava_id   varchar(255),
    strava_token   TEXT,
    strava_refresh_token   TEXT,
    created_date timestamp,
    membership_until timestamp,
    updated_date timestamp,
    first_login_date timestamp,
    last_login_date timestamp
);

create table user_role
(
    user_id bigint not null
        constraint fkj345gk1bovqvfame88rcx7yyx
            references users,
    role_id bigint not null
        constraint fka68196081fvovjhkek5m97n3y
            references role,
    constraint user_role_pkey
        primary key (user_id, role_id)
);
