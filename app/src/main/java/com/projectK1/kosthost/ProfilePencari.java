package com.projectK1.kosthost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfilePencari extends AppCompatActivity {
    private Button btnKembali, btnKeluar, btnEditProfil;
    String URL_PROFILE_PENCARI = "http://192.168.1.7/kosthostmobile/profil-pencari.php";
    String namaPencari, tanggalLahir, telepon, email, IDPencari, fotoPencari;
    private TextView txtNamaPencari, txtTanggalLahir, txtTelepon, txtEmail, txtStatus;
    private ImageView imagePencari;
    String URL_FOTO_PENCARI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pencari);
        btnKembali = findViewById(R.id.btnKembali);
        btnKeluar = findViewById(R.id.btnKeluar);
        btnEditProfil = findViewById(R.id.btnEditProfil);
        txtNamaPencari = findViewById(R.id.txtNamaPencari);
        txtTanggalLahir = findViewById(R.id.txtTanggalLahir);
        txtTelepon = findViewById(R.id.txtTelepon);
        txtEmail = findViewById(R.id.txtEmail);
        txtStatus = findViewById(R.id.txtStatus);
        imagePencari = findViewById(R.id.imagePencari);
        Intent i = getIntent();
        IDPencari = i.getStringExtra("IDPencari");
        profilePencari(IDPencari);

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DashboardPencari.class);
                i.putExtra("IDPencari", IDPencari);
                startActivity(i);
                ProfilePencari.this.finish();
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
                Intent i = new Intent(getApplicationContext(), EditProfilePencari.class);
                i.putExtra("IDPencari", IDPencari);
                i.putExtra("namaPencari", namaPencari);
                i.putExtra("tanggalLahir", tanggalLahir);
                i.putExtra("telepon", telepon);
                i.putExtra("email", email);
                i.putExtra("status", txtStatus.getText().toString());
                i.putExtra("fotoPencari", fotoPencari);
                startActivity(i);
            }
        });
    }

    private void profilePencari(String IDPencari) {
        RequestQueue queue = Volley.newRequestQueue(ProfilePencari.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_PROFILE_PENCARI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if (success == 1) {
                                namaPencari = jsonObject.getString("namaPencari");
                                tanggalLahir = jsonObject.getString("tanggalLahir");
                                telepon = jsonObject.getString("telepon");
                                email = jsonObject.getString("email");
                                fotoPencari = jsonObject.getString("fotoPencari");
                                URL_FOTO_PENCARI = "http://192.168.1.7/kosthostmobile/" + fotoPencari;
                                Picasso.get().load(URL_FOTO_PENCARI).into(imagePencari);

                                txtNamaPencari.setText(namaPencari);
                                txtTanggalLahir.setText(tanggalLahir);
                                txtTelepon.setText(telepon);
                                txtEmail.setText(email);
                                txtStatus.setText("Pencari Kost");
                            } else {
                                Toast.makeText(ProfilePencari.this, "Data profil gagal diambil", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ProfilePencari.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProfilePencari.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("IDPencari", IDPencari);
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }
}