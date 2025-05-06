CREATE TABLE IF NOT EXISTS pagamento_alimento (
                                                  pagamento_id INTEGER NOT NULL REFERENCES pagamento(id) ON DELETE CASCADE,
                                                  alimento_id INTEGER NOT NULL REFERENCES alimento(id) ON DELETE CASCADE,
                                                  PRIMARY KEY (pagamento_id, alimento_id)
);