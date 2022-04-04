package com.projectK1.kosthost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Penjaga extends ListActivity implements AdapterView.OnItemLongClickListener {
    private MyDBHandler dbHandler;
    private ArrayList<PenjagaKost> values;
    private ListView list;
    private ImageButton btnTambahPenjaga, btnKH;
    private Button btnEditPenjaga, btnDeletePenjaga;
    Context context = this;
    String IDPemilik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjaga);
        dbHandler = new MyDBHandler(this);
        Intent i = getIntent();
        IDPemilik = i.getStringExtra("IDPemilik");
        //Membuka koneksi database
        try {
            dbHandler.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        values = dbHandler.getAllPenjagaKost();

        ArrayAdapter<PenjagaKost> adapter = new ArrayAdapter<PenjagaKost>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        list = (ListView) findViewById(android.R.id.list);
        list.setOnItemLongClickListener(this);

        btnTambahPenjaga = findViewById(R.id.btnTambahPenjaga);
        btnTambahPenjaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TambahPenjaga.class);
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
                Penjaga.this.finish();
            }
        });
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_penjaga);
        dialog.setTitle("Pilih Aksi!");
        dialog.show();

        final PenjagaKost penjagaKost = (PenjagaKost) getListAdapter().getItem(i);
        final long id = penjagaKost.getID();

        btnEditPenjaga = dialog.findViewById(R.id.btnEditPenjaga);
        btnDeletePenjaga = dialog.findViewById(R.id.btnDeletePenjaga);

        btnEditPenjaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PenjagaKost penjagaKost = dbHandler.getPenjagaKost(id);
                Intent i = new Intent(getApplicationContext(), EditPenjaga.class);
                Bundle bundle = new Bundle();
                bundle.putLong("id", penjagaKost.getID());
                bundle.putString("namaKost", penjagaKost.getNamaKost());
                bundle.putString("namaPenjaga", penjagaKost.getNamaPenjaga());
                bundle.putString("teleponPenjaga", penjagaKost.getTeleponPenjaga());
                i.putExtras(bundle);
                i.putExtra("IDPemilik", IDPemilik);
                startActivity(i);
                dialog.dismiss();
            }
        });

        btnDeletePenjaga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder konfirmasi = new AlertDialog.Builder(context);
                konfirmasi.setTitle("Hapus Data Penjaga Kost");
                konfirmasi.setMessage("Apakah Anda yakin akan menghapus data ini?");
                konfirmasi.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHandler.deletePenjagaKost(id);
                        Toast.makeText(Penjaga.this, "Data Penjaga berhasil dihapus", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), Penjaga.class);
                        i.putExtra("IDPemilik", IDPemilik);
                        startActivity(i);
                    }
                });
                konfirmasi.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                konfirmasi.show();
                dialog.dismiss();
            }
        });
        return true;
    }
}