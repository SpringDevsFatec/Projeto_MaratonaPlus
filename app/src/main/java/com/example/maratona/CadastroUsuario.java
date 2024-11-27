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

import com.example.maratona.dao.CorredoresDAO;
import com.example.maratona.dao.EmpresasDAO;
import com.example.maratona.model.Corredores;
import com.example.maratona.model.Empresas;

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
   // private EditText edtGenero;
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
        edtSenha = findViewById(R.id.edtSenha);
        edtCpf = findViewById(R.id.edtCpf);
        edtCnpj = findViewById(R.id.edtCnpj);
        edtEndereco = findViewById(R.id.edtEndereco);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtOrigem = findViewById(R.id.edtOrigem);
        //edtGenero = findViewById(R.id.edtGenero);
        //edtUrl_foto = findViewById(R.id.edtUrl_foto);
    }


    public void Cadastrar(View view){
        String nome = edtNome.getText().toString();
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        if (email.trim().isEmpty() ) {
            // O EditText está vazio
            Toast.makeText(this, "O campo Email não pode estar vazio", Toast.LENGTH_SHORT).show();
            return;
        } else if(senha.trim().isEmpty()) {
            Toast.makeText(this, "O campo Senha não pode estar vazio", Toast.LENGTH_SHORT).show();
            return;
        } else if(nome.trim().isEmpty()) {
            Toast.makeText(this, "O campo Nome não pode estar vazio", Toast.LENGTH_SHORT).show();
            return;
        }

        if(rdbRunner.isChecked()) {

            Corredores c = new Corredores();
            c.setNome(edtNome.getText().toString());
            c.setTelefone(edtTelefone.getText().toString());
            c.setEmail(edtEmail.getText().toString());
            c.setSenha(edtSenha.getText().toString());
            c.setCpf(edtCpf.getText().toString());
            c.setEndereco(edtEndereco.getText().toString());
            c.setPaisOrigem(edtOrigem.getText().toString());
            //c.setGenero(edtGenero.getText().toString());

            CorredoresDAO corredordao = new CorredoresDAO(this);

            long id = corredordao.insert(c);
            Log.i("idInsert", String.valueOf(id));
            Toast.makeText(getApplicationContext(), "Corredor inserido com o ID " + id,
                    Toast.LENGTH_LONG).show();


            setResult(RESULT_OK);
            Log.d("aaaaa","aaaaaa");

            finish();

        }else{


            Empresas e = new Empresas();
            e.setNome(edtNome.getText().toString());
            e.setTelefone(edtTelefone.getText().toString());
            e.setEmail(edtEmail.getText().toString());
            e.setSenha(edtSenha.getText().toString());
            e.setCnpj(edtCnpj.getText().toString());
            e.setLocal(edtEndereco.getText().toString());

            EmpresasDAO empresadao = new EmpresasDAO(this);

            long id = empresadao.insert(e);
            Log.i("idInsert", String.valueOf(id));
            Toast.makeText(getApplicationContext(), "Empresa inserida com o ID " + id,
                    Toast.LENGTH_LONG).show();


            setResult(RESULT_OK);

            //Declarando uma variável do tipo Intent


            finish();
        }


    }

    public void TrocaCorredor(View view) {

            edtCnpj.setVisibility(View.GONE);
            edtCpf.setVisibility(View.VISIBLE);
            edtOrigem.setVisibility(View.VISIBLE);

    }
    public void TrocaAdm(View view) {
            edtCpf.setVisibility(View.GONE);
            edtOrigem.setVisibility(View.GONE);
            edtCnpj.setVisibility(View.VISIBLE);
    }


}