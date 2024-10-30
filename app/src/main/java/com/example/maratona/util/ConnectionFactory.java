package com.example.maratona.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConnectionFactory extends SQLiteOpenHelper{

    private static final String NAME ="banco.db";
    private static final int VERSION = 1;

    public ConnectionFactory(@Nullable Context context){
        super(context, NAME, null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela Empresa
        db.execSQL("CREATE TABLE empresa ("
                + "id_empresa INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome VARCHAR(50), "
                + "telefone VARCHAR(50), "
                + "email VARCHAR(50), "
                + "usuario VARCHAR(50), "
                + "senha VARCHAR(50), "
                + "cnpj VARCHAR(50), "
                + "local VARCHAR(100), "
                + "url_logo TEXT, "
                + "data_criacao TIMESTAMP"
                + ")");

        // Tabela Maratona
        db.execSQL("CREATE TABLE maratona ("
                + "id_maratona INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "criador INTEGER, "
                + "nome VARCHAR(50), "
                + "local VARCHAR(100), "
                + "data_inicio DATETIME, "
                + "data_final DATETIME, "
                + "status VARCHAR(20), "
                + "distancia VARCHAR(50), "
                + "descricao TEXT, "
                + "limite_participantes INTEGER, "
                + "regras TEXT, "
                + "valor FLOAT, "
                + "tipo_terreno VARCHAR(50), "
                + "clima_esperado VARCHAR(50)"
                + ")");


        // Tabela Corredor
        db.execSQL("CREATE TABLE corredor ("
                + "id_corredor INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome VARCHAR(50), "
                + "telefone VARCHAR(50), "
                + "email VARCHAR(50), "
                + "senha VARCHAR(50), "
                + "data_nasc VARCHAR(50), "
                + "cpf VARCHAR(50), "
                + "endereco VARCHAR(100), "
                + "genero VARCHAR(20), "
                + "url_foto TEXT, "
                + "pais_origem VARCHAR(50)"
                + ")");

        // Tabela Inscricao
        db.execSQL("CREATE TABLE inscricao ("
                + "id_inscricao INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id_corredor INTEGER, "
                + "id_maratona INTEGER, "
                + "data_hora DATETIME, "
                + "status VARCHAR(20), "
                + "forma_pagamento VARCHAR(50), "
                + "FOREIGN KEY(id_corredor) REFERENCES corredor(id_corredor), "
                + "FOREIGN KEY(id_maratona) REFERENCES maratona(id_maratona)"
                + ")");

        // Tabela Participacao
        db.execSQL("CREATE TABLE participacao ("
                + "id_participacao INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id_inscricao INTEGER, "
                + "status_conclusao VARCHAR(50), "
                + "tempo_registrado VARCHAR(50), "
                + "tempo_inicio VARCHAR(50), "
                + "tempo_fim VARCHAR(50), "
                + "passos INTEGER, "
                + "FOREIGN KEY(id_inscricao) REFERENCES inscricao(id_inscricao)"
                + ")");

        db.execSQL("INSERT INTO empresa (nome, telefone, email, usuario, senha, cnpj, local, url_logo) VALUES "
                + "('Corridas do Brasil', '21911223344', 'cb@gmail.com', 'Fernanda Melo', '123', '33.222.111/0001-12', 'Av. Paulista, 1000, São Paulo, SP', 'http://example.com/logo/corridas.jpg'), "
                + "('Desafio Run', '31999887766', 'rd@gmail.com', 'João Mendes', '123', '44.333.222/0001-23', 'Av. dos Esportes, 300, São Paulo, SP', 'http://example.com/logo/desafio.jpg'), "
                + "('Viva Maratonas', '11988776655', 'vm@gmail.com', 'Juliana Andrade', '123', '55.444.333/0001-34', 'Rua da Corrida, 50, São Paulo, SP', 'http://example.com/logo/viva.jpg'), "
                + "('Elite Runners', '21966554433', 'er@gmail.com', 'Carlos Santos', '123', '66.555.444/0001-45', 'Av. da Elite, 678, São Paulo, SP', 'http://example.com/logo/elite.jpg'), "
                + "('Endurance Brasil', '31988771122', 'eb@gmail.com', 'Mariana Silva', '123', '77.666.555/0001-56', 'Av. Principal, 345, São Paulo, SP', 'http://example.com/logo/endurance.jpg');");

        db.execSQL("INSERT INTO maratona (criador, nome, local, data_inicio,data_final, distancia, status, descricao, regras, valor, tipo_terreno, clima_esperado) VALUES "
                // maratona da empresa rd@gmail.com
                + "(2, 'Maratona da Independência', 'Brasília, DF', '2024-09-07','2024-10-07', '42.195', 'Aberta', 'Celebre a história do Brasil com a Maratona da Independência','Uso obrigatório de chip.', 120.00, 'Asfalto', 'Quente'), "
                // maratona da empresa vm@gmail.com
                + "(3, 'Corrida da Primavera', 'Curitiba, PR', '2024-09-21','2024-10-21', '10', 'Aber ta para Inscrição', 'A Corrida da Primavera é um evento vibrante e cheio de cores que marca a chegada da estação das flores. ', 'Uso de tênis adequado obrigatório.', 80.00, 'Trilha', 'Fresco'), "
                // maratona da empresa er@gmail.com
                + "(4, 'Desafio da Montanha', 'Campos do Jordão, SP', '2023-12-15','2024-01-15', '21', 'Finalizada', 'Para os que buscam aventura e superação', 'Uso de roupas térmicas.', 200.00, 'Montanha', 'Frio'), "
                // maratona da empresa eb@gmail.com
                + "(5, 'Maratona Noturna', 'Fortaleza, CE', '2024-08-10','2024-09-10', '5', 'Aberta', 'A Maratona Noturna oferece uma experiência única de correr sob as estrelas.','Obrigatório uso de lanterna.', 50.00, 'Asfalto', 'Quente'), "
                // maratona da empresa cb@gmail.com
                + "(1, 'Maratona Internacional de São Paulo', 'São Paulo, SP', '2024-04-12','2025-11-12',  '42.195', 'Aberta', 'Uma das maiores provas do Brasil', 'Uso de chip e número obrigatório.', 180.00, 'Asfalto', 'Temperado');");

        db.execSQL("INSERT INTO corredor (nome, telefone, email, senha, data_nasc, cpf, endereco, genero, url_foto, pais_origem) VALUES "
                + "('Ana Costa', '11999998888', 'ana@gmail.com', '123', '1988-08-22', '321.654.987-00', 'Rua dos Jardins, 200, São Paulo, SP', 'Feminino', 'http://example.com/fotos/ana.jpg', 'Brasil'), "
                + "('Carlos Mendes', '21987654321', 'me@gmail.com', '123', '1975-03-10', '111.222.333-44', 'Avenida Central, 101, São Paulo, SP', 'Masculino', 'http://example.com/fotos/carlos.jpg', 'Brasil'), "
                + "('Fernanda Oliveira', '31965432109', 'fe@gmail.com', '123', '1995-07-18', '555.666.777-88', 'Rua das Laranjeiras, 456, São Paulo, SP', 'Feminino', 'http://example.com/fotos/fernanda.jpg', 'Brasil'), "
                + "('Rafael Lima', '21911223344', 'raf@gmail.com', '123', '1993-11-05', '888.999.000-11', 'Rua das Palmeiras, 789, São Paulo, SP', 'Masculino', 'http://example.com/fotos/rafael.jpg', 'Brasil'), "
                + "('Juliana Martins', '31987655443', 'ju@gmail.com', '123', '2000-02-12', '999.888.777-66', 'Rua Primavera, 345, São Paulo, SP', 'Feminino', 'http://example.com/fotos/juliana.jpg', 'Brasil');");

        db.execSQL("INSERT INTO inscricao (id_corredor, id_maratona, forma_pagamento, status) VALUES "
                // maratona Corrida da Primavera pelo me@gmail.com podendo participar mas prescisa abrir com a empresa
                + "(2, 2, 'Cartão de Crédito', 'Inscrito'), "
                // maratona Maratona da Independência pelo fe@gmail.com podendo participar
                + "(3, 1, 'Boleto Bancário', 'Inscrito'), "
                // maratona Desafio da Montanha pelo raf@gmail.com já concluiu e a maratona já acabou
                + "(4, 3, 'PIX', 'Concluido'), "
                // maratona Corrida da Primavera pelo ju@gmail.com já concluiu e a maratona já acabou
                + "(5, 2, 'Cartão de Débito', 'Inscrito'), "
                // maratona Maratona Noturna pelo ana@gmail.com que está pronta para participar
                + "(1, 4, 'Transferência Bancária', 'Inscrito');");


        db.execSQL("INSERT INTO participacao (id_inscricao, status_conclusao, tempo_registrado, tempo_inicio, tempo_fim, passos) VALUES "
                //dados da participação da terceira Inscrição que ja conclui a maratona e ela está fechada.
                + "(3, 'Desativado', '4:00:00', '08:00:00', '12:00:00', 9000); ");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Apaga as tabelas se elas já existirem
        db.execSQL("DROP TABLE IF EXISTS empresa");
        db.execSQL("DROP TABLE IF EXISTS maratona");
        db.execSQL("DROP TABLE IF EXISTS corredor");
        db.execSQL("DROP TABLE IF EXISTS inscricao");
        db.execSQL("DROP TABLE IF EXISTS participacao");

        // Recria as tabelas
        onCreate(db);
    }


}
