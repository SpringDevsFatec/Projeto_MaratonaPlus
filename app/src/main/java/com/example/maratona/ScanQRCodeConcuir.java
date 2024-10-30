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

public class ScanQRCodeConcuir extends AppCompatActivity {

    private int userId,maratonaId, inscricaoId;
    private CompoundBarcodeView barcodeScannerView;
    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_qrcode_concuir);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        maratonaId = intent.getIntExtra("maratonaId", -1);
        inscricaoId = intent.getIntExtra("inscricaoId", -1);



        barcodeScannerView = findViewById(R.id.barcodeScanner2);

        // Verificar se a permissão da câmera foi concedida
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Se a permissão não foi concedida, solicitar permissão
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            FinalizarLeituraQRCode();
        }

    }

    // Método para iniciar a leitura do QR Code
    private void FinalizarLeituraQRCode() {
        barcodeScannerView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result != null) {
                    String scannedData = result.getText();
                    if (scannedData.equals(String.valueOf(maratonaId))) {


                        ParticipacaoDAO pdao = new ParticipacaoDAO(ScanQRCodeConcuir.this);
                        int id = pdao.getIdParticipacao(inscricaoId);

                        // Buscar tempo inicial
                        long time_inicial = pdao.readTimeInicialParticipacao(id);


                        if (time_inicial != -1) {

                            long time_atual = System.currentTimeMillis();


                            long tempo_registrado_ms = time_atual - time_inicial;


                            Participacao p = new Participacao();
                            p.setIdParticipacao(id);
                            p.setStatusConclusao("Desativado");


                            p.setTempoFim(new Time(time_atual));
                            p.setTempoRegistrado(new Time(tempo_registrado_ms));  // Converte de milissegundos para Time

                            pdao.updateStatus(p);
                        }else{
                            Toast.makeText(ScanQRCodeConcuir.this, "Tempo errado!", Toast.LENGTH_SHORT).show();

                            return ;
                        }

                        InscricaoDAO idao = new InscricaoDAO(ScanQRCodeConcuir.this);


                        idao.updateStatus(inscricaoId,"Concluido");
                        Toast.makeText(ScanQRCodeConcuir.this, "Participação Finalizada, Parabens!", Toast.LENGTH_SHORT).show();
                        // Vai para a tela de Participando
                        Intent intent;
                        intent = new Intent(ScanQRCodeConcuir.this, VisualizarConcluidas.class);


                        intent.putExtra("id", userId);


                        //  startActivityForResult(intent, 1);
                        startActivityForResult(intent,2);
                        finish();



                }
            }}

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
                FinalizarLeituraQRCode();
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