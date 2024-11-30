package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.InscricaoDAO;
import com.example.maratona.dao.ParticipacaoDAO;
import com.example.maratona.model.Participacao;
import com.google.android.material.button.MaterialButton;

import java.text.MessageFormat;
import java.util.Locale;

public class Cronometro extends AppCompatActivity {
    private int userId,maratonaId, distanciaMaratona, idinscricao, idParticipacao;
    TextView textView;
    MaterialButton stop;
    String timeBuff;
    int seconds, minutes, milliSeconds;
    long millisecondTime, startTime,Buff,  updateTime = 0L ;
    Handler handler;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            millisecondTime = SystemClock.uptimeMillis() - startTime;
            updateTime = Buff + millisecondTime;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSeconds = (int) (updateTime % 1000);

            textView.setText(MessageFormat.format("{0}:{1}:{2}", minutes, String.format(Locale.getDefault(), "%02d", seconds), String.format(Locale.getDefault(),"%01d", milliSeconds)));
            handler.postDelayed(this, 0);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cronometro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView = findViewById(R.id.textView);
        stop = findViewById(R.id.stop);

        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        maratonaId = intent.getIntExtra("maratonaId", -1);
        distanciaMaratona = intent.getIntExtra("distancia", -1);
        idinscricao = intent.getIntExtra("inscricaoId", -1);
        idParticipacao = intent.getIntExtra("participacaoId", -1);

        handler = new Handler(Looper.getMainLooper());

        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }
    public void onClick(View view) {
        timeBuff = String.valueOf(textView.getText());
        handler.removeCallbacks(runnable);

        Participacao p = new Participacao();
        p.setStatusConclusao("FINALIZADO");
        p.setTempoRegistrado(timeBuff);
        ParticipacaoDAO pdao = new ParticipacaoDAO(Cronometro.this);
        pdao.finalizarParticipacao(idParticipacao, distanciaMaratona, p);

        Toast.makeText(Cronometro.this, "Participação Finalizada!", Toast.LENGTH_SHORT).show();

        Intent it = new Intent(Cronometro.this, TelaConcluida.class);
        it.putExtra("maratonaId", maratonaId);
        it.putExtra("id", userId);
        it.putExtra("inscricaoId", idinscricao);
        it.putExtra("participacaoId", idParticipacao);
        startActivity(it);
        finishActivity(1);

    }

    public void CancelarParticipacao(View view){
        AlertDialog.Builder confirmaCancelamento = new AlertDialog.Builder(this, R.style.RoundedAlertDialog);
        confirmaCancelamento.setTitle("Alerta de Desistência!");
        confirmaCancelamento.setMessage("A sua Participação será Cancelada, tem certeza desta ação?");
        confirmaCancelamento.setIcon(R.drawable.logo);
        confirmaCancelamento.setCancelable(false);
        confirmaCancelamento.setPositiveButton("Sim", (dialogInterface, i) -> {
            /*Desisti Participação*/
            Participacao pa = new Participacao();
            pa.setStatusConclusao("DESISTENCIA");
            pa.setTempoRegistrado(timeBuff);
            ParticipacaoDAO pdao = new ParticipacaoDAO(Cronometro.this);
            pdao.finalizarParticipacao(idParticipacao, distanciaMaratona, pa);
            /*Desisti Inscricao*/
            InscricaoDAO m = new InscricaoDAO(Cronometro.this);
            m.updateStatusParaDesistente(idinscricao);
            Toast.makeText(Cronometro.this, "Participação Cancelada com Sucesso.", Toast.LENGTH_SHORT).show();
            /*Manda para tela de Maratonas Concluidas*/
            Intent it = new Intent(Cronometro.this, TelaConcluida.class);
            it.putExtra("id", userId);
            startActivity(it);
            finishActivity(1);
        });
        confirmaCancelamento.setNegativeButton("Não", null);

        AlertDialog dialog = confirmaCancelamento.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.green));
            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.red));
        });
        dialog.show();

    }
}