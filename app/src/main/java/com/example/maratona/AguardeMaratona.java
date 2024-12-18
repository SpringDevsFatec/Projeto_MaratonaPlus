package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.dao.ParticipacaoDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AguardeMaratona extends AppCompatActivity {

    private int userId, maratonaId, distanciaMaratona, idInscricao, idParticipacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aguarde_maratona);

        // Configuração de bordas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recebendo dados da Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        maratonaId = intent.getIntExtra("maratonaId", -1);
        distanciaMaratona = intent.getIntExtra("distancia", -1);
        idInscricao = intent.getIntExtra("inscricaoId", -1);
        idParticipacao = intent.getIntExtra("participacaoId", -1);

        Log.i("USERID_AGUARDE", String.valueOf(userId));
        Log.i("MARATONAID_AGUARDE", String.valueOf(maratonaId));
        Log.i("INSCRICAOID_AGUARDE", String.valueOf(idInscricao));
        Log.i("DISTANCIA_AGUARDE", String.valueOf(distanciaMaratona));
        Log.i("PARTICIPACAOID_AGUARDE", String.valueOf(idParticipacao));

        MaratonasDAO mdao = new MaratonasDAO(this);

        // Verifica se a maratona está em andamento
        boolean emAndamento = mdao.verificaEmAndamento(maratonaId);

        if (emAndamento) {
            ParticipacaoDAO pdao = new ParticipacaoDAO(this);
            pdao.InicarParticipacao(idInscricao);
            showToast("Sua Prova já foi Iniciada, Boa Sorte!");

            Intent it = new Intent(AguardeMaratona.this, Cronometro.class);
            it.putExtra("maratonaId", maratonaId);
            it.putExtra("id", userId);
            it.putExtra("distancia", distanciaMaratona);
            it.putExtra("inscricaoId", idInscricao);
            it.putExtra("participacaoId", idParticipacao);

            startActivity(it);
            finish(); // Fecha a Activity atual
        }

        // Executor para tarefas agendadas
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Tarefa a ser repetida
        Runnable task = () -> {
            try {
                // Verifica se a maratona está em andamento
                boolean em_Andamento = mdao.verificaEmAndamento(maratonaId);

                runOnUiThread(() -> showToast("Aguarde a sua prova ser Iniciada!"));

                if (em_Andamento) {
                    runOnUiThread(() -> {
                        ParticipacaoDAO pdao = new ParticipacaoDAO(this);
                        pdao.InicarParticipacao(idParticipacao);
                        showToast("Prova Iniciada, Boa Sorte!");

                        Intent it = new Intent(AguardeMaratona.this, Cronometro.class);
                        it.putExtra("maratonaId", maratonaId);
                        it.putExtra("id", userId);
                        it.putExtra("distancia", distanciaMaratona);
                        it.putExtra("inscricaoId", idInscricao);
                        it.putExtra("participacaoId", idParticipacao);

                        startActivity(it);
                        scheduler.shutdown(); // Finaliza o executor
                        finish(); // Fecha a Activity atual
                    });
                }

            } catch (Exception e) {
                Log.e("AguardeMaratona", "Erro ao verificar maratona em andamento", e);
            }
        };

        // Agenda a tarefa para ser executada a cada 5 segundos
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(AguardeMaratona.this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Certifique-se de que o executor seja encerrado para evitar vazamento de recursos
        ExecutorService scheduler = null;
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}
