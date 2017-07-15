package com.alfian.android.laundry;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class EditPassword extends AppCompatActivity {
    SessionManager sm;
    WebServiceConnect wsc;
    private EditText pass_lama, pass_baru, pass_konfir;
    private Button simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        sm = new SessionManager(this);
        pass_lama = (EditText) findViewById(R.id.pass_lama);
        pass_baru = (EditText) findViewById(R.id.pass_baru);
        pass_konfir = (EditText) findViewById(R.id.pass_konfir);
        simpan = (Button) findViewById(R.id.simpan);

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass_lama.getText().length() > 0){
                    if (pass_baru.getText().length() > 0){
                        if (pass_konfir.getText().length() > 0){
                            wsc = new WebServiceConnect();
                            Map<String, String> param = new Hashtable<String, String>();
                            param.put("username", sm.getSesi());
                            param.put("password", pass_lama.getText().toString());
                            param.put("password_baru", pass_baru.getText().toString());
                            wsc.connectNow(Constants.BASE_API_USER + "&state=edit_password", param, new WebServiceConnect.WscCallBack() {
                                @Override
                                public void onError(String message) {
                                    Toast.makeText(getApplicationContext(), "Tidak bisa terkoneksi dengan server", Toast.LENGTH_SHORT).show();
                                    Log.d("WSC", "onError: ");
                                }

                                @Override
                                public void onResponse(String response) {
                                    String b = pass_baru.getText().toString();
                                    String k = pass_konfir.getText().toString();

                                    if (b.equals(k)){
                                        Log.d("WSC", "onResponsw: " + response);
                                        Responses r = new Gson().fromJson(response, Responses.class);
                                        if (r.getStatus().equals("success")) {
                                            ResponseDataUser rd = new Gson().fromJson(response, ResponseDataUser.class);
                                            Toast.makeText(EditPassword.this, "Sukses ganti password", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(EditPassword.this, "Password tidak cocok", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }else {
                            Toast.makeText(EditPassword.this, "Konfirmasi password masih kosong", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(EditPassword.this, "Password baru masih kosong", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(EditPassword.this, "Password lama masih kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
