package com.example.maratona;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.content.Context;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.CorredoresDAO;
import com.example.maratona.dao.EmpresasDAO;
import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.util.NetworkUtils;

public class TeladeLogin extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private RadioButton rdbRunner;
    private RadioButton rdbEmpresa;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_telade_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        rdbRunner = findViewById(R.id.rdbRunner);
        rdbEmpresa = findViewById(R.id.rdbEmpresa);
    }

    public void Logar(View view) {
        // Verificar se há conexão com a internet antes de continuar
       // if (!NetworkUtils.isInternetAvailable(this)) {
         //   Toast.makeText(this, "Sem conexão com a Internet. Verifique e tente novamente.", Toast.LENGTH_LONG).show();
           // return; // Encerra o método para evitar execução adicional
        //}

        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        if (email.trim().isEmpty()) {
            // O EditText está vazio
            Toast.makeText(this, "O campo Email não pode estar vazio", Toast.LENGTH_SHORT).show();
        } else if (senha.trim().isEmpty()) {
            Toast.makeText(this, "O campo Senha não pode estar vazio", Toast.LENGTH_SHORT).show();
        } else if (!rdbRunner.isChecked() && !rdbEmpresa.isChecked()) {
            Toast.makeText(this, "Escolha a forma de Login", Toast.LENGTH_SHORT).show();
        } else {
            if (rdbEmpresa.isChecked()) {
                // Lógica de buscar e comparar os dados
                EmpresasDAO empresaDAO = new EmpresasDAO(this);
                int loginValido = empresaDAO.verificarLoginEmpresaId(email, senha);
                Log.i("UserId", String.valueOf(loginValido));
                if (loginValido != -1) {
                    Toast.makeText(this, "Seja bem-vindo novamente!", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(this, TelaAdm.class);
                    it.putExtra("id", loginValido);
                    startActivity(it);
                } else {
                    Toast.makeText(this, "Login inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Lógica de buscar e comparar os dados
                CorredoresDAO corredorDAO = new CorredoresDAO(this);
                int loginValido = corredorDAO.verificarLoginCorredorId(email, senha);
                if (loginValido != -1) {
                    Toast.makeText(this, "Seja bem-vindo novamente!", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(this, TelaHub.class);
                    it.putExtra("id", loginValido);
                    startActivity(it);
                } else {
                    Toast.makeText(this, "Login inválido", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void Tela_Cadastrar(View view) {
        //if (!NetworkUtils.isInternetAvailable(this)) {
        //Declarando uma variável do tipo Intent
            Intent it = new Intent(this, CadastroUsuario.class);

            startActivity(it);
        //} else {
            // Caso contrário, exibir uma mensagem de erro
           // Toast.makeText(this, "Sem conexão com a Internet. Verifique e tente novamente.", Toast.LENGTH_LONG).show();
        //}
    }

}

