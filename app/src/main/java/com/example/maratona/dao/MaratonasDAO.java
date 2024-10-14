package com.example.maratona.dao;

import android.database.sqlite.SQLiteDatabase;

import com.example.maratona.model.Maratonas;
import com.example.maratona.util.ConnectionFactory;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.sql.Timestamp;
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
            values.put("id_empresa", maratona.getCriador());
            values.put("nome", maratona.getNome());
            values.put("local", maratona.getLocal());
            values.put("data_inicio", String.valueOf(maratona.getData_inicio())); // DATETIME
            values.put("criador", maratona.getCriador());
            values.put("status", maratona.getStatus());
            values.put("distancia", maratona.getDistancia());
            values.put("descricao", maratona.getDescricao());
            values.put("limite_participantes", maratona.getLimite_participantes());
            values.put("regras", maratona.getRegras());
            values.put("valor", maratona.getValor());
            values.put("data_final", String.valueOf(maratona.getData_final())); // TIMESTAMP
            values.put("tipo_terreno", maratona.getTipo_terreno());
            values.put("clima_esperado", maratona.getClima_esperado());
            return banco.insert("maratona", null, values);
        }

        // Atualizar
        public void update(Maratonas maratona) {
            ContentValues values = new ContentValues();
            values.put("id_empresa", maratona.getCriador());
            values.put("nome", maratona.getNome());
            values.put("local", maratona.getLocal());
            values.put("data_inicio", String.valueOf(maratona.getData_inicio())); // DATETIME
            values.put("criador", maratona.getCriador());
            values.put("status", maratona.getStatus());
            values.put("distancia", maratona.getDistancia());
            values.put("descricao", maratona.getDescricao());
            values.put("limite_participantes", maratona.getLimite_participantes());
            values.put("regras", maratona.getRegras());
            values.put("valor", maratona.getValor());
            values.put("data_final", String.valueOf(maratona.getData_final())); // TIMESTAMP
            values.put("tipo_terreno", maratona.getTipo_terreno());
            values.put("clima_esperado", maratona.getClima_esperado());
            String[] args = {String.valueOf(maratona.getId())};
            banco.update("maratona", values, "id_maratona=?", args);
        }

        // Atualizar status da maratona para "fechado"
        public void fecharMaratona(int idMaratona) {
            ContentValues values = new ContentValues();
            values.put("status", "fechado");

            // Atualiza a maratona com o id especificado
            String[] args = {String.valueOf(idMaratona)};
            banco.update("maratona", values, "id_maratona=?", args);
        }


        // Deletar
        public void delete(Maratonas maratona) {
            String[] args = {String.valueOf(maratona.getId())};
            banco.delete("maratona", "id_maratona=?", args);
        }

        // Obter todas as maratonas
        public List<Maratonas> obterTodos() {
            List<Maratonas> maratonas = new ArrayList<>();
            Cursor cursor = banco.query("maratona", new String[]{"id_maratona", "id_empresa", "nome", "local", "data_inicio", "criador", "status", "distancia", "descricao", "limite_participantes", "regras", "valor", "data_criacao", "tipo_terreno", "clima_esperado"},
                    null, null, null, null, null);
            while (cursor.moveToNext()) {
                Maratonas maratona = new Maratonas();
                maratona.setId(cursor.getInt(0));
                maratona.setCriador(cursor.getInt(1));
                maratona.setNome(cursor.getString(2));
                maratona.setLocal(cursor.getString(3));
                maratona.setData_inicio(Date.valueOf(cursor.getString(4))); // DATETIME
                maratona.setCriador(Integer.parseInt(cursor.getString(5)));
                maratona.setStatus(cursor.getString(6));
                maratona.setDistancia(cursor.getString(7));
                maratona.setDescricao(cursor.getString(8));
                maratona.setLimite_participantes(cursor.getInt(9));
                maratona.setRegras(cursor.getString(10));
                maratona.setValor(cursor.getFloat(11));
                maratona.setData_final(Date.valueOf(cursor.getString(12))); // TIMESTAMP
                maratona.setTipo_terreno(cursor.getString(13));
                maratona.setClima_esperado(cursor.getString(14));
                maratonas.add(maratona);
            }
            return maratonas;
        }



        // Obter todas as maratonas com status "aberta"
        public List<Maratonas> obterMaratonasAbertas() {
            List<Maratonas> maratonas = new ArrayList<>();

            // Query para buscar maratonas onde o status é 'aberta'
            Cursor cursor = banco.query("maratona",
                    new String[]{"id_maratona", "id_empresa", "nome", "local", "data_inicio", "criador", "status", "distancia", "descricao", "limite_participantes", "regras", "valor", "data_criacao", "tipo_terreno", "clima_esperado"},
                    "status = ?", // Cláusula WHERE
                    new String[]{"aberta"}, // Parâmetro para o WHERE
                    null, null, null);

            while (cursor.moveToNext()) {
                Maratonas maratona = new Maratonas();
                maratona.setId(cursor.getInt(0));
                maratona.setCriador(cursor.getInt(1));
                maratona.setNome(cursor.getString(2));
                maratona.setLocal(cursor.getString(3));
                maratona.setData_inicio(Date.valueOf(cursor.getString(4))); // DATETIME
                maratona.setCriador(Integer.parseInt(cursor.getString(5)));
                maratona.setStatus(cursor.getString(6));
                maratona.setDistancia(cursor.getString(7));
                maratona.setDescricao(cursor.getString(8));
                maratona.setLimite_participantes(cursor.getInt(9));
                maratona.setRegras(cursor.getString(10));
                maratona.setValor(cursor.getFloat(11));
                maratona.setData_final(Date.valueOf(cursor.getString(12))); // TIMESTAMP
                maratona.setTipo_terreno(cursor.getString(13));
                maratona.setClima_esperado(cursor.getString(14));
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
                    new String[]{"id_maratona", "id_empresa", "nome", "local", "data_inicio", "criador", "status", "distancia", "descricao", "limite_participantes", "regras", "valor", "data_criacao", "tipo_terreno", "clima_esperado"},
                    "status = ?", // Cláusula WHERE
                    new String[]{"fechado"}, // Parâmetro para o WHERE
                    null, null, null);

            while (cursor.moveToNext()) {
                Maratonas maratona = new Maratonas();
                maratona.setId(cursor.getInt(0));
                maratona.setCriador(cursor.getInt(1));
                maratona.setNome(cursor.getString(2));
                maratona.setLocal(cursor.getString(3));
                maratona.setData_inicio(Date.valueOf(cursor.getString(4))); // DATETIME
                maratona.setCriador(cursor.getInt(5));
                maratona.setStatus(cursor.getString(6));
                maratona.setDistancia(cursor.getString(7));
                maratona.setDescricao(cursor.getString(8));
                maratona.setLimite_participantes(cursor.getInt(9));
                maratona.setRegras(cursor.getString(10));
                maratona.setValor(cursor.getFloat(11));
                maratona.setData_final(Date.valueOf(cursor.getString(12))); // TIMESTAMP
                maratona.setTipo_terreno(cursor.getString(13));
                maratona.setClima_esperado(cursor.getString(14));
                maratonas.add(maratona);
            }

            cursor.close(); // Não se esqueça de fechar o cursor
            return maratonas;
        }

        // Obter todas as maratonas criadas por um determinado criador
        public List<Maratonas> obterMaratonasPorCriador(int idCriador) {
            List<Maratonas> maratonas = new ArrayList<>();

            // Query para buscar maratonas onde o id_criador é igual ao id passado
            Cursor cursor = banco.query("maratona",
                    new String[]{"id_maratona", "id_empresa", "nome", "local", "data_inicio", "criador", "status", "distancia", "descricao", "limite_participantes", "regras", "valor", "data_criacao", "tipo_terreno", "clima_esperado"},
                    "criador = ?", // Cláusula WHERE
                    new String[]{String.valueOf(idCriador)}, // Parâmetro para o WHERE
                    null, null, null);

            while (cursor.moveToNext()) {
                Maratonas maratona = new Maratonas();
                maratona.setId(cursor.getInt(0));
                maratona.setCriador(cursor.getInt(1));
                maratona.setNome(cursor.getString(2));
                maratona.setLocal(cursor.getString(3));
                maratona.setData_inicio(Date.valueOf(cursor.getString(4))); // DATETIME
                maratona.setCriador(cursor.getInt(5));
                maratona.setStatus(cursor.getString(6));
                maratona.setDistancia(cursor.getString(7));
                maratona.setDescricao(cursor.getString(8));
                maratona.setLimite_participantes(cursor.getInt(9));
                maratona.setRegras(cursor.getString(10));
                maratona.setValor(cursor.getFloat(11));
                maratona.setData_final(Date.valueOf(cursor.getString(12))); // TIMESTAMP
                maratona.setTipo_terreno(cursor.getString(13));
                maratona.setClima_esperado(cursor.getString(14));
                maratonas.add(maratona);
            }

            cursor.close(); // Não se esqueça de fechar o cursor
            return maratonas;
        }

        // Ler uma maratona pelo ID
        public Maratonas read(int id) {
            String[] args = {String.valueOf(id)};
            Cursor cursor = banco.query("maratona", new String[]{"id_maratona", "id_empresa", "nome", "local", "data_inicio", "criador", "status", "distancia", "descricao", "limite_participantes", "regras", "valor", "data_criacao", "tipo_terreno", "clima_esperado"},
                    "id_maratona=?", args, null, null, null);
            Maratonas maratona = new Maratonas();
            if (cursor.moveToFirst()) {
                maratona.setId(cursor.getInt(0));
                maratona.setCriador(cursor.getInt(1));
                maratona.setNome(cursor.getString(2));
                maratona.setLocal(cursor.getString(3));
                maratona.setData_inicio(Date.valueOf(cursor.getString(4))); // DATETIME
                maratona.setCriador(Integer.parseInt(cursor.getString(5)));
                maratona.setStatus(cursor.getString(6));
                maratona.setDistancia(cursor.getString(7));
                maratona.setDescricao(cursor.getString(8));
                maratona.setLimite_participantes(cursor.getInt(9));
                maratona.setRegras(cursor.getString(10));
                maratona.setValor(cursor.getFloat(11));
                maratona.setData_final(Date.valueOf(cursor.getString(12))); // TIMESTAMP
                maratona.setTipo_terreno(cursor.getString(13));
                maratona.setClima_esperado(cursor.getString(14));
            }
            return maratona;
        }
}
