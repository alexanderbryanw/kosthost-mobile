package com.projectK1.kosthost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookedPencari extends AppCompatActivity {
    private ListView listBooked;
    private ImageButton btnKH;
    ArrayList<HashMap<String, String>> listbooked;
    String URL_GET_BOOKEDKOST = "http://192.168.1.7/kosthostmobile/getbooked-pencari.php";
    String namaKost, alamatKost;
    String IDPencari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_pencari);
        Intent i = getIntent();
        IDPencari = i.getStringExtra("IDPencari");
        listBooked = findViewById(R.id.listBooked);
        listbooked = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(BookedPencari.this);

        StringRequest request = new StringRequest(Request.Method.POST, URL_GET_BOOKEDKOST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject kost = jsonArray.getJSONObject(i);
                                namaKost = kost.getString("namaKost");
                                alamatKost = kost.getString("alamatKost");

                                HashMap<String, String> map = new HashMap<>();
                                map.put("namaKost", namaKost);
                                map.put("alamatKost", alamatKost);

                                listbooked.add(map);

                                String[] from = {"namaKost", "alamatKost"};
                                int[] to = {R.id.txtNamaKost, R.id.txtAlamatKost};
                                ListAdapter adapter = new SimpleAdapter(BookedPencari.this, listbooked, R.layout.list_bookedpencari, from, to);
                                listBooked.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(BookedPencari.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BookedPencari.this, message, Toast.LENGTH_SHORT).show();
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

        btnKH = findViewById(R.id.btnKH);
        btnKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DashboardPencari.class);
                i.putExtra("IDPencari", IDPencari);
                startActivity(i);
                BookedPencari.this.finish();
            }
        });
    }
}