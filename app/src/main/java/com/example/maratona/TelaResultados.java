package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.ParticipacaoDAO;
import com.example.maratona.model.Participacao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TelaResultados extends AppCompatActivity {

    private int userId, maratonaId, idInscricao;
    private TextView title, idPartipacaoView, idInscricaoView, statusConclusaoView, tempoRegistradoView;
    private TextView tempoInicioView, tempoFimView, velocidadeKmView, velocidadeMsView, passosView;

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
        idInscricao = intent.getIntExtra("inscricaoId", -1);

        Log.i("USERID_RESULTADOS", String.valueOf(userId));
        Log.i("MARATONAID_RESULTADOS", String.valueOf(maratonaId));
        Log.i("INSCRICAOID_RESULTADOS", String.valueOf(idInscricao));

        // Instanciando os TextViews
        title = findViewById(R.id.title);
        idPartipacaoView = findViewById(R.id.idPartipacao);
        idInscricaoView = findViewById(R.id.idInscricao);
        statusConclusaoView = findViewById(R.id.statusConclusao);
        tempoRegistradoView = findViewById(R.id.tempoRegistrado);
        tempoInicioView = findViewById(R.id.tempoInicio);
        tempoFimView = findViewById(R.id.tempoFim);
        velocidadeKmView = findViewById(R.id.velocidadeKm);
        velocidadeMsView = findViewById(R.id.velocidadeMs);
        passosView = findViewById(R.id.passos);

        // Recuperando os dados do objeto Participacao a partir do banco de dados
        ParticipacaoDAO pdao = new ParticipacaoDAO(TelaResultados.this);
        Participacao p = pdao.readParticipacao(idInscricao);

        if (p != null) {
            // Definindo os valores nos TextViews
            title.setText("Detalhes da Participação");
            idPartipacaoView.setText("ID Participação: " + p.getIdParticipacao());
            idInscricaoView.setText("ID Inscrição: " + p.getIdInscricao());
            statusConclusaoView.setText("Status Conclusão: " + p.getStatusConclusao());
            tempoRegistradoView.setText("Tempo Registrado: " + p.getTempoRegistrado());
            tempoInicioView.setText("Tempo Início: " + formatarHora(p.getTempoInicio()));
            tempoFimView.setText("Tempo Fim: " + formatarHora(p.getTempoFim()));
            velocidadeKmView.setText("Velocidade (km/h): " + p.getVelocidadeKm());
            velocidadeMsView.setText("Velocidade (m/s): " + p.getVelocidadeMs());
            passosView.setText("Passos: " + p.getPassos());

            // Alterando a cor do texto caso o status seja "DESISTENCIA"
            if (p.getStatusConclusao().equals("DESISTENCIA")) {
                statusConclusaoView.setTextColor(getResources().getColor(R.color.red)); // Use uma cor definida no XML
            }
        } else {
            // Tratando o caso de nenhum dado encontrado
            title.setText("Participação não encontrada");
            idPartipacaoView.setText("");
            idInscricaoView.setText("");
            statusConclusaoView.setText("");
            tempoRegistradoView.setText("");
            tempoInicioView.setText("");
            tempoFimView.setText("");
            velocidadeKmView.setText("");
            velocidadeMsView.setText("");
            passosView.setText("");
        }
    }

    // Método para formatar data e hora
    private String formatarHora(String dataISO8601) {
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
        SimpleDateFormat formatoSaida = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        try {
            Date data = formatoEntrada.parse(dataISO8601);
            return formatoSaida.format(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return dataISO8601; // Retorna o valor original em caso de erro
        }
    }
}
