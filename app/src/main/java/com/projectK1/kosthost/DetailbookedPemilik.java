package com.projectK1.kosthost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailbookedPemilik extends AppCompatActivity {
    TextView txtNamaKost, txtNamaPenyewa, txtKontakPenyewa, txtIDSewa;
    Button btnDeleteBooked, btnConfirmBooked;
    String IDKost, IDSewa, namaKost, namaPencari, telepon, IDPemilik, status;

    String URL_DELETE_BOOKEDKOST = "http://192.168.1.7/kosthostmobile/deletebooked-pemilik.php";
    String URL_CONFIRM_BOOKEDKOST = "http://192.168.1.7/kosthostmobile/minuskamar-pemilik.php";
    String URL_UPDATE_STATUS = "http://192.168.1.7/kosthostmobile/updatestatus-penyewaan.php";
    String URL_TAMBAH_KAMAR = "http://192.168.1.7/kosthostmobile/tambahkamar-pemilik.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailbooked_pemilik);
        txtNamaKost = findViewById(R.id.txtNamaKost);
        txtNamaPenyewa = findViewById(R.id.txtNamaPenyewa);
        txtKontakPenyewa = findViewById(R.id.txtKontakPenyewa);
        txtIDSewa = findViewById(R.id.txtIDSewa);
        btnDeleteBooked = findViewById(R.id.btnDeleteBooked);
        btnConfirmBooked = findViewById(R.id.btnConfirmBooked);
        Intent i = getIntent();
        IDPemilik = i.getStringExtra("IDPemilik");
        IDKost = i.getStringExtra("IDKost");
        IDSewa = i.getStringExtra("IDSewa");
        namaKost = i.getStringExtra("namaKost");
        namaPencari = i.getStringExtra("namaPencari");
        telepon = i.getStringExtra("telepon");
        txtNamaKost.setText(namaKost);
        txtNamaPenyewa.setText(namaPencari);
        txtKontakPenyewa.setText(telepon);
        txtIDSewa.setText(IDSewa);

        status = i.getStringExtra("status");
        if (status.equalsIgnoreCase("Booked")) {
            btnConfirmBooked.setVisibility(View.GONE);
        } else
            btnConfirmBooked.setEnabled(true);

        btnConfirmBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusKamar(IDKost);
                updateStatus(IDSewa);
            }
        });

        btnDeleteBooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahKamar(IDKost);
                deleteBooked(IDSewa);
            }
        });
    }

    private void tambahKamar(String IDKost) {
        RequestQueue queue = Volley.newRequestQueue(DetailbookedPemilik.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_TAMBAH_KAMAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        return;
                    } else {
                        Toast.makeText(DetailbookedPemilik.this, "Data penyewaan gagal dihapus", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(DetailbookedPemilik.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(DetailbookedPemilik.this, message, Toast.LENGTH_SHORT).show();

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

    private void deleteBooked(String IDSewa) {
        RequestQueue queue = Volley.newRequestQueue(DetailbookedPemilik.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_DELETE_BOOKEDKOST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        Toast.makeText(DetailbookedPemilik.this, "Data penyewaan berhasil dihapus", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), BookedPemilik.class);
                        i.putExtra("IDPemilik", IDPemilik);
                        startActivity(i);
                        DetailbookedPemilik.this.finish();

                    } else {
                        Toast.makeText(DetailbookedPemilik.this, "Data penyewaan gagal dihapus", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(DetailbookedPemilik.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(DetailbookedPemilik.this, message, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("IDSewa", IDSewa);
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }

    private void minusKamar(String IDKost) {
        RequestQueue queue = Volley.newRequestQueue(DetailbookedPemilik.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_CONFIRM_BOOKEDKOST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        return;
                    } else {
                        Toast.makeText(DetailbookedPemilik.this, "Data penyewaan gagal dikonfirmasi", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(DetailbookedPemilik.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(DetailbookedPemilik.this, message, Toast.LENGTH_SHORT).show();

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

    private void updateStatus(String IDSewa) {
        RequestQueue queue = Volley.newRequestQueue(DetailbookedPemilik.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL_UPDATE_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");
                    if (success == 1) {
                        Toast.makeText(DetailbookedPemilik.this, "Data penyewaan berhasil dikonfirmasi", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), BookedPemilik.class);
                        i.putExtra("IDPemilik", IDPemilik);
                        startActivity(i);
                        DetailbookedPemilik.this.finish();

                    } else {
                        Toast.makeText(DetailbookedPemilik.this, "Data penyewaan gagal dikonfirmasi", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Toast.makeText(DetailbookedPemilik.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(DetailbookedPemilik.this, message, Toast.LENGTH_SHORT).show();

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("IDSewa", IDSewa);
                return params;
            }
        };
        queue.getCache().clear();
        queue.add(request);
    }
}