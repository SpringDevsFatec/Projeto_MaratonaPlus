package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.CorredoresDAO;
import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.model.Corredores;
import com.example.maratona.model.Maratonas;

import java.sql.Date;

public class CadastrarMaratona extends AppCompatActivity {

    EditText edtNome,edtEndereco,edtData_I,edtData_F,edtDistancia,edtDescricao,edtRegras,edtLimite_p, edtValor, edtTipoTerreno, edtClimaEsperado;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastrar_maratona);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);

        edtNome = findViewById(R.id.edtNome);
        edtEndereco = findViewById(R.id.edtEndereco);
        edtData_I = findViewById(R.id.edtData_I);
        edtData_F = findViewById(R.id.edtData_F);
        edtDistancia = findViewById(R.id.edtDistancia);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtRegras = findViewById(R.id.edtRegras);
        edtLimite_p = findViewById(R.id.edtLimite_p);
        edtValor = findViewById(R.id.edtValor);
        edtTipoTerreno = findViewById(R.id.edtTipoTerreno);
        edtClimaEsperado = findViewById(R.id.edtClimaEsperado);

    }

    public void CadastrarMaratonas(View view) {
        try {
            String nome = edtNome.getText().toString();
            String descricao = edtDescricao.getText().toString();

            // Verificação de campos obrigatórios
            if (nome.trim().isEmpty() || descricao.trim().isEmpty()) {
                Toast.makeText(this, "Campo nome e descricao precisam ser preenchidos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Criação de objeto Maratona
            Maratonas m = new Maratonas();
            m.setNome(nome);
            m.setCriador(userId);

            // Validação e conversão de datas
                m.setData_inicio(edtData_I.getText().toString());
                m.setData_final(edtData_F.getText().toString());


            m.setStatus("aberta para Inscrição");
            m.setLocal(edtEndereco.getText().toString());
            m.setDistancia(edtDistancia.getText().toString());
            m.setDescricao(descricao);

            // Validação e conversão do limite de participantes
            try {
                m.setLimite_participantes(Integer.parseInt(edtLimite_p.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "O limite de participantes deve ser um número.", Toast.LENGTH_SHORT).show();
                return;
            }

            m.setRegras(edtRegras.getText().toString());

            // Validação e conversão do valor
            try {
                m.setValor(Float.parseFloat(edtValor.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "O valor deve ser um número.", Toast.LENGTH_SHORT).show();
                return;
            }

            m.setTipo_terreno(edtTipoTerreno.getText().toString());
            m.setClima_esperado(edtClimaEsperado.getText().toString());

            // Inserção no banco de dados
            MaratonasDAO maratonadao = new MaratonasDAO(this);
            long id = maratonadao.insert(m);

            // Confirmação e Log de sucesso
            Toast.makeText(getApplicationContext(), "Maratona inserida com sucesso com o ID " + id, Toast.LENGTH_LONG).show();
            Log.d("CadastrarMaratona", "Maratona inserida com ID: " + id);

            // Finaliza a atividade
            setResult(RESULT_OK);
            finish();

        } catch (Exception e) {
            // Log de erro e mensagem ao usuário
            Log.e("CadastrarMaratona", "Erro ao cadastrar maratona", e);
            Toast.makeText(getApplicationContext(), "Erro ao cadastrar maratona: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}