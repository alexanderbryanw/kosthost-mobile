package com.projectK1.kosthost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignupPencari extends AppCompatActivity {
    private EditText edtNamaPencari, edtTanggalLahirPencari, edtTeleponPencari, edtEmailPencari, edtPasswordPencari;
    private Button btnDatePicker, btnSignUp;
    int year, month, dayOfMonth;
    String namaPencari, tanggalLahir, telepon, email, password;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    String URL_INSERT_PENCARI = "http://192.168.1.7/kosthostmobile/insert-pencari.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_pencari);
        edtNamaPencari = findViewById(R.id.edtNamaPencari);
        edtTanggalLahirPencari = findViewById(R.id.edtTanggalLahirPencari);
        edtTeleponPencari = findViewById(R.id.edtTeleponPencari);
        edtEmailPencari = findViewById(R.id.edtEmailPencari);
        edtPasswordPencari = findViewById(R.id.edtPasswordPencari);
        btnDatePicker = findViewById(R.id.btnTimePicker);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(SignupPencari.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                edtTanggalLahirPencari.setText(year + "-" + (month + 1) + "-" + day);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namaPencari = edtNamaPencari.getText().toString();
                tanggalLahir = edtTanggalLahirPencari.getText().toString();
                telepon = edtTeleponPencari.getText().toString();
                email = edtEmailPencari.getText().toString();
                password = edtPasswordPencari.getText().toString();
                insertPencari(namaPencari, tanggalLahir, telepon, email, password);
            }
        });
    }

    private void insertPencari(String namaPencari, String tglPencari, String telpPencari, String emailPencari, String passwordPencari) {
        RequestQueue queue = Volley.newRequestQueue(SignupPencari.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_INSERT_PENCARI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        Toast.makeText(SignupPencari.this, "Akun berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), LoginPencari.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(SignupPencari.this, "Akun gagal ditambahkan", Toast.LENGTH_SHORT).show();
                        edtEmailPencari.setText("");
                        edtPasswordPencari.setText("");
                    }
                } catch (JSONException e) {
                    Toast.makeText(SignupPencari.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(SignupPencari.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("namaPencari", namaPencari);
                params.put("tanggalLahir", tanggalLahir);
                params.put("telepon", telepon);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }
}