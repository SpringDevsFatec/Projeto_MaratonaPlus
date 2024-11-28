package com.example.maratona;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.CorredoresDAO;
import com.example.maratona.dao.MaratonasDAO;
import com.example.maratona.model.Corredores;
import com.example.maratona.model.Maratonas;

import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;

public class CadastrarMaratona extends AppCompatActivity {

    EditText edtNome,edtEndereco,edtData_I,edtData_F,edtDistancia,edtDescricao,edtRegras,edtLimite_p, edtValor, edtTipoTerreno, edtClimaEsperado;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastrar_maratona);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);

        edtNome = findViewById(R.id.edtNome);
        edtEndereco = findViewById(R.id.edtEndereco);
        edtData_I = findViewById(R.id.edtData_I);
        edtData_F = findViewById(R.id.edtData_F);
        edtDistancia = findViewById(R.id.edtDistancia);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtRegras = findViewById(R.id.edtRegras);
        edtLimite_p = findViewById(R.id.edtLimite_p);
        edtValor = findViewById(R.id.edtValor);
        edtTipoTerreno = findViewById(R.id.edtTipoTerreno);
        edtClimaEsperado = findViewById(R.id.edtClimaEsperado);

        // DatePicker para a data inicial
        edtData_I.setOnClickListener(v -> showDatePicker(edtData_I));

        // DatePicker para a data final
        edtData_F.setOnClickListener(v -> showDatePicker(edtData_F));

    }

    public void CadastrarMaratonas(View view) {
        try {
            String nome = edtNome.getText().toString();
            String descricao = edtDescricao.getText().toString();

            // Verificação de campos obrigatórios
            if (nome.trim().isEmpty() || descricao.trim().isEmpty()) {
                Toast.makeText(this, "Campo nome e descricao precisam ser preenchidos", Toast.LENGTH_SHORT).show();
                return;
            }

            Maratonas m = new Maratonas();
            m.setNome(nome);
            m.setCriador(userId);

            m.setData_inicio(edtData_I.getText().toString());
            m.setData_final(edtData_F.getText().toString());

            m.setLocal(edtEndereco.getText().toString());
            m.setDistancia(edtDistancia.getText().toString());
            m.setDescricao(descricao);

            try {
                m.setLimite_participantes(Integer.parseInt(edtLimite_p.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "O limite de participantes deve ser um número.", Toast.LENGTH_SHORT).show();
                return;
            }

            m.setRegras(edtRegras.getText().toString());

            try {
                m.setValor(Float.parseFloat(edtValor.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(this, "O valor deve ser um número.", Toast.LENGTH_SHORT).show();
                return;
            }

            m.setTipo_terreno(edtTipoTerreno.getText().toString());
            m.setClima_esperado(edtClimaEsperado.getText().toString());

            MaratonasDAO maratonadao = new MaratonasDAO(this);
            long id = maratonadao.insert(m);


            Toast.makeText(getApplicationContext(), "Maratona inserida com sucesso com o ID " + id, Toast.LENGTH_LONG).show();


            setResult(RESULT_OK);
            finish();

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Erro ao cadastrar maratona: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showDatePicker(EditText editText) {
        // Obter a data atual para exibir no DatePicker
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Exibir o DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, yearSelected, monthSelected, daySelected) -> {
            // Formatar a data no formato desejado (ISO-8601)
            String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", yearSelected, monthSelected + 1, daySelected);
            editText.setText(formattedDate); // Exibir no campo
        }, year, month, day);

        datePickerDialog.show();
    }


}
