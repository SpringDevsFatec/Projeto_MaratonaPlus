package com.example.maratona.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.maratona.model.Participacao;
import com.example.maratona.util.ConnectionFactory;

import java.sql.Time;
import java.util.ArrayList;
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
        values.put("idParticipacao", participacao.getIdParticipacao());
        values.put("idInscricao", participacao.getIdInscricao());
        values.put("statusConclusao", participacao.getStatusConclusao());
        values.put("tempoRegistrado", participacao.getTempoRegistrado().toString()); // Armazenar como string
        values.put("tempoInicio", participacao.getTempoInicio().toString());
        values.put("tempoFim", participacao.getTempoFim().toString());
        values.put("Passos", participacao.getPassos());
        return banco.insert("participacao", null, values);
    }

    // Atualizar
    public void update(Participacao participacao) {
        ContentValues values = new ContentValues();
        values.put("idInscricao", participacao.getIdInscricao());
        values.put("statusConclusao", participacao.getStatusConclusao());
        values.put("tempoRegistrado", participacao.getTempoRegistrado().toString());
        values.put("tempoInicio", participacao.getTempoInicio().toString());
        values.put("tempoFim", participacao.getTempoFim().toString());
        values.put("Passos", participacao.getPassos());
        String[] args = {String.valueOf(participacao.getIdParticipacao())};
        banco.update("participacao", values, "idParticipacao=?", args);
    }

    // Deletar
    public void delete(Participacao participacao) {
        String[] args = {String.valueOf(participacao.getIdParticipacao())};
        banco.delete("participacao", "idParticipacao=?", args);
    }

    // Ler uma Participacao por ID
    public Participacao readParticipacao(int id) {
        String[] args = {String.valueOf(id)};
        Cursor cursor = banco.rawQuery("SELECT * FROM participacao WHERE idParticipacao = ?", args);

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
}
