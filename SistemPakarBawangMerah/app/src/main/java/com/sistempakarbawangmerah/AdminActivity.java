package com.sistempakarbawangmerah;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sistempakarrambutan.R;
import com.sistempakarbawangmerah.admin.AturanActivity;
import com.sistempakarbawangmerah.admin.GejalaActivity;
import com.sistempakarbawangmerah.admin.PenyakitActivity;

public class AdminActivity extends AppCompatActivity {

    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setTitle("Menu Utama Admin");

        session = new SessionHandler(getApplicationContext());

        Button btn_gejala = findViewById(R.id.btn_gejala);
        Button btn_penyakit = findViewById(R.id.btn_penyakit);
        Button btn_aturan = findViewById(R.id.btn_aturan);
        Button btn_logout = findViewById(R.id.btn_logout);

        btn_gejala.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, GejalaActivity.class);
            startActivity(intent);
        });

        btn_penyakit.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, PenyakitActivity.class);
            startActivity(intent);
        });

        btn_aturan.setOnClickListener(view -> {
            Intent intent = new Intent(AdminActivity.this, AturanActivity.class);
            startActivity(intent);
        });

        btn_logout.setOnClickListener(view -> new AlertDialog.Builder(AdminActivity.this)
                .setTitle("Konfirmasi")
                .setMessage("Anda yakin mau logout ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ya, Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        session.logoutUser();
                        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Tidak", null).show());

    }
}