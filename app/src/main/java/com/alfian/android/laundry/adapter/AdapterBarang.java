package com.alfian.android.laundry.adapter;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alfian.android.laundry.DatabaseHelper;
import com.alfian.android.laundry.R;
import com.alfian.android.laundry.model.DataBarang;
import com.alfian.android.laundry.volley.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ALFIAN on 07/06/2017.
 */

public class AdapterBarang extends BaseAdapter {
    Activity mActivity;
    ArrayList<DataBarang> arrayListBrg;
    DatabaseHelper db;
    ListView listView;
    AdapterBarang adapterBarang;
    TextView cekData, lanjut;
    Calendar calendar;

    public AdapterBarang(Activity a, ArrayList<DataBarang> mBarang, ListView lv, AdapterBarang adapter, DatabaseHelper dbase, TextView cek, TextView tombol) {
        mActivity = a;
        listView = lv;
        adapterBarang = adapter;
        arrayListBrg = mBarang;
        db = dbase;
        cekData = cek;
        lanjut = tombol;
    }

    @Override
    public int getCount() {
        return arrayListBrg.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final DataBarang brg = arrayListBrg.get(i);
        if (view == null) {
            LayoutInflater li = mActivity.getLayoutInflater();
            view = li.inflate(R.layout.list_barang, null);
        }

        TextView jenis_jasa = (TextView) view.findViewById(R.id.jenis_jasa);
        TextView jenis_brg  = (TextView) view.findViewById(R.id.jenis_brg);
        TextView jumlah_brg = (TextView) view.findViewById(R.id.jumlah_brg);
        TextView keterangan = (TextView) view.findViewById(R.id.keterangan);
        jenis_jasa.setText(brg.getJenis_jasa());
        jenis_brg.setText(brg.getJenis_brg());
        jumlah_brg.setText(brg.getJumlah_brg());
        keterangan.setText(brg.getKeterangan());

        CardView card_menu = (CardView) view.findViewById(R.id.card_menu);

        card_menu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
                LayoutInflater inflater = mActivity.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_pilihan, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(true);
                final AlertDialog alertDialog = dialogBuilder.create();

                TextView judul = (TextView) dialogView.findViewById(R.id.judul);
                TextView edit = (TextView) dialogView.findViewById(R.id.edit);
                TextView hapus = (TextView) dialogView.findViewById(R.id.hapus);

                judul.setText(brg.getJenis_brg());
                alertDialog.show();

                db = new DatabaseHelper(mActivity);
                db.getWritableDatabase();

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
                        LayoutInflater inflater = mActivity.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.form_tambah_barang, null);
                        dialogBuilder.setView(dialogView);
                        dialogBuilder.setCancelable(true);
                        dialogBuilder.setTitle("Edit Barang");
                        final AlertDialog alertDialog1 = dialogBuilder.create();

                        //tanggal
                        calendar = Calendar.getInstance();
                        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                        final String tgl = date.format(calendar.getTime());

                        EditText back = (EditText) dialogView.findViewById(R.id.back);
                        EditText tanggal = (EditText) dialogView.findViewById(R.id.tanggal);
                        final Spinner spinner_jasa = (Spinner) dialogView.findViewById(R.id.spinner_jasa);
                        final Spinner spinner_brg = (Spinner) dialogView.findViewById(R.id.spinner_brg);
                        final EditText jumlah_brg = (EditText) dialogView.findViewById(R.id.jumlah_brg);
                        final EditText keterangan = (EditText) dialogView.findViewById(R.id.keterangan);
                        Button edit = (Button) dialogView.findViewById(R.id.tambah);
                        edit.setText("EDIT");

