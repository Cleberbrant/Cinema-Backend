CREATE TABLE pessoa (
                        id SERIAL PRIMARY KEY,
                        nome VARCHAR(255) NOT NULL,
                        data_nascimento DATE,
                        cpf VARCHAR(14) UNIQUE NOT NULL,
                        email VARCHAR(255) UNIQUE NOT NULL,
                        senha VARCHAR(255) NOT NULL
);

CREATE TABLE cliente (
                         id INT PRIMARY KEY REFERENCES pessoa(id),
                         estado VARCHAR(2) NOT NULL,
                         cidade VARCHAR(100) NOT NULL
);

CREATE TABLE administrador (
                               id INT PRIMARY KEY REFERENCES pessoa(id),
                               endereco TEXT NOT NULL
);

CREATE TABLE filme (
                       id SERIAL PRIMARY KEY,
                       titulo VARCHAR(255) NOT NULL,
                       duracao VARCHAR(20) NOT NULL,
                       sinopse TEXT,
                       genero VARCHAR(50),
                       em_cartaz BOOLEAN DEFAULT false,
                       avaliacao NUMERIC(3,1),
                       diretor VARCHAR(100)
);

CREATE TABLE localidade (
                            id SERIAL PRIMARY KEY,
                            endereco TEXT NOT NULL,
                            cep VARCHAR(9) NOT NULL,
                            referencia VARCHAR(255)
);

CREATE TABLE cinema (
                        id SERIAL PRIMARY KEY,
                        nome VARCHAR(255) NOT NULL,
                        localidade_id INT UNIQUE REFERENCES localidade(id)
);

CREATE TABLE sala (
                      id SERIAL PRIMARY KEY,
                      numero_da_sala INT NOT NULL,
                      capacidade INT NOT NULL,
                      mapa_da_sala TEXT,
                      tecnologia VARCHAR(50),
                      cinema_id INT REFERENCES cinema(id)
);
