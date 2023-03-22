create sequence hibernate_sequence;
create table aircraft
(
    id             bigint not null
        constraint aircraft_pkey
            primary key,
    icao           varchar(255),
    max_passengers bigint,
    max_cargo bigint,
    name           varchar(255),
    price          bigint,
    cargo          boolean
);

create table airport
(
    id        bigint not null
        constraint airport_pkey
            primary key,
    city      varchar(255),
    country   varchar(255),
    iata      varchar(255),
    icao      varchar(255),
    latitude  double precision,
    longitude double precision,
    name      varchar(255)
);


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
    minutes     bigint,
    password    varchar(255),
    username    varchar(255),
    vatsim_id   varchar(255),
    ivao_id   varchar(255),
    poscon_id   varchar(255)
);

create table flight
(
    id                  bigint not null
        constraint flight_pkey
            primary key,
    earning             bigint,
    end_fuel            double precision,
    end_time            timestamp,
    flight_length       double precision,
    fuel_cost           bigint,
    landing_rate        double precision,
    profit              bigint,
    start_fuel          double precision,
    start_time          timestamp,
    aircraft_id         bigint
        constraint fkmofq89ullrd4qk1hllnyf8pn5
            references aircraft,
    arrivall_id         bigint
        constraint fksmtefvi75bej556fijyel6v25
            references airport,
    departure_id        bigint
        constraint fkaxqek9h4f7km4qg67twbx2go5
            references airport,
    user_id             bigint
        constraint fktkgf41ycgc88quo3itp3npxy9
            references users
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
