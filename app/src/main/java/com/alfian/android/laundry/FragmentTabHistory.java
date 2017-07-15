package com.alfian.android.laundry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.alfian.android.laundry.adapter.AdapterSekarang;
import com.alfian.android.laundry.model.DataPesanan;
import com.alfian.android.laundry.model.ResponseDataPesanan;
import com.alfian.android.laundry.model.Responses;
import com.alfian.android.laundry.volley.Constants;
import com.alfian.android.laundry.volley.SessionManager;
import com.alfian.android.laundry.volley.WebServiceConnect;
import com.google.gson.Gson;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by neokree on 16/12/14.
 */
public class FragmentTabHistory extends Fragment {
    ListView lv;
    AdapterSekarang adapter;
    List<DataPesanan> list;
    SessionManager sm;
    WebServiceConnect wsc;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sm = new SessionManager(getActivity());
        lv = (ListView)view.findViewById(R.id.list_history);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);


        wsc = new WebServiceConnect();
        Map<String, String> param = new Hashtable<String, String>();
        param.put("user_plg", sm.getSesi());
        wsc.connectNow(Constants.BASE_API_USER + "&state=get_data_pesanan", param, new WebServiceConnect.WscCallBack() {
            @Override
            public void onError(String message) {
                Toast.makeText(getActivity().getApplicationContext(), "Tidak bisa terkoneksi dengan server", Toast.LENGTH_SHORT).show();
                Log.d("WSC", "onError: ");
            }

            @Override
            public void onResponse(String response) {
                Log.d("WSC", "onResponse: " + response);
                Responses r = new Gson().fromJson(response, Responses.class);
                if (r.getStatus().equals("success")){
                    ResponseDataPesanan rPsn = new Gson().fromJson(response, ResponseDataPesanan.class);
                    if (rPsn.getData().get(0).getStatus_bayar().equals("S")){
                        AdapterSekarang as = new AdapterSekarang(getActivity(), rPsn.getData());
                        lv.setAdapter(as);
                    }

                }else {
                    Toast.makeText(getActivity(), "Data tidak ada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myUpdateOperation();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    public void myUpdateOperation(){
        wsc = new WebServiceConnect();
        Map<String, String> param = new Hashtable<String, String>();
        param.put("user_plg", sm.getSesi());
        wsc.connectNow(Constants.BASE_API_USER + "&state=get_data_pesanan", param, new WebServiceConnect.WscCallBack() {
            @Override
            public void onError(String message) {
                Toast.makeText(getActivity().getApplicationContext(), "Tidak bisa terkoneksi dengan server", Toast.LENGTH_SHORT).show();
                Log.d("WSC", "onError: ");
            }

            @Override
            public void onResponse(String response) {
                Log.d("WSC", "onResponse: " + response);
                Responses r = new Gson().fromJson(response, Responses.class);
                if (r.getStatus().equals("success")){
                    ResponseDataPesanan rPsn = new Gson().fromJson(response, ResponseDataPesanan.class);
                    if (rPsn.getData().get(0).getStatus_bayar().equals("Selesai") && rPsn.getData().get(0).getStatus_bayar().equals("Dibayar")){
                        AdapterSekarang as = new AdapterSekarang(getActivity(), rPsn.getData());
                        lv.setAdapter(as);
                    }

                }else {
                    Toast.makeText(getActivity(), "Data tidak ada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
