package com.example.maratona.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.maratona.model.Corredores;
import com.example.maratona.model.Inscricao;
import com.example.maratona.model.Maratonas;
import com.example.maratona.util.ConnectionFactory;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class InscricaoDAO {

    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public InscricaoDAO(Context context) {
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }



    // Inserir nova inscrição
    public long insert(Inscricao inscricao) {
        ContentValues values = new ContentValues();
        values.put("id_corredor", inscricao.getIdCorredor());
        values.put("id_maratona", inscricao.getIdMaratona());
        values.put("data_hora", "CURRENT_TIMESTAMP"); // Se estiver usando Timestamp
        values.put("forma_pagamento", inscricao.getFormaPagamento());
        values.put("status", "Inscrito");
        return banco.insert("inscricao", null, values);
    }

    // Atualizar uma inscrição existente
    public void update(Inscricao inscricao) {
        ContentValues values = new ContentValues();
        values.put("id_corredor", inscricao.getIdCorredor());
        values.put("id_maratona", inscricao.getIdMaratona());
        values.put("data_hora", String.valueOf(inscricao.getDataHora()));
        values.put("forma_pagamento", inscricao.getFormaPagamento());
        values.put("status", inscricao.getFormaPagamento());
        String[] args = {String.valueOf(inscricao.getIdInscricao())};
        banco.update("inscricao", values, "id_inscricao=?", args);
    }

    // Deletar uma inscrição
    public void delete(Inscricao inscricao) {
        String[] args = {String.valueOf(inscricao.getIdInscricao())};
        banco.delete("inscricao", "id_inscricao=?", args);
    }

    // Obter todas as inscrições
    public List<Inscricao> obterTodos() {
        List<Inscricao> inscricoes = new ArrayList<>();
        Cursor cursor = banco.query("inscricao", new String[]{"id_inscricao", "id_corredor", "id_maratona", "data_hora", "forma_pagamento"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            Inscricao inscricao = new Inscricao();
            inscricao.setIdInscricao(cursor.getInt(0));
            inscricao.setIdCorredor(cursor.getInt(1));
            inscricao.setIdMaratona(cursor.getInt(2));
            inscricao.setDataHora(Date.valueOf(cursor.getString(3))); // Se estiver usando Timestamp
            inscricao.setFormaPagamento(cursor.getString(4));
            inscricoes.add(inscricao);
        }
        cursor.close(); // Não se esqueça de fechar o cursor
        return inscricoes;
    }

    // Obter maratonas de um determinado corredor
    public List<Maratonas> obterMaratonasPorCorredor(int idCorredor) {
        List<Maratonas> maratonas = new ArrayList<>();

        // Query com JOIN entre a tabela de inscrição e maratona
        String query = "SELECT m.id_maratona, m.criador, m.nome, m.local, m.data_inicio, " +
                "m.criador, m.status, m.distancia, m.descricao, m.limite_participantes, " +
                "m.regras, m.valor, m.data_final, m.tipo_terreno, m.clima_esperado " +
                "FROM maratona m " +
                "INNER JOIN inscricao i ON m.id_maratona = i.id_maratona " +
                "WHERE i.id_corredor = ? " +
                "AND (m.status = 'Aberta para Inscrição' OR m.status = 'Aberta')";

        Cursor cursor = banco.rawQuery(query, new String[]{String.valueOf(idCorredor)});

        while (cursor.moveToNext()) {
            Maratonas maratona = new Maratonas();
            maratona.setId(cursor.getInt(0));
            maratona.setCriador(cursor.getInt(1));
            maratona.setNome(cursor.getString(2));
            maratona.setLocal(cursor.getString(3));
            maratona.setData_inicio(cursor.getString(4)); // DATETIME
            maratona.setCriador(cursor.getInt(5));
            maratona.setStatus(cursor.getString(6));
            maratona.setDistancia(cursor.getString(7));
            maratona.setDescricao(cursor.getString(8));
            maratona.setLimite_participantes(cursor.getInt(9));
            maratona.setRegras(cursor.getString(10));
            maratona.setValor(cursor.getFloat(11));
            maratona.setData_final(cursor.getString(12)); // TIMESTAMP
            maratona.setTipo_terreno(cursor.getString(13));
            maratona.setClima_esperado(cursor.getString(14));
            maratonas.add(maratona);
        }

        cursor.close(); // Não se esqueça de fechar o cursor
        return maratonas;
    }

    // Obter maratonas de um determinado corredor
    public List<Maratonas> obterMaratonasConcluidasPorCorredor(int idCorredor) {
        List<Maratonas> maratonas = new ArrayList<>();

        // Query com JOIN entre a tabela de inscrição e maratona
        String query = "SELECT m.id_maratona, m.criador, m.nome, m.local, m.data_inicio, " +
                "m.status, m.distancia, m.descricao, m.limite_participantes, " +
                "m.regras, m.valor, m.data_final, m.tipo_terreno, m.clima_esperado " +
                "FROM maratona m " +
                "INNER JOIN inscricao i ON m.id_maratona = i.id_maratona " +
                "WHERE i.id_corredor = ? " +
                "AND i.status = 'Concluido'";

        Cursor cursor = banco.rawQuery(query, new String[]{String.valueOf(idCorredor)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Maratonas maratona = new Maratonas();
                maratona.setId(cursor.getInt(0));
                maratona.setCriador(cursor.getInt(1));
                maratona.setNome(cursor.getString(2));
                maratona.setLocal(cursor.getString(3));
                maratona.setData_inicio(cursor.getString(4)); // DATETIME
                maratona.setStatus(cursor.getString(5)); // Correção do índice
                maratona.setDistancia(cursor.getString(6));
                maratona.setDescricao(cursor.getString(7));
                maratona.setLimite_participantes(cursor.getInt(8));
                maratona.setRegras(cursor.getString(9));
                maratona.setValor(cursor.getFloat(10));
                maratona.setData_final(cursor.getString(11)); // TIMESTAMP
                maratona.setTipo_terreno(cursor.getString(12));
                maratona.setClima_esperado(cursor.getString(13));

                maratonas.add(maratona);
            } while (cursor.moveToNext());
        }

        // Fechar cursor para evitar memory leaks
        if (cursor != null) {
            cursor.close();
        }

        return maratonas;
    }


    public List<Corredores> obterCorredoresPorMaratona(int idMaratona) {
        List<Corredores> corredores = new ArrayList<>();

        // Consulta com JOIN entre a tabela de inscricao e corredor
        String query = "SELECT c.id_corredor, c.nome, c.telefone, c.email, c.senha, " +
                "c.cpf, c.endereco, c.pais_origem " +
                "FROM corredor c " +
                "INNER JOIN inscricao i ON c.id_corredor = i.id_corredor " +
                "WHERE i.id_maratona = ?  AND i.status = 'Inscrito'";

        // Executa a consulta e passa o idMaratona como parâmetro
        Cursor cursor = banco.rawQuery(query, new String[]{String.valueOf(idMaratona)});

        while (cursor.moveToNext()) {
            Corredores corredor = new Corredores();
            corredor.setIdCorredor(cursor.getInt(0)); // id_corredor
            corredor.setNome(cursor.getString(1)); // nome
            corredor.setTelefone(cursor.getString(2)); // telefone
            corredor.setEmail(cursor.getString(3)); // email
            corredor.setSenha(cursor.getString(4)); // senha
            //corredor.setDataNasc(cursor.getString(5)); // data_nasc
            corredor.setCpf(cursor.getString(5)); // cpf
            corredor.setEndereco(cursor.getString(6)); // endereco
            //corredor.setGenero(cursor.getString(8)); // genero
            //corredor.setUrlFoto(cursor.getString(9)); // url_foto
            corredor.setPaisOrigem(cursor.getString(7)); // pais_origem

            // Adiciona o corredor à lista
            corredores.add(corredor);
        }

        // Fecha o cursor para liberar os recursos
        cursor.close();

        return corredores;
    }

    public List<Corredores> obterCorredoresParticipandoPorMaratona(int idMaratona) {
        List<Corredores> corredores = new ArrayList<>();

        // Consulta com JOIN entre a tabela de inscricao e corredor, incluindo filtro de status "Participando"
        String query = "SELECT c.id_corredor, c.nome, c.telefone, c.email, c.senha, " +
                "c.cpf, c.endereco, c.pais_origem " +
                "FROM corredor c " +
                "INNER JOIN inscricao i ON c.id_corredor = i.id_corredor " +
                "WHERE i.id_maratona = ? AND i.status = 'Participando'";

        // Executa a consulta e passa o idMaratona como parâmetro
        Cursor cursor = banco.rawQuery(query, new String[]{String.valueOf(idMaratona)});

        while (cursor.moveToNext()) {
            Corredores corredor = new Corredores();
            corredor.setIdCorredor(cursor.getInt(0)); // id_corredor
            corredor.setNome(cursor.getString(1)); // nome
            corredor.setTelefone(cursor.getString(2)); // telefone
            corredor.setEmail(cursor.getString(3)); // email
            corredor.setSenha(cursor.getString(4)); // senha
            //corredor.setDataNasc(cursor.getString(5)); // data_nasc
            corredor.setCpf(cursor.getString(5)); // cpf
            corredor.setEndereco(cursor.getString(6)); // endereco
            //corredor.setGenero(cursor.getString(8)); // genero
            //corredor.setUrlFoto(cursor.getString(9)); // url_foto
            corredor.setPaisOrigem(cursor.getString(7)); // pais_origem

            // Adiciona o corredor à lista
            corredores.add(corredor);
        }

        // Fecha o cursor para liberar os recursos
        cursor.close();

        return corredores;
    }


    public List<Corredores> obterCorredoresConcluidosPorMaratona(int idMaratona) {
        List<Corredores> corredores = new ArrayList<>();

        // Consulta com JOIN entre a tabela de inscricao e corredor, incluindo filtro de status "Participando"
        String query = "SELECT c.id_corredor, c.nome, c.telefone, c.email, c.senha, " +
                "c.cpf, c.endereco, c.pais_origem " +
                "FROM corredor c " +
                "INNER JOIN inscricao i ON c.id_corredor = i.id_corredor " +
                "WHERE i.id_maratona = ? AND i.status = 'Concluido'";

        // Executa a consulta e passa o idMaratona como parâmetro
        Cursor cursor = banco.rawQuery(query, new String[]{String.valueOf(idMaratona)});

        while (cursor.moveToNext()) {
            Corredores corredor = new Corredores();
            corredor.setIdCorredor(cursor.getInt(0)); // id_corredor
            corredor.setNome(cursor.getString(1)); // nome
            corredor.setTelefone(cursor.getString(2)); // telefone
            corredor.setEmail(cursor.getString(3)); // email
            corredor.setSenha(cursor.getString(4)); // senha
            //corredor.setDataNasc(cursor.getString(5)); // data_nasc
            corredor.setCpf(cursor.getString(5)); // cpf
            corredor.setEndereco(cursor.getString(6)); // endereco
            //corredor.setGenero(cursor.getString(8)); // genero
            //corredor.setUrlFoto(cursor.getString(9)); // url_foto
            corredor.setPaisOrigem(cursor.getString(7)); // pais_origem

            // Adiciona o corredor à lista
            corredores.add(corredor);
        }

        // Fecha o cursor para liberar os recursos
        cursor.close();

        return corredores;
    }



    // Ler uma inscrição pelo ID
    public Inscricao read(int id) {
        String[] args = {String.valueOf(id)};
        Cursor cursor = banco.query("inscricao", new String[]{"id_inscricao", "id_corredor", "id_maratona", "data_hora", "forma_pagamento"},
                "id_inscricao=?", args, null, null, null);
        Inscricao inscricao = new Inscricao();
        if (cursor.moveToFirst()) {
            inscricao.setIdInscricao(cursor.getInt(0));
            inscricao.setIdCorredor(cursor.getInt(1));
            inscricao.setIdMaratona(cursor.getInt(2));
            inscricao.setDataHora(Date.valueOf(cursor.getString(3)));
            inscricao.setFormaPagamento(cursor.getString(4));
        }
        cursor.close();
        return inscricao;
    }

    public int getIdInscricao(int idCorredor, int idMaratona) {
        String[] args = {String.valueOf(idCorredor), String.valueOf(idMaratona)};

        // Query para buscar o id_inscricao baseado no id_corredor e id_maratona
        Cursor cursor = banco.query("inscricao", new String[]{"id_inscricao"},
                "id_corredor=? AND id_maratona=?", args, null, null, null);

        int idInscricao = -1; // Valor padrão caso não seja encontrada uma inscrição

        if (cursor.moveToFirst()) {
            idInscricao = cursor.getInt(0); // Pega o id_inscricao
        }

        cursor.close();

        return idInscricao; // Retorna o id_inscricao ou -1 se não encontrado
    }

    //atualiza status
    public void updateStatus(int idInscricao, String novoStatus) {
        ContentValues values = new ContentValues();
        values.put("status", novoStatus);

        String[] args = {String.valueOf(idInscricao)};

        // Atualiza apenas o campo status da maratona com o id fornecido
        banco.update("inscricao", values, "id_inscricao=?", args);
    }

}
