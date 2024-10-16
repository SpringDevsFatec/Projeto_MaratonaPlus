package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.InscricaoDAO;
import com.example.maratona.model.Maratonas;

import java.util.List;

public class VisualizarInscritas extends AppCompatActivity {

    ListView listViewInscritas;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visualizar_inscritas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        // Inicializando o ListView
        listViewInscritas = findViewById(R.id.listabertas);

        carregarMaratonasConcluidas();

        /* Adicionando o OnItemClickListener */
        listViewInscritas.setOnItemClickListener((parent, view, position, id) -> {
            Maratonas maratonaSelecionada = (Maratonas) parent.getItemAtPosition(position);

            // Cria um Intent para abrir o AlterarExcluirActivity com os dados do aluno
            Intent it = new Intent(VisualizarInscritas.this, TelaMaratona.class);
            it.putExtra("id", maratonaSelecionada.getId());
            it.putExtra("activity", "VisualizarInscritas");

            startActivityForResult(it, 1);
        });
    }

    private void carregarMaratonasConcluidas() {
        InscricaoDAO dao = new InscricaoDAO(this);
        List<Maratonas> listaMaratonas = dao.obterMaratonasPorCorredor(userId);

        // Criando um ArrayAdapter para mostrar a lista
        ArrayAdapter<Maratonas> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaMaratonas
        );

        listViewInscritas.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Verifica se o resultado é OK e se é a resposta da AlterarExcluirActivity
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Recarrega a lista de alunos
            carregarMaratonasConcluidas();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            // Recarrega a lista de alunos após adicionar
            carregarMaratonasConcluidas();
        }
    }
}