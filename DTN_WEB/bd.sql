CREATE TABLE IF NOT EXISTS cliente_rede (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(250) NOT NULL,
    telefone VARCHAR(250) NOT NULL
);

CREATE TABLE IF NOT EXISTS mensagem (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    emissor INT NOT NULL,
    destinatario INT NOT NULL,
    texto TEXT NOT NULL,
    status BOOLEAN NOT NULL,
    data_envio DATETIME NOT NULL,
    FOREIGN KEY (emissor) REFERENCES cliente_rede(id),
    FOREIGN KEY (destinatario) REFERENCES cliente_rede(id)
);