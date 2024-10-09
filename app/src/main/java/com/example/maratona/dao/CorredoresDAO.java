package com.example.maratona.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.maratona.util.ConnectionFactory;

public class CorredoresDAO {
    private ConnectionFactory conexao;
    private SQLiteDatabase banco;
    public CorredoresDAO(Context context){
        //ConnectionFactory com o banco de dados

        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();

    }


}
