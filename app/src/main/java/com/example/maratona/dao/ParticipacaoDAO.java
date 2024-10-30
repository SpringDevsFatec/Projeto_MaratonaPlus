package com.example.maratona.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.maratona.model.Participacao;
import com.example.maratona.util.ConnectionFactory;



import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParticipacaoDAO {

    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public ParticipacaoDAO(Context context) {
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }

    // Inserir
    public long insert(Participacao participacao) {
        ContentValues values = new ContentValues();
        values.put("id_participacao", participacao.getIdParticipacao()); // Corrigido
        values.put("id_inscricao", participacao.getIdInscricao()); // Corrigido
        values.put("status_conclusao", participacao.getStatusConclusao()); // Corrigido
        values.put("tempo_registrado", "0:0:0"); // Armazenar como string
        values.put("tempo_inicio", participacao.getTempoInicio().toString()); // Corrigido
        values.put("tempo_fim", "0:0:0"); // Corrigido
        values.put("passos", participacao.getPassos()); // Corrigido
        return banco.insert("participacao", null, values);
    }

    // Atualizar
    public void update(Participacao participacao) {
        ContentValues values = new ContentValues();
        values.put("id_inscricao", participacao.getIdInscricao());
        values.put("status_conclusao", participacao.getStatusConclusao());
        values.put("tempo_registrado", participacao.getTempoRegistrado().toString());
        values.put("tempo_inicio", participacao.getTempoInicio().toString());
        values.put("tempo_fim", participacao.getTempoFim().toString());
        values.put("passos", participacao.getPassos());
        String[] args = {String.valueOf(participacao.getIdParticipacao())};
        banco.update("participacao", values, "id_participacao=?", args);
    }

    // Deletar
    public void delete(Participacao participacao) {
        String[] args = {String.valueOf(participacao.getIdParticipacao())};
        banco.delete("participacao", "id_participacao=?", args);
    }

    // Ler uma Participacao por ID
    public Participacao readParticipacao(int id) {
        String[] args = {String.valueOf(id)};
        Cursor cursor = banco.rawQuery("SELECT * FROM participacao WHERE id_participacao = ?", args); // Corrigido

        Participacao participacao = new Participacao();
        if (cursor.moveToFirst()) {
            participacao.setIdParticipacao(cursor.getInt(0));
            participacao.setIdInscricao(cursor.getInt(1));
            participacao.setStatusConclusao(cursor.getString(2));
            participacao.setTempoRegistrado(Time.valueOf(cursor.getString(3)));
            participacao.setTempoInicio(Time.valueOf(cursor.getString(4)));
            participacao.setTempoFim(Time.valueOf(cursor.getString(5)));
            participacao.setPassos(cursor.getInt(6));
        }
        cursor.close();
        return participacao;
    }

    // Ler apenas o tempo (horas, minutos, segundos) de uma Participação por ID
    public long readTimeInicialParticipacao(int id) {
        String[] args = {String.valueOf(id)};
        Cursor cursor = banco.rawQuery("SELECT tempo_inicio FROM participacao WHERE id_participacao = ?", args); // Corrigido

        long timeInicial = -1;
        if (cursor.moveToFirst()) {
            // Verificar se a coluna "tempo_inicio" existe
            int columnIndex = cursor.getColumnIndex("tempo_inicio");
            if (columnIndex == -1) {
                Log.e("DB_ERROR", "A coluna 'tempo_inicio' não foi encontrada.");
                return timeInicial; // Ou trate o erro de outra forma, como lançar uma exceção
            }

            // Recupera o valor de tempo_inicio como String
            String dataInicioStr = cursor.getString(columnIndex);

            // Define o formato para pegar apenas o tempo (HH:mm:ss)
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            try {
                // Converte a string de tempo em um objeto Date
                Date timeInicio = timeFormat.parse(dataInicioStr);

                // Converte o tempo em milissegundos (desde meia-noite)
                timeInicial = timeInicio.getTime();
            } catch (Exception e) {
                e.printStackTrace(); // Trate a exceção de parsing, se necessário
            }
        }

        cursor.close();
        return timeInicial;
    }


    //atualiza status
    public void updateStatus(Participacao p ) {
        ContentValues values = new ContentValues();
        values.put("status_conclusao", p.getStatusConclusao());
        values.put("tempo_fim", String.valueOf(p.getTempoFim()));
        values.put("tempo_registrado", String.valueOf(p.getTempoRegistrado()));
        String[] args = {String.valueOf(p.getIdParticipacao())};

        // Atualiza apenas o campo status da maratona com o id fornecido
        banco.update("participacao", values, "id_participacao=?", args);
    }


    // Obter todas as participações
    public List<Participacao> obterTodas() {
        List<Participacao> participacoes = new ArrayList<>();
        Cursor cursor = banco.query("participacao", new String[]{
                        "idParticipacao", "idInscricao", "statusConclusao", "tempoRegistrado", "tempoInicio", "tempoFim", "Passos"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Participacao participacao = new Participacao();
            participacao.setIdParticipacao(cursor.getInt(0));
            participacao.setIdInscricao(cursor.getInt(1));
            participacao.setStatusConclusao(cursor.getString(2));
            participacao.setTempoRegistrado(Time.valueOf(cursor.getString(3)));
            participacao.setTempoInicio(Time.valueOf(cursor.getString(4)));
            participacao.setTempoFim(Time.valueOf(cursor.getString(5)));
            participacao.setPassos(cursor.getInt(6));
            participacoes.add(participacao);
        }
        cursor.close();
        return participacoes;
    }

    // Obter participações por status de conclusão
    public List<Participacao> obterPorStatus(String statusConclusao) {
        List<Participacao> participacoes = new ArrayList<>();
        String[] args = {statusConclusao};
        Cursor cursor = banco.query("participacao",
                new String[]{"idParticipacao", "idInscricao", "statusConclusao", "tempoRegistrado", "tempoInicio", "tempoFim", "Passos"},
                "statusConclusao = ?", args, null, null, null);

        while (cursor.moveToNext()) {
            Participacao participacao = new Participacao();
            participacao.setIdParticipacao(cursor.getInt(0));
            participacao.setIdInscricao(cursor.getInt(1));
            participacao.setStatusConclusao(cursor.getString(2));
            participacao.setTempoRegistrado(Time.valueOf(cursor.getString(3)));
            participacao.setTempoInicio(Time.valueOf(cursor.getString(4)));
            participacao.setTempoFim(Time.valueOf(cursor.getString(5)));
            participacao.setPassos(cursor.getInt(6));
            participacoes.add(participacao);
        }
        cursor.close();
        return participacoes;
    }

    public int getIdParticipacao(int idInscricao) {
        String[] args = {String.valueOf(idInscricao)};

        // Query para buscar o id_participacao baseado no id_inscricao
        Cursor cursor = banco.query("participacao", new String[]{"id_participacao"},
                "id_inscricao=?", args, null, null, null);

        int idParticipacao = -1; // Valor padrão caso não seja encontrada uma participação

        if (cursor.moveToFirst()) {
            idParticipacao = cursor.getInt(0); // Pega o id_participacao
        }

        cursor.close();

        return idParticipacao; // Retorna o id_participacao ou -1 se não encontrado
    }


}
