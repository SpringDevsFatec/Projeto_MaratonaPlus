package com.example.maratona;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

    private String Activity;
    private int userId,maratonaId;
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
        Activity = String.valueOf(intent.getStringExtra("activity"));


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
                        if (Activity.equals("Participar")){
                            // adicona participação e muda status da inscrição
                            InscricaoDAO idao = new InscricaoDAO(ScanQRCode.this);
                            int id = idao.getIdInscricao(maratonaId, userId);
                            idao.updateStatus(id,"Participando");

                            Participacao p = new Participacao();
                            p.setIdInscricao(id);
                            p.setStatusConclusao("Ativo");
                            p.setPassos(0);
                            p.setTempoInicio(Time.valueOf(String.valueOf(new Time(System.currentTimeMillis()))));

                            ParticipacaoDAO pdao = new ParticipacaoDAO(ScanQRCode.this);
                            Toast.makeText(ScanQRCode.this, "Participação Aceita!", Toast.LENGTH_SHORT).show();

                            pdao.insert(p);

                            // Vai para a tela de Participando
                            Intent intent;
                            intent = new Intent(ScanQRCode.this, TelaParticipando.class);

                            intent.putExtra("maratonaId", maratonaId);
                            intent.putExtra("userId", userId);
                            intent.putExtra("inscricaoId", id);

                            //  startActivityForResult(intent, 1);
                            startActivity(intent);
                        }else {

                        }
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