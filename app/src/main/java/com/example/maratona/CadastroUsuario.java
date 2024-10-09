package com.example.maratona;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.util.Log;
import android.widget.Toast;

public class CadastroUsuario extends AppCompatActivity {
    // instanciando EditText
    private EditText edtNome;
    private EditText edtEmail;
    private EditText edtSenha;
    private EditText edtTelefone;
    private EditText edtCpf;
    private EditText edtCnpj;
    private EditText edtEndereco;
    private EditText edtOrigem;
    private RadioButton rdbRunner;
    private RadioButton rdbEmpresa;
    //private EditText url_foto;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rdbRunner = findViewById(R.id.rdbRunner);
        rdbEmpresa = findViewById(R.id.rdbEmpresa);
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtCpf = findViewById(R.id.edtCpf);
        edtCnpj = findViewById(R.id.edtCnpj);
        edtEndereco = findViewById(R.id.edtEndereco);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtOrigem = findViewById(R.id.edtOrigem);
        //edtUrl_foto = findViewById(R.id.edtUrl_foto);
    }


    public void Cadastrar(View view){
        String nome = edtNome.getText().toString();
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        if (email.trim().isEmpty() ) {
            // O EditText está vazio
            Toast.makeText(this, "O campo Email não pode estar vazio", Toast.LENGTH_SHORT).show();
        } else if(senha.trim().isEmpty()) {
            Toast.makeText(this, "O campo Senha não pode estar vazio", Toast.LENGTH_SHORT).show();
        } else if(nome.trim().isEmpty()) {
            Toast.makeText(this, "O campo Nome não pode estar vazio", Toast.LENGTH_SHORT).show();
        }

        if(rdbRunner.isChecked()) {


            Log.d("aaaaa","aaaaaa");


            //Declarando uma variável do tipo Intent
            Intent it = new Intent(this, TelaHub.class);

            //Iniciando a Tela desejada (tela 2)
            startActivity(it);

        }else{



            //Declarando uma variável do tipo Intent
            Intent it = new Intent(this, TelaHub.class);

            //Iniciando a Tela desejada (tela 2)
            startActivity(it);

        }


    }

    public void Troca(View view) {

        if(rdbEmpresa.isChecked()){
            edtCpf.setVisibility(View.GONE);
            edtCnpj.setVisibility(View.VISIBLE);
        }if (rdbRunner.isChecked()){
            edtCnpj.setVisibility(View.GONE);
            edtCpf.setVisibility(View.VISIBLE);
        }
    }

}