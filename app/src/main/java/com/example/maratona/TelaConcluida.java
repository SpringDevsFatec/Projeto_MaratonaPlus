package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TelaConcluida extends AppCompatActivity {
    private int userId,maratonaId, distanciaMaratona, idinscricao, idParticipacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_concluida);
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




    }

    public void ScanQrcode(View view) {
        Log.d("TelaHub", "Sending userId to EditarUsuario: " + userId);  // Log para verificar o ID
        Intent it = new Intent(this, ScanQRCodeConcuir.class);
        it.putExtra("maratonaId", maratonaId);
        it.putExtra("id", userId);
        it.putExtra("inscricaoId", idinscricao);
        it.putExtra("participacaoId", idParticipacao);
        startActivity(it);
    }
}