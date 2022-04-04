package com.projectK1.kosthost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TambahPenjaga extends AppCompatActivity {
    String IDPemilik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_penjaga);
        final EditText edtNamaKost = (EditText) findViewById(R.id.edtNamaKost);
        final EditText edtNamaPenjaga = (EditText) findViewById(R.id.edtNamaPenjaga);
        final EditText edtTeleponPenjaga = (EditText) findViewById(R.id.edtTeleponPenjaga);
        Button btnSimpan = (Button) findViewById(R.id.btnSimpan);
        Intent i = getIntent();
        IDPemilik = i.getStringExtra("IDPemilik");

        //Buat objek untuk Class MyDBHandler
        final MyDBHandler dbHandler = new MyDBHandler(this);
        //Membuka Koneksi Database
        try {
            dbHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PenjagaKost penjagaKost = new PenjagaKost();
                String namaKost = edtNamaKost.getText().toString();
                String namaPenjaga = edtNamaPenjaga.getText().toString();
                String teleponPenjaga = edtTeleponPenjaga.getText().toString();
                dbHandler.createPenjagaKost(namaKost, namaPenjaga, teleponPenjaga);
                Toast.makeText(TambahPenjaga.this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                edtNamaKost.setText("");
                edtNamaPenjaga.setText("");
                edtTeleponPenjaga.setText("");
                edtNamaKost.requestFocus();
                Intent i = new Intent(TambahPenjaga.this, Penjaga.class);
                i.putExtra("IDPemilik", IDPemilik);
                startActivity(i);
                TambahPenjaga.this.finish();
                dbHandler.close();
            }
        });
    }
}