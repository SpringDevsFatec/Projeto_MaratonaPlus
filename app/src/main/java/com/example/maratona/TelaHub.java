package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TelaHub extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_hub);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Maratonas_Abertas(View view) {

        //Declarando uma variável do tipo Intent
        Intent it = new Intent(this, visualizar_abertas.class);

        //Iniciando a Tela desejada (tela 2)
        startActivity(it);
    }
    public void Maratonas_Concluidas(View view) {

        //Declarando uma variável do tipo Intent
        Intent it = new Intent(this, visualizarConcluidas.class);

        //Iniciando a Tela desejada (tela 2)
        startActivity(it);
    }
    public void Maratonas_Inscritas(View view) {

        //Declarando uma variável do tipo Intent
        Intent it = new Intent(this, visualizarInscritas.class);

        //Iniciando a Tela desejada (tela 2)
        startActivity(it);
    }

    public void voltar(View view){
        finish();
    }
}