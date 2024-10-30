package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.model.Maratonas;

import java.util.Objects;

public class TelaMaratonaAdm extends AppCompatActivity {

    private String Activity,Status,nomeMaratona;
    private int userId,maratonaId;
    //private int inscreve = 0;
    private Button btnIniciarMaratona, btnEncerrar, btnAtualizarMaratona, btnGerarVencedores, btnMostrarInscritos;
    private TextView txtTituloMaratona, txtDescricaoMaratona, txtlocal, txtDataIncial, txtDataFinal, txtStatus,txtDistancia,txtRegras,txtTipoT, txtClimaEsperado,txtValor, txtIdmaratona, txtEmpresa, txtForma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_maratona_adm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        maratonaId = intent.getIntExtra("maratonaId", -1);
        Activity = String.valueOf(intent.getStringExtra("activity"));



        txtTituloMaratona = findViewById(R.id.txtTituloMaratona);
        txtDescricaoMaratona = findViewById(R.id.txtDescricaoMaratona);
        txtlocal = findViewById(R.id.txtlocal);
        txtDataIncial = findViewById(R.id.txtDataIncial);
        txtDataFinal = findViewById(R.id.txtDataFinal);
        txtStatus = findViewById(R.id.txtStatus);
        txtDistancia = findViewById(R.id.txtDistancia);
        txtRegras = findViewById(R.id.txtRegras);
        txtTipoT = findViewById(R.id.txtTipoT);
        txtClimaEsperado = findViewById(R.id.txtClimaEsperado);
        txtValor = findViewById(R.id.txtValor);
        txtIdmaratona = findViewById(R.id.txtIdmaratona);
        txtEmpresa = findViewById(R.id.txtEmpresa);
        btnEncerrar = findViewById(R.id.btnEncerrar);
        btnAtualizarMaratona = findViewById(R.id.btnAtualizarMaratona);
        btnGerarVencedores = findViewById(R.id.btnConcluirVencedores);
        btnIniciarMaratona = findViewById(R.id.btnIniciarMaratona);
        btnMostrarInscritos = findViewById(R.id.btnMostrarInscritos);


        Toast.makeText(this, String.valueOf(Activity), Toast.LENGTH_SHORT).show();

        MaratonasDAO dao = new MaratonasDAO(this);
        Maratonas maratona = dao.readMaratona(maratonaId);

        Status = maratona.getStatus();
        confereStatus(Status);

        if (maratona == null){
            Toast.makeText(this, "Maratona não encontrada.", Toast.LENGTH_SHORT).show();
            finish();


        }else{
            txtTituloMaratona.setText(maratona.getNome());
            txtDescricaoMaratona.setText("Descrição:"+ maratona.getDescricao());
            txtlocal.setText("Local:"+maratona.getLocal());
            txtDataIncial.setText("data de Inicio:"+maratona.getData_inicio());
            txtDataFinal.setText("data de termino:"+maratona.getData_final());
            txtStatus.setText("Status:"+maratona.getStatus());
            txtDistancia.setText("Distancia:"+maratona.getDistancia());
            txtRegras.setText("Regras:"+maratona.getRegras());
            txtTipoT.setText("Tipo de Terreno:"+maratona.getTipo_terreno());
            txtClimaEsperado.setText("Clima Esperado:"+maratona.getClima_esperado());
            txtValor.setText("Valor: R$"+ maratona.getValor());
            txtIdmaratona.setText("Id da Maratona:"+ maratona.getId());
            txtEmpresa.setText("nome da empresa:"+maratona.getNomeCriador());

            nomeMaratona = maratona.getNome();
        }

    }
    private void confereStatus(String maratonas){

        if (maratonas.equals("Aberta para Inscrição")){
            btnIniciarMaratona.setVisibility(View.VISIBLE);
            btnMostrarInscritos.setVisibility(View.VISIBLE);
            btnGerarVencedores.setVisibility(View.GONE);
            btnEncerrar.setVisibility(View.GONE);
        } else if (maratonas.equals("Aberta")){
            btnIniciarMaratona.setVisibility(View.VISIBLE);
            btnIniciarMaratona.setText("Exibir QrCode");
            btnMostrarInscritos.setVisibility(View.GONE);
            btnGerarVencedores.setVisibility(View.VISIBLE);
            btnEncerrar.setVisibility(View.VISIBLE);
        }else if(maratonas.equals("Finalizada")){
            btnIniciarMaratona.setVisibility(View.GONE);
            btnMostrarInscritos.setVisibility(View.GONE);
            btnGerarVencedores.setVisibility(View.VISIBLE);
            btnEncerrar.setVisibility(View.GONE);
            btnAtualizarMaratona.setVisibility(View.GONE);
        }
    }

    public void IniciaMaratona(View view){

        Intent intent = new Intent(this, GerarQRCode.class);
        intent.putExtra("maratonaId", maratonaId);
        intent.putExtra("nomeMaratona", nomeMaratona );
        intent.putExtra("activity", Status);

        startActivity(intent);
        finish();
    }

    public void EditarMaratona(View view){

        Intent intent = new Intent(this, EditarMaratona.class);
        intent.putExtra("maratonaId", maratonaId);
        intent.putExtra("id", userId);
        intent.putExtra("activity", Status);

        startActivity(intent);
        finish();
    }
}