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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
    private int userId,maratonaId, distancia, inscricaoId;
    private int inscreve = 0;
    private Button btnInscrever, btnIniciarCorrida, btnVerResultado,btnCancelarInscricao;
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
        btnVerResultado = findViewById(R.id.btnVerResultado);
        btnCancelarInscricao = findViewById(R.id.btnCancelarInscricao);
        rdbBoleto = findViewById(R.id.rdbBoleto);
        rdbPix = findViewById(R.id.rdbPix);
        rdbCartao = findViewById(R.id.rdbCartao);

        //Toast.makeText(this, String.valueOf(Activity), Toast.LENGTH_SHORT).show();

        /*Puxa Id Inscricão*/
        InscricaoDAO i = new InscricaoDAO(this);
        inscricaoId = i.getIdInscricao(userId,maratonaId);
        /*Puxa Maratona*/
        MaratonasDAO dao = new MaratonasDAO(this);
        Maratonas maratona = dao.readMaratona(maratonaId);
        distancia = Integer.parseInt(maratona.getDistancia());

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
            txtIdmaratona.setText("Id da Maratona:"+ maratona.getIdMaratona());
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



        Log.i("USERID_TELAMARATONA",String.valueOf(userId));
        Log.i("MARATONAID_TELAMARATONA",String.valueOf(maratonaId));
        Log.i("INSCRICAOID_TELAMARATONA",String.valueOf(inscricaoId));
        Log.i("DISTANCIA_TELAMARATONA",String.valueOf(distancia));


    }

    private void confereStatus(Maratonas maratonas){
        String status = maratonas.getStatus();

        if (status.equals("ABERTA_PARA_INSCRICAO")){
            btnInscrever.setVisibility(View.VISIBLE);
            btnIniciarCorrida.setVisibility(View.GONE);
            btnVerResultado.setVisibility(View.GONE);
            btnCancelarInscricao.setVisibility(View.GONE);
        } else if (status.equals("ABERTA")){
            btnInscrever.setVisibility(View.GONE);
            btnIniciarCorrida.setVisibility(View.VISIBLE);
            btnVerResultado.setVisibility(View.GONE);
            btnCancelarInscricao.setVisibility(View.VISIBLE);
        }else {
            btnInscrever.setVisibility(View.GONE);
            btnIniciarCorrida.setVisibility(View.GONE);
            btnVerResultado.setVisibility(View.VISIBLE);
            btnCancelarInscricao.setVisibility(View.GONE);
            Toast.makeText(this, "Você já Participou deste Evento!", Toast.LENGTH_SHORT).show();
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
            confereInsc = inscricaoId;

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

        Intent intent = new Intent(this, ScanQRCode.class);
        intent.putExtra("maratonaId", maratonaId);
        intent.putExtra("id", userId);
        intent.putExtra("distancia", distancia);
        intent.putExtra("inscricaoId", inscricaoId);

        startActivityForResult(intent, 1);


    }

    public void CancelaInscricao(View view){
        AlertDialog.Builder confirmaCancelamento = new AlertDialog.Builder(this, R.style.RoundedAlertDialog);
        confirmaCancelamento.setTitle("Alerta de Cancelamento!");
        confirmaCancelamento.setMessage("A Inscrição será Cancelada, tem certeza desta ação?");
        confirmaCancelamento.setIcon(R.drawable.logo);
        confirmaCancelamento.setCancelable(false);
        confirmaCancelamento.setPositiveButton("Sim", (dialogInterface, i) -> {
            InscricaoDAO m = new InscricaoDAO(TelaMaratona.this);
            m.updateStatusParaDesistente(inscricaoId);
            Toast.makeText(TelaMaratona.this, "Inscrição Cancelada com Sucesso.", Toast.LENGTH_SHORT).show();
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

    public void VerResultado(View view){
        Intent intent = new Intent(this, TelaResultados.class);
        intent.putExtra("maratonaId", maratonaId);
        intent.putExtra("id", userId);
        intent.putExtra("inscricaoId", inscricaoId);

        startActivity(intent);
        finish();
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