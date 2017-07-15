package com.alfian.android.laundry.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alfian.android.laundry.R;
import com.alfian.android.laundry.model.DataBarang;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALFIAN on 10/06/2017.
 */

public class AdapterDetailPesanan extends BaseAdapter{
    Activity mActivity;
    List<DataBarang> mData;
    ArrayList<DataBarang> arrayList;

    public AdapterDetailPesanan(Activity a, List<DataBarang> d){
        mActivity       = a;
        mData           = d;
        arrayList       = new ArrayList<DataBarang>();
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
        final DataBarang pr = mData.get(i);
        if (view==null){
            LayoutInflater li = mActivity.getLayoutInflater();
            view = li.inflate(R.layout.list_pesanan, null);
        }

        TextView jenis_brg  = (TextView)view.findViewById(R.id.jenis_brg);
        TextView jumlah_brg = (TextView)view.findViewById(R.id.jumlah_brg);
        TextView jenis_jasa = (TextView)view.findViewById(R.id.jenis_jasa);
        TextView ket_brg    = (TextView)view.findViewById(R.id.ket_brg);
        TextView berat      = (TextView)view.findViewById(R.id.berat);
        TextView biaya      = (TextView)view.findViewById(R.id.biaya);
        jenis_brg.setText(pr.getJenis_brg());
        jumlah_brg.setText(pr.getJumlah_brg());
        jenis_jasa.setText(pr.getJenis_jasa());
        ket_brg.setText(pr.getKeterangan());
        berat.setText(pr.getBerat()+" Kg");
        biaya.setText("Rp "+ pr.getBiaya());


        return view;
    }
}
