package com.projectK1.kosthost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Signup extends AppCompatActivity {
    private Button btnSignupPencari, btnSignupPemilik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnSignupPencari = findViewById(R.id.btnSignupPencari);
        btnSignupPemilik = findViewById(R.id.btnSignupPemilik);

        btnSignupPencari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignupPencari.class);
                startActivity(i);
            }
        });

        btnSignupPemilik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignupPemilik.class);
                startActivity(i);
            }
        });

    }
}