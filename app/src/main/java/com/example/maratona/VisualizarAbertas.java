package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.model.Maratonas;

import java.util.List;

public class VisualizarAbertas extends AppCompatActivity {

    ListView listViewAbertas;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visualizar_abertas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recebendo os dados do Intent
        Intent it = getIntent();
        userId = it.getIntExtra("id", -1);

        // Inicializando o ListView
        listViewAbertas = findViewById(R.id.listabertas);

        carregarMaratonasAbertas();

        /* Adicionando o OnItemClickListener */
        listViewAbertas.setOnItemClickListener((parent, view, position, id) -> {
            Maratonas maratonaSelecionada = (Maratonas) parent.getItemAtPosition(position);
            //Toast.makeText(this, String.valueOf(maratonaSelecionada.getId()), Toast.LENGTH_SHORT).show();
            // Cria um Intent para abrir o AlterarExcluirActivity com os dados do aluno
            Intent intent = new Intent(VisualizarAbertas.this, TelaMaratona.class);
            intent.putExtra("maratonaId", maratonaSelecionada.getId());
            intent.putExtra("id", userId);
            intent.putExtra("activity", "VisualizarAbertas");

            startActivityForResult(intent, 1);
        });
    }




    private void carregarMaratonasAbertas() {
        MaratonasDAO dao = new MaratonasDAO(this);
        List<Maratonas> listaMaratonas = dao.obterMaratonasAbertas();

        // Verificar se a lista está preenchida
        if (listaMaratonas == null || listaMaratonas.isEmpty()) {
            Toast.makeText(this, "Nenhuma maratona aberta encontrada", Toast.LENGTH_SHORT).show();
            Log.d("VisualizarAbertas", "Nenhuma maratona aberta encontrada");
        }

        // Criando um ArrayAdapter para mostrar a lista
        ArrayAdapter<Maratonas> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaMaratonas
        );

        listViewAbertas.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Verifica se o resultado é OK e se é a resposta da AlterarExcluirActivity
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Recarrega a lista de alunos
            carregarMaratonasAbertas();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            // Recarrega a lista de alunos após adi cionar
            carregarMaratonasAbertas();
        }
    }
}