                        tanggal.setText(tgl);
                        if (brg.getJenis_jasa().equals("Cuci")) {
                            ArrayList<String> data = new ArrayList<String>();
                            data.add("Cuci");
                            data.add("Setrika");
                            data.add("Cuci + Setrika");
                            ArrayAdapter NoCoreAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, data);
                            spinner_jasa.setAdapter(NoCoreAdapter);
                        } else if (brg.getJenis_jasa().equals("Setrika")) {
                            ArrayList<String> data = new ArrayList<String>();
                            data.add("Setrika");
                            data.add("Cuci");
                            data.add("Cuci + Setrika");
                            ArrayAdapter NoCoreAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, data);
                            spinner_jasa.setAdapter(NoCoreAdapter);
                        } else if (brg.getJenis_jasa().equals("Cuci + Setrika")) {
                            ArrayList<String> data = new ArrayList<String>();
                            data.add("Cuci + Setrika");
                            data.add("Cuci");
                            data.add("Setrika");
                            ArrayAdapter NoCoreAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, data);
                            spinner_jasa.setAdapter(NoCoreAdapter);
                        }

                        spinner_jasa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (spinner_jasa.getSelectedItem().toString().equals("Cuci")) {
                                    if (brg.getJenis_brg().equals("Boneka")) {
                                        ArrayList<String> data = new ArrayList<String>();
                                        data.add("Boneka");
                                        data.add("Bantal/Guling");
                                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, data);
                                        spinner_brg.setAdapter(NoCoreAdapter);
                                    } else if (brg.getJenis_brg().equals("Bantal/Guling")) {
                                        ArrayList<String> data = new ArrayList<String>();
                                        data.add("Bantal/Guling");
                                        data.add("Boneka");
                                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, data);
                                        spinner_brg.setAdapter(NoCoreAdapter);
                                    }
                                } else if (spinner_jasa.getSelectedItem().equals("Setrika")) {
                                    if (brg.getJenis_brg().equals("Pakaian")) {
                                        ArrayList<String> data = new ArrayList<String>();
                                        data.add("Pakaian");
                                        data.add("Jas");
                                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, data);
                                        spinner_brg.setAdapter(NoCoreAdapter);
                                    } else if (brg.getJenis_brg().equals("Jas")) {
                                        ArrayList<String> data = new ArrayList<String>();
                                        data.add("Jas");
                                        data.add("Pakaian");
                                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, data);
                                        spinner_brg.setAdapter(NoCoreAdapter);
                                    }
                                } else {
                                    if (brg.getJenis_brg().equals("Pakaian")) {
                                        ArrayList<String> data = new ArrayList<String>();
                                        data.add("Pakaian");
                                        data.add("Jas");
                                        data.add("Selimut");
                                        data.add("Alat Sholat");
                                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, data);
                                        spinner_brg.setAdapter(NoCoreAdapter);
                                    } else if (brg.getJenis_brg().equals("Jas")) {
                                        ArrayList<String> data = new ArrayList<String>();
                                        data.add("Jas");
                                        data.add("Pakaian");
                                        data.add("Selimut");
                                        data.add("Alat Sholat");
                                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, data);
                                        spinner_brg.setAdapter(NoCoreAdapter);
                                    } else if (brg.getJenis_brg().equals("Selimut")) {
                                        ArrayList<String> data = new ArrayList<String>();
                                        data.add("Selimut");
                                        data.add("Pakaian");
                                        data.add("Jas");
                                        data.add("Alat Sholat");
                                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, data);
                                        spinner_brg.setAdapter(NoCoreAdapter);
                                    } else if (brg.getJenis_brg().equals("Alat Sholat")) {
                                        ArrayList<String> data = new ArrayList<String>();
                                        data.add("Alat Sholat");
                                        data.add("Pakaian");
                                        data.add("Jas");
                                        data.add("Selimut");
                                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_1, data);
                                        spinner_brg.setAdapter(NoCoreAdapter);
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        jumlah_brg.setText(brg.getJumlah_brg());
                        keterangan.setText(brg.getKeterangan());
                        alertDialog.dismiss();

                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SessionManager sm;
                                sm = new SessionManager(mActivity);
                                String id_barang = brg.getId_barang();
                                String id_pesanan = brg.getId_pesanan();

                                boolean update = db.edit_barang(id_barang, id_pesanan,
                                        spinner_jasa.getSelectedItem().toString(), spinner_brg.getSelectedItem().toString(),
                                        jumlah_brg.getText().toString(), keterangan.getText().toString());
                                Toast.makeText(mActivity, "Edit Sukses", Toast.LENGTH_LONG).show();

                                arrayListBrg = db.getAllDataBarang(mActivity, sm.getId());
                                adapterBarang = new AdapterBarang(mActivity, arrayListBrg, listView, adapterBarang, db, cekData, lanjut);
                                listView.setAdapter(adapterBarang);
                                alertDialog1.dismiss();
                            }
                        });

                        alertDialog1.show();

                    }
                });

                hapus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SessionManager sm;
                        sm = new SessionManager(mActivity);

                        Integer hapus = db.hapus_barang(brg.getId_barang());
                        if (hapus > 0) {
                            Toast.makeText(mActivity, "Berhasil hapus", Toast.LENGTH_SHORT).show();
                            arrayListBrg = db.getAllDataBarang(mActivity, sm.getId());
                            adapterBarang = new AdapterBarang(mActivity, arrayListBrg, listView, adapterBarang, db, cekData, lanjut);
                            listView.setAdapter(adapterBarang);
                            alertDialog.dismiss();

                            if (arrayListBrg.size() > 0) {
                                listView.setAdapter(adapterBarang);
                                cekData.setVisibility(View.INVISIBLE);
                                lanjut.setVisibility(View.VISIBLE);
                            } else {
                                cekData.setVisibility(View.VISIBLE);
                                cekData.setText("Tidak ada barang, silahkan tambahkan data!");
                                lanjut.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            Toast.makeText(mActivity, "Gagal hapus", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                return true;
            }
        });

        return view;
    }

}
