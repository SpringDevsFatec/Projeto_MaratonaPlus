package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button btnAtualizarCorredor;
    private EditText edtPerfil, edtId, edtNome, edtTelefone, edtEmail, edtSenha, edtCpf, edtGenero, edtPaisOrigem;

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



        // Log para verificar se o userId foi recebido corretamente
        Log.d("EditarUsuario", "Received userId: " + userId);

        // Inicializar campos de entrada
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

        // Buscar dados do usuário
        CorredoresDAO dao = new CorredoresDAO(this);
        Corredores corredor = dao.read(userId);

        // Verificar se o objeto Corredores foi carregado corretamente
        if (corredor == null) {
            Log.e("EditarUsuario", "Corredor não encontrado para userId: " + userId);
            Toast.makeText(this, "Corredor não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Carregar dados nos campos de entrada
            Log.d("EditarUsuario", "Corredor encontrado: " + corredor.getNome());
            edtId.setText(String.valueOf(corredor.getIdCorredor()));
            edtNome.setText(corredor.getNome());
            edtTelefone.setText(corredor.getTelefone());
            edtEmail.setText(corredor.getEmail());
            edtSenha.setText(corredor.getSenha());
            edtCpf.setText(corredor.getCpf());
            edtGenero.setText(corredor.getGenero());
            edtPaisOrigem.setText(corredor.getPaisOrigem());
        }

        if (Activity.equals("VisualizarPerfil")) {
            edtPerfil.setText("Perfil de Usuário");
            edtId.setInputType(InputType.TYPE_NULL);
            edtNome.setInputType(InputType.TYPE_NULL);
            edtTelefone.setInputType(InputType.TYPE_NULL);
            edtEmail.setInputType(InputType.TYPE_NULL);
            edtSenha.setInputType(InputType.TYPE_NULL);
            edtCpf.setInputType(InputType.TYPE_NULL);
            edtGenero.setInputType(InputType.TYPE_NULL);
            edtPaisOrigem.setInputType(InputType.TYPE_NULL);

            edtId.setFocusable(false);
            edtNome.setFocusable(false);
            edtTelefone.setFocusable(false);
            edtEmail.setFocusable(false);
            edtSenha.setFocusable(false);
            edtCpf.setFocusable(false);
            edtGenero.setFocusable(false);
            edtPaisOrigem.setFocusable(false);

            btnAtualizarCorredor.setVisibility(View.GONE);
        }


        // Configurar ação do botão de atualização
        btnAtualizarCorredor.setOnClickListener(this::AtualizarCorredor);
    }

    public void AtualizarCorredor(View view) {
        // Obter valores dos campos
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

        // Criar objeto Corredores com dados atualizados
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
            // Atualizar no banco de dados
            CorredoresDAO dao = new CorredoresDAO(this);
            dao.update(corredorAtualizado);

            // Log de sucesso e mensagem de confirmação
            Log.d("EditarUsuario", "Dados do corredor atualizados com sucesso: " + userId);
            Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();

            // Finalizar atividade e voltar para tela anterior
            finish();
        } catch (Exception e) {
            Log.e("EditarUsuario", "Erro ao atualizar dados do corredor: " + e.getMessage());
            Toast.makeText(this, "Erro ao atualizar os dados.", Toast.LENGTH_SHORT).show();
        }
    }
}
