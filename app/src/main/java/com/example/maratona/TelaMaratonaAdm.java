package com.example.maratona;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.InscricaoDAO;
import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.model.Corredores;
import com.example.maratona.model.Maratonas;

import java.util.List;
import java.util.Objects;

public class TelaMaratonaAdm extends AppCompatActivity {

    private String Activity,Status,nomeMaratona;
    private int userId,maratonaId, click;
    //private int inscreve = 0;
    private Button btnIniciarMaratona,btnMostrarParticipando, btnCancelaMaratona, btnLargadaMaratona, btnEncerrar, btnAtualizarMaratona, btnGerarVencedores, btnMostrarInscritos;
    private TextView txtParticipantes, txtTituloMaratona, txtDescricaoMaratona, txtlocal, txtDataIncial, txtDataFinal, txtStatus,txtDistancia,txtRegras,txtTipoT, txtClimaEsperado,txtValor, txtIdmaratona, txtEmpresa, txtForma;
    ListView listViewParticipantes;

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
        // Recebendo os dados do Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        maratonaId = intent.getIntExtra("maratonaId", -1);
        Activity = String.valueOf(intent.getStringExtra("activity"));


        listViewParticipantes = findViewById(R.id.listaParticipantes2);
        txtParticipantes = findViewById(R.id.txtParticipantes2);
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
        btnMostrarParticipando = findViewById(R.id.btnMostrarParticipando);
        btnLargadaMaratona = findViewById(R.id.btnLargadaMaratona);
        btnCancelaMaratona = findViewById(R.id.btnCancelaMaratona);
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
            txtIdmaratona.setText("Id da Maratona:"+ maratona.getIdMaratona());
            txtEmpresa.setText("nome da empresa:"+maratona.getNomeCriador());

