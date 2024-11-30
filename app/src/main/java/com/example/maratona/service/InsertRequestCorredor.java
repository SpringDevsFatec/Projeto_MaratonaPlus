package com.example.maratona.service;

import android.os.AsyncTask;

import com.example.maratona.util.ConnectionFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class InsertRequestCorredor extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        try {
            // Configura a conexão
            URL insert = new URL("http://" + ConnectionFactory.serverIP + "/corredores");
            HttpURLConnection connection = (HttpURLConnection) insert.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Envia o JSON no corpo da requisição
            try (PrintStream printStream = new PrintStream(connection.getOutputStream())) {
                printStream.println(strings[0]);
            }

            connection.connect();

            // Lê a resposta completa
            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                scanner.useDelimiter("\\A"); // Lê tudo
                if (scanner.hasNext()) {
                    return scanner.next();
                } else {
                    return ""; // Resposta vazia
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ""; // Retorna vazio em caso de erro
        }
    }

}