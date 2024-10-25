package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
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
    private int maratonaId;
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

    public void Atualizar(View view){

    }
}