-- 1. Remova a constraint
ALTER TABLE pagamento DROP CONSTRAINT IF EXISTS pagamento_usuario_id_fkey;

-- 2. Altere o tipo da coluna
ALTER TABLE pagamento ALTER COLUMN usuario_id TYPE VARCHAR(255);