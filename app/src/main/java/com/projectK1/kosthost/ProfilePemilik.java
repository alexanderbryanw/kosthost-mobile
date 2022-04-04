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
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilePemilik extends AppCompatActivity {
    private Button btnKembali, btnKeluar, btnEditProfil;
    String URL_PROFILE_PEMILIK = "http://192.168.1.7/kosthostmobile/profil-pemilik.php";
    String namaPemilik, tanggalLahir, telepon, email, IDPemilik, fotoPemilik;
    private TextView txtNamaPemilik, txtTanggalLahir, txtTelepon, txtEmail, txtStatus;
    String URL_FOTO_PEMILIK;
    private ImageView imagePemilik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pemilik);
        btnKembali = findViewById(R.id.btnKembali);
        btnKeluar = findViewById(R.id.btnKeluar);
        btnEditProfil = findViewById(R.id.btnEditProfil);
        txtNamaPemilik = findViewById(R.id.txtNamaPemilik);
        txtTanggalLahir = findViewById(R.id.txtTanggalLahir);
        txtTelepon = findViewById(R.id.txtTelepon);
        txtEmail = findViewById(R.id.txtEmail);
        txtStatus = findViewById(R.id.txtStatus);
        imagePemilik = findViewById(R.id.imagePemilik);
        Intent i = getIntent();
        IDPemilik = i.getStringExtra("IDPemilik");
        profilePemilik(IDPemilik);

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DashboardPemilik.class);
                i.putExtra("IDPemilik", IDPemilik);
                startActivity(i);
            }
        });
        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
        btnEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditProfilePemilik.class);
                i.putExtra("IDPemilik", IDPemilik);
                i.putExtra("namaPemilik", namaPemilik);
                i.putExtra("tanggalLahir", tanggalLahir);
                i.putExtra("telepon", telepon);
                i.putExtra("email", email);
                i.putExtra("status", txtStatus.getText().toString());
                i.putExtra("fotoPemilik", fotoPemilik);
                startActivity(i);
            }
        });
    }

    private void profilePemilik(String IDPemilik) {
        RequestQueue queue = Volley.newRequestQueue(ProfilePemilik.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_PROFILE_PEMILIK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if (success == 1) {
                                namaPemilik = jsonObject.getString("namaPemilik");
                                tanggalLahir = jsonObject.getString("tanggalLahir");
                                telepon = jsonObject.getString("telepon");
                                email = jsonObject.getString("email");
                                fotoPemilik = jsonObject.getString("fotoPemilik");
                                URL_FOTO_PEMILIK = "http://192.168.1.7/kosthostmobile/" + fotoPemilik;
                                Picasso.get().load(URL_FOTO_PEMILIK).into(imagePemilik);

                                txtNamaPemilik.setText(namaPemilik);
                                txtTanggalLahir.setText(tanggalLahir);
                                txtTelepon.setText(telepon);
                                txtEmail.setText(email);
                                txtStatus.setText("Pemilik Kost");
                            } else {
                                Toast.makeText(ProfilePemilik.this, "Data profil gagal diambil", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ProfilePemilik.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProfilePemilik.this, message, Toast.LENGTH_SHORT).show();
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
    }
}