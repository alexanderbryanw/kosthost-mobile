package com.projectK1.kosthost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookKost extends AppCompatActivity {
    private TextView txtNamaKost, txtHargaKost, txtAlamatKost, txtKotaKost, txtJenisKost, txtJumlahKamar, txtFasilitas, txtIDKost;
    String IDPencari, namaKost, alamatKost, kotaKost, jenisKost, jumlahKamar, hargaKost, deskripsiFasilitas, IDKost, fotoKost;
    String URL_INSERT_PENYEWAAN = "http://192.168.1.7/kosthostmobile/insert-penyewaan.php";
    private Button btnBookKost;
    String URL_FOTO_KOST;
    private ImageView imageKost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_kost);
        txtNamaKost = findViewById(R.id.txtNamaKost);
        txtHargaKost = findViewById(R.id.txtHargaKost);
        txtAlamatKost = findViewById(R.id.txtAlamatKost);
        txtKotaKost = findViewById(R.id.txtKotaKost);
        txtJenisKost = findViewById(R.id.txtJenisKost);
        txtJumlahKamar = findViewById(R.id.txtJumlahKamar);
        txtFasilitas = findViewById(R.id.txtFasilitas);
        txtIDKost = findViewById(R.id.txtIDKost);
        btnBookKost = findViewById(R.id.btnBookKost);
        imageKost = findViewById(R.id.imageKost);

        Intent i = getIntent();
        IDPencari = i.getStringExtra("IDPencari");
        namaKost = i.getStringExtra("namaKost");
        txtNamaKost.setText(namaKost);
        alamatKost = i.getStringExtra("alamatKost");
        txtAlamatKost.setText(alamatKost);
        kotaKost = i.getStringExtra("kotaKost");
        txtKotaKost.setText(kotaKost);
        jenisKost = i.getStringExtra("jenisKost");
        txtJenisKost.setText(jenisKost);
        jumlahKamar = i.getStringExtra("jumlahKamar");
        txtJumlahKamar.setText(jumlahKamar);
        hargaKost = i.getStringExtra("hargaKost");
        txtHargaKost.setText(hargaKost);
        deskripsiFasilitas = i.getStringExtra("deskripsiFasilitas");
        txtFasilitas.setText(deskripsiFasilitas);
        IDKost = i.getStringExtra("IDKost");
        txtIDKost.setText(IDKost);
        fotoKost = i.getStringExtra("fotoKost");
        URL_FOTO_KOST = "http://192.168.1.7/kosthostmobile/" + fotoKost;
        Picasso.get().load(URL_FOTO_KOST).into(imageKost);

        btnBookKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDKost = txtIDKost.getText().toString();
                bookKost(IDKost, IDPencari);
            }
        });
    }

    private void bookKost(String idKost, String idPencari) {
        RequestQueue queue = Volley.newRequestQueue(BookKost.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERT_PENYEWAAN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        Toast.makeText(BookKost.this, "Kost berhasil dibook", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), BookedPencari.class);
                        i.putExtra("IDPencari", IDPencari);
                        startActivity(i);
                        BookKost.this.finish();
                    } else {
                        Toast.makeText(BookKost.this, "Kost gagal dibook", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(BookKost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BookKost.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("IDKost", IDKost);
                params.put("IDPencari", IDPencari);
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }
}