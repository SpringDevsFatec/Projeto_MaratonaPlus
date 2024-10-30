package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TelaParticipando extends AppCompatActivity {

    private int userId,maratonaId,inscricaoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_participando);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Recebendo os dados do Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        maratonaId = intent.getIntExtra("maratonaId", -1);
        inscricaoId = intent.getIntExtra("inscricaoId", -1);


        Button buttonConcluir = findViewById(R.id.button);
        buttonConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaParticipando.this, ScanQRCodeConcuir.class);
                intent.putExtra("id", userId);
                intent.putExtra("maratonaId", maratonaId);
                intent.putExtra("inscricaoId", inscricaoId);
                startActivity(intent);
                finish();
            }
        });
        // Area esperado para o tema livre
    }
}
