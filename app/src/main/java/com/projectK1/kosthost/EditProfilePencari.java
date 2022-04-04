package com.projectK1.kosthost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProfilePencari extends AppCompatActivity {
    private Button btnKembali, btnSimpan, btnDatePicker, btnChoose;
    int year, month, dayOfMonth;
    Calendar calendar;
    Bitmap bitmap;
    private ImageView imagePencari;
    DatePickerDialog datePickerDialog;
    String fotoPencari;
    String URL_FOTO_PENCARI;
    final int CODE_GALLERY_REQUEST = 999;
    String URL_EDITPROFILE_PENCARI = "http://192.168.1.7/kosthostmobile/editprofile-pencari.php";
    private TextView txtNamaPencari, txtTanggalLahir, txtTelepon, txtEmail, txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_pencari);
        btnKembali = findViewById(R.id.btnKembali);
        btnSimpan = findViewById(R.id.btnSimpan);
        txtNamaPencari = findViewById(R.id.txtNamaPencari);
        txtTanggalLahir = findViewById(R.id.txtTanggalLahir);
        btnDatePicker = findViewById(R.id.btnTimePicker);
        txtTelepon = findViewById(R.id.txtTelepon);
        txtEmail = findViewById(R.id.txtEmail);
        txtStatus = findViewById(R.id.txtStatus);
        btnChoose = findViewById(R.id.btnChoose);
        imagePencari = findViewById(R.id.imagePencari);

        Intent i = getIntent();
        String IDPencari = i.getStringExtra("IDPencari");
        String namaPencari = i.getStringExtra("namaPencari");
        String tanggalLahir = i.getStringExtra("tanggalLahir");
        String telepon = i.getStringExtra("telepon");
        String email = i.getStringExtra("email");
        String status = i.getStringExtra("status");
        String fotoPencari = i.getStringExtra("fotoPencari");

        URL_FOTO_PENCARI = "http://192.168.1.7/kosthostmobile/" + fotoPencari;
        Picasso.get().load(URL_FOTO_PENCARI).into(imagePencari);
        txtNamaPencari.setText(namaPencari);
        txtTanggalLahir.setText(tanggalLahir);
        txtTelepon.setText(telepon);
        txtEmail.setText(email);
        txtStatus.setText(status);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        EditProfilePencari.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfilePencari.class);
                i.putExtra("IDPencari", IDPencari);
                startActivity(i);
                EditProfilePencari.this.finish();
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(EditProfilePencari.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                txtTanggalLahir.setText(year + "-" + (month + 1) + "-" + day);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaPencari = txtNamaPencari.getText().toString();
                String tanggalLahir = txtTanggalLahir.getText().toString();
                String telepon = txtTelepon.getText().toString();
                editProfilePencari(IDPencari, namaPencari, tanggalLahir, telepon);
            }
        });
    }

    private void editProfilePencari(String IDPencari, String namaPencari, String tanggalLahir, String telepon) {
        RequestQueue queue = Volley.newRequestQueue(EditProfilePencari.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_EDITPROFILE_PENCARI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        Toast.makeText(EditProfilePencari.this, "Profil anda berhasil diedit", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), ProfilePencari.class);
                        i.putExtra("IDPencari", IDPencari);
                        startActivity(i);
                        EditProfilePencari.this.finish();
                    } else {
                        Toast.makeText(EditProfilePencari.this, "Profil anda gagal diedit", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(EditProfilePencari.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditProfilePencari.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                fotoPencari = imageToString(bitmap);
                params.put("IDPencari", IDPencari);
                params.put("namaPencari", namaPencari);
                params.put("tanggalLahir", tanggalLahir);
                params.put("telepon", telepon);
                params.put("fotoPencari", fotoPencari);
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imagePencari.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
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

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
        return encodedImage;
    }
}