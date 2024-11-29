package com.example.maratona;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
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

import com.example.maratona.dao.CorredoresDAO;
import com.example.maratona.model.Corredores;

public class EditarUsuario extends AppCompatActivity {

    private String Activity;
    private int userId;
    private Button btnAtualizarCorredor, btnEnviarEmail;
    private EditText  edtId, edtNome, edtTelefone, edtEmail, edtSenha, edtCpf, edtGenero, edtPaisOrigem;
    private TextView edtPerfil, txtIdCorredor, txtSenha, txtCpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_usuario);

        // Configuração das bordas
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Receber dados do Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        Activity = String.valueOf(intent.getStringExtra("activity"));

        edtPerfil = findViewById(R.id.edtPerfil);
        edtId = findViewById(R.id.edtId);
        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtCpf = findViewById(R.id.edtCpf);
        edtGenero = findViewById(R.id.edtGenero);
        edtPaisOrigem = findViewById(R.id.edtPaisOrigem);
        btnAtualizarCorredor = findViewById(R.id.btnAtualizarCorredor);

        btnEnviarEmail = findViewById(R.id.btnEnviarEmail);

        txtIdCorredor = findViewById(R.id.txtIdCorredor);
        txtSenha = findViewById(R.id.txtSenha);
        txtCpf = findViewById(R.id.txtCpf);

        CorredoresDAO dao = new CorredoresDAO(this);
        Corredores corredor = dao.read(userId);

        // Verificar se o objeto Corredores foi carregado corretamente
        if (corredor == null) {
            Log.e("EditarUsuario", "Corredor não encontrado para userId: " + userId);
            Toast.makeText(this, "Corredor não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
        } else {

            desativarCampo(edtId);
            edtNome.setText(corredor.getNome());
            edtTelefone.setText(corredor.getTelefone());
            edtEmail.setText(corredor.getEmail());
            edtSenha.setText(corredor.getSenha());
            edtCpf.setText(corredor.getCpf());
            edtGenero.setText(corredor.getGenero());
            edtPaisOrigem.setText(corredor.getPaisOrigem());

            txtIdCorredor.setVisibility(View.GONE);
            edtId.setVisibility(View.GONE);
        }

        if (Activity.equals("VisualizarPerfil")) {
            configurarModoVisualizar();
        } else if (Activity.equals("EditarPerfil")) {
            btnEnviarEmail.setVisibility(View.GONE); // Oculta o botão de envio de e-mail
        }

        btnAtualizarCorredor.setOnClickListener(this::AtualizarCorredor);
        btnEnviarEmail.setOnClickListener(v -> enviarEmail(corredor.getEmail()));

    }

    private void configurarModoVisualizar() {
        edtPerfil.setText("Perfil de Usuário");
        desativarCampo(edtId);
        desativarCampo(edtNome);
        desativarCampo(edtTelefone);
        desativarCampo(edtEmail);
        desativarCampo(edtSenha);
        desativarCampo(edtCpf);
        desativarCampo(edtGenero);
        desativarCampo(edtPaisOrigem);

        txtIdCorredor.setVisibility(View.GONE);
        edtId.setVisibility(View.GONE);

        txtSenha.setVisibility(View.GONE);
        edtSenha.setVisibility(View.GONE);

        txtCpf.setVisibility(View.GONE);
        edtCpf.setVisibility(View.GONE);

        btnAtualizarCorredor.setVisibility(View.GONE);
    }

    private void desativarCampo(EditText campo) {
        campo.setInputType(InputType.TYPE_NULL);
        campo.setFocusable(false);
    }

    public void AtualizarCorredor(View view) {
        String nome = edtNome.getText().toString();
        String telefone = edtTelefone.getText().toString();
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
        String cpf = edtCpf.getText().toString();
        String genero = edtGenero.getText().toString();
        String paisOrigem = edtPaisOrigem.getText().toString();

        // Verificação de campos obrigatórios
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Nome, Email e Senha são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }

        Corredores corredorAtualizado = new Corredores();
        corredorAtualizado.setIdCorredor(userId);
        corredorAtualizado.setNome(nome);
        corredorAtualizado.setTelefone(telefone);
        corredorAtualizado.setEmail(email);
        corredorAtualizado.setSenha(senha);
        corredorAtualizado.setCpf(cpf);
        corredorAtualizado.setGenero(genero);
        corredorAtualizado.setPaisOrigem(paisOrigem);

        try {
            CorredoresDAO dao = new CorredoresDAO(this);
            dao.update(corredorAtualizado);

            Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();


            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao atualizar os dados.", Toast.LENGTH_SHORT).show();
        }
    }

    private void enviarEmail(String emailDestinatario) {
        if (emailDestinatario == null || emailDestinatario.isEmpty()) {
            Toast.makeText(this, "E-mail não encontrado para este corredor.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // Apenas apps de e-mail
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailDestinatario});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Assunto sobre a maratona");
        intent.putExtra(Intent.EXTRA_TEXT, "Olá! Gostaria de conversar sobre a maratona.");

        // Verifica se há algum aplicativo que pode lidar com a intenção
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Nenhum aplicativo de e-mail encontrado.", Toast.LENGTH_SHORT).show();
        }
    }

}
