package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.example.maratona.model.Corredores;
import com.example.maratona.model.Inscricao;
import com.example.maratona.model.Maratonas;


import java.util.List;


public class TelaMaratona extends AppCompatActivity {

    private String Activity;
    private int userId,maratonaId;
    private int inscreve = 0;
    private Button btnInscrever, btnIniciarCorrida;
    private RadioButton rdbBoleto,rdbCartao, rdbPix ;
    private TextView txtTituloMaratona, txtDescricaoMaratona, txtlocal, txtDataIncial, txtDataFinal, txtStatus,txtDistancia,txtRegras,txtTipoT, txtClimaEsperado,txtValor, txtIdmaratona, txtEmpresa, txtForma, txtParticipantes;
    ListView listViewParticipantes;


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

        // Recebendo os dados do Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        maratonaId = intent.getIntExtra("maratonaId", -1);
        Activity = String.valueOf(intent.getStringExtra("activity"));


        // Inicializando o ListView
        listViewParticipantes = findViewById(R.id.listaParticipantes);
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
        txtParticipantes = findViewById(R.id.txtParticipantes);
        btnInscrever = findViewById(R.id.btnInscrever);
        btnIniciarCorrida = findViewById(R.id.btnIniciarCorrida);
        rdbBoleto = findViewById(R.id.rdbBoleto);
        rdbPix = findViewById(R.id.rdbPix);
        rdbCartao = findViewById(R.id.rdbCartao);

        Toast.makeText(this, String.valueOf(Activity), Toast.LENGTH_SHORT).show();



        MaratonasDAO dao = new MaratonasDAO(this);
        Maratonas maratona = dao.readMaratona(maratonaId);

        confereStatus(maratona);
        confereActivity(Activity);

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

        /* Adicionando o OnItemClickListener */
        listViewParticipantes.setOnItemClickListener((parent, view, position, id) -> {
            Corredores corredorSelecionado = (Corredores) parent.getItemAtPosition(position);
            //Toast.makeText(this, String.valueOf(maratonaSelecionada.getId()), Toast.LENGTH_SHORT).show();
            Intent it = new Intent(TelaMaratona.this, EditarUsuario.class);
            it.putExtra("id", corredorSelecionado.getIdCorredor());
            it.putExtra("activity", "VisualizarPerfil");

            startActivityForResult(it, 1);
        });


    }

    private void confereStatus(Maratonas maratonas){
        String status = maratonas.getStatus();

        if (status.equals("Aberta para Inscrição")){
            btnInscrever.setVisibility(View.VISIBLE);
            btnIniciarCorrida.setVisibility(View.GONE);
        } else if (status.equals("Aberta")){
            btnInscrever.setVisibility(View.GONE);
            btnIniciarCorrida.setVisibility(View.VISIBLE);
        }else {
            btnInscrever.setVisibility(View.GONE);
            btnIniciarCorrida.setVisibility(View.GONE);
            Toast.makeText(this, "Evento Já finalizado ou passado o Prazo de Inscrição!", Toast.LENGTH_SHORT).show();
        }
    }

    private void confereActivity(String Activity){
        if (Activity.equals("VisualizarAbertas")){
            txtParticipantes.setText("Lista de Inscritos que talvez você conheça:");
            carregarCorredoresIncritos();
        } else if (Activity.equals("VisualizarInscritas")){
            txtParticipantes.setText("Lista de Inscritos Junto com você nesta Maratona:");
            carregarCorredoresIncritos();
        } else {
            txtParticipantes.setText("Lista de participantes que concluiram junto contigo:");
            carregarCorredoresConcluidos();
        }


    }

    private void carregarCorredoresIncritos() {
        InscricaoDAO dao = new InscricaoDAO(this);
        List<Corredores> listaCorredores = dao.obterCorredoresPorMaratona(maratonaId);

        // Verificar se a lista está preenchida
        if (listaCorredores == null || listaCorredores.isEmpty()) {
            Toast.makeText(this, "Nenhuma Corredor Inscrito", Toast.LENGTH_SHORT).show();
            Log.d("VisualizarAbertas", "Nenhuma maratona aberta encontrada");
        }

        // Criando um ArrayAdapter para mostrar a lista
        assert listaCorredores != null;
        ArrayAdapter<Corredores> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaCorredores
        );

        listViewParticipantes.setAdapter(adapter);
    }

    private void carregarCorredoresConcluidos() {
        InscricaoDAO dao = new InscricaoDAO(this);
        List<Corredores> listaCorredores = dao.obterCorredoresConcluidosPorMaratona(maratonaId);

        // Verificar se a lista está preenchida
        if (listaCorredores == null || listaCorredores.isEmpty()) {
            Toast.makeText(this, "Nenhum Corredor Concluiu está Maratona", Toast.LENGTH_SHORT).show();
            Log.d("VisualizarAbertas", "Nenhuma maratona aberta encontrada");
        }

        // Criando um ArrayAdapter para mostrar a lista
        assert listaCorredores != null;
        ArrayAdapter<Corredores> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                listaCorredores
        );

        listViewParticipantes.setAdapter(adapter);
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
                btnInscrever.setText("Inscrição já realizada");
            }else{
                btnInscrever.setText("Finalizar Inscrição");
                txtForma.setVisibility(View.VISIBLE);
                rdbCartao.setVisibility(View.VISIBLE);
                rdbBoleto.setVisibility(View.VISIBLE);
                rdbPix.setVisibility(View.VISIBLE);
                Toast.makeText(this, "Escolha a forma de Pagamento.", Toast.LENGTH_SHORT).show();
                inscreve = 1;
            }
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
                btnInscrever.setText("Inscrição já realizada");
                inscreve = 0;
            }
        }
    }

    public void iniciarMaratona(View view){

        InscricaoDAO idao = new InscricaoDAO(this);
        int id = idao.getIdInscricao(maratonaId, userId);
        Inscricao i = new Inscricao();



        Intent intent = new Intent(this, ScanQRCode.class);
        intent.putExtra("maratonaId", maratonaId);
        intent.putExtra("id", userId);
        intent.putExtra("activity", "Participar");

        startActivityForResult(intent, 1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
           if (Activity.equals("VisualizarAbertas") || Activity.equals("VisualizarInscritas")){
                carregarCorredoresIncritos();
           }else{
               carregarCorredoresConcluidos();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            if (Activity.equals("VisualizarAbertas") || Activity.equals("VisualizarInscritas")){
                carregarCorredoresIncritos();
            }else{
                carregarCorredoresConcluidos();
            }
        }
    }
}