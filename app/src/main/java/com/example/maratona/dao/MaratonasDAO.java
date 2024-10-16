package com.example.maratona.dao;

import android.database.sqlite.SQLiteDatabase;

import com.example.maratona.model.Maratonas;
import com.example.maratona.util.ConnectionFactory;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class MaratonasDAO {

    private ConnectionFactory conexao;
    private SQLiteDatabase banco;

    public MaratonasDAO(Context context) {
        conexao = new ConnectionFactory(context);
        banco = conexao.getWritableDatabase();
    }

    // Inserir
    public long insert(Maratonas maratona) {
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
        String[] args = {String.valueOf(maratona.getId())};
        banco.update("maratona", values, "id_maratona=?", args);
    }

    // Fechar maratona
    public void fecharMaratona(int idMaratona) {
        ContentValues values = new ContentValues();
        values.put("status", "fechado");
        String[] args = {String.valueOf(idMaratona)};
        banco.update("maratona", values, "id_maratona=?", args);
    }

    // Deletar
    public void delete(Maratonas maratona) {
        String[] args = {String.valueOf(maratona.getId())};
        banco.delete("maratona", "id_maratona=?", args);
    }

    public Maratonas readMaratona(int id) {
        String[] args = {String.valueOf(id)};
        // Usando JOIN para buscar o nome da empresa (criador) na tabela empresa
        Cursor cursor = banco.rawQuery(
                "SELECT m.id_maratona, e.nome AS nome_criador, m.Nome, m.local, m.data_inicio, m.data_final, m.status, m.distancia, m.descricao, m.limite_participantes, m.regras, m.valor, m.tipo_terreno, m.clima_esperado " +
                        "FROM maratona m " +
                        "JOIN empresa e ON m.criador = e.id_empresa " +
                        "WHERE m.id_maratona = ?", args);

        Maratonas maratona = new Maratonas();
        if (cursor.moveToFirst()) {
            maratona.setId(cursor.getInt(0));
            maratona.setNomeCriador(cursor.getString(1)); // Nome da empresa (criador)
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
            maratona.setId(cursor.getInt(0));
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

        // Query para buscar maratonas onde o status é 'aberta'
        Cursor cursor = banco.query("maratona",
                new String[]{"id_maratona", "criador", "nome", "local", "data_inicio", "status", "distancia", "descricao", "limite_participantes", "regras", "valor", "data_final", "tipo_terreno", "clima_esperado"},
                "status = ?", // Cláusula WHERE
                new String[]{"aberta"}, // Parâmetro para o WHERE
                null, null, null);

        while (cursor.moveToNext()) {
            Maratonas maratona = new Maratonas();
            maratona.setId(cursor.getInt(0));
            maratona.setCriador(cursor.getInt(1));
            maratona.setNome(cursor.getString(2));
            maratona.setLocal(cursor.getString(3));
            maratona.setData_inicio(cursor.getString(4)); // DATETIME
            maratona.setStatus(cursor.getString(5));
            maratona.setDistancia(cursor.getString(6));
            maratona.setDescricao(cursor.getString(7));
            maratona.setLimite_participantes(cursor.getInt(8));
            maratona.setRegras(cursor.getString(9));
            maratona.setValor(cursor.getFloat(10));
            maratona.setData_final(cursor.getString(11)); // TIMESTAMP
            maratona.setTipo_terreno(cursor.getString(12));
            maratona.setClima_esperado(cursor.getString(13));
            maratonas.add(maratona);
        }

        cursor.close(); // Não se esqueça de fechar o cursor
        return maratonas;
    }


    // Obter todas as maratonas com status "fechado"
    public List<Maratonas> obterMaratonasFechadas() {
        List<Maratonas> maratonas = new ArrayList<>();

        // Query para buscar maratonas onde o status é 'fechado'
        Cursor cursor = banco.query("maratona",
                new String[]{"id_maratona", "criador", "nome", "local", "data_inicio", "status", "distancia", "descricao", "limite_participantes", "regras", "valor", "data_final", "tipo_terreno", "clima_esperado"},
                "status = ?", // Cláusula WHERE
                new String[]{"fechado"}, // Parâmetro para o WHERE
                null, null, null);

        while (cursor.moveToNext()) {
            Maratonas maratona = new Maratonas();
            maratona.setId(cursor.getInt(0));
            maratona.setCriador(cursor.getInt(1));
            maratona.setNome(cursor.getString(2));
            maratona.setLocal(cursor.getString(3));
            maratona.setData_inicio(cursor.getString(4)); // DATETIME
            maratona.setStatus(cursor.getString(5));
            maratona.setDistancia(cursor.getString(6));
            maratona.setDescricao(cursor.getString(7));
            maratona.setLimite_participantes(cursor.getInt(8));
            maratona.setRegras(cursor.getString(9));
            maratona.setValor(cursor.getFloat(10));
            maratona.setData_final(cursor.getString(11)); // TIMESTAMP
            maratona.setTipo_terreno(cursor.getString(12));
            maratona.setClima_esperado(cursor.getString(13));
            maratonas.add(maratona);
        }

        cursor.close(); // Não se esqueça de fechar o cursor
        return maratonas;
    }


    // Obter maratonas por criador
    public List<Maratonas> obterMaratonasPorCriador(int idCriador) {
        List<Maratonas> maratonas = new ArrayList<>();
        String[] args = {String.valueOf(idCriador)};
        Cursor cursor = banco.query("maratona", new String[]{
                        "id_maratona", "criador", "nome", "local", "data_inicio",
                        "status", "distancia", "descricao", "limite_participantes",
                        "regras", "valor", "data_final", "tipo_terreno", "clima_esperado"},
                "criador=?", args, null, null, null);

        while (cursor.moveToNext()) {
            Maratonas maratona = new Maratonas();
            maratona.setId(cursor.getInt(0));
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
}

