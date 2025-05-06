CREATE TABLE pagamento (
                           id SERIAL PRIMARY KEY,
                           numero_do_cartao VARCHAR(30),
                           nome_impresso VARCHAR(100),
                           data_de_validade VARCHAR(7),
                           codigo_de_seguranca VARCHAR(5),
                           valor_total NUMERIC(8,2),
                           data_pagamento TIMESTAMP,
                           usuario_id BIGINT NOT NULL REFERENCES usuario(id),
                           filme_id INTEGER REFERENCES filme(id)
);

CREATE TABLE pagamento_alimento (
                                    pagamento_id INTEGER REFERENCES pagamento(id) ON DELETE CASCADE,
                                    alimento_id INTEGER REFERENCES alimento(id) ON DELETE CASCADE,
                                    PRIMARY KEY (pagamento_id, alimento_id)
);

CREATE INDEX idx_pagamento_usuario ON pagamento(usuario_id);