package com.projectK1.kosthost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KostPemilik extends AppCompatActivity {
    private ListView listKost;
    private ImageButton btnTambahKost, btnKH;
    ArrayList<HashMap<String, String>> listkost;
    String URL_GET_KOST = "http://192.168.1.7/kosthostmobile/getmykost-pemilik.php";
    String IDKost, namaKost, alamatKost, kotaKost, jenisKost, jumlahKamar, hargaKost, deskripsiFasilitas, fotoKost;
    String IDPemilik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kost_pemilik);
        Intent i = getIntent();
        IDPemilik = i.getStringExtra("IDPemilik");

        btnTambahKost = findViewById(R.id.btnTambahKost);
        btnTambahKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TambahKost.class);
                i.putExtra("IDPemilik", IDPemilik);
                startActivity(i);
            }
        });

        listKost = findViewById(R.id.listKost);
        listkost = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(KostPemilik.this);

        StringRequest request = new StringRequest(Request.Method.POST, URL_GET_KOST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject kost = jsonArray.getJSONObject(i);
                                IDKost = kost.getString("IDKost");
                                namaKost = kost.getString("namaKost");
                                alamatKost = kost.getString("alamatKost");
                                kotaKost = kost.getString("kotaKost");
                                jenisKost = kost.getString("jenisKost");
                                jumlahKamar = kost.getString("jumlahKamar");
                                hargaKost = kost.getString("hargaKost");
                                fotoKost = kost.getString("fotoKost");

                                double dhargaKost = Double.parseDouble(hargaKost);
                                Locale localeID = new Locale("in", "ID");
                                NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
                                String hargaKostFormat = formatRupiah.format(dhargaKost);
                                deskripsiFasilitas = kost.getString("deskripsiFasilitas");

                                HashMap<String, String> map = new HashMap<>();
                                map.put("IDKost", IDKost);
                                map.put("namaKost", namaKost);
                                map.put("alamatKost", alamatKost);
                                map.put("kotaKost", kotaKost);
                                map.put("jenisKost", jenisKost);
                                map.put("jumlahKamar", jumlahKamar);
                                map.put("hargaKost", hargaKost);
                                map.put("hargaKostFormat", hargaKostFormat);
                                map.put("deskripsiFasilitas", deskripsiFasilitas);
                                map.put("fotoKost", fotoKost);
                                listkost.add(map);

                                String[] from = {"IDKost", "namaKost", "alamatKost", "kotaKost", "jenisKost", "jumlahKamar", "hargaKost", "hargaKostFormat", "deskripsiFasilitas", "fotoKost"};
                                int[] to = {R.id.txtIDKost, R.id.txtKost, R.id.txtAlamat, R.id.txtKota, R.id.txtJenis, R.id.txtJumlahKamar, R.id.txtHargaAsli, R.id.txtHarga, R.id.txtFasilitas, R.id.txtfotoKost};

                                ListAdapter adapter = new SimpleAdapter(KostPemilik.this, listkost, R.layout.list_kost, from, to);
                                listKost.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(KostPemilik.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(KostPemilik.this, message, Toast.LENGTH_SHORT).show();
            }

        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("IDPemilik", IDPemilik);
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(request);
        listKost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String get_nama = ((TextView) view.findViewById(R.id.txtKost)).getText().toString();
                String get_alamat = ((TextView) view.findViewById(R.id.txtAlamat)).getText().toString();
                String get_kota = ((TextView) view.findViewById(R.id.txtKota)).getText().toString();
                String get_jenis = ((TextView) view.findViewById(R.id.txtJenis)).getText().toString();
                String get_jumlahKamar = ((TextView) view.findViewById(R.id.txtJumlahKamar)).getText().toString();
                String get_fasilitas = ((TextView) view.findViewById(R.id.txtFasilitas)).getText().toString();
                String get_harga = ((TextView) view.findViewById(R.id.txtHargaAsli)).getText().toString();
                String get_idkost = ((TextView) view.findViewById(R.id.txtIDKost)).getText().toString();
                String get_fotoKost = ((TextView) view.findViewById(R.id.txtfotoKost)).getText().toString();

                Intent i = new Intent(KostPemilik.this, EditKost.class);
                i.putExtra("namaKost", get_nama);
                i.putExtra("alamatKost", get_alamat);
                i.putExtra("kotaKost", get_kota);
                i.putExtra("jenisKost", get_jenis);
                i.putExtra("jumlahKamar", get_jumlahKamar);
                i.putExtra("hargaKost", get_harga);
                i.putExtra("deskripsiFasilitas", get_fasilitas);
                i.putExtra("IDKost", get_idkost);
                i.putExtra("IDPemilik", IDPemilik);
                i.putExtra("fotoKost", get_fotoKost);
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
                KostPemilik.this.finish();
            }
        });
    }
}