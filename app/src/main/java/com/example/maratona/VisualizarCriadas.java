package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.model.Maratonas;

import java.util.List;

public class VisualizarCriadas extends AppCompatActivity {

    ListView listViewCriadas;
    private int userId;
    private TextView idid2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visualizar_criadas);
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
        listViewCriadas = findViewById(R.id.listabertas);

        carregarMaratonasCriadas();

        /* Adicionando o OnItemClickListener */
        listViewCriadas.setOnItemClickListener((parent, view, position, id) -> {
            Maratonas maratonaSelecionada = (Maratonas) parent.getItemAtPosition(position);

            // Cria um Intent para abrir o AlterarExcluirActivity com os dados do aluno
            Intent it = new Intent(VisualizarCriadas.this, TelaMaratonaAdm.class);
            it.putExtra("maratonaId", maratonaSelecionada.getId());
            it.putExtra("id", userId);
            it.putExtra("activity", "VisualizarCriadas");

            startActivityForResult(it, 1);
        });
    }
    private void carregarMaratonasCriadas() {
        MaratonasDAO dao = new MaratonasDAO(this);
        List<Maratonas> listaMaratonas = dao.obterMaratonasPorCriador(userId);

        // Criando um ArrayAdapter para mostrar a lista
        ArrayAdapter<Maratonas> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaMaratonas
        );

        listViewCriadas.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Verifica se o resultado é OK e se é a resposta da AlterarExcluirActivity
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Recarrega a lista de alunos
            carregarMaratonasCriadas();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            // Recarrega a lista de alunos após adicionar
            carregarMaratonasCriadas();
        }
    }
}