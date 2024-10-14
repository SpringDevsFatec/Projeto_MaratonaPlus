package com.example.maratona.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.maratona.model.Empresas;
import com.example.maratona.util.ConnectionFactory;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EmpresasDAO {

    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public EmpresasDAO(Context context) {
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }

    // Inserir
    public long insert(Empresas empresa) {
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

    // Atualizar
    public void update(Empresas empresa) {
        ContentValues values = new ContentValues();
        values.put("nome", empresa.getNome());
        values.put("telefone", empresa.getTelefone());
        values.put("email", empresa.getEmail());
        values.put("usuario", empresa.getUsuario());
        values.put("senha", empresa.getSenha());
        values.put("cnpj", empresa.getCnpj());
        values.put("local", empresa.getLocal());
        values.put("url_logo", empresa.getUrlLogo());
        values.put("data_criacao", String.valueOf(empresa.getDataCriacao()));
        String[] args = {String.valueOf(empresa.getIdEmpresa())};
        banco.update("empresa", values, "id_empresa=?", args);
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

    // Ler uma empresa pelo ID
    public Empresas read(int id) {
        String[] args = {String.valueOf(id)};
        Cursor cursor = banco.query("empresa", new String[]{"id_empresa", "nome", "telefone", "email", "usuario", "cnpj", "local", "url_logo", "data_criacao"},
                "id_empresa=?", args, null, null, null);
        Empresas empresa = new Empresas();
        if (cursor.moveToFirst()) {
            empresa.setIdEmpresa(cursor.getInt(0));
            empresa.setNome(cursor.getString(1));
            empresa.setTelefone(cursor.getString(2));
            empresa.setEmail(cursor.getString(3));
            empresa.setUsuario(cursor.getString(4));
            empresa.setCnpj(cursor.getString(5));
            empresa.setLocal(cursor.getString(6));
            empresa.setUrlLogo(cursor.getString(7));
            empresa.setDataCriacao(Timestamp.valueOf(cursor.getString(8)));
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

    // Verificar login da empresa e retornar o id_empresa
    public int verificarLoginEmpresaId(String email, String senha) {
        String[] columns = {"id_empresa"};  // Coluna que queremos obter
        String selection = "email = ? AND senha = ?";
        String[] selectionArgs = {email, senha};

        Cursor cursor = banco.query("empresa", columns, selection, selectionArgs, null, null, null);

        // Se o cursor tiver algum resultado, significa que a combinação de email e senha existe
        int idEmpresa = -1; // Valor padrão para indicar falha
        if (cursor.moveToFirst()) {
            idEmpresa = cursor.getInt(0);  // Obtém o id_empresa da primeira (e única) linha
        }
        cursor.close(); // Não se esqueça de fechar o cursor para liberar recursos
        return idEmpresa; // Retorna o id da empresa ou -1 se não encontrar
    }


}