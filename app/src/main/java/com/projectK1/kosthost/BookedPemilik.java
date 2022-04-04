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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookedPemilik extends AppCompatActivity {
    private ListView listBooked;
    private ImageButton btnKH;
    ArrayList<HashMap<String, String>> listbooked;
    String URL_GET_BOOKEDKOST = "http://192.168.1.7/kosthostmobile/getbooked-pemilik.php";
    String IDKost, IDSewa, namaKost, namaPencari, telepon, status;
    String IDPemilik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_pemilik);
        Intent i = getIntent();
        IDPemilik = i.getStringExtra("IDPemilik");
        listBooked = findViewById(R.id.listBooked);
        listbooked = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(BookedPemilik.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_GET_BOOKEDKOST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject kost = jsonArray.getJSONObject(i);
                                IDKost = kost.getString("IDKost");
                                IDSewa = kost.getString("IDSewa");
                                namaKost = kost.getString("namaKost");
                                namaPencari = kost.getString("namaPencari");
                                telepon = kost.getString("telepon");
                                status = kost.getString("status");

                                HashMap<String, String> map = new HashMap<>();
                                map.put("IDKost", IDKost);
                                map.put("IDSewa", IDSewa);
                                map.put("namaKost", namaKost);
                                map.put("namaPencari", namaPencari);
                                map.put("telepon", telepon);
                                map.put("status", status);

                                listbooked.add(map);

                                String[] from = {"IDKost", "IDSewa", "namaKost", "namaPencari", "telepon", "status"};
                                int[] to = {R.id.txtIDKost, R.id.txtIDSewa, R.id.txtNamaKost, R.id.txtNamaPencari, R.id.txtTelepon, R.id.txtStatus};
                                ListAdapter adapter = new SimpleAdapter(BookedPemilik.this, listbooked, R.layout.list_bookedpemilik, from, to);
                                listBooked.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            Toast.makeText(BookedPemilik.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BookedPemilik.this, message, Toast.LENGTH_SHORT).show();
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
        listBooked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String get_idkost = ((TextView) view.findViewById(R.id.txtIDKost)).getText().toString();
                String get_idsewa = ((TextView) view.findViewById(R.id.txtIDSewa)).getText().toString();
                String get_namakost = ((TextView) view.findViewById(R.id.txtNamaKost)).getText().toString();
                String get_namapencari = ((TextView) view.findViewById(R.id.txtNamaPencari)).getText().toString();
                String get_telepon = ((TextView) view.findViewById(R.id.txtTelepon)).getText().toString();
                String get_status = ((TextView) view.findViewById(R.id.txtStatus)).getText().toString();

                Intent i = new Intent(getApplicationContext(), DetailbookedPemilik.class);
                i.putExtra("IDKost", get_idkost);
                i.putExtra("IDSewa", get_idsewa);
                i.putExtra("namaKost", get_namakost);
                i.putExtra("namaPencari", get_namapencari);
                i.putExtra("telepon", get_telepon);
                i.putExtra("IDPemilik", IDPemilik);
                i.putExtra("status", get_status);
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
                BookedPemilik.this.finish();
            }
        });
    }
}