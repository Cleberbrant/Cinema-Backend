CREATE TABLE usuario (
                         id SERIAL PRIMARY KEY,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         role VARCHAR(50) NOT NULL
);
