package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.InscricaoDAO;
import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.model.Maratonas;

import java.util.List;

public class VisualizarConcluidas extends AppCompatActivity {

    ListView listViewConcluidas;
    private int userId;
    private TextView idid2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visualizar_concluidas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        idid2 = findViewById(R.id.idid2);
        idid2.setText(String.valueOf(userId));
        // Inicializando o ListView
        listViewConcluidas = findViewById(R.id.listConcluidas);

        carregarMaratonasConcluidas();

        /* Adicionando o OnItemClickListener */
        listViewConcluidas.setOnItemClickListener((parent, view, position, id) -> {
            Maratonas maratonaSelecionada = (Maratonas) parent.getItemAtPosition(position);

            // Cria um Intent para abrir o AlterarExcluirActivity com os dados do aluno
            Intent it = new Intent(VisualizarConcluidas.this, TelaMaratona.class);
            it.putExtra("maratonaId", maratonaSelecionada.getIdMaratona());
            it.putExtra("id", userId);
            it.putExtra("activity", "VisualizarConcluidas");

            startActivityForResult(it, 1);
        });
    }

    private void carregarMaratonasConcluidas() {
        InscricaoDAO dao = new InscricaoDAO(this);
        List<Maratonas> listaMaratonas = dao.obterMaratonasConcluidasPorCorredor(userId);

        // Criando um ArrayAdapter para mostrar a lista
        ArrayAdapter<Maratonas> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaMaratonas
        );
        Log.d("VisualizarConcluidas", "Maratonas encontradas: " + listaMaratonas.size());
        Toast.makeText(this, "Maratonas encontradas: " + listaMaratonas.size(), Toast.LENGTH_SHORT).show();
        listViewConcluidas.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                // Recarregar a lista após a TelaMaratona
                carregarMaratonasConcluidas();
            } else if (requestCode == 2) {
                // Recarregar a lista após a ScanQRCodeConcluir
                carregarMaratonasConcluidas();
            }
        }
    }
}