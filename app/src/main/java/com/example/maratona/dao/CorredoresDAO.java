package com.example.maratona.dao;

import static com.example.maratona.util.ConnectionFactory.FormConnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.maratona.model.Corredores;
import com.example.maratona.service.InsertRequestCorredor;
import com.example.maratona.service.InsertRequestCorredorLogin;
import com.example.maratona.util.ConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CorredoresDAO {

    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public CorredoresDAO(Context context) {
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }

    // Inserir um novo corredor
    public long insert(Corredores corredor) {
        if ("Online".equals(FormConnect)) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                String jsonUser = objectMapper.writeValueAsString(corredor);

                InsertRequestCorredor insertRequest = new InsertRequestCorredor();
                String jsonString = insertRequest.execute(jsonUser).get();
                Log.i("jsonInsert", jsonString);
                Map<String, Object> map = objectMapper.readValue(jsonString, Map.class);
                Corredores corredorRetornado = objectMapper.readValue(jsonString, Corredores.class);

                // Retorna o ID do corredor inserido
                return corredorRetornado.getIdCorredor();
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
            values.put("nome", corredor.getNome());
            values.put("telefone", corredor.getTelefone());
            values.put("email", corredor.getEmail());
            values.put("senha", corredor.getSenha());
            values.put("cpf", corredor.getCpf());
            values.put("endereco", corredor.getEndereco());
            values.put("pais_origem", corredor.getPaisOrigem());
            return banco.insert("corredor", null, values);

        }
    }

    // Atualizar dados do corredor
    public void update(Corredores corredor) {
        ContentValues values = new ContentValues();
        values.put("nome", corredor.getNome());
        values.put("telefone", corredor.getTelefone());
        values.put("email", corredor.getEmail());
        values.put("senha", corredor.getSenha());
        values.put("cpf", corredor.getCpf());
        values.put("endereco", corredor.getEndereco());
        values.put("pais_origem", corredor.getPaisOrigem());
        String[] args = {String.valueOf(corredor.getIdCorredor())};
        banco.update("corredor", values, "id_corredor=?", args);
    }

    // Deletar um corredor
    public void delete(Corredores corredor) {
        String[] args = {String.valueOf(corredor.getIdCorredor())};
        banco.delete("corredor", "id_corredor=?", args);
    }

    // Obter todos os corredores
    public List<Corredores> obterTodos() {
        List<Corredores> corredores = new ArrayList<>();
        Cursor cursor = banco.query("corredor", new String[]{"id_corredor", "nome", "telefone", "email", "senha", "cpf", "endereco", "pais_origem"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            Corredores corredor = new Corredores();
            corredor.setIdCorredor(cursor.getInt(0));
            corredor.setNome(cursor.getString(1));
            corredor.setTelefone(cursor.getString(2));
            corredor.setEmail(cursor.getString(3));
            corredor.setSenha(cursor.getString(4));
            corredor.setCpf(cursor.getString(5));
            corredor.setEndereco(cursor.getString(6));
            corredor.setPaisOrigem(cursor.getString(7));
            corredores.add(corredor);
        }
        cursor.close(); // Não se esqueça de fechar o cursor
        return corredores;
    }

    // Ler um corredor pelo ID
    public Corredores read(int id) {
        String[] args = {String.valueOf(id)};
        Cursor cursor = banco.query("corredor", new String[]{"id_corredor", "nome", "telefone", "email", "senha", "cpf", "endereco", "pais_origem"},
                "id_corredor=?", args, null, null, null);
        Corredores corredor = new Corredores();
        if (cursor.moveToFirst()) {
            corredor.setIdCorredor(cursor.getInt(0));
            corredor.setNome(cursor.getString(1));
            corredor.setTelefone(cursor.getString(2));
            corredor.setEmail(cursor.getString(3));
            corredor.setSenha(cursor.getString(4));
            corredor.setCpf(cursor.getString(5));
            corredor.setEndereco(cursor.getString(6));
            corredor.setPaisOrigem(cursor.getString(7));
        }
        cursor.close(); // Não se esqueça de fechar o cursor
        return corredor;
    }

    // Verificar se um corredor existe pelo email e senha
    public boolean verificarLoginCorredor(String email, String senha) {
        String[] columns = {"id_corredor"};  // Coluna que queremos obter
        String selection = "email = ? AND senha = ?";
        String[] selectionArgs = {email, senha};

        Cursor cursor = banco.query("corredor", columns, selection, selectionArgs, null, null, null);

        // Se o cursor tiver algum resultado, significa que a combinação de email e senha existe
        boolean existe = cursor.getCount() > 0;
        cursor.close(); // Não se esqueça de fechar o cursor para liberar recursos
        return existe;
    }
    // Verificar login da empresa e retornar o id_empresa
    public int verificarLoginCorredorId(String email, String senha) {
        if ("Online".equals(FormConnect)) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                // Cria o objeto com email e senha
                Corredores c = new Corredores();
                c.setEmail(email);
                c.setSenha(senha);

                // Converte para JSON
                String jsonUser = objectMapper.writeValueAsString(c);

                // Faz a requisição e obtém a resposta
                InsertRequestCorredorLogin insertRequest = new InsertRequestCorredorLogin();
                String jsonString = insertRequest.execute(jsonUser).get();

                // Converte diretamente o JSON (número) para um inteiro
                return objectMapper.readValue(jsonString, Integer.class);
            } catch (ExecutionException | InterruptedException | JsonProcessingException ex) {
                ex.printStackTrace(); // Log da exceção
                return -1; // Retorna -1 em caso de falha
            }
        } else {
            String[] columns = {"id_corredor"}; // Coluna que queremos obter
            String selection = "email = ? AND senha = ?";
            String[] selectionArgs = {email, senha};

            Cursor cursor = null;
            try {
                cursor = banco.query("corredor", columns, selection, selectionArgs, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getInt(0); // Retorna o id_corredor
                }
                return -1; // Retorna -1 se não encontrar
            } catch (Exception ex) {
                ex.printStackTrace(); // Log da exceção
                return -1;
            } finally {
                if (cursor != null) {
                    cursor.close(); // Fecha o cursor
                }
            }
        }
    }



}
