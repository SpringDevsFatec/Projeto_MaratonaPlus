package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.ParticipacaoDAO;
import com.example.maratona.model.Participacao;

public class TelaResultados extends AppCompatActivity {

    private int userId, maratonaId, distanciaMaratona, idinscricao, idparticipacao;
    private TextView title, idPartipacao, idInscricao, statusConclusao, tempoRegistrado;
    private TextView tempoInicio, tempoFim, velocidadeKm, velocidadeMs, passos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_resultados);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializando as variáveis primitivas com valores do Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        maratonaId = intent.getIntExtra("maratonaId", -1);
        distanciaMaratona = intent.getIntExtra("distancia", -1);
        idinscricao = intent.getIntExtra("inscricaoId", -1);
        idparticipacao = intent.getIntExtra("participacaoId", -1);

        // Instanciando os TextViews
        title = findViewById(R.id.title);
        idPartipacao = findViewById(R.id.idPartipacao);
        idInscricao = findViewById(R.id.idInscricao);
        statusConclusao = findViewById(R.id.statusConclusao);
        tempoRegistrado = findViewById(R.id.tempoRegistrado);
        tempoInicio = findViewById(R.id.tempoInicio);
        tempoFim = findViewById(R.id.tempoFim);
        velocidadeKm = findViewById(R.id.velocidadeKm);
        velocidadeMs = findViewById(R.id.velocidadeMs);
        passos = findViewById(R.id.passos);
    }

}