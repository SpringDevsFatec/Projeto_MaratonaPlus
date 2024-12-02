package com.example.maratona.service;

import android.os.AsyncTask;

import com.example.maratona.util.ConnectionFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteRequestParticipacao extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        try {
            URL delete = new URL("http://" + ConnectionFactory.serverIP + "/participacoes/" + strings[0] );
            HttpURLConnection connection = (HttpURLConnection) delete.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(15000);
            connection.connect();

            connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}