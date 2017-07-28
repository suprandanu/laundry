package com.alfian.android.laundry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alfian.android.laundry.model.ResponseDataUser;
import com.alfian.android.laundry.model.Responses;
import com.alfian.android.laundry.volley.Constants;
import com.alfian.android.laundry.volley.SessionManager;
import com.alfian.android.laundry.volley.WebServiceConnect;
import com.google.gson.Gson;

import java.util.Hashtable;
import java.util.Map;

public class EditProfil extends AppCompatActivity {
    SessionManager sm;
    WebServiceConnect wsc;
    private EditText nama, hp, alamat;
    private Button simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        sm      = new SessionManager(this);
        nama    = (EditText)findViewById(R.id.nama);
        hp      = (EditText)findViewById(R.id.hp);
        alamat  = (EditText)findViewById(R.id.alamat);
        simpan  = (Button)findViewById(R.id.simpan);

        wsc = new WebServiceConnect();
        Map<String, String> param = new Hashtable<String, String>();
        param.put("username", sm.getSesi());
        wsc.connectNow(Constants.BASE_API_USER + "&state=get_data_user", param, new WebServiceConnect.WscCallBack() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), "Tidak bisa terkoneksi dengan server", Toast.LENGTH_SHORT).show();
                Log.d("WSC", "onError: ");
            }

            @Override
            public void onResponse(String response) {
                Log.d("WSC", "onResponse: "+response);
                Responses r = new Gson().fromJson(response, Responses.class);
                if (r.getStatus().equals("success")){
                    ResponseDataUser rd = new Gson().fromJson(response, ResponseDataUser.class);
                    nama.setText(rd.getData().get(0).getNama_plg());
                    hp.setText(rd.getData().get(0).getHp());
                    //alamat.setText(rd.getData().get(0).getAlamat());
                }else {
                    Log.d("WSC", "onResponse: gagal");
                    ResponseDataUser rd = new Gson().fromJson(response, ResponseDataUser.class);
                }
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wsc = new WebServiceConnect();
                Map<String, String> param = new Hashtable<String, String>();
                param.put("username", sm.getSesi());
                param.put("nama", nama.getText().toString());
                param.put("hp", hp.getText().toString());
                param.put("alamat", alamat.getText().toString());
                wsc.connectNow(Constants.BASE_API_USER + "&state=edit_user", param, new WebServiceConnect.WscCallBack() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(getApplicationContext(), "Tidak bisa terkoneksi dengan server", Toast.LENGTH_SHORT).show();
                        Log.d("WSC", "onError: ");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("WSC", "onResponse: "+response);
                        Responses r = new Gson().fromJson(response, Responses.class);
                        if (r.getStatus().equals("success")){
                            Toast.makeText(EditProfil.this, "Edit Profil Sukses", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(EditProfil.this, "Edit Profil Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.menu_ubah){
            nama.setEnabled(true);
            hp.setEnabled(true);
            //alamat.setEnabled(true);
            simpan.setVisibility(View.VISIBLE);

        }

        return super.onOptionsItemSelected(item);
    }
}
