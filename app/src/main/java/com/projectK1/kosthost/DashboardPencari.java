package com.projectK1.kosthost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DashboardPencari extends AppCompatActivity {
    private Button btnSearch, btnBooked, btnFAQ, btnProfile;
    private ImageButton btnKH;
    String IDPencari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_pencari);
        btnSearch = findViewById(R.id.btnSearch);
        btnBooked = findViewById(R.id.btnBooked);
        btnFAQ = findViewById(R.id.btnFAQ);
        btnProfile = findViewById(R.id.btnProfile);
        Intent i = getIntent();
        IDPencari = i.getStringExtra("IDPencari");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SearchPencari.class);
                i.putExtra("IDPencari", IDPencari);
                startActivity(i);
            }
        });

        btnBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BookedPencari.class);
                i.putExtra("IDPencari", IDPencari);
                startActivity(i);
            }
        });

        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), faqPencari.class);
                i.putExtra("IDPencari", IDPencari);
                startActivity(i);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfilePencari.class);
                i.putExtra("IDPencari", IDPencari);
                startActivity(i);
            }
        });

        btnKH = findViewById(R.id.btnKH);
        btnKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DashboardPencari.class);
                i.putExtra("IDPencari", IDPencari);
                startActivity(i);
            }
        });
    }
}