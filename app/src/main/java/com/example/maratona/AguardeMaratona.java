package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.dao.ParticipacaoDAO;
import com.example.maratona.model.Maratonas;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AguardeMaratona extends AppCompatActivity {

    private int userId,maratonaId, distanciaMaratona, idinscricao, idParticipacao;
    private String passa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aguarde_maratona);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        maratonaId = intent.getIntExtra("maratonaId", -1);
        distanciaMaratona = intent.getIntExtra("distancia", -1);
        idinscricao = intent.getIntExtra("inscricaoId", -1);
        idParticipacao = intent.getIntExtra("participacaoId", -1);
        MaratonasDAO mdao = new MaratonasDAO(this);
        // Cria um ScheduledExecutorService com um único thread
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // Tarefa a ser repetida
        Runnable task = () -> {
            try {
                //String passa = mdao.confereMaratona(maratonaId);


                if (passa.equals("true")){

                    ParticipacaoDAO pdao = new ParticipacaoDAO(AguardeMaratona.this);
                    //long idParticipacao = pdao.insertParticipacao();
                    Intent it = new Intent(this, Cronometro.class);
                    it.putExtra("maratonaId", maratonaId);
                    it.putExtra("id", userId);
                    it.putExtra("distancia", distanciaMaratona);
                    it.putExtra("inscricaoId", idinscricao);
                    it.putExtra("participacaoId", idParticipacao);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // Agenda a tarefa para ser executada a cada 5 segundos
        scheduler.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }




}