package com.example.maratona.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.maratona.model.Corredores;
import com.example.maratona.model.Inscricao;
import com.example.maratona.model.Maratonas;
import com.example.maratona.service.GetRequestCorredoresConcluidos;
import com.example.maratona.service.GetRequestCorredoresInscritos;
import com.example.maratona.service.GetRequestCorredoresParticipando;
import com.example.maratona.service.GetRequestInscricaoPorCorredoreEMaratona;
import com.example.maratona.service.GetRequestMaratonaAbertaCorredor;
import com.example.maratona.service.GetRequestMaratonaConcluidaCorredor;
import com.example.maratona.service.InsertRequestInscricao;
import com.example.maratona.util.ConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class InscricaoDAO {

    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public InscricaoDAO(Context context) {
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }

    // Inserir nova inscrição
    public long insert(Inscricao inscricao) {
        ObjectMapper objectMapper = new ObjectMapper();
        long idInscricao = -1;

        try {
            // Converte o objeto Inscricao para JSON
            String jsonInscricao = objectMapper.writeValueAsString(inscricao);

            // Realiza a requisição para inserir a inscrição
            InsertRequestInscricao insertRequest = new InsertRequestInscricao();
            String jsonResponse = insertRequest.execute(jsonInscricao).get();

            // Processa a resposta da API
            if (jsonResponse != null && !jsonResponse.isEmpty()) {
                // Supondo que a API retorne o ID da inscrição inserida
                Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);
                if (responseMap.containsKey("id_inscricao")) {
                    idInscricao = Long.parseLong(responseMap.get("id_inscricao").toString());
                }
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return idInscricao; // Retorna o id da inscrição inserida ou -1 se ocorrer algum erro
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
        Log.i("MARATONAS_CORREDOR", "Iniciando busca de maratonas abertas para o corredor...");

        try {
            // Realiza a requisição para buscar as maratonas abertas por corredor
            GetRequestMaratonaAbertaCorredor request = new GetRequestMaratonaAbertaCorredor();
            String jsonString = request.execute(String.valueOf(idCorredor)).get(); // ID do corredor como parâmetro
            Log.i("MARATONAS_JSON", jsonString);

            // Usa o ObjectMapper para converter o JSON em uma lista de objetos Maratonas
            ObjectMapper objectMapper = new ObjectMapper();
            maratonas = objectMapper.readValue(jsonString,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Maratonas.class));

            // Log para verificar se a conversão foi bem-sucedida
            Log.i("MARATONAS_CONVERTIDAS", maratonas.toString());

        } catch (ExecutionException | InterruptedException e) {
            Log.e("MARATONAS_ERRO", "Erro ao executar a requisição: ", e);
        } catch (JsonProcessingException e) {
            Log.e("MARATONAS_ERRO", "Erro ao processar o JSON: ", e);
        }

        return maratonas;
    }


    // Obter maratonas de um determinado corredor
    public List<Maratonas> obterMaratonasConcluidasPorCorredor(int idCorredor) {
        List<Maratonas> maratonas = new ArrayList<>();
        Log.i("MARATONAS_CORREDOR", "Iniciando busca de maratonas abertas para o corredor...");

        try {
            // Realiza a requisição para buscar as maratonas abertas por corredor
            GetRequestMaratonaConcluidaCorredor request = new GetRequestMaratonaConcluidaCorredor();
            String jsonString = request.execute(String.valueOf(idCorredor)).get(); // ID do corredor como parâmetro
            Log.i("MARATONAS_JSON", jsonString);

            // Usa o ObjectMapper para converter o JSON em uma lista de objetos Maratonas
            ObjectMapper objectMapper = new ObjectMapper();
            maratonas = objectMapper.readValue(jsonString,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Maratonas.class));

            // Log para verificar se a conversão foi bem-sucedida
            Log.i("MARATONAS_CONVERTIDAS", maratonas.toString());

        } catch (ExecutionException | InterruptedException e) {
            Log.e("MARATONAS_ERRO", "Erro ao executar a requisição: ", e);
        } catch (JsonProcessingException e) {
            Log.e("MARATONAS_ERRO", "Erro ao processar o JSON: ", e);
        }

        return maratonas;
    }


    public List<Corredores> obterCorredoresPorMaratona(int idMaratona) {
        List<Corredores> corredores = new ArrayList<>();
        Log.i("CORREDORES_MARATONA", "Iniciando busca de corredores inscritos para a maratona...");

        try {
            // Realiza a requisição para buscar os corredores inscritos na maratona
            GetRequestCorredoresInscritos request = new GetRequestCorredoresInscritos();
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
        Log.i("CORREDORES_MARATONA", "Iniciando busca de corredores inscritos para a maratona...");

        try {
            // Realiza a requisição para buscar os corredores inscritos na maratona
            GetRequestCorredoresConcluidos request = new GetRequestCorredoresConcluidos();
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

    public List<Corredores> obterCorredoresParticipando(int idMaratona) {
        List<Corredores> corredores = new ArrayList<>();
        Log.i("CORREDORES_MARATONA", "Iniciando busca de corredores inscritos para a maratona...");

        try {
            // Realiza a requisição para buscar os corredores inscritos na maratona
            GetRequestCorredoresParticipando request = new GetRequestCorredoresParticipando();
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
        int idInscricao = -1; // Valor padrão caso não seja encontrada a inscrição

        try {
            // Realiza a requisição para buscar a inscrição do corredor na maratona
            GetRequestInscricaoPorCorredoreEMaratona request = new GetRequestInscricaoPorCorredoreEMaratona();
            String jsonString = request.execute(String.valueOf(idCorredor), String.valueOf(idMaratona)).get();

            // Verifica se o JSON contém o id_inscricao
            if (jsonString != null && !jsonString.isEmpty()) {
                // Processa o JSON para obter o id_inscricao
                JSONObject jsonResponse = new JSONObject(jsonString);
                if (jsonResponse.has("id_inscricao")) {
                    idInscricao = jsonResponse.getInt("id_inscricao");
                }
            }

        } catch (ExecutionException | InterruptedException e) {
            Log.e("INSCRICAO_ERRO", "Erro ao executar a requisição: ", e);
        } catch (JSONException e) {
            Log.e("INSCRICAO_ERRO", "Erro ao processar o JSON: ", e);
        }

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
