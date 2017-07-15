package com.alfian.android.laundry.volley;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class SessionManager {
    private Activity mActivity;
    private SharedPreferences mSharedPreferences;

    public SessionManager(Activity a){
        mActivity=a;
        mSharedPreferences = mActivity.getSharedPreferences("Alfian", Context.MODE_PRIVATE);
    }

    public void saveLogin(String[] data){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("status",data[0]);
        editor.putString("sesi", data[1]);
        editor.commit();
    }

    public void saveId(String data){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("id", data);
        editor.commit();
    }

    public void saveFilter(String data){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("filter", data);
        editor.commit();
    }

    public void saveLatitute(double data){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("latitute", String.valueOf(data));
        editor.commit();
    }

    public void saveLongitute(double data){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("longitute", String.valueOf(data));
        editor.commit();
    }

    public void saveAlamat(String data){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("alamat", data);
        editor.commit();
    }

    public void saveJarak(String data){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("jarak", data);
        editor.commit();
    }

    public void saveOngkir(String data){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("ongkir", data);
        editor.commit();
    }

    public void clearData(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("status","kosong");
        editor.putString("sesi", "kosong");
        editor.commit();
    }

    public void clearId(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("id", "kosong");
        editor.commit();
    }

    public void clearFilter(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("filter", "0");
        editor.commit();
    }

    public void clearJarak(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("jarak", "kosong");
        editor.commit();
    }

    public void clearAlamat(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("jarak", "kosong");
        editor.commit();
    }

    public void clearLongitute(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("longitute", "0");
        editor.commit();
    }

    public void clearLatitute(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("latitute", "0");
        editor.commit();
    }

    public void clearOngkir(){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("ongkir", "0");
        editor.commit();
    }

    public String getStatus(){
        return mSharedPreferences.getString("status","kosong");
    }
    public String getSesi(){
        return mSharedPreferences.getString("sesi","kosong");
    }
    public String getId() {
        return mSharedPreferences.getString("id", "kosong");
    }
    public String getFilter() { return mSharedPreferences.getString("filter", "0");}
    public String getLatitute() { return mSharedPreferences.getString("latitute", "0");}
    public String getLongitute() { return mSharedPreferences.getString("longitute", "0");}
    public String getAlamat() { return mSharedPreferences.getString("alamat", "kosong");}
    public String getJarak() { return mSharedPreferences.getString("jarak", "kosong");}
    public String getOngkir() { return mSharedPreferences.getString("ongkir", "0");}

}
