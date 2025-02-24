CREATE TABLE customer (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    last_name VARCHAR(100) NOT NULL,
    second_last_name VARCHAR(100),
    email VARCHAR(100) NOT NULL UNIQUE,
    address TEXT NOT NULL,
    phone VARCHAR(15) NOT NULL,
    country CHAR(2) NOT NULL,
    demonym VARCHAR(100)
);