package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.model.Maratonas;

public class EditarMaratona extends AppCompatActivity {

    private String Activity;
    private int maratonaId, userId;
    private Button btnAtualizarMaratona;
    private EditText edtId,edtStatus, edtNome, edtLocal, edtData_I, edtData_F, edtDistancia, edtDescricao,
            edtLimite_p,edtRegras,edtValor, edtTipoTerreno,edtClimaEsperado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_maratona);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        maratonaId = intent.getIntExtra("maratonaId", -1);
        userId = intent.getIntExtra("id", -1);
        Activity = String.valueOf(intent.getStringExtra("activity"));

        edtId = findViewById(R.id.edtId);
        edtStatus = findViewById(R.id.edtStatus);
        edtNome = findViewById(R.id.edtNome);
        edtLocal = findViewById(R.id.edtLocal);
        edtData_I = findViewById(R.id.edtData_I);
        edtData_F = findViewById(R.id.edtData_F);
        edtDistancia = findViewById(R.id.edtDistancia);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtLimite_p = findViewById(R.id.edtLimite_p);
        edtRegras = findViewById(R.id.edtRegras);
        edtTipoTerreno = findViewById(R.id.edtTipoTerreno);
        edtClimaEsperado = findViewById(R.id.edtClimaEsperado);
        edtValor = findViewById(R.id.edtValor);

        MaratonasDAO dao = new MaratonasDAO(this);
        Maratonas maratona = dao.readMaratona(maratonaId);

        if (maratona == null){
            Toast.makeText(this, "Maratona não encontrada.", Toast.LENGTH_SHORT).show();
            finish();


        }else{
            edtId.setText(String.valueOf(maratona.getId()));
            edtNome.setText(maratona.getNome());
            edtDescricao.setText(maratona.getDescricao());
            edtLocal.setText(maratona.getLocal());
            edtData_I.setText(maratona.getData_inicio());
            edtData_F.setText(maratona.getData_final());
            edtStatus.setText(maratona.getStatus());
            edtDistancia.setText(String.valueOf(maratona.getDistancia()));
            edtLimite_p.setText(String.valueOf(maratona.getLimite_participantes()));
            edtRegras.setText(maratona.getRegras());
            edtTipoTerreno.setText(maratona.getTipo_terreno());
            edtClimaEsperado.setText(maratona.getClima_esperado());
            edtValor.setText(String.valueOf(maratona.getValor()));
        }

    }

    public void AtualizarMaratona(View view){


            String nome = edtNome.getText().toString();
            String descricao = edtDescricao.getText().toString();

            // Verificação de campos obrigatórios
            if (nome.trim().isEmpty() || descricao.trim().isEmpty()) {
                Toast.makeText(this, "Campo nome e descricao precisam ser preenchidos", Toast.LENGTH_SHORT).show();
                return;
            }


            Maratonas m = new Maratonas();
            m.setId(Integer.parseInt(edtId.getText().toString()));
            m.setNome(nome);
            m.setCriador(userId);
            m.setData_inicio(edtData_I.getText().toString());
            m.setData_final(edtData_F.getText().toString());
            m.setStatus(edtStatus.getText().toString());
            m.setLocal(edtLocal.getText().toString());
            m.setDistancia(edtDistancia.getText().toString());
            m.setDescricao(descricao);
            try {
                m.setLimite_participantes(Integer.parseInt(edtLimite_p.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "O limite de participantes deve ser um número.", Toast.LENGTH_SHORT).show();
                return;
            }
            m.setRegras(edtRegras.getText().toString());
            try {
                m.setValor(Float.parseFloat(edtValor.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "O valor deve ser um número.", Toast.LENGTH_SHORT).show();
                return;
            }
            m.setTipo_terreno(edtTipoTerreno.getText().toString());
            m.setClima_esperado(edtClimaEsperado.getText().toString());

            try{
            MaratonasDAO maratonadao = new MaratonasDAO(this);
            maratonadao.update(m);

                Toast.makeText(getApplicationContext(), "Maratona Atualizada com sucesso " , Toast.LENGTH_LONG).show();



            setResult(RESULT_OK);


            Intent intent = new Intent(this, TelaMaratonaAdm.class);
                intent.putExtra("maratonaId", maratonaId);
                intent.putExtra("id", userId);
                intent.putExtra("activity", "VisualizarCriadas");

                startActivity(intent);
                finish();

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Erro ao cadastrar maratona: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }



    }
}