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
