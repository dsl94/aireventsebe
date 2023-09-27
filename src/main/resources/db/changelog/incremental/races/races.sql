CREATE TABLE race (
                      id SERIAL PRIMARY KEY,
                      date_of_race TIMESTAMP,
                      race_title TEXT,
                      distances TEXT
);

CREATE TABLE user_race (
                           user_id BIGINT REFERENCES users(id),
                           race_id BIGINT REFERENCES race(id),
                           PRIMARY KEY (user_id, race_id)
);