create table race_type (
    id SERIAL PRIMARY KEY,
    name text
);

create table race_distance (
    id SERIAL PRIMARY KEY,
    distance text
);

CREATE TABLE race_v2 (
                      id SERIAL PRIMARY KEY,
                      date_of_race TIMESTAMP,
                      race_title TEXT,
                      country text,
                      city text,
                      custom_distance text
);


ALTER TABLE race_distance
    ADD COLUMN race_type_id BIGINT,
ADD CONSTRAINT fka6819fv231fvovjhke1235m97n3y FOREIGN KEY (race_type_id) REFERENCES race_type(id);

ALTER TABLE race_v2
    ADD COLUMN race_type_id BIGINT,
ADD CONSTRAINT fka6819fv231fv1vjhke1235m97n3y FOREIGN KEY (race_type_id) REFERENCES race_type(id);

create table race_distance_join (
                               race_id bigint not null
                                   constraint fkj3453gk1bovqvfame88rcx7yyx
                                       references race_v2,
                               distance_id bigint not null
                                   constraint fka681961081fvovjhkek5m97n3y
                                       references race_distance,
                               constraint race_distance_join_pkey
                                   primary key (race_id, distance_id)
);