            nomeMaratona = maratona.getNome();
        }

    }
    private void confereStatus(String maratonas){

        if (maratonas.equals("ABERTA_PARA_INSCRICAO")){
            /*Parte de Maratona*/
            btnIniciarMaratona.setVisibility(View.VISIBLE);
            btnLargadaMaratona.setVisibility(View.GONE);
            btnEncerrar.setVisibility(View.GONE);
            /*Parte de Corredores*/
            btnMostrarInscritos.setVisibility(View.VISIBLE);
            btnMostrarParticipando.setVisibility(View.GONE);
            btnGerarVencedores.setVisibility(View.GONE);
        } else if (maratonas.equals("ABERTA")) {
            /*Parte de Maratona*/
            btnIniciarMaratona.setVisibility(View.VISIBLE);
            btnIniciarMaratona.setText("Exibir QrCode");
            btnLargadaMaratona.setVisibility(View.VISIBLE);
            btnEncerrar.setVisibility(View.GONE);
            /*Parte de Corredores*/
            btnMostrarInscritos.setVisibility(View.VISIBLE);
            btnMostrarParticipando.setVisibility(View.GONE);
            btnGerarVencedores.setVisibility(View.GONE);
        } else if (maratonas.equals("EM_ANDAMENTO")){
            /*Parte de Maratona*/
            btnIniciarMaratona.setVisibility(View.VISIBLE);
            btnIniciarMaratona.setText("Exibir QrCode");
            btnLargadaMaratona.setVisibility(View.GONE);
            btnEncerrar.setVisibility(View.VISIBLE);
            /*Parte de Corredores*/
            btnMostrarInscritos.setVisibility(View.GONE);
            btnMostrarParticipando.setVisibility(View.VISIBLE);
            btnGerarVencedores.setVisibility(View.GONE);
        }else if(maratonas.equals("CONCLUIDA")){
            /*Parte de Maratona*/
            btnIniciarMaratona.setVisibility(View.GONE);
            btnLargadaMaratona.setVisibility(View.GONE);
            btnEncerrar.setVisibility(View.GONE);
            /*Parte de Corredores*/
            btnMostrarInscritos.setVisibility(View.GONE);
            btnGerarVencedores.setVisibility(View.VISIBLE);
            btnMostrarParticipando.setVisibility(View.GONE);
            btnEncerrar.setVisibility(View.GONE);
            /*Parte de Atualizar*/
            btnAtualizarMaratona.setVisibility(View.GONE);
            btnCancelaMaratona.setVisibility(View.GONE);
        }else if(maratonas.equals("CANCELADA")){
            /*Parte de Maratona*/
            btnIniciarMaratona.setVisibility(View.GONE);
            btnLargadaMaratona.setVisibility(View.GONE);
            btnEncerrar.setVisibility(View.GONE);
            /*Parte de Corredores*/
            btnMostrarInscritos.setVisibility(View.VISIBLE);
            btnGerarVencedores.setVisibility(View.GONE);
            btnMostrarParticipando.setVisibility(View.GONE);
            btnEncerrar.setVisibility(View.GONE);
            /*Parte de Atualizar*/
            btnAtualizarMaratona.setVisibility(View.GONE);
            btnCancelaMaratona.setVisibility(View.VISIBLE);
            btnCancelaMaratona.setText("Maratona Cancelada");
            btnCancelaMaratona.setInputType(InputType.TYPE_NULL);
            btnCancelaMaratona.setFocusable(false);
        }

        /* Adicionando o OnItemClickListener */
        listViewParticipantes.setOnItemClickListener((parent, view, position, id) -> {
            Corredores corredorSelecionado = (Corredores) parent.getItemAtPosition(position);
            //Toast.makeText(this, String.valueOf(maratonaSelecionada.getId()), Toast.LENGTH_SHORT).show();
            Intent it = new Intent(TelaMaratonaAdm .this, EditarUsuario.class);
            it.putExtra("id", corredorSelecionado.getIdCorredor());
            it.putExtra("activity", "VisualizarPerfil");

            startActivityForResult(it, 1);
        });
    }

    public void IniciaMaratona(View view){

        Intent intent = new Intent(this, GerarQRCode.class);
        intent.putExtra("maratonaId", maratonaId);
        intent.putExtra("nomeMaratona", nomeMaratona );
        intent.putExtra("activity", Status);

        startActivity(intent);
        finish();
    }

    public void VisualizarCorredoresInscritos(View view){

        if (click == 1){
            Toast.makeText(this, "já exibido na tela os Inscritos.", Toast.LENGTH_SHORT).show();
        }else{
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
        click = 1;
    }

    public void VisualizarCorredoresParticipando(View view){

        if (click == 1){
            Toast.makeText(this, "já exibido na tela os Participandos.", Toast.LENGTH_SHORT).show();
        }else{
            InscricaoDAO dao = new InscricaoDAO(this);
            List<Corredores> listaCorredores = dao.obterCorredoresParticipando(maratonaId);

            // Verificar se a lista está preenchida
            if (listaCorredores == null || listaCorredores.isEmpty()) {
                Toast.makeText(this, "Nenhuma Corredor Participando", Toast.LENGTH_SHORT).show();
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
        click = 1;
    }

    public void VisualizarCorredoresConcluidos(View view){

        if (click == 1){
            Toast.makeText(this, "já exibido na tela os ganhadores.", Toast.LENGTH_SHORT).show();
        }else{
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
        click = 1;
    }

    public void EditarMaratona(View view){

        Intent intent = new Intent(this, EditarMaratona.class);
        intent.putExtra("maratonaId", maratonaId);
        intent.putExtra("id", userId);
        intent.putExtra("activity", Status);

        startActivity(intent);
        finish();
    }

    public void DarLargada(View view){


        MaratonasDAO m = new MaratonasDAO(this);
        m.updateMaratonaIniciar(maratonaId);
        Toast.makeText(this, "Maratona Iniciada com Suceso.", Toast.LENGTH_SHORT).show();
    }

    public void FecharMaratona(View view){


        MaratonasDAO m = new MaratonasDAO(this);
        m.updateMaratonaConcluir(maratonaId);
        Toast.makeText(this, "Maratona Concluida com Suceso.", Toast.LENGTH_SHORT).show();
    }

    public void CancelarMaratona(View view){

        AlertDialog.Builder confirmaCancelamento = new AlertDialog.Builder(this, R.style.RoundedAlertDialog);
        confirmaCancelamento.setTitle("Alerta de Cancelamento!");
        confirmaCancelamento.setMessage("A Maratona será Cancelada, tem certeza desta ação?");
        confirmaCancelamento.setIcon(R.drawable.logo);
        confirmaCancelamento.setCancelable(false);
        confirmaCancelamento.setPositiveButton("Sim", (dialogInterface, i) -> {
            MaratonasDAO m = new MaratonasDAO(TelaMaratonaAdm.this);
            m.updateMaratonaCancelar(maratonaId);
            Toast.makeText(TelaMaratonaAdm.this, "Maratona Cancelada com Sucesso.", Toast.LENGTH_SHORT).show();
        });
        confirmaCancelamento.setNegativeButton("Não", null);

        AlertDialog dialog = confirmaCancelamento.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.green));
            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.red));
        });
        dialog.show();

    }
}