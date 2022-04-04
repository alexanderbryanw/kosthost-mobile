package com.projectK1.kosthost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class faqPencari extends AppCompatActivity {
    private ImageButton btnKH;
    String IDPencari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_pencari);
        Intent i = getIntent();
        IDPencari = i.getStringExtra("IDPencari");
        btnKH = findViewById(R.id.btnKH);
        btnKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DashboardPencari.class);
                i.putExtra("IDPencari", IDPencari);
                startActivity(i);
                faqPencari.this.finish();
            }
        });
    }
}