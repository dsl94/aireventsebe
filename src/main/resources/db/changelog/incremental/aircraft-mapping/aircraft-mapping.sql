create table aircraft_mapping
(
    id                  bigint  not null
        constraint aircraft_mapping_pkey
            primary key,
    sim_key               varchar(255),
    icao             varchar(255)
);