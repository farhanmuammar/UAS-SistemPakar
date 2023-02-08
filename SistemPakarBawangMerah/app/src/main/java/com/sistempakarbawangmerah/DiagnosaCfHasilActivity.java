package com.sistempakarbawangmerah;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.request.JsonObjectRequest;
import com.sistempakarrambutan.R;

import org.json.JSONException;
import org.json.JSONObject;

public class DiagnosaCfHasilActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "http://192.168.235.48/sp_bawangmerah/get_hasil_cf.php";
    private String hasil;
    private Button btn_penyakit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosa_cf_hasil);
        setTitle("Hasil Diagnosa");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hasil = extras.getString("hasil");
        }

        btn_penyakit = findViewById(R.id.btn_penyakit);

        getHasilDiagnosa();
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(DiagnosaCfHasilActivity.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void getHasilDiagnosa() {
        displayLoader();
        JSONObject request = new JSONObject();
        SessionHandler session = new SessionHandler(this);
        User user = session.getUserDetails();
        try {
            request.put("hasil", hasil);
            request.put("metode", "Certainty Factor");
            request.put("id_pengguna", user.getIdPengguna());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            if (response.getString("id_penyakit").equals("")) {
                                TextView tv_title = findViewById(R.id.tv_title);
                                tv_title.setText(response.getString("nama_penyakit"));
                                btn_penyakit.setVisibility(View.GONE);
                            } else {
                                final String id_penyakit = response.getString("id_penyakit");
                                btn_penyakit.setText(response.getString("nama_penyakit") +
                                        " (" + response.getString("nilai") + "%)");
                                btn_penyakit.setOnClickListener(v -> {
                                    Intent myIntent = new Intent(v.getContext(), PenyakitDetailActivity.class);
                                    myIntent.putExtra("id_penyakit", id_penyakit);
                                    startActivity(myIntent);
                                });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }
}