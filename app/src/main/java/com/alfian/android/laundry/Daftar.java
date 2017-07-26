package com.alfian.android.laundry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alfian.android.laundry.model.Responses;
import com.alfian.android.laundry.volley.Constants;
import com.alfian.android.laundry.volley.WebServiceConnect;
import com.google.gson.Gson;

import java.util.Hashtable;
import java.util.Map;

public class Daftar extends AppCompatActivity {
    WebServiceConnect wsc;
    EditText username, nama, hp, password;
    Button bdaftar;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        username = (EditText) findViewById(R.id.username);
        nama = (EditText) findViewById(R.id.nama);
        hp = (EditText) findViewById(R.id.hp);
        password = (EditText) findViewById(R.id.password);
        bdaftar = (Button) findViewById(R.id.bdaftar);

        bdaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().length() > 0) {
                    if (nama.getText().length() > 0) {
                        if (hp.getText().length() > 0) {
                            if (password.getText().length() > 0) {
                                pd = ProgressDialog.show(Daftar.this, "Proses Pendaftaran", "Tunggu", true);

                                wsc = new WebServiceConnect();
                                final Map<String, String> param = new Hashtable<String, String>();
                                param.put("username", username.getText().toString());
                                param.put("nama", nama.getText().toString());
                                param.put("hp", hp.getText().toString());
                                param.put("password", password.getText().toString());
                                wsc.connectNow(Constants.BASE_API_USER + "&state=daftar", param, new WebServiceConnect.WscCallBack() {
                                    @Override
                                    public void onError(String message) {
                                        Toast.makeText(getApplicationContext(), "Tidak bisa terkoneksi dengan server", Toast.LENGTH_SHORT).show();
                                        Log.d("WSC", "onError: ");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run()
                                            {
                                                pd.dismiss();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(String response) {
                                        Log.d("WSC", "onResponse: " + response);
                                        Responses r = new Gson().fromJson(response, Responses.class);
                                        if (r.getStatus().equals("success")){
                                            Toast.makeText(Daftar.this, "Pendaftaran Sukses", Toast.LENGTH_SHORT).show();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run()
                                                {
                                                    pd.dismiss();
                                                }
                                            });
                                            startActivity(new Intent(Daftar.this, Login.class));
                                            Daftar.this.finish();
                                        }else {
                                            Toast.makeText(Daftar.this, "Pendaftaraan Gagal", Toast.LENGTH_SHORT).show();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run()
                                                {
                                                    pd.dismiss();
                                                }
                                            });
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Daftar.this, "Password Masih Kosong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Daftar.this, "No. Hp Masih Kosong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Daftar.this, "Nama Masih Kosong", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(Daftar.this, "Username Masih Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void masuk(View v) {
        startActivity(new Intent(this, Login.class));
        this.finish();
    }
}
