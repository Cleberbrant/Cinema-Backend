-- 1. Remover tabelas antigas e constraints relacionadas
DROP TABLE IF EXISTS administrador CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;
DROP TABLE IF EXISTS pessoa CASCADE;

-- 2. Ajustar tabela usuario (adicionar campos pessoais)
ALTER TABLE usuario
    ADD COLUMN nome VARCHAR(255) NOT NULL,
    ADD COLUMN data_nascimento DATE NOT NULL,
    ADD COLUMN cpf VARCHAR(20) NOT NULL UNIQUE,
    ADD COLUMN endereco VARCHAR(255) NOT NULL,
    ADD COLUMN estado VARCHAR(100) NOT NULL,
    ADD COLUMN cidade VARCHAR(100) NOT NULL;

-- 3. Ajustar tabela filme
ALTER TABLE filme
    ALTER COLUMN duracao TYPE TIME USING duracao::time,
    ALTER COLUMN sinopse TYPE VARCHAR(1000),
    ALTER COLUMN genero TYPE VARCHAR(30),
    ALTER COLUMN diretor TYPE VARCHAR(255),
    DROP COLUMN IF EXISTS avaliacao,
    ADD COLUMN valor_ingresso NUMERIC(8,2) NOT NULL DEFAULT 10.00;

-- 4. Ajustar localidade (garantir tipos e not null)
ALTER TABLE localidade
    ALTER COLUMN endereco TYPE VARCHAR(255),
    ALTER COLUMN cep TYPE VARCHAR(20),
    ALTER COLUMN referencia TYPE VARCHAR(255);

-- 5. Ajustar cinema (garantir relacionamento 1:1 com localidade)
ALTER TABLE cinema
    ALTER COLUMN nome TYPE VARCHAR(255),
    ALTER COLUMN localidade_id SET NOT NULL;

-- 6. Ajustar sala (garantir tipos e relacionamento)
ALTER TABLE sala
    ALTER COLUMN mapa_da_sala TYPE VARCHAR(1000),
    ALTER COLUMN tecnologia TYPE VARCHAR(100);

-- 7. Criar tabela alimento (se ainda não existir)
CREATE TABLE IF NOT EXISTS alimento (
                                        id SERIAL PRIMARY KEY,
                                        nome VARCHAR(255) NOT NULL,
                                        tipo VARCHAR(30) NOT NULL,
                                        preco NUMERIC(8,2) NOT NULL CHECK (preco > 0),
                                        descricao VARCHAR(1000)
);

-- 8. Índices úteis
CREATE INDEX IF NOT EXISTS idx_filme_genero ON filme(genero);
CREATE INDEX IF NOT EXISTS idx_alimento_tipo ON alimento(tipo);
CREATE INDEX IF NOT EXISTS idx_cinema_nome ON cinema(nome);
CREATE INDEX IF NOT EXISTS idx_localidade_cep ON localidade(cep);
CREATE INDEX IF NOT EXISTS idx_sala_tecnologia ON sala(tecnologia);