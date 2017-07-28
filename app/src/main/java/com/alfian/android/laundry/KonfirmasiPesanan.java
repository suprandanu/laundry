package com.alfian.android.laundry;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alfian.android.laundry.model.DataBarang;
import com.alfian.android.laundry.model.DataPesanan;
import com.alfian.android.laundry.model.Responses;
import com.alfian.android.laundry.volley.Constants;
import com.alfian.android.laundry.volley.SessionManager;
import com.alfian.android.laundry.volley.WebServiceConnect;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class KonfirmasiPesanan extends AppCompatActivity {
    SessionManager sm;
    WebServiceConnect wsc;
    DatabaseHelper db;
    ArrayList<DataPesanan> mPesanan;
    ArrayList<DataBarang> mBarang;

    EditText ket_lokasi;
    TextView ubah, alamat, jarak, ongkir, kirim, log;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_pesanan);

        sm = new SessionManager(this);
        db = new DatabaseHelper(this);
        db.getWritableDatabase();

        ubah        = (TextView) findViewById(R.id.ubah);
        ket_lokasi  = (EditText) findViewById(R.id.ket_lokasi);
        alamat      = (TextView) findViewById(R.id.alamat);
        jarak       = (TextView) findViewById(R.id.jarak);
        ongkir      = (TextView) findViewById(R.id.ongkir);
        kirim       = (TextView) findViewById(R.id.kirim);

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KonfirmasiPesanan.this, AlamatPengantaran.class));
                sm.clearLatitute();
                sm.clearLongitute();
                sm.clearOngkir();
                sm.clearJarak();
            }
        });

        alamat.setText("Alamat: "+ sm.getAlamat());
        jarak.setText(" ( "+ sm.getJarak() + ")");
        ongkir.setText("Rp "+ sm.getOngkir());

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alamat.getText().length() > 0) {
                    if (ket_lokasi.getText().length() > 0) {
                        pd = ProgressDialog.show(KonfirmasiPesanan.this, "Pengiriman Pesanan", "Tunggu", true);

                        boolean simpan = db.konfirmasi_pesanan(sm.getId(), sm.getLongitute(),
                                sm.getLatitute(), sm.getAlamat(), ket_lokasi.getText().toString(),
                                sm.getOngkir());
                        Log.d("joss", "onClick: " + simpan);
                        if (simpan == true) {
                            ket_lokasi.setText("");
                            mPesanan = db.getAllDataPesanan(KonfirmasiPesanan.this, sm.getId());
                            Log.d("cek", "onCreate: " + mPesanan.toString());

                            wsc = new WebServiceConnect();
                            Map<String, String> param = new Hashtable<String, String>();
                            param.put("id_pesanan", mPesanan.get(0).getId_pesanan());
                            Log.d("joss", "onClick: " + mPesanan.get(0).getId_pesanan());
                            param.put("user_plg", mPesanan.get(0).getUser_plg());
                            param.put("tanggal", mPesanan.get(0).getTanggal());
                            param.put("longitute", mPesanan.get(0).getLongitute());
                            param.put("latitute", mPesanan.get(0).getLatitute());
                            param.put("alamat", mPesanan.get(0).getAlamat());
                            param.put("ket_lokasi", mPesanan.get(0).getKet_lokasi());
                            param.put("ongkir", mPesanan.get(0).getOngkir());
                            //param.put("total_biaya", mPesanan.get(0).getTotal_biaya());
                            param.put("status_brg", "Menunggu");
                            param.put("status_bayar", "Belum Dibayar");
                            wsc.connectNow(Constants.BASE_API_USER + "&state=pesanan", param, new WebServiceConnect.WscCallBack() {
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
                                    if (r.getStatus().equals("success")) {
                                        //cek("pengiriman sukses");
                                        //Toast.makeText(KonfirmasiPesanan.this, "Pengiriman Pesanan Sukses", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //cek("pengiriman gagal");
                                        Toast.makeText(KonfirmasiPesanan.this, "Pengiriman Pesanan Gagal", Toast.LENGTH_SHORT).show();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run()
                                            {
                                                pd.dismiss();
                                            }
                                        });
                                    }

                                    mBarang = db.getAllDataBarang(KonfirmasiPesanan.this, sm.getId());
                                    //cek("size "+String.valueOf(mBarang.size()));
                                    Log.d("cek", "onCreate: " + mBarang.toString());

                                    for (int i = 0; i < mBarang.size(); i++) {
                                        wsc = new WebServiceConnect();
                                        Log.d("WSC", "onResponse: ");
                                        Map<String, String> param = new Hashtable<String, String>();
                                        param.put("id_barang", mBarang.get(i).getId_barang()+String.valueOf(i));
                                        param.put("id_pesanan", mBarang.get(i).getId_pesanan());
                                        param.put("jenis_jasa", mBarang.get(i).getJenis_jasa());
                                        param.put("jenis_brg", mBarang.get(i).getJenis_brg());
                                        param.put("jumlah_brg", mBarang.get(i).getJumlah_brg());
                                        param.put("keterangan", mBarang.get(i).getKeterangan());


                                        //cek("posisi"+String.valueOf(i)+" "+param);
                                        final int finalI = i;
                                        wsc.connectNow(Constants.BASE_API_USER + "&state=barang", param, new WebServiceConnect.WscCallBack() {
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
                                                if (r.getStatus().equals("success")) {
                                                    //Toast.makeText(KonfirmasiPesanan.this, "Pengiriman Barang Sukses"+String.valueOf(finalI), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(KonfirmasiPesanan.this, "Pengiriman Barang Gagal"+String.valueOf(finalI), Toast.LENGTH_SHORT).show();
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

                                        if (i == mBarang.size() - 0){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run()
                                                {
                                                    pd.dismiss();
                                                }
                                            });
                                            Log.d("CANTIK", "onResponse: "+ i);
                                            Log.d("CANTIK", "onResponse: "+ mBarang.size());
                                            AlertDialog.Builder builder = new AlertDialog.Builder(KonfirmasiPesanan.this);
                                            builder.setTitle("INFO");
                                            builder.setMessage("Pesanan Berhasil Dikirim!").setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(KonfirmasiPesanan.this, Beranda.class));
                                                }
                                            }).show();
                                        }

                                    }

                                    sm.clearId();
                                    sm.clearFilter();
                                    sm.clearJarak();
                                    sm.clearOngkir();
                                    sm.clearLongitute();
                                    sm.clearLatitute();
                                    sm.clearAlamat();
                                    KonfirmasiPesanan.this.finish();
                                }
                            });
                        } else {
                            Toast.makeText(KonfirmasiPesanan.this, "Data Tidak Tersimpan", Toast.LENGTH_LONG).show();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run()
                                {
                                    pd.dismiss();
                                }
                            });
                        }
                    }else {
                        Toast.makeText(KonfirmasiPesanan.this, "Keterangan Lokasi Masih Kosong", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(KonfirmasiPesanan.this, "Alamat Masih Kosong", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    //private void cek(String s){
        //log.setText(log.getText().toString()+"\n"+s);
    //}
}
