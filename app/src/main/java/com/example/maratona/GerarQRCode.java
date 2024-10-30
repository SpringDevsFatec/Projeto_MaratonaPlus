package com.example.maratona;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maratona.dao.MaratonasDAO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class  GerarQRCode extends AppCompatActivity {

    private String Activity, qrData,nomeMaratona;
    private int maratonaId;
    private ImageView qrCodeImageView;
    private Button btnGenerateQRCode;
    private TextView txtNomeMaratona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerar_qrcode);

        Intent intent = getIntent();
        nomeMaratona = String.valueOf(intent.getStringExtra("nomeMaratona"));
        maratonaId = intent.getIntExtra("maratonaId", -1);
        Activity = String.valueOf(intent.getStringExtra("activity"));
        qrData = String.valueOf(maratonaId);


        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        btnGenerateQRCode = findViewById(R.id.btnGenerateQRCode);
        txtNomeMaratona = findViewById(R.id.txtNomeMaratona);

        txtNomeMaratona.setText(nomeMaratona);

        if (!Activity.equals("Aberta para Inscrição")){
            btnGenerateQRCode.setVisibility(View.GONE);
            geraQrCode(qrData);
        }



        // Ao clicar no botão, gera o QR Code
        btnGenerateQRCode.setOnClickListener(view -> {
            geraQrCode(qrData);
        });

    }

    public void geraQrCode(String qrData){
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, 300, 300);
            Bitmap qrBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.RGB_565);

            for (int x = 0; x < 300; x++) {
                for (int y = 0; y < 300; y++) {
                    qrBitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF); // Preencher os pixels
                }
            }

            qrCodeImageView.setImageBitmap(qrBitmap);

            MaratonasDAO m = new MaratonasDAO(this);
            m.updateStatus(maratonaId, "Aberta");

        } catch (WriterException e) {
            e.printStackTrace(); // Tratar exceções
        }
    }
}
