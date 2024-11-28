package com.example.maratona.dao;

import static com.example.maratona.util.ConnectionFactory.FormConnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.maratona.model.Participacao;
import com.example.maratona.service.InsertRequestParticipacao;
import com.example.maratona.service.UpdateRequestEmpresa;
import com.example.maratona.util.ConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ParticipacaoDAO {

    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public ParticipacaoDAO(Context context) {
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }

    public long insertParticipacao(Participacao participacao) {
        if ("Online".equals(FormConnect)) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                // Serializa o objeto Participacao em JSON
                String jsonParticipacao = objectMapper.writeValueAsString(participacao);

                // Realiza a requisição de inserção
                InsertRequestParticipacao insertRequest = new InsertRequestParticipacao();
                String jsonString = insertRequest.execute(jsonParticipacao).get();

                Log.i("jsonInsert", jsonString);

                // Converte a resposta JSON de volta para um objeto Participacao
                Participacao participacaoRetornada = objectMapper.readValue(jsonString, Participacao.class);

                // Retorna o ID da participação inserida
                return participacaoRetornada.getIdParticipacao();
            } catch (ExecutionException | InterruptedException e) {
                // Lida com erros na execução assíncrona
                e.printStackTrace();
                throw new RuntimeException("Erro ao executar a inserção online.", e);
            } catch (JsonProcessingException e) {
                // Lida com erros de serialização/deserialização JSON
                e.printStackTrace();
                throw new RuntimeException("Erro ao processar JSON.", e);
            }
        } else {
            ContentValues values = new ContentValues();
            values.put("id_participacao", participacao.getIdParticipacao());
            values.put("id_inscricao", participacao.getIdInscricao());
            values.put("status_conclusao", participacao.getStatusConclusao());
            values.put("tempo_registrado", "0:0:0"); // Armazenar como string
            values.put("tempo_inicio", participacao.getTempoInicio().toString());
            values.put("tempo_fim", "0:0:0");
            values.put("passos", participacao.getPassos());

            // Insere no banco de dados local e retorna o ID gerado
            return banco.insert("participacao", null, values);
        }
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


    public void finalizarParticipacao(int id, int distancia, Participacao p) {

        try {
            // Cria o JSON do objeto Empresas
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(p);

            // Envia os dados para o servidor usando um PUT
            UpdateRequestEmpresa updateRequest = new UpdateRequestEmpresa();
            String response = updateRequest.execute(String.valueOf(id), String.valueOf(distancia), jsonString).get();

            if (response == null || response.isEmpty()) {
                System.err.println("Erro: Nenhuma resposta ao atualizar empresa com ID " + p.getIdParticipacao());
            } else {
                System.out.println("Empresa atualizada com sucesso no servidor.");
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar o objeto Empresas para JSON", e);
        }
    }

        // Ler uma Participacao por ID
        public Participacao readParticipacao ( int id){
            String[] args = {String.valueOf(id)};
            Cursor cursor = banco.rawQuery("SELECT * FROM participacao WHERE id_participacao = ?", args); // Corrigido

            Participacao participacao = new Participacao();
            if (cursor.moveToFirst()) {
                participacao.setIdParticipacao(cursor.getInt(0));
                participacao.setIdInscricao(cursor.getInt(1));
                participacao.setStatusConclusao(cursor.getString(2));
                participacao.setTempoRegistrado(String.valueOf(cursor.getString(3)));
                participacao.setTempoInicio(Time.valueOf(cursor.getString(4)));
                participacao.setTempoFim(Time.valueOf(cursor.getString(5)));
                participacao.setPassos(cursor.getInt(6));
            }
            cursor.close();
            return participacao;
        }


        public int getIdParticipacao ( int idInscricao){
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
