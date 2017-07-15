package com.alfian.android.laundry.adapter;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alfian.android.laundry.R;
import com.alfian.android.laundry.model.DataBarang;
import com.alfian.android.laundry.model.DataPesanan;
import com.alfian.android.laundry.model.ResponseDataBarang;
import com.alfian.android.laundry.model.Responses;
import com.alfian.android.laundry.volley.Constants;
import com.alfian.android.laundry.volley.WebServiceConnect;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by ALFIAN on 05/06/2017.
 */

public class AdapterSekarang extends BaseAdapter {
    Activity mActivity;
    List<DataPesanan> mData;
    ArrayList<DataPesanan> arrayList;
    ListView lv;

    public AdapterSekarang(Activity a, List<DataPesanan> d){
        mActivity   = a;
        mData       = d;
        arrayList   = new ArrayList<DataPesanan>();
        arrayList.addAll(mData);
    }

    @Override
    public int getCount() {
        return mData.size();
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
        final DataPesanan pr = mData.get(i);
        if (view==null){
            LayoutInflater li = mActivity.getLayoutInflater();
            view = li.inflate(R.layout.list_sekarang, null);
        }

        CardView card_menu      = (CardView)view.findViewById(R.id.card_menu);
        TextView tgl            = (TextView)view.findViewById(R.id.tgl);
        TextView status_brg     = (TextView)view.findViewById(R.id.status_brg);
        TextView status_bayar   = (TextView)view.findViewById(R.id.status_bayar);
        TextView ongkir         = (TextView)view.findViewById(R.id.ongkir);
        TextView tot_biaya      = (TextView)view.findViewById(R.id.tot_biaya);
        tgl.setText(pr.getTanggal());
        status_brg.setText(pr.getStatus_brg());
        status_bayar.setText(pr.getStatus_bayar());
        ongkir.setText("Rp "+ pr.getOngkir());
        tot_biaya.setText("Rp "+ pr.getTotal_biaya());


        card_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
                LayoutInflater inflater = mActivity.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.detail_pesanan, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(true);
                dialogBuilder.setTitle("Detail Pesanan");
                final AlertDialog alertDialog = dialogBuilder.create();

                final ListView list = (ListView) dialogView.findViewById(R.id.list_pesanan);

                AdapterDetailPesanan adapter;
                List<DataBarang> mDataBarang;

                WebServiceConnect wsc;
                wsc = new WebServiceConnect();
                Map<String, String> param = new Hashtable<String, String>();
                param.put("id_pesanan", pr.getId_pesanan());
                Log.d("alfiannn", "onClick: " + param);
                wsc.connectNow(Constants.BASE_API_USER + "&state=get_data_barang", param, new WebServiceConnect.WscCallBack() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(mActivity.getApplicationContext(), "Tidak bisa terkoneksi dengan server", Toast.LENGTH_SHORT).show();
                        Log.d("WSC", "onError: ");
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.d("WSC", "onResponse alfian: " + response);
                        Responses r = new Gson().fromJson(response, Responses.class);
                        if (r.getStatus().equals("success")){
                            ResponseDataBarang rPsn = new Gson().fromJson(response, ResponseDataBarang.class);
                            AdapterDetailPesanan as = new AdapterDetailPesanan(mActivity, rPsn.getData());
                            list.setAdapter(as);
                        }else {
                            Toast.makeText(mActivity, "Data tidak ada", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                //adapter = new AdapterDetailPesanan(mActivity, )
                //DataBarang
                //adapter = new AdapterDetailPesanan(mActivity, mDataBarang, adapter,  cekData, lanjut);


                alertDialog.show();
            }
        });

        return view;
    }
}
