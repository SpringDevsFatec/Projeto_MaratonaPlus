package com.example.maratona.dao;

import static com.example.maratona.util.ConnectionFactory.FormConnect;

import android.database.sqlite.SQLiteDatabase;

import com.example.maratona.model.Maratonas;
import com.example.maratona.service.GetRequestMaratonaCriador;
import com.example.maratona.service.GetRequestMaratonaId;
import com.example.maratona.service.GetRequestMaratonaStatus;
import com.example.maratona.service.InsertRequestMaratona;
import com.example.maratona.service.UpdateRequestMaratonaAbrir;
import com.example.maratona.service.UpdateRequestMaratonaCancelar;
import com.example.maratona.service.UpdateRequestMaratonaConcluir;
import com.example.maratona.service.UpdateRequestMaratonaIniciar;
import com.example.maratona.service.UpdateRequestMaratonaStatus;
import com.example.maratona.util.ConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MaratonasDAO {

    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public MaratonasDAO(Context context) {
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }

    // Inserir
    public long insert(Maratonas maratona) {
        if ("Online".equals(FormConnect)) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                String jsonUser = objectMapper.writeValueAsString(maratona);

                InsertRequestMaratona insertRequest = new InsertRequestMaratona();
                String jsonString = insertRequest.execute(jsonUser).get();
                Log.i("jsonInsert", jsonString);
                Map<String, Object> map = objectMapper.readValue(jsonString, Map.class);
                Maratonas maratonaRetornado = objectMapper.readValue(jsonString,Maratonas.class);

                // Retorna o ID do corredor inserido
                return maratonaRetornado.getIdMaratona();
            } catch (ExecutionException | InterruptedException e) {
                // Lida com problemas na execução assíncrona
                e.printStackTrace();
                throw new RuntimeException("Erro ao executar a inserção online.", e);
            } catch (JsonProcessingException e) {
                // Lida com erros de serialização/deserialização JSON
                e.printStackTrace();
                throw new RuntimeException("Erro ao processar JSON.", e);
            }
        } else {
            ContentValues values = new ContentValues();
            values.put("criador", maratona.getCriador());
            values.put("nome", maratona.getNome());
            values.put("local", maratona.getLocal());
            values.put("data_inicio", maratona.getData_inicio()); // Armazenar como string
            values.put("status", maratona.getStatus());
            values.put("distancia", maratona.getDistancia());
            values.put("descricao", maratona.getDescricao());
            values.put("limite_participantes", maratona.getLimite_participantes());
            values.put("regras", maratona.getRegras());
            values.put("valor", maratona.getValor());
            values.put("data_final", maratona.getData_final()); // Armazenar como string
            values.put("tipo_terreno", maratona.getTipo_terreno());
            values.put("clima_esperado", maratona.getClima_esperado());
            return banco.insert("maratona", null, values);
        }
    }

    // Atualizar
    public void update(Maratonas maratona) {

        ContentValues values = new ContentValues();
        values.put("criador", maratona.getCriador());
        values.put("nome", maratona.getNome());
        values.put("local", maratona.getLocal());
        values.put("data_inicio", maratona.getData_inicio());
        values.put("status", maratona.getStatus());
        values.put("distancia", maratona.getDistancia());
        values.put("descricao", maratona.getDescricao());
        values.put("limite_participantes", maratona.getLimite_participantes());
        values.put("regras", maratona.getRegras());
        values.put("valor", maratona.getValor());
        values.put("data_final", maratona.getData_final());
        values.put("tipo_terreno", maratona.getTipo_terreno());
        values.put("clima_esperado", maratona.getClima_esperado());
        String[] args = {String.valueOf(maratona.getIdMaratona())};
        banco.update("maratona", values, "id_maratona=?", args);
    }

    //atualiza status
    public void updateStatus(int idMaratona, Maratonas novoStatus) {

        if ("Online".equals(FormConnect)) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                String jsonUser = objectMapper.writeValueAsString(novoStatus);

                UpdateRequestMaratonaStatus insertRequest = new UpdateRequestMaratonaStatus();
                String jsonString = insertRequest.execute(String.valueOf(idMaratona),jsonUser).get();
                Log.i("UPDATE_RETURN", jsonString);


            } catch (ExecutionException | InterruptedException e) {
                // Lida com problemas na execução assíncrona
                e.printStackTrace();
                throw new RuntimeException("Erro ao executar a Update online.", e);
            } catch (JsonProcessingException e) {
                // Lida com erros de serialização/deserialização JSON
                e.printStackTrace();
                throw new RuntimeException("Erro ao processar JSON.", e);
            }
        }else {
            ContentValues values = new ContentValues();
            values.put("status", novoStatus.getStatus() );

            String[] args = {String.valueOf(idMaratona)};

            // Atualiza apenas o campo status da maratona com o id fornecido
            banco.update("maratona", values, "id_maratona=?", args);
        }
        }

    //atualiza status
    public void updateMaratonaAbrir(int idMaratona) {

        if ("Online".equals(FormConnect)) {
            try {
                UpdateRequestMaratonaAbrir insertRequest = new UpdateRequestMaratonaAbrir();
                String jsonString = insertRequest.execute(String.valueOf(idMaratona)).get();
                Log.i("UPDATE_ABRIR_RETURN", jsonString);

            } catch (ExecutionException | InterruptedException e) {
                // Lida com problemas na execução assíncrona
                e.printStackTrace();
                throw new RuntimeException("Erro ao executar a Update online.", e);
            }
        }
    }

    //atualiza status
    public void updateMaratonaCancelar(int idMaratona) {

        if ("Online".equals(FormConnect)) {
            try {
                UpdateRequestMaratonaCancelar insertRequest = new UpdateRequestMaratonaCancelar();
                String jsonString = insertRequest.execute(String.valueOf(idMaratona)).get();
                Log.i("UPDATE_CANCELAR_RETURN", jsonString);

            } catch (ExecutionException | InterruptedException e) {
                // Lida com problemas na execução assíncrona
                e.printStackTrace();
                throw new RuntimeException("Erro ao executar a Update online.", e);
            }
        }
    }

    //atualiza status
    public void updateMaratonaConcluir(int idMaratona) {

        if ("Online".equals(FormConnect)) {
            try {
                UpdateRequestMaratonaConcluir insertRequest = new UpdateRequestMaratonaConcluir();
                String jsonString = insertRequest.execute(String.valueOf(idMaratona)).get();
                Log.i("UPDATE_CONCLUIR_RETURN", jsonString);

            } catch (ExecutionException | InterruptedException e) {
                // Lida com problemas na execução assíncrona
                e.printStackTrace();
                throw new RuntimeException("Erro ao executar a Update online.", e);
            }
        }
    }

    //atualiza status
    public void updateMaratonaIniciar(int idMaratona) {
        if ("Online".equals(FormConnect)) {

            try {
                UpdateRequestMaratonaIniciar insertRequest = new UpdateRequestMaratonaIniciar();
                String jsonString = insertRequest.execute(String.valueOf(idMaratona)).get();
                Log.i("UPDATE_CONCLUIR_RETURN", jsonString);

            } catch (ExecutionException | InterruptedException e) {
                // Lida com problemas na execução assíncrona
                e.printStackTrace();
                throw new RuntimeException("Erro ao executar a Update online.", e);
            }

        }
    }


    // Fechar maratona
    public void fecharMaratona(int idMaratona) {
        ContentValues values = new ContentValues();
        values.put("status", "Finalizada");
        String[] args = {String.valueOf(idMaratona)};
        banco.update("maratona", values, "id_maratona=?", args);
    }

    // Deletar
    public void delete(Maratonas maratona) {
        String[] args = {String.valueOf(maratona.getIdMaratona())};
        banco.delete("maratona", "id_maratona=?", args);
    }

    public Maratonas readMaratona(int id) {
        Maratonas maratona = null;

        if ("Online".equals(FormConnect)) {
            try {
                // Faz a solicitação para obter o JSON correspondente ao ID
                GetRequestMaratonaId findByIdRequest = new GetRequestMaratonaId();
                String jsonString = findByIdRequest.execute(String.valueOf(id)).get();
                Log.i("MARATONA JSONNNNNNN",jsonString);
                if (jsonString != null && !jsonString.isEmpty()) {
                    maratona = parseMaratonaFromJson(jsonString);
                } else {
                    System.err.println("Erro: Resposta JSON vazia ou nula para o ID " + id);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException("Erro ao processar o JSON", e);
            }
        } else {
            maratona = getMaratonaFromDatabase(id);
        }

        return maratona;
    }

    private Maratonas parseMaratonaFromJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);

        // Acessar os dados da chave "maratona"
        JSONObject maratonajson = jsonObject.getJSONObject("maratona");

        Maratonas maratona = new Maratonas();
        maratona.setIdMaratona(maratonajson.getInt("idMaratona"));
        maratona.setCriador(maratonajson.getInt("criador"));
        maratona.setNome(maratonajson.getString("nome"));
        maratona.setLocal(maratonajson.getString("local"));
        maratona.setData_inicio(maratonajson.getString("data_inicio"));
        maratona.setData_final(maratonajson.getString("data_final"));
        maratona.setStatus(maratonajson.getString("status"));
        maratona.setDistancia(maratonajson.getString("distancia"));
        maratona.setDescricao(maratonajson.getString("descricao"));
        maratona.setLimite_participantes(maratonajson.getInt("limite_participantes"));
        maratona.setRegras(maratonajson.getString("regras"));
        maratona.setValor((float) maratonajson.getDouble("valor"));
        maratona.setTipo_terreno(maratonajson.getString("tipo_terreno"));
        maratona.setClima_esperado(maratonajson.getString("clima_esperado"));
        maratona.setNomeCriador(jsonObject.getString("nomeEmpresa"));

        return maratona;
    }

    private Maratonas getMaratonaFromDatabase(int id) {
        Maratonas maratona = null;
        String[] args = {String.valueOf(id)};
        Cursor cursor = null;

        try {
            cursor = banco.rawQuery(
                    "SELECT m.id_maratona, e.nome AS nome_criador, m.Nome, m.local, m.data_inicio, " +
                            "m.data_final, m.status, m.distancia, m.descricao, m.limite_participantes, " +
                            "m.regras, m.valor, m.tipo_terreno, m.clima_esperado " +
                            "FROM maratona m " +
                            "JOIN empresa e ON m.criador = e.id_empresa " +
                            "WHERE m.id_maratona = ?",
                    args
            );

            if (cursor != null && cursor.moveToFirst()) {
                maratona = new Maratonas();
                maratona.setIdMaratona(cursor.getInt(0));
                maratona.setNomeCriador(cursor.getString(1));
                maratona.setNome(cursor.getString(2));
                maratona.setLocal(cursor.getString(3));
                maratona.setData_inicio(cursor.getString(4));
                maratona.setData_final(cursor.getString(5));
                maratona.setStatus(cursor.getString(6));
                maratona.setDistancia(cursor.getString(7));
                maratona.setDescricao(cursor.getString(8));
                maratona.setLimite_participantes(cursor.getInt(9));
                maratona.setRegras(cursor.getString(10));
                maratona.setValor(cursor.getFloat(11));
                maratona.setTipo_terreno(cursor.getString(12));
                maratona.setClima_esperado(cursor.getString(13));
            } else {
                System.err.println("Erro: Nenhuma maratona encontrada para o ID " + id);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return maratona;
    }




    // Obter todas as maratonas
    public List<Maratonas> obterTodos() {
        List<Maratonas> maratonas = new ArrayList<>();
        Cursor cursor = banco.query("maratona", new String[]{
                        "id_maratona", "criador", "nome", "local", "data_inicio",
                        "status", "distancia", "descricao", "limite_participantes",
                        "regras", "valor", "data_final", "tipo_terreno", "clima_esperado"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            Maratonas maratona = new Maratonas();
            maratona.setIdMaratona(cursor.getInt(0));
            maratona.setCriador(cursor.getInt(1));
            maratona.setNome(cursor.getString(2));
            maratona.setLocal(cursor.getString(3));
            maratona.setData_inicio(cursor.getString(4));
            maratona.setStatus(cursor.getString(5));
            maratona.setDistancia(cursor.getString(6));
            maratona.setDescricao(cursor.getString(7));
            maratona.setLimite_participantes(cursor.getInt(8));
            maratona.setRegras(cursor.getString(9));
            maratona.setValor(cursor.getFloat(10));
            maratona.setData_final(cursor.getString(11));
            maratona.setTipo_terreno(cursor.getString(12));
            maratona.setClima_esperado(cursor.getString(13));
            maratonas.add(maratona);
        }
        cursor.close();
        return maratonas;
    }

    // Obter todas as maratonas com status "aberta"
    public List<Maratonas> obterMaratonasAbertas() {
        List<Maratonas> maratonas = new ArrayList<>();
        Log.i("MARATONAS_PROCESSAMENTO", "Iniciando busca de maratonas abertas...");

        try {
            // Realiza a requisição para obter os dados no formato JSON
            GetRequestMaratonaStatus request = new GetRequestMaratonaStatus(); // Classe para realizar a requisição
            String jsonString = request.execute("ABERTA_PARA_INSCRICAO").get(); // Executa e obtém a resposta como JSON string
            Log.i("MARATONAS_JSON", jsonString);

            // Usa o ObjectMapper para converter o JSON diretamente para uma lista de objetos Maratonas
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



    // Obter todas as maratonas com status "fechado"
    public List<Maratonas> obterMaratonasFechadas() {
        List<Maratonas> maratonas = new ArrayList<>();
        Log.i("MARATONAS_PROCESSAMENTO", "Iniciando busca de maratonas fechadas...");

        try {
            // Realiza a requisição para buscar as maratonas com status "fechado"
            GetRequestMaratonaStatus request = new GetRequestMaratonaStatus();
            String jsonString = request.execute("CONCLUIDA").get(); // Status "CONCLUIDA" como parâmetro
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


    // Obter maratonas por criador
    public List<Maratonas> obterMaratonasPorCriador(int idCriador) {
        List<Maratonas> maratonas = new ArrayList<>();
        Log.i("MARATONAS_PROCESSAMENTO", "Iniciando busca de maratonas por criador...");

        try {
            // Realiza a requisição para obter os dados no formato JSON
            GetRequestMaratonaCriador request = new GetRequestMaratonaCriador();
            String jsonString = request.execute(String.valueOf(idCriador)).get(); // Executa e obtém a resposta como JSON string
            Log.i("MARATONAS_JSON", jsonString);

            // Usa o ObjectMapper para converter o JSON diretamente para uma lista de objetos Maratonas
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
            Log.i("MARATONAS EM OBJETOOOOO" , maratonas.toString());
        return maratonas;
    }

}

