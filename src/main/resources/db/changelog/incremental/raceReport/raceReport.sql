CREATE TABLE race_report (
                             id SERIAL PRIMARY KEY,
                             race_date TIMESTAMP,
                             distance VARCHAR(255),
                             race_title VARCHAR(255),
                             additional_info TEXT,
                             created_date timestamp,
                             updated_date timestamp,
                             user_id BIGINT REFERENCES users(id)
);