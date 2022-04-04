package com.projectK1.kosthost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DashboardPemilik extends AppCompatActivity {
    private Button btnKostSaya, btnBookedKost, btnPenjagaKost, btnFAQPemilik, btnProfile;
    private ImageButton btnKH;
    String IDPemilik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_pemilik);

        btnKostSaya = findViewById(R.id.btnKostSaya);
        btnBookedKost = findViewById(R.id.btnBookedKost);
        btnPenjagaKost = findViewById(R.id.btnPenjagaKost);
        btnFAQPemilik = findViewById(R.id.btnFAQPemilik);
        btnProfile = findViewById(R.id.btnProfile);
        Intent i = getIntent();
        IDPemilik = i.getStringExtra("IDPemilik");

        btnKostSaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), KostPemilik.class);
                i.putExtra("IDPemilik", IDPemilik);
                startActivity(i);
            }
        });
        btnBookedKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BookedPemilik.class);
                i.putExtra("IDPemilik", IDPemilik);
                startActivity(i);
            }
        });
        btnPenjagaKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Penjaga.class);
                i.putExtra("IDPemilik", IDPemilik);
                startActivity(i);
            }
        });
        btnFAQPemilik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), faqPemilik.class);
                i.putExtra("IDPemilik", IDPemilik);
                startActivity(i);
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfilePemilik.class);
                i.putExtra("IDPemilik", IDPemilik);
                startActivity(i);
            }
        });

        btnKH = findViewById(R.id.btnKH);
        btnKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DashboardPemilik.class);
                i.putExtra("IDPemilik", IDPemilik);
                startActivity(i);
            }
        });
    }
}