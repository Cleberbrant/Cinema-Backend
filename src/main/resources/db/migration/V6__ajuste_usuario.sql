-- 1. Adiciona a coluna localidade_id na tabela usuario, se não existir
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS localidade_id INTEGER REFERENCES localidade(id);

-- 2. Remove campos antigos do usuário (caso ainda existam)
ALTER TABLE usuario DROP COLUMN IF EXISTS endereco;
ALTER TABLE usuario DROP COLUMN IF EXISTS estado;
ALTER TABLE usuario DROP COLUMN IF EXISTS cidade;

-- 3. Cria a localidade do admin master, se não existir
INSERT INTO localidade (endereco, cep, referencia)
SELECT 'Admin', '00000-000', 'Localidade do Admin Master'
WHERE NOT EXISTS (
    SELECT 1 FROM localidade WHERE endereco = 'Admin' AND cep = '00000-000'
);

-- 4. Insere o usuário administrador master, se não existir
INSERT INTO usuario (nome, data_nascimento, cpf, email, password, role, localidade_id)
SELECT
    'Admin',
    '1990-01-01',
    '00000000000',
    'admin@gmail.com',
    'admin',
    'ROLE_ADMIN',
    id
FROM localidade
WHERE endereco = 'Admin' AND cep = '00000-000'
  AND NOT EXISTS (SELECT 1 FROM usuario WHERE email = 'admin@gmail.com');

-- 5. Torna localidade_id obrigatória
ALTER TABLE usuario ALTER COLUMN localidade_id SET NOT NULL;