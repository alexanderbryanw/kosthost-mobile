package com.projectK1.kosthost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.util.Base64;

import java.util.HashMap;
import java.util.Map;

public class TambahKost extends AppCompatActivity {
    private EditText edtNama, edtHarga, edtAlamat, edtJumlahKamarKost, edtFasilitasKost, edtIDPemilikKost;
    private Spinner spinKotaKost, spinJenisKost;
    private Button btnTambahKost, btnChoose;
    private ImageView imageKost;
    Bitmap bitmap;
    final int CODE_GALLERY_REQUEST = 999;
    private String[] kota = {"-Pilih Kota-", "Jakarta", "Tangerang", "Bogor"};
    private String[] tipeKost = {"-Pilih Jenis Kost-", "Kost Putra", "Kost Putri", "Kost Campur"};
    private String namaKost, hargaKost, alamatKost, kotaKost, jenisKost, jumlahKamar, deskripsiFasilitas, IDPemilik, fotoKost;
    String URL_INSERT_KOST = "http://192.168.1.7/kosthostmobile/insert-kost.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kost);

        Intent i = getIntent();
        IDPemilik = i.getStringExtra("IDPemilik");
        edtIDPemilikKost = findViewById(R.id.edtIDPemilikKost);
        edtIDPemilikKost.setText(IDPemilik);
        btnChoose = findViewById(R.id.btnChoose);
        imageKost = findViewById(R.id.imageKost);

        edtNama = findViewById(R.id.edtNama);
        edtHarga = findViewById(R.id.edtHarga);
        edtAlamat = findViewById(R.id.edtAlamat);
        edtJumlahKamarKost = findViewById(R.id.edtJumlahKamarKost);
        edtFasilitasKost = findViewById(R.id.edtFasilitasKost);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        TambahKost.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });

        spinKotaKost = findViewById(R.id.spinKotaKost);
        ArrayAdapter adapterKota = new ArrayAdapter(this, android.R.layout.simple_spinner_item, kota);
        spinKotaKost.setAdapter(adapterKota);

        spinKotaKost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                kotaKost = value;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinJenisKost = findViewById(R.id.spinJenisKost);
        ArrayAdapter adapterJenisKost = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tipeKost);
        spinJenisKost.setAdapter(adapterJenisKost);

        spinJenisKost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                jenisKost = value;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnTambahKost = findViewById(R.id.btnTambahKost);
        btnTambahKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namaKost = edtNama.getText().toString();
                hargaKost = edtHarga.getText().toString();
                alamatKost = edtAlamat.getText().toString();
                jumlahKamar = edtJumlahKamarKost.getText().toString();
                deskripsiFasilitas = edtFasilitasKost.getText().toString();
                IDPemilik = edtIDPemilikKost.getText().toString();


                tambahKost(namaKost, alamatKost, kotaKost, jenisKost, jumlahKamar, hargaKost, deskripsiFasilitas, IDPemilik);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "Tidak ada akses ke gallery", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageKost.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void tambahKost(final String namaKost, final String alamatKost, final String kotaKost, final String jenisKost, final String jumlahKamar, final String hargaKost, final String deskripsiFasilitas, final String IDPemilik) {
        RequestQueue queue = Volley.newRequestQueue(TambahKost.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERT_KOST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        Toast.makeText(TambahKost.this, "Kost berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), KostPemilik.class);
                        i.putExtra("IDPemilik", IDPemilik);
                        startActivity(i);
                    } else {
                        Toast.makeText(TambahKost.this, "Kost gagal ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(TambahKost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TambahKost.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                fotoKost = imageToString(bitmap);
                params.put("namaKost", namaKost);
                params.put("alamatKost", alamatKost);
                params.put("kotaKost", kotaKost);
                params.put("jenisKost", jenisKost);
                params.put("jumlahKamar", jumlahKamar);
                params.put("hargaKost", hargaKost);
                params.put("deskripsiFasilitas", deskripsiFasilitas);
                params.put("IDPemilik", IDPemilik);
                params.put("fotoKost", fotoKost);
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        return encodedImage;
    }
}