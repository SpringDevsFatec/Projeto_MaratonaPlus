package com.example.maratona;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ConfirmacaoQrCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao_qr_code);

        // Inicia o escaneamento do QR Code
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // Valida o conteúdo escaneado
                if (result.getContents().equals("Confirmado na corrida")) {
                    // Redireciona para a tela de confirmação
                    Intent intent = new Intent(this, ConfirmacaoQrCode.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "QR Code inválido!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Escaneamento cancelado", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
