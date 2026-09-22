CREATE TABLE IF NOT EXISTS bootcamp (
    bootcamp_id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    release_date DATE NOT NULL,
    duration_days INT NOT NULL,

    PRIMARY KEY (bootcamp_id)
);
