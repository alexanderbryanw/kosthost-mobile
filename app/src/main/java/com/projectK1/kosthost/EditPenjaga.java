package com.projectK1.kosthost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditPenjaga extends AppCompatActivity {
    private long id;
    private String namaKost;
    private String namaPenjaga;
    private String teleponPenjaga;
    String IDPemilik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_penjaga);
        Intent i = getIntent();
        IDPemilik = i.getStringExtra("IDPemilik");
        final EditText edtNamaKost = (EditText) findViewById(R.id.edtNamaKost);
        final EditText edtNamaPenjaga = (EditText) findViewById(R.id.edtNamaPenjaga);
        final EditText edtTeleponPenjaga = (EditText) findViewById(R.id.edtTeleponPenjaga);
        Button btnSimpan = (Button) findViewById(R.id.btnSimpan);

        //Buat objek untuk Class MyDBHandler
        final MyDBHandler dbHandler = new MyDBHandler(this);
        //Membuka Koneksi Database
        try {
            dbHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getLong("id");
        namaKost = bundle.getString("namaKost");
        namaPenjaga = bundle.getString("namaPenjaga");
        teleponPenjaga = bundle.getString("teleponPenjaga");
        this.setTitle("Edit Data dengan ID: " + id);
        edtNamaKost.setText(namaKost);
        edtNamaPenjaga.setText(namaPenjaga);
        edtTeleponPenjaga.setText(teleponPenjaga);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PenjagaKost penjagaKost = new PenjagaKost();
                penjagaKost.setID(id);
                penjagaKost.setNamaKost(edtNamaKost.getText().toString());
                penjagaKost.setNamaPenjaga(edtNamaPenjaga.getText().toString());
                penjagaKost.setTeleponPenjaga(edtTeleponPenjaga.getText().toString());
                dbHandler.updatePenjagaKost(penjagaKost);

                Toast.makeText(EditPenjaga.this, "Data Penjaga berhasil diedit", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditPenjaga.this, Penjaga.class);
                i.putExtra("IDPemilik", IDPemilik);
                startActivity(i);
                EditPenjaga.this.finish();
                dbHandler.close();
            }
        });
    }
}