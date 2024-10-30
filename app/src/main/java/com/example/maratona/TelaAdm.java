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

public class TelaAdm extends AppCompatActivity {

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_adm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Recebendo os dados do Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
    }

    public void Criarmaratona(View view) {

        //Declarando uma variável do tipo Intent
        Intent it = new Intent(this, CadastrarMaratona.class);
        it.putExtra(
                "id",
                userId
        );

        //Iniciando a Tela desejada (tela 2)
        startActivity(it);
    }

    public void Visualizarmaratona(View view) {

        //Declarando uma variável do tipo Intent
        Intent it = new Intent(this, VisualizarCriadas.class);
        it.putExtra(
                "id",
                userId
        );

        startActivity(it);
    }

    public void Editar_Empresa(View view) {
        Log.d("TelaHub", "Sending userId to EditarUsuario: " + userId);  // Log para verificar o ID
        Intent it = new Intent(this, EditarEmpresa.class);
        it.putExtra("id", userId);
        startActivity(it);
    }
}