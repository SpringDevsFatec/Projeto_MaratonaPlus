package com.example.maratona;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.maratona.dao.InscricaoDAO;
import com.example.maratona.dao.ParticipacaoDAO;
import com.example.maratona.model.Inscricao;
import com.example.maratona.model.Participacao;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.sql.Time;
import java.util.List;

public class ScanQRCode extends AppCompatActivity {

    private int userId,maratonaId, distanciaMaratona,inscricaoId;
    private CompoundBarcodeView barcodeScannerView;
    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_qrcode);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        maratonaId = intent.getIntExtra("maratonaId", -1);
        distanciaMaratona = intent.getIntExtra("distancia", -1);
        inscricaoId = intent.getIntExtra("inscricaoId", -1);


        Log.i("USERID_SCANQRCODE",String.valueOf(userId));
        Log.i("MARATONAID_SCANQRCODE",String.valueOf(maratonaId));
        Log.i("INSCRICAOID_SCANQRCODE",String.valueOf(inscricaoId));
        Log.i("DISTANCIA_SCANQRCODE",String.valueOf(distanciaMaratona));

        barcodeScannerView = findViewById(R.id.barcodeScanner);

        // Verificar se a permissão da câmera foi concedida
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Se a permissão não foi concedida, solicitar permissão
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            iniciarLeituraQRCode();  // Iniciar leitura do QR Code se a permissão já foi concedida
        }

    }

    // Método para iniciar a leitura do QR Code
    private void iniciarLeituraQRCode() {
        barcodeScannerView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result != null) {
                    String scannedData = result.getText();
                    if (scannedData.equals(String.valueOf(maratonaId))) {

                            // adicona participação e muda status da inscrição
                            InscricaoDAO idao = new InscricaoDAO(ScanQRCode.this);
                            int id = inscricaoId;
                            idao.updateStatusParaParticipando(id);

                            Participacao p = new Participacao();
                            p.setIdInscricao(id);

                            ParticipacaoDAO pdao = new ParticipacaoDAO(ScanQRCode.this);

                            long idParticipacao = pdao.insertParticipacao(p);

                            Log.i("IDPARTICIPACAO_CRIADA_SCANQRCODE", String.valueOf(idParticipacao));
                        Toast.makeText(ScanQRCode.this, "Participação Aceita! seu numero é "+ idParticipacao, Toast.LENGTH_SHORT).show();
                            // Vai para a tela de Participando
                            Intent intent;
                            intent = new Intent(ScanQRCode.this, AguardeMaratona.class);

                            intent.putExtra("maratonaId", maratonaId);
                            intent.putExtra("id", userId);
                            intent.putExtra("distancia", distanciaMaratona);
                            intent.putExtra("inscricaoId", Integer.valueOf(id));
                            intent.putExtra("participacaoId", Integer.valueOf((int) idParticipacao));


                            startActivity(intent);
                            finish();

                    }
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        });
    }

    // Método chamado quando o usuário responde ao pedido de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Se a permissão foi concedida, iniciar a leitura do QR Code
                iniciarLeituraQRCode();
            } else {
                // Se a permissão for negada, você pode mostrar uma mensagem ao usuário
                // dizendo que a câmera é necessária para ler o QR Code
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }
}