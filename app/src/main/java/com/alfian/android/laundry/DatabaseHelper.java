package com.alfian.android.laundry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.alfian.android.laundry.model.DataBarang;
import com.alfian.android.laundry.model.DataPesanan;

import java.util.ArrayList;

/**
 * Created by ALFIAN on 02/06/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String nama_db = "db_laundry";

    public static final String nama_tb1 = "pesanan";
    public static final String nama_tb2 = "barang";

    public static final String tb1_field1   = "id_pesanan";
    public static final String tb1_field2   = "user_plg";
    public static final String tb1_field3   = "user_kurir";
    public static final String tb1_field4   = "tanggal";
    public static final String tb1_field5   = "longitute";
    public static final String tb1_field6   = "latitute";
    public static final String tb1_field7   = "alamat";
    public static final String tb1_field8   = "ket_lokasi";
    public static final String tb1_field9   = "ongkir";
    public static final String tb1_field10  = "total_biaya";
    public static final String tb1_field11  = "status_brg";
    public static final String tb1_field12  = "status_bayar";

    public static final String tb2_field1   = "id_barang";
    public static final String tb2_field2   = "id_pesanan";
    public static final String tb2_field3   = "jenis_jasa";
    public static final String tb2_field4   = "jenis_brg";
    public static final String tb2_field5   = "jumlah_brg";
    public static final String tb2_field6   = "keterangan";
    public static final String tb2_field7   = "berat";
    public static final String tb2_field8   = "biaya";

    public DatabaseHelper(Context context) {
        super(context, nama_db, null, 1);

        try{
            SQLiteDatabase db = this.getWritableDatabase();
        }catch (Exception e){
            Toast.makeText(context, "Terjadi kesalahan pada pembuatan Database!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Sql1 = "CREATE TABLE "+nama_tb1+"("+tb1_field1+", "+tb1_field2+", "+
                        tb1_field3+", "+tb1_field4+", "+tb1_field5+", "+
                        tb1_field6+", "+tb1_field7+", "+tb1_field8+", "+
                        tb1_field9+", "+tb1_field10+", "+tb1_field11+", "+tb1_field12+");";

        String Sql2 = "CREATE TABLE "+nama_tb2+"("+tb2_field1+", "+tb2_field2+", "+
                        tb2_field3+", "+tb2_field4+", "+tb2_field5+", "+ tb2_field6+", "+tb2_field7+", "+tb2_field8+");";

        db.execSQL(Sql1);
        Log.d("SQL", "onCreate: "+ Sql1);
        db.execSQL(Sql2);
        Log.d("SQL", "onCreate: "+ Sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP DATABASE IF NO EXIST "+ nama_tb1);
        db.execSQL("DROP DATABASE IF NO EXIST "+ nama_tb2);
        onCreate(db);

    }


    public boolean tambah_pesanan (String id_pesanan, String user_plg, String tanggal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(tb1_field1,id_pesanan);
        cv.put(tb1_field2,user_plg);
        cv.put(tb1_field4,tanggal);


        long result = db.insert(nama_tb1, null, cv);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean tambah_barang (String id_barang, String id_pesanan, String jenis_jasa,
                                  String jenis_brg, String jumlah_brg, String keterangan){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(tb2_field1,id_barang);
        cv.put(tb2_field2,id_pesanan);
        cv.put(tb2_field3,jenis_jasa);
        cv.put(tb2_field4,jenis_brg);
        cv.put(tb2_field5,jumlah_brg);
        cv.put(tb2_field6,keterangan);

        long result = db.insert(nama_tb2, null, cv);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean konfirmasi_pesanan (String id_pesanan, String longitute, String latitute,
                                 String alamat, String ket_lokasi, String ongkir){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(tb1_field5,longitute);
        cv.put(tb1_field6,latitute);
        cv.put(tb1_field7,alamat);
        cv.put(tb1_field8,ket_lokasi);
        cv.put(tb1_field9,ongkir);

        db.update(nama_tb1, cv, "id_pesanan = "+ "'"+id_pesanan+"'", null);
        return true;
    }

    public ArrayList<DataPesanan>getAllDataPesanan(Context c, String s){
        ArrayList<DataPesanan> list=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from pesanan where id_pesanan = "+ "'"+s+"'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false){
            DataPesanan dp = new DataPesanan();
            dp.setId_pesanan(res.getString(res.getColumnIndex("id_pesanan")));
            dp.setUser_plg(res.getString(res.getColumnIndex("user_plg")));
            //dp.setUser_kurir(res.getString(res.getColumnIndex("user_kurir")));
            dp.setTanggal(res.getString(res.getColumnIndex("tanggal")));
            dp.setLongitute(res.getString(res.getColumnIndex("longitute")));
            dp.setLatitute(res.getString(res.getColumnIndex("latitute")));
            dp.setAlamat(res.getString(res.getColumnIndex("alamat")));
            dp.setKet_lokasi(res.getString(res.getColumnIndex("ket_lokasi")));
            dp.setOngkir(res.getString(res.getColumnIndex("ongkir")));
            //dp.setTotal_biaya(res.getString(res.getColumnIndex("total_biaya")));
            //dp.setStatus_brg(res.getString(res.getColumnIndex("status_brg")));
            //dp.setStatus_bayar(res.getString(res.getColumnIndex("status_bayar")));
            list.add(dp);
            res.moveToNext();
        }
        db.close();
        res.close();
        return list;
    }

    public ArrayList<DataBarang>getAllDataBarang(Context c, String s){
        ArrayList<DataBarang> list=new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from barang where id_pesanan = "+ "'"+s+"'", null);
        res.moveToFirst();

        while (res.isAfterLast() == false){
            DataBarang dbrg = new DataBarang();
            dbrg.setId_barang(res.getString(res.getColumnIndex("id_barang")));
            dbrg.setId_pesanan(res.getString(res.getColumnIndex("id_pesanan")));
            dbrg.setJenis_jasa(res.getString(res.getColumnIndex("jenis_jasa")));
            dbrg.setJenis_brg(res.getString(res.getColumnIndex("jenis_brg")));
            dbrg.setJumlah_brg(res.getString(res.getColumnIndex("jumlah_brg")));
            dbrg.setKeterangan(res.getString(res.getColumnIndex("keterangan")));
            //dbrg.setBerat(res.getString(res.getColumnIndex("berat")));
            //dbrg.setBiaya(res.getString(res.getColumnIndex("biaya")));
            list.add(dbrg);
            res.moveToNext();
        }
        db.close();
        res.close();
        return list;
    }

    public Integer hapus_barang (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(nama_tb2, tb2_field1 + " = ?", new String[] {id});
    }

    public Integer hapus_all_barang (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(nama_tb2, tb2_field2 + " = ?", new String[] {id});
    }

    public Integer hapus_all_pesanan (String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(nama_tb1, tb1_field1 + " = ?", new String[] {id});
    }

    public boolean edit_barang(String id_barang, String id_pesanan, String jenis_jasa,
                               String jenis_brg, String jumlah_brg, String keterangan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(tb2_field1, id_barang);
        cv.put(tb2_field2, id_pesanan);
        cv.put(tb2_field3, jenis_jasa);
        cv.put(tb2_field4, jenis_brg);
        cv.put(tb2_field5, jumlah_brg);
        cv.put(tb2_field6, keterangan);

        db.update(nama_tb2, cv, tb2_field1 + " = ?", new String[] {id_barang});
        return true;
    }

}
