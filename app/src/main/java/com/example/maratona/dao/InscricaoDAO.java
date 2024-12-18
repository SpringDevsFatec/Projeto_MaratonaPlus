package com.example.maratona.dao;

import static com.example.maratona.util.ConnectionFactory.FormConnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.maratona.model.Corredores;
import com.example.maratona.model.Inscricao;
import com.example.maratona.model.Maratonas;
import com.example.maratona.service.DeleteRequestInscricao;
import com.example.maratona.service.GetRequestCorredoresConcluidos;
import com.example.maratona.service.GetRequestCorredoresInscritos;
import com.example.maratona.service.GetRequestCorredoresParticipando;
import com.example.maratona.service.GetRequestInscricaoPorCorredoreEMaratona;
import com.example.maratona.service.GetRequestMaratonaAbertaCorredor;
import com.example.maratona.service.GetRequestMaratonaConcluidaCorredor;
import com.example.maratona.service.InsertRequestInscricao;
import com.example.maratona.service.UpdateRequestInscricaoAtualizar;
import com.example.maratona.service.UpdateRequestInscricaoDesistente;
import com.example.maratona.service.UpdateRequestInscricaoFinalizado;
import com.example.maratona.service.UpdateRequestInscricaoParticipacao;
import com.example.maratona.util.ConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
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

        try {
            String jsonInscricao = objectMapper.writeValueAsString(inscricao);

            InsertRequestInscricao insertRequest = new InsertRequestInscricao();
            String jsonString = insertRequest.execute(jsonInscricao).get();
            Log.i("jsonInsert", jsonString);
            Map<String, Object> map = objectMapper.readValue(jsonString, Map.class);
            Inscricao inscricaoRetornada = objectMapper.readValue(jsonString, Inscricao.class);

            // Retorna o ID da inscrição inserida
            return inscricaoRetornada.getIdInscricao();
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

    // Atualizar uma inscrição existente
    public void update(Inscricao inscricao) {
        if ("Online".equals(FormConnect)) {
            try {
                // Cria o JSON do objeto Inscricao
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonString = objectMapper.writeValueAsString(inscricao);

                // Envia os dados para o servidor usando um PUT
                UpdateRequestInscricaoAtualizar updateRequest = new UpdateRequestInscricaoAtualizar();
                String response = updateRequest.execute(String.valueOf(inscricao.getIdInscricao()), jsonString).get();

                if (response == null || response.isEmpty()) {
                    System.err.println("Erro: Nenhuma resposta ao atualizar inscricao com ID " + inscricao.getIdInscricao());
                } else {
                    System.out.println("Inscricao atualizada com sucesso no servidor.");
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar o objeto Inscricao para JSON", e);
            }
        } else {
            // Atualização local no banco de dados SQLite
            ContentValues values = new ContentValues();
            values.put("id_corredor", inscricao.getIdCorredor());
            values.put("id_maratona", inscricao.getIdMaratona());
            values.put("data_hora", String.valueOf(inscricao.getDataHora()));
            values.put("forma_pagamento", inscricao.getFormaPagamento());
            values.put("status", inscricao.getFormaPagamento());
            String[] args = {String.valueOf(inscricao.getIdInscricao())};
            banco.update("inscricao", values, "id_inscricao=?", args);
        }
    }

    // Deletar uma inscrição
    public void delete(Inscricao inscricao) {
        if ("Online".equals(FormConnect)) {
            try {
                // Cria o JSON do objeto Inscricao
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonString = objectMapper.writeValueAsString(inscricao);

                DeleteRequestInscricao deleteRequest = new DeleteRequestInscricao();
                deleteRequest.execute(String.valueOf(inscricao.getIdInscricao()), jsonString).get();

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }   else {
            String[] args = {String.valueOf(inscricao.getIdInscricao())};
            banco.delete("inscricao", "id_inscricao=?", args);
        }
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
        Log.i("CORREDORES_MARATONA", "Iniciando busca de corredores participando da maratona...");

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

    public List<Corredores> obterCorredoresConcluidosPorMaratona(int idMaratona) {
        List<Corredores> corredores = new ArrayList<>();
        Log.i("CORREDORES_MARATONA", "Iniciando busca de corredores concluiram a maratona...");

        try {
            // Realiza a requisição para buscar os corredores concluiram a maratona
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

                    idInscricao = Integer.parseInt(jsonString);

            }

        } catch (ExecutionException | InterruptedException e) {
            Log.e("INSCRICAO_ERRO", "Erro ao executar a requisição: ", e);
        }

        return idInscricao; // Retorna o id_inscricao ou -1 se não encontrado
    }

    //atualiza status
    public void updateStatus(int idInscricao, String novoStatus) {
        if ("Online".equals(FormConnect)) {
            try {
                // Cria o JSON contendo apenas o campo a ser atualizado
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> partialUpdate = new HashMap<>();
                partialUpdate.put("status", novoStatus);

                String jsonString = objectMapper.writeValueAsString(partialUpdate);

                // Envia os dados para o servidor usando um PUT
                UpdateRequestInscricaoAtualizar updateRequest = new UpdateRequestInscricaoAtualizar();
                String response = updateRequest.execute(String.valueOf(idInscricao), jsonString).get();

                if (response == null || response.isEmpty()) {
                    System.err.println("Erro: Nenhuma resposta ao atualizar o status da inscrição com ID " + idInscricao);
                } else {
                    System.out.println("Status da inscrição atualizado com sucesso no servidor.");
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar o JSON para a atualização parcial", e);
            }
        } else {
            ContentValues values = new ContentValues();
            values.put("status", novoStatus);

            String[] args = {String.valueOf(idInscricao)};

            // Atualiza apenas o campo status da inscrição com o id fornecido
            banco.update("inscricao", values, "id_inscricao=?", args);
        }
    }

    public void updateStatusParaFinalizado(int idInscricao) {
        if ("Online".equals(FormConnect)) {
            try {
                // Cria a requisição para o servidor
                UpdateRequestInscricaoFinalizado updateRequest = new UpdateRequestInscricaoFinalizado();

                // Executa a atualização e obtém a resposta do servidor
                String response = updateRequest.execute(String.valueOf(idInscricao)).get();

                if (response == null || response.isEmpty()) {
                    System.err.println("Erro: Nenhuma resposta ao atualizar o status da inscrição com ID " + idInscricao);
                } else {
                    System.out.println("Status da inscrição atualizado com sucesso no servidor.");
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // Atualiza o status localmente no banco offline
            ContentValues values = new ContentValues();
            values.put("status", "FINALIZADO");

            String[] args = {String.valueOf(idInscricao)};

            // Atualiza o campo 'status' no SQLite
            banco.update("inscricao", values, "id_inscricao=?", args);
            System.out.println("Status atualizado localmente no banco offline.");
        }
    }

    public void updateStatusParaDesistente(int idInscricao) {
        if ("Online".equals(FormConnect)) {
            try {
                // Cria a requisição para o servidor
                UpdateRequestInscricaoDesistente updateRequest = new UpdateRequestInscricaoDesistente();

                // Executa a atualização e obtém a resposta do servidor
                String response = updateRequest.execute(String.valueOf(idInscricao)).get();

                if (response == null || response.isEmpty()) {
                    System.err.println("Erro: Nenhuma resposta ao atualizar o status da inscrição com ID " + idInscricao);
                } else {
                    System.out.println("Status da inscrição atualizado com sucesso no servidor.");
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // Atualiza o status localmente no banco offline
            ContentValues values = new ContentValues();
            values.put("status", "DESISTENTE");

            String[] args = {String.valueOf(idInscricao)};

            // Atualiza o campo 'status' no SQLite
            banco.update("inscricao", values, "id_inscricao=?", args);
            System.out.println("Status atualizado localmente no banco offline.");
        }
    }

    public void updateStatusParaParticipando(int idInscricao) {
        if ("Online".equals(FormConnect)) {
            try {
                // Cria a requisição para o servidor
                UpdateRequestInscricaoParticipacao updateRequest = new UpdateRequestInscricaoParticipacao();

                // Executa a atualização e obtém a resposta do servidor
                String response = updateRequest.execute(String.valueOf(idInscricao)).get();

                if (response == null || response.isEmpty()) {
                    System.err.println("Erro: Nenhuma resposta ao atualizar o status da inscrição com ID " + idInscricao);
                } else {
                    System.out.println("Status da inscrição atualizado com sucesso no servidor.");
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // Atualiza o status localmente no banco offline
            ContentValues values = new ContentValues();
            values.put("status", "DESISTENTE");

            String[] args = {String.valueOf(idInscricao)};

            // Atualiza o campo 'status' no SQLite
            banco.update("inscricao", values, "id_inscricao=?", args);
            System.out.println("Status atualizado localmente no banco offline.");
        }
    }
}
