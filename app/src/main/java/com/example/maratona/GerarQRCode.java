package com.example.maratona;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class GerarQRCode extends AppCompatActivity {

    private ImageView qrCodeImageView;
    private Button btnGenerateQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerar_qrcode);

        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        btnGenerateQRCode = findViewById(R.id.btnGenerateQRCode);

        // Ao clicar no botão, gera o QR Code
        btnGenerateQRCode.setOnClickListener(view -> {
            String qrData = "Confirmado na corrida";
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
            } catch (WriterException e) {
                e.printStackTrace(); // Tratar exceções
            }
        });
    }
}
