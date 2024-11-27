package com.example.maratona.dao;

import static com.example.maratona.util.ConnectionFactory.FormConnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.maratona.model.Corredores;
import com.example.maratona.model.Empresas;
import com.example.maratona.service.GetRequestEmpresaId;
import com.example.maratona.service.InsertRequestCorredor;
import com.example.maratona.service.InsertRequestEmpresa;
import com.example.maratona.service.InsertRequestEmpresaLogin;
import com.example.maratona.service.UpdateRequestEmpresa;
import com.example.maratona.util.ConnectionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EmpresasDAO {

    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public EmpresasDAO(Context context) {
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }

    // Inserir
    public long insert(Empresas empresa) {
        if ("Online".equals(FormConnect)) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                String jsonUser = objectMapper.writeValueAsString(empresa);

                InsertRequestEmpresa insertRequest = new InsertRequestEmpresa();
                String jsonString = insertRequest.execute(jsonUser).get();
                Log.i("jsonInsert", jsonString);
                Map<String, Object> map = objectMapper.readValue(jsonString, Map.class);
                Empresas empresaRetornado = objectMapper.readValue(jsonString, Empresas.class);

                // Retorna o ID do corredor inserido
                return empresaRetornado.getIdEmpresa();
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
            values.put("nome", empresa.getNome());
            values.put("telefone", empresa.getTelefone());
            values.put("email", empresa.getEmail());
            values.put("usuario", empresa.getUsuario());
            values.put("senha", empresa.getSenha());
            values.put("cnpj", empresa.getCnpj());
            values.put("local", empresa.getLocal());
            values.put("url_logo", empresa.getUrlLogo());
            values.put("data_criacao", String.valueOf(empresa.getDataCriacao())); // Se estiver usando Timestamp
            return banco.insert("empresa", null, values);

        }
    }

    public void update(Empresas empresa) {
        if ("Online".equals(FormConnect)) {
            try {
                // Cria o JSON do objeto Empresas
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonString = objectMapper.writeValueAsString(empresa);

                // Envia os dados para o servidor usando um PUT
                UpdateRequestEmpresa updateRequest = new UpdateRequestEmpresa();
                String response = updateRequest.execute(String.valueOf(empresa.getIdEmpresa()),jsonString).get();

                if (response == null || response.isEmpty()) {
                    System.err.println("Erro: Nenhuma resposta ao atualizar empresa com ID " + empresa.getIdEmpresa());
                } else {
                    System.out.println("Empresa atualizada com sucesso no servidor.");
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar o objeto Empresas para JSON", e);
            }
        } else {
            // Atualização local no banco de dados SQLite
            ContentValues values = new ContentValues();
            values.put("nome", empresa.getNome());
            values.put("telefone", empresa.getTelefone());
            values.put("email", empresa.getEmail());
            values.put("usuario", empresa.getUsuario());
            values.put("senha", empresa.getSenha());
            values.put("cnpj", empresa.getCnpj());
            values.put("local", empresa.getLocal());
            // Caso deseje atualizar a URL do logo, descomente a linha abaixo:
            // values.put("url_logo", empresa.getUrlLogo());
            String[] args = {String.valueOf(empresa.getIdEmpresa())};
            banco.update("empresa", values, "id_empresa=?", args);
        }
    }


    // Deletar
    public void delete(Empresas empresa) {
        String[] args = {String.valueOf(empresa.getIdEmpresa())};
        banco.delete("empresa", "id_empresa=?", args);
    }

    // Obter todas as empresas
    public List<Empresas> obterTodos() {
        List<Empresas> empresas = new ArrayList<>();
        Cursor cursor = banco.query("empresa", new String[]{"id_empresa", "nome", "telefone", "email", "usuario", "cnpj", "local", "url_logo", "data_criacao"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            Empresas empresa = new Empresas();
            empresa.setIdEmpresa(cursor.getInt(0));
            empresa.setNome(cursor.getString(1));
            empresa.setTelefone(cursor.getString(2));
            empresa.setEmail(cursor.getString(3));
            empresa.setUsuario(cursor.getString(4));
            empresa.setCnpj(cursor.getString(5));
            empresa.setLocal(cursor.getString(6));
            empresa.setUrlLogo(cursor.getString(7));
            empresa.setDataCriacao(Timestamp.valueOf(cursor.getString(8))); // Se estiver usando Timestamp
            empresas.add(empresa);
        }
        return empresas;
    }

    public Empresas read(int id) {
        Empresas empresa = null;

        if ("Online".equals(FormConnect)) {
            try {
                // Faz a solicitação para obter o JSON correspondente ao ID
                GetRequestEmpresaId findByIdRequest = new GetRequestEmpresaId();
                String jsonString = findByIdRequest.execute(String.valueOf(id)).get();

                if (jsonString != null && !jsonString.isEmpty()) {
                    // Desserializa o JSON diretamente para um objeto Empresas
                    ObjectMapper objectMapper = new ObjectMapper();
                    empresa = objectMapper.readValue(jsonString, Empresas.class);
                } else {
                    System.err.println("Erro: Resposta JSON vazia ou nula para o ID " + id);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao desserializar o JSON", e);
            }
        } else {
            String[] args = {String.valueOf(id)};
            Cursor cursor = null;
            try {
                cursor = banco.query(
                        "empresa",
                        new String[]{
                                "id_empresa", "nome", "telefone", "email", "usuario",
                                "cnpj", "local", "url_logo", "senha"
                        },
                        "id_empresa=?",
                        args,
                        null, null, null
                );

                if (cursor != null && cursor.moveToFirst()) {
                    empresa = new Empresas();
                    empresa.setIdEmpresa(cursor.getInt(0));
                    empresa.setNome(cursor.getString(1));
                    empresa.setTelefone(cursor.getString(2));
                    empresa.setEmail(cursor.getString(3));
                    empresa.setUsuario(cursor.getString(4));
                    empresa.setCnpj(cursor.getString(5));
                    empresa.setLocal(cursor.getString(6));
                    empresa.setUrlLogo(cursor.getString(7));
                    empresa.setSenha(cursor.getString(8));
                } else {
                    System.err.println("Erro: Nenhuma empresa encontrada para o ID " + id);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        return empresa;
    }



    public boolean verificarLoginEmpresa(String email, String senha) {
        String[] columns = {"id_empresa"};  // Você pode escolher retornar mais colunas se necessário
        String selection = "email = ? AND senha = ?";
        String[] selectionArgs = {email, senha};

        Cursor cursor = banco.query("empresa", columns, selection, selectionArgs, null, null, null);

        // Se o cursor tiver algum resultado, isso significa que a combinação de email e senha existe
        boolean existe = cursor.getCount() > 0;
        cursor.close(); // Não se esqueça de fechar o cursor para liberar recursos
        return existe;
    }

    public int verificarLoginEmpresaId(String email, String senha) {
        if ("Online".equals(FormConnect)) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                // Cria o objeto com email e senha
                Empresas e = new Empresas();
                e.setEmail(email);
                e.setSenha(senha);

                // Converte para JSON
                String jsonUser = objectMapper.writeValueAsString(e);

                // Faz a requisição e obtém a resposta
                InsertRequestEmpresaLogin insertRequest = new InsertRequestEmpresaLogin();
                String jsonString = insertRequest.execute(jsonUser).get();

                // Converte diretamente o JSON (número) para um inteiro
                return objectMapper.readValue(jsonString, Integer.class);
            } catch (ExecutionException | InterruptedException | JsonProcessingException ex) {
                ex.printStackTrace(); // Log da exceção
                Log.i("erro", "erro na chegada dos json");
                return -1; // Retorna -1 em caso de falha
            }
        } else {
            String[] columns = {"id_empresa"}; // Coluna que queremos obter
            String selection = "email = ? AND senha = ?";
            String[] selectionArgs = {email, senha};

            Cursor cursor = null;
            try {
                cursor = banco.query("empresa", columns, selection, selectionArgs, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    return cursor.getInt(0); // Retorna o id_empresa
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