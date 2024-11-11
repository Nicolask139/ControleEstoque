SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));

create database controleEstoque_db;
use controleEstoque_db;

CREATE TABLE produto (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Nome varchar(60) DEFAULT NULL,
  Preco double NOT NULL,
  Quantidade int NOT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE historico (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Nome varchar(60) DEFAULT NULL,
  Preco double NOT NULL,
  Quantidade int NOT NULL,
  data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  Produto_id INT,
  FOREIGN KEY (Produto_id) REFERENCES produto(id),
  PRIMARY KEY (Id)
);

CREATE TABLE usuario (
         id INT AUTO_INCREMENT PRIMARY KEY,
        nome VARCHAR(100) NOT NULL,
         senha VARCHAR(100) NOT NULL
     );
     
     INSERT INTO usuario (nome, senha) VALUES ('admin', 'mudar123');