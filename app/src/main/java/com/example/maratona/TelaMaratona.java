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

import com.example.maratona.dao.InscricaoDAO;
import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.model.Inscricao;
import com.example.maratona.model.Maratonas;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class TelaMaratona extends AppCompatActivity {

    private String Activity;
    private int userId,maratonaId;
    private int inscreve = 0;
    private Button btnInscrever, btnIniciarCorrida;
    private RadioButton rdbBoleto,rdbCartao, rdbPix ;
    private TextView txtTituloMaratona, txtDescricaoMaratona, txtlocal, txtDataIncial, txtDataFinal, txtStatus,txtDistancia,txtRegras,txtTipoT, txtClimaEsperado,txtValor, txtIdmaratona, txtEmpresa, txtForma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_maratona);
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
        txtForma = findViewById(R.id.txtForma);
        btnInscrever = findViewById(R.id.btnInscrever);
        btnIniciarCorrida = findViewById(R.id.btnIniciarCorrida);
        rdbBoleto = findViewById(R.id.rdbBoleto);
        rdbPix = findViewById(R.id.rdbPix);
        rdbCartao = findViewById(R.id.rdbCartao);

        Toast.makeText(this, String.valueOf(Activity), Toast.LENGTH_SHORT).show();

        if (!Objects.equals(Activity, "VisualizarAbertas")){
            btnInscrever.setVisibility(View.GONE);
        }

        MaratonasDAO dao = new MaratonasDAO(this);
        Maratonas maratona = dao.readMaratona(maratonaId);

        confereStatus(maratona);

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
        }

    }

    private void confereStatus(Maratonas maratonas){
        String status = maratonas.getStatus();

        if (!status.equals("aberta para Inscrição")){
            btnIniciarCorrida.setVisibility(View.GONE);
        }
    }


    private String confereForma(){
        String Forma = "";


        if (rdbBoleto.isChecked() && rdbPix.isChecked() && !rdbCartao.isChecked()){
            Toast.makeText(this, "clique em alguma forma de Pagamento.", Toast.LENGTH_SHORT).show();
            Forma = "";
        } else if (rdbBoleto.isChecked()) {
            Forma = "Boleto";
        }else if (rdbCartao.isChecked()) {
            Forma =  "Cartao";
        }else if (rdbPix.isChecked()) {
            Forma = "Pix";
        }

        return Forma;
    }

    public void Inscrever(View view){

        if (inscreve == 0 ){
            int confereInsc;
            InscricaoDAO dao = new InscricaoDAO(this);

            confereInsc = dao.getIdInscricao(userId, maratonaId);

            if(confereInsc != -1){
                Toast.makeText(this, "Você já está inscrito na Maratona.", Toast.LENGTH_SHORT).show();
            }else{
                txtForma.setVisibility(View.VISIBLE);
                rdbCartao.setVisibility(View.VISIBLE);
                rdbBoleto.setVisibility(View.VISIBLE);
                rdbPix.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Escolha a forma de Pagamento.", Toast.LENGTH_SHORT).show();
            }

            inscreve = 1;
        }else{
            InscricaoDAO dao = new InscricaoDAO(this);
            Inscricao i = new Inscricao();
            String Form = confereForma();

            if (Form == ""){
                Toast.makeText(this, "Escolha uma forma.", Toast.LENGTH_SHORT).show();
            }else{
                i.setIdCorredor(userId);
                i.setIdMaratona(maratonaId);
                i.setFormaPagamento(Form);
               long id = dao.insert(i);
                Toast.makeText(getApplicationContext(), "Inscricao inserida com o ID " + id,
                        Toast.LENGTH_LONG).show();

                setResult(RESULT_OK);
                view.invalidate();
                txtForma.setVisibility(View.GONE);
                rdbCartao.setVisibility(View.GONE);
                rdbBoleto.setVisibility(View.GONE);
                rdbPix.setVisibility(View.GONE);
                inscreve = 0;
            }
        }
    }
}