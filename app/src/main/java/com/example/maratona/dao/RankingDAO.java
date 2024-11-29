package com.example.maratona.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.maratona.model.Corredores;
import com.example.maratona.model.Inscricao;
import com.example.maratona.model.Ranking;
import com.example.maratona.service.GetRequestCorredoresConcluidos;
import com.example.maratona.service.GetRequestRankingId;
import com.example.maratona.service.InsertRequestInscricao;
import com.example.maratona.service.InsertRequestRanking;
import com.example.maratona.util.ConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RankingDAO {

    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public RankingDAO(Context context) {
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }

    public List<Corredores> readByMaratona(int idMaratona) {
        List<Corredores> corredores = new ArrayList<>();
        Log.i("CORREDORES_MARATONA", "Iniciando busca de ranking dos corredores da maratona...");

        try {
            // Realiza a requisição para buscar o ranking dos corredores concluiram a maratona
            GetRequestRankingId request = new GetRequestRankingId();
            String jsonString = request.execute(String.valueOf(idMaratona)).get(); // ID da maratona como parâmetro
            Log.i("CORREDORES_JSON", jsonString);

            // Usa o ObjectMapper para converter o JSON em uma lista de objetos Corredores
            ObjectMapper objectMapper = new ObjectMapper();
            corredores = objectMapper.readValue(jsonString,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Corredores.class));

            // Log para verificar se a conversão foi bem-sucedida
            Log.i("CORREDORES_CONVERTIDOS", corredores.toString());

        } catch (ExecutionException | InterruptedException e) {
            Log.e("CORREDORES_ERRO", "Erro ao executar a requisição: ", e);
        } catch (JsonProcessingException e) {
            Log.e("CORREDORES_ERRO", "Erro ao processar o JSON: ", e);
        }

        return corredores;
    }

    public long insert(Ranking ranking) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonRanking = objectMapper.writeValueAsString(ranking);

            InsertRequestRanking insertRequest = new InsertRequestRanking();
            String jsonString = insertRequest.execute(jsonRanking).get();
            Log.i("jsonInsert", jsonString);
            Map<String, Object> map = objectMapper.readValue(jsonString, Map.class);
            Ranking rankingRetornado = objectMapper.readValue(jsonString, Ranking.class);

            // Retorna o ID do ranking gerado
            return rankingRetornado.getId();
        } catch (ExecutionException | InterruptedException e) {
            // Lida com problemas na execução assíncrona
            e.printStackTrace();
            throw new RuntimeException("Erro ao executar a inserção online.", e);
        } catch (JsonProcessingException e) {
            // Lida com erros de serialização/deserialização JSON
            e.printStackTrace();
            throw new RuntimeException("Erro ao processar JSON.", e);
        }
    }
}
