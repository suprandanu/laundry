package com.alfian.android.laundry;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alfian.android.laundry.adapter.AdapterBarang;
import com.alfian.android.laundry.model.DataBarang;
import com.alfian.android.laundry.volley.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TambahBarang extends AppCompatActivity {
    SessionManager sm;
    DatabaseHelper db;

    ListView lv;
    AdapterBarang adapter;
    ArrayList<DataBarang> mBarang;

    Calendar calendar;
    EditText back, tanggal, jumlah_brg, keterangan;
    Spinner spinner_jasa, spinner_brg;
    Button tambah;
    FloatingActionButton floatingActionButton;
    TextView lanjut, cekData, judul, edit, hapus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_barang);

        sm = new SessionManager(this);
        db = new DatabaseHelper(this);
        db.getWritableDatabase();

        lv = (ListView) findViewById(R.id.list_barang);

        calendar        = Calendar.getInstance();
        back            = (EditText)findViewById(R.id.back);
        tanggal         = (EditText)findViewById(R.id.tanggal);
        spinner_jasa    = (Spinner)findViewById(R.id.spinner_jasa);
        spinner_brg     = (Spinner)findViewById(R.id.spinner_brg);
        jumlah_brg      = (EditText)findViewById(R.id.jumlah_brg);
        keterangan      = (EditText)findViewById(R.id.keterangan);
        tambah          = (Button) findViewById(R.id.tambah);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
        lanjut          = (TextView) findViewById(R.id.lanjut);
        cekData         = (TextView) findViewById(R.id.cekData);
        judul           = (TextView) findViewById(R.id.judul);
        edit            = (TextView) findViewById(R.id.edit);
        hapus           = (TextView) findViewById(R.id.hapus);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TambahBarang.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.form_tambah_barang, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(true);
                dialogBuilder.setTitle("Tambah Barang");
                final AlertDialog alertDialog = dialogBuilder.create();

                back = (EditText) dialogView.findViewById(R.id.back);
                tanggal = (EditText) dialogView.findViewById(R.id.tanggal);
                spinner_jasa = (Spinner) dialogView.findViewById(R.id.spinner_jasa);
                spinner_brg = (Spinner) dialogView.findViewById(R.id.spinner_brg);
                jumlah_brg = (EditText) dialogView.findViewById(R.id.jumlah_brg);
                keterangan = (EditText) dialogView.findViewById(R.id.keterangan);
                tambah = (Button) dialogView.findViewById(R.id.tambah);

                //tanggal
                final SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                String tgl = date.format(calendar.getTime());
                tanggal.setText(tgl);

                spinner_jasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //spinner
                        if (spinner_jasa.getSelectedItem().toString().equals("Cuci")) {
                            ArrayList<String> data = new ArrayList<String>();
                            data.add("Boneka");
                            data.add("Bantal/Guling");
                            ArrayAdapter NoCoreAdapter = new ArrayAdapter(TambahBarang.this, android.R.layout.simple_list_item_1, data);
                            spinner_brg.setAdapter(NoCoreAdapter);
                        } else if (spinner_jasa.getSelectedItem().equals("Setrika")) {
                            ArrayList<String> data = new ArrayList<String>();
                            data.add("Pakaian");
                            data.add("Jas");
                            ArrayAdapter NoCoreAdapter = new ArrayAdapter(TambahBarang.this, android.R.layout.simple_list_item_1, data);
                            spinner_brg.setAdapter(NoCoreAdapter);
                        } else {
                            ArrayList<String> data = new ArrayList<String>();
                            data.add("Pakaian");
                            data.add("Jas");
                            data.add("Selimut");
                            data.add("Alat Sholat");
                            ArrayAdapter NoCoreAdapter = new ArrayAdapter(TambahBarang.this, android.R.layout.simple_list_item_1, data);
                            spinner_brg.setAdapter(NoCoreAdapter);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                tambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //id_pesanan
                        SimpleDateFormat dateTime1 = new SimpleDateFormat("yyyyMMdd-hhmmss");
                        String time = dateTime1.format(calendar.getTime());
                        String id_pesanan = ("pesanan-" + time);

                        //id_barang
                        String tgl = date.format(calendar.getTime());
                        //SimpleDateFormat dateTime2 = new SimpleDateFormat("yyyyMMdd-hhmmss");
                        //String brg = dateTime2.format(calendar.getTime());
                        String brg = String.valueOf(Math.random());
                        String id_barang = ("brg-" + brg);
                        if (jumlah_brg.getText().length() > 0) {
                            if (keterangan.getText().length() > 0) {
                                if (sm.getFilter().equals("0")) {
                                    boolean simpan1 = db.tambah_pesanan(id_pesanan, sm.getSesi(), tgl);
                                    if (simpan1 = true) {
                                        sm.saveId(id_pesanan);
                                        boolean simpan2 = db.tambah_barang(id_barang, sm.getId(),
                                                spinner_jasa.getSelectedItem().toString(), spinner_brg.getSelectedItem().toString(),
                                                jumlah_brg.getText().toString(), keterangan.getText().toString());
                                        Toast.makeText(TambahBarang.this, "Data Tersimpan", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(TambahBarang.this, "Data Tidak Tersimpan", Toast.LENGTH_LONG).show();
                                    }
                                    sm.saveFilter("1");
                                } else {
                                    boolean simpan2 = db.tambah_barang(id_barang, sm.getId(),
                                            spinner_jasa.getSelectedItem().toString(), spinner_brg.getSelectedItem().toString(),
                                            jumlah_brg.getText().toString(), keterangan.getText().toString());
                                    Toast.makeText(TambahBarang.this, "Data Tersimpan", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Toast.makeText(TambahBarang.this, "Keterangan masih kosong", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(TambahBarang.this, "Jumlah barang masih kosong", Toast.LENGTH_SHORT).show();
                        }
                        alertDialog.dismiss();
                        isiList();
                    }
                });
                alertDialog.show();
            }
        });

        isiList();

        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TambahBarang.this, KonfirmasiPesanan.class));
            }
        });
    }

    private void isiList() {
        mBarang = db.getAllDataBarang(TambahBarang.this, sm.getId());
        lv = (ListView) findViewById(R.id.list_barang);
        adapter = new AdapterBarang(this, mBarang, lv, adapter, db, cekData, lanjut);

        if (mBarang.size() > 0){
            lv.setAdapter(adapter);
            lanjut.setVisibility(View.VISIBLE);
            cekData.setVisibility(View.INVISIBLE);
        }else {
            cekData.setText("Tidak ada barang, silahkan tambahkan data!");
        }
    }
}
