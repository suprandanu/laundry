package com.alfian.android.laundry;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfian.android.laundry.model.ResponseDataUser;
import com.alfian.android.laundry.model.Responses;
import com.alfian.android.laundry.volley.Constants;
import com.alfian.android.laundry.volley.SessionManager;
import com.alfian.android.laundry.volley.WebServiceConnect;
import com.google.gson.Gson;

import java.util.Hashtable;
import java.util.Map;

/**
 *
 */
public class FragmentAkun extends Fragment {
    SessionManager sm;
    WebServiceConnect wsc;
    TextView nama, saldo;
    SwipeRefreshLayout swipeRefreshLayout;

    private FrameLayout fragmentContainer;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Create a new instance of the fragment
     */
    public static FragmentAkun newInstance(int index) {
        FragmentAkun fragment = new FragmentAkun();
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragment.setArguments(b);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sm = new SessionManager(getActivity());

        wsc = new WebServiceConnect();
        Map<String, String> param = new Hashtable<String, String>();
        param.put("username", sm.getSesi());
        wsc.connectNow(Constants.BASE_API_USER + "&state=get_data_user", param, new WebServiceConnect.WscCallBack() {
            @Override
            public void onError(String message) {
                nama.setText("offline");
            }

            @Override
            public void onResponse(String response) {
                Log.d("WSC", "onResponse: "+response);
                Responses r = new Gson().fromJson(response, Responses.class);
                if (r.getStatus().equals("success")){
                    ResponseDataUser rd = new Gson().fromJson(response, ResponseDataUser.class);
                    nama.setText(rd.getData().get(0).getNama_plg());
                    saldo.setText("Saldo Anda Rp " + rd.getData().get(0).getSaldo());
                }
            }
        });


        View view = inflater.inflate(R.layout.fragment_akun, container, false);
        willBeDisplayed();
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout ln = (LinearLayout)view.findViewById(R.id.ubahProfil);
        LinearLayout ln1 = (LinearLayout)view.findViewById(R.id.ubahPass);
        LinearLayout ln2 = (LinearLayout)view.findViewById(R.id.logout);
        ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubahProfil();
            }
        });
        ln1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubahPass();
            }
        });
        ln2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);

        nama = (TextView) view.findViewById(R.id.nama);
        saldo = (TextView) view.findViewById(R.id.saldo);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myUpdateOperation();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

    }

    private void ubahProfil () {
        getActivity().startActivity(new Intent(getActivity(), EditProfil.class));
    }

    private void ubahPass () {
        getActivity().startActivity(new Intent(getActivity(), EditPassword.class));
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Apakah yakin ingin keluar dari aplikasi Laundry?").setCancelable(false).setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sm.clearData();
                getActivity().finish();
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();

    }


    public void willBeDisplayed() {
        // Do what you want here, for example animate the content
        if (fragmentContainer != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            fragmentContainer.startAnimation(fadeIn);
        }
    }

    public void myUpdateOperation(){
        sm = new SessionManager(getActivity());

        wsc = new WebServiceConnect();
        Map<String, String> param = new Hashtable<String, String>();
        param.put("username", sm.getSesi());
        wsc.connectNow(Constants.BASE_API_USER + "&state=get_data_user", param, new WebServiceConnect.WscCallBack() {
            @Override
            public void onError(String message) {
                nama.setText("offline");
            }

            @Override
            public void onResponse(String response) {
                Log.d("WSC", "onResponse: "+response);
                Responses r = new Gson().fromJson(response, Responses.class);
                if (r.getStatus().equals("success")){
                    ResponseDataUser rd = new Gson().fromJson(response, ResponseDataUser.class);
                    nama.setText(rd.getData().get(0).getNama_plg());
                }
            }
        });
    }

}
