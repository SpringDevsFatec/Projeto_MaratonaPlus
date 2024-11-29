package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.dao.ParticipacaoDAO;
import com.example.maratona.model.Maratonas;

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
        userId = intent.getIntExtra("userId", -1);
        maratonaId = intent.getIntExtra("maratonaId", -1);
        distanciaMaratona = intent.getIntExtra("distancia", -1);
        idInscricao = intent.getIntExtra("inscricaoId", -1);
        idParticipacao = intent.getIntExtra("participacaoId", -1);

        MaratonasDAO mdao = new MaratonasDAO(this);

        // Executor para tarefas agendadas
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Tarefa a ser repetida
        Runnable task = () -> {
            try {
                // Verifica se a maratona está em andamento
                boolean emAndamento = mdao.verificaEmAndamento(maratonaId);

                if (emAndamento) {
                    runOnUiThread(() -> {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Certifique-se de que o executor seja encerrado para evitar vazamento de recursos
        ExecutorService scheduler = null;
        scheduler.shutdownNow();
    }
}