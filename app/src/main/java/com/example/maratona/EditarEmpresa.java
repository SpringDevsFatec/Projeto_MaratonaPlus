package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.maratona.dao.EmpresasDAO;
import com.example.maratona.model.Corredores;
import com.example.maratona.model.Empresas;

public class EditarEmpresa extends AppCompatActivity {

    private int userId;
    private Button btnAtualizarEmpresa;
    private EditText edtNomeEmpresa, edtTelefone, edtEmail, edtUsuario, edtSenha, edtCnpj, edtLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_empresa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Receber dados do Intent
        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);

        edtNomeEmpresa = findViewById(R.id.edtNomeEmpresa2);
        edtTelefone = findViewById(R.id.edtTelefone2);
        edtEmail = findViewById(R.id.edtEmail2);
        edtUsuario = findViewById(R.id.edtUsuario2);
        edtSenha = findViewById(R.id.edtSenha2);
        edtCnpj = findViewById(R.id.edtCnpj2);
        edtLocal = findViewById(R.id.edtLocal2);


        EmpresasDAO dao = new EmpresasDAO(this);
        Empresas empresa = dao.read(userId);

        // Verificar se o objeto Corredores foi carregado corretamente
        if (empresa == null) {
            Log.e("EditarUsuario", "empresa não encontrado para userId: " + userId);
            Toast.makeText(this, "empresa não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Carregar dados nos campos de entrada
            Log.d("EditarUsuario", "empresa encontrado: " + empresa.getNome());
            edtNomeEmpresa.setText(empresa.getNome()); // Exemplo, supondo que tenha esse método
            edtTelefone.setText(empresa.getTelefone());
            edtEmail.setText(empresa.getEmail());
            edtUsuario.setText(empresa.getUsuario()); // Exemplo, supondo que tenha esse método
            edtSenha.setText(empresa.getSenha()); // Exemplo, supondo que tenha esse método
            edtCnpj.setText(empresa.getCnpj()); // Exemplo, supondo que tenha esse método
            edtLocal.setText(empresa.getLocal());
        }
    }
    public void AtualizarEmpresa(View view) {

        // Obter valores dos campos
        String nomeEmpresa = edtNomeEmpresa.getText().toString();
        String telefone = edtTelefone.getText().toString();
        String email = edtEmail.getText().toString();
        String usuario = edtUsuario.getText().toString(); // Considerando o campo de usuário
        String senha = edtSenha.getText().toString();
        String cnpj = edtCnpj.getText().toString();
        String localizacao = edtLocal.getText().toString();

    // Verificação de campos obrigatórios
        if (nomeEmpresa.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Nome, Email e Senha são obrigatórios!", Toast.LENGTH_SHORT).show();
            return;
        }


        // Criar objeto Empresas com dados atualizados
        Empresas empresaAtualizada = new Empresas();
        empresaAtualizada.setIdEmpresa(userId);
        empresaAtualizada.setNome(nomeEmpresa);
        empresaAtualizada.setTelefone(telefone);
        empresaAtualizada.setEmail(email);
        empresaAtualizada.setUsuario(usuario); // Se você tiver esse método na sua classe
        empresaAtualizada.setSenha(senha); // Se você tiver esse método na sua classe
        empresaAtualizada.setCnpj(cnpj); // Se você tiver esse método na sua classe
        empresaAtualizada.setLocal(localizacao); // Se você tiver esse método na sua classe

        try {
            // Atualizar no banco de dados
            EmpresasDAO dao = new EmpresasDAO(this);
            dao.update(empresaAtualizada);

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