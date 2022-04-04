package com.projectK1.kosthost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginPencari extends AppCompatActivity {
    private EditText edtEmailPencari, edtPasswordPencari;
    private Button btnLogin;
    String URL_LOGIN_PENCARI = "http://192.168.1.7/kosthostmobile/login-pencari.php";
    String email, password;
    String IDPencari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pencari);
        edtEmailPencari = findViewById(R.id.edtEmailPencari);
        edtPasswordPencari = findViewById(R.id.edtPasswordPencari);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtEmailPencari.getText().toString();
                password = edtPasswordPencari.getText().toString();
                login(email, password);
            }
        });
    }

    private void login(String email, String password) {
        RequestQueue queue = Volley.newRequestQueue(LoginPencari.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_LOGIN_PENCARI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            if (success == 1) {
                                Toast.makeText(LoginPencari.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), DashboardPencari.class);
                                IDPencari = jsonObject.getString("message");
                                //Toast.makeText(LoginPencari.this, IDPencari, Toast.LENGTH_SHORT).show();
                                i.putExtra("IDPencari", IDPencari);
                                startActivity(i);
                            } else {
                                Toast.makeText(LoginPencari.this, "Login gagal, username atau password salah", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(LoginPencari.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginPencari.this, message, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }
}