package com.example.maratona.service;

import android.os.AsyncTask;

import com.example.maratona.util.ConnectionFactory;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class UpdateRequestMaratonaIniciar extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        StringBuilder apiResponse = new StringBuilder();
        try {
            URL update = new URL("http://" + ConnectionFactory.serverIP + "/maratonas/" + strings[0] + "/iniciar" );
            HttpURLConnection connection = (HttpURLConnection) update.openConnection();

            connection.setRequestMethod("PUT");

            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.setDoOutput(true);

            PrintStream printStream = new PrintStream(connection.getOutputStream());
            printStream.println(strings[1]);

            connection.connect();

            String jsonResponse = new Scanner(connection.getInputStream()).next();

            return jsonResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiResponse.toString();
    }

}

