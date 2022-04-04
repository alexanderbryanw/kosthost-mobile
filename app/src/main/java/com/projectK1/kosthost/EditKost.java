package com.projectK1.kosthost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditKost extends AppCompatActivity {
    private EditText edtNama, edtHarga, edtAlamat, edtJumlahKamarKost, edtFasilitasKost, edtIDKost;
    private Spinner spinKotaKost, spinJenisKost;
    private Button btnEditKost, btnDeleteKost, btnChoose;
    private ImageView imageKost;
    Bitmap bitmap;
    final int CODE_GALLERY_REQUEST = 999;
    private String[] kota = {"-Pilih Kota-", "Jakarta", "Tangerang", "Bogor"};
    private String[] jenis = {"-Pilih Jenis Kost-", "Kost Putra", "Kost Putri", "Kost Campur"};
    private String IDKost, namaKost, hargaKost, alamatKost, kotaKost, jumlahKamar, deskripsiFasilitas, jenisKost, fotoKost;
    String IDPemilik;
    String URL_FOTO_KOST;

    String URL_EDIT_KOST = "http://192.168.1.7/kosthostmobile/edit-kost.php";
    String URL_DELETE_KOST = "http://192.168.1.7/kosthostmobile/delete-kost.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kost);

        edtIDKost = findViewById(R.id.edtIDKost);
        edtNama = findViewById(R.id.edtNama);
        edtHarga = findViewById(R.id.edtHarga);
        edtAlamat = findViewById(R.id.edtAlamat);
        edtJumlahKamarKost = findViewById(R.id.edtJumlahKamarKost);
        edtFasilitasKost = findViewById(R.id.edtFasilitasKost);
        btnEditKost = findViewById(R.id.btnEditKost);
        btnDeleteKost = findViewById(R.id.btnDeleteKost);
        btnChoose = findViewById(R.id.btnChoose);
        imageKost = findViewById(R.id.imageKost);

        Intent i = getIntent();
        IDPemilik = i.getStringExtra("IDPemilik");
        String namakost = i.getStringExtra("namaKost");
        edtNama.setText(namakost);
        String hargakost = i.getStringExtra("hargaKost");
        edtHarga.setText(hargakost);
        String alamatkost = i.getStringExtra("alamatKost");
        edtAlamat.setText(alamatkost);
        String kotakost = i.getStringExtra("kotaKost");
        String jeniskost = i.getStringExtra("jenisKost");
        String jumlahkamar = i.getStringExtra("jumlahKamar");
        edtJumlahKamarKost.setText(jumlahkamar);
        String fasilitaskost = i.getStringExtra("deskripsiFasilitas");
        edtFasilitasKost.setText(fasilitaskost);
        String idkost = i.getStringExtra("IDKost");
        edtIDKost.setText(idkost);
        String fotoKost = i.getStringExtra("fotoKost");
        URL_FOTO_KOST = "http://192.168.1.7/kosthostmobile/" + fotoKost;
        Picasso.get().load(URL_FOTO_KOST).into(imageKost);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        EditKost.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });
        spinKotaKost = findViewById(R.id.spinKotaKost);
        ArrayAdapter adapterKota = new ArrayAdapter(this, android.R.layout.simple_spinner_item, kota);
        spinKotaKost.setAdapter(adapterKota);
        spinKotaKost.setSelection(getIndexKota(spinKotaKost, kotakost));

        spinKotaKost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String kota = parent.getItemAtPosition(position).toString();
                kotaKost = kota;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinJenisKost = findViewById(R.id.spinJenisKost);
        ArrayAdapter adapterJenisKost = new ArrayAdapter(this, android.R.layout.simple_spinner_item, jenis);
        spinJenisKost.setAdapter(adapterJenisKost);
        spinJenisKost.setSelection(getIndexJenis(spinJenisKost, jeniskost));

        spinJenisKost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String jenis = parent.getItemAtPosition(position).toString();
                jenisKost = jenis;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnEditKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDKost = edtIDKost.getText().toString();
                namaKost = edtNama.getText().toString();
                hargaKost = edtHarga.getText().toString();
                alamatKost = edtAlamat.getText().toString();
                jumlahKamar = edtJumlahKamarKost.getText().toString();
                deskripsiFasilitas = edtFasilitasKost.getText().toString();
                editkost(IDKost, namakost, hargaKost, alamatKost, kotaKost, jenisKost, jumlahKamar, deskripsiFasilitas);
            }
        });
        btnDeleteKost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditKost.this);
                builder.setTitle("Konfirmasi Hapus");
                builder.setMessage("Apakah anda yakin ingin menghapus kost ?");

                builder.setPositiveButton("Ya", (dialog, which) -> {
                    IDKost = edtIDKost.getText().toString();
                    deletekost(IDKost);
                });

                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

    }

    private int getIndexKota(Spinner spinKotaKost, String kotakost) {
        int indexKota = 0;
        for (int i = 0; i < spinKotaKost.getCount(); i++) {
            if (spinKotaKost.getItemAtPosition(i).toString().equalsIgnoreCase(kotakost)) {
                indexKota = i;
                break;
            }
        }
        return indexKota;
    }

    private int getIndexJenis(Spinner spinJenisKost, String jeniskost) {
        int indexJenis = 0;
        for (int i = 0; i < spinJenisKost.getCount(); i++) {
            if (spinJenisKost.getItemAtPosition(i).toString().equalsIgnoreCase(jeniskost)) {
                indexJenis = i;
                break;
            }
        }
        return indexJenis;
    }

    private void deletekost(String IDKost) {
        RequestQueue queue = Volley.newRequestQueue(EditKost.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_DELETE_KOST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        Toast.makeText(EditKost.this, "Data Kost berhasil dihapus", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), KostPemilik.class);
                        i.putExtra("IDPemilik", IDPemilik);
                        startActivity(i);
                        EditKost.this.finish();

                    } else {
                        Toast.makeText(EditKost.this, "Data Kost gagal dihapus", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(EditKost.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(EditKost.this, message, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("IDKost", IDKost);
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }

    private void editkost(String IDKost, String namaKost, String hargaKost, String alamatKost, String kotaKost, String jenisKost, String jumlahKamar, String deskripsiFasilitas) {
        RequestQueue queue = Volley.newRequestQueue(EditKost.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_EDIT_KOST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        Toast.makeText(EditKost.this, "Data Kost berhasil diedit", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), KostPemilik.class);
                        i.putExtra("IDPemilik", IDPemilik);
                        startActivity(i);
                        EditKost.this.finish();
                    } else {
                        Toast.makeText(EditKost.this, "Data Kost gagal diedit", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(EditKost.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(EditKost.this, message, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                fotoKost = imageToString(bitmap);
                params.put("IDKost", IDKost);
                params.put("namaKost", namaKost);
                params.put("hargaKost", hargaKost);
                params.put("alamatKost", alamatKost);
                params.put("kotaKost", kotaKost);
                params.put("jenisKost", jenisKost);
                params.put("jumlahKamar", jumlahKamar);
                params.put("deskripsiFasilitas", deskripsiFasilitas);
                params.put("fotoKost", fotoKost);
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(request);
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

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        return encodedImage;
    }

}