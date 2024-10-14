package com.example.maratona.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.maratona.model.Corredores;
import com.example.maratona.util.ConnectionFactory;
import java.util.ArrayList;
import java.util.List;

public class CorredoresDAO {

    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public CorredoresDAO(Context context) {
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }

    // Inserir um novo corredor
    public long insert(Corredores corredor) {
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
        String[] columns = {"id_corredor"};  // Coluna que queremos obter
        String selection = "email = ? AND senha = ?";
        String[] selectionArgs = {email, senha};

        Cursor cursor = banco.query("corredor", columns, selection, selectionArgs, null, null, null);

        // Se o cursor tiver algum resultado, significa que a combinação de email e senha existe
        int idCorredor = -1; // Valor padrão para indicar falha
        if (cursor.moveToFirst()) {
            idCorredor = cursor.getInt(0);  // Obtém o id_empresa da primeira (e única) linha
        }
        cursor.close(); // Não se esqueça de fechar o cursor para liberar recursos
        return idCorredor; // Retorna o id da empresa ou -1 se não encontrar
    }
}
