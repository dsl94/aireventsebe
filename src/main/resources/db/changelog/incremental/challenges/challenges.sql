CREATE TABLE challenge (
                           id SERIAL PRIMARY KEY,
                           start_date TIMESTAMP,
                           end_date TIMESTAMP,
                           last_sync TIMESTAMP,
                           title TEXT
);

CREATE TABLE user_challenge (
                                user_id BIGINT REFERENCES users(id),
                                challenge_id BIGINT REFERENCES challenge(id),
                                PRIMARY KEY (user_id, challenge_id)
);