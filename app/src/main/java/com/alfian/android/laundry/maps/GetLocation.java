package com.alfian.android.laundry.maps;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ALFIAN on 13/06/2017.
 */

public class GetLocation {
    private GPSTracker gps;
    private double longitude, latitude;
    private Activity mActivity;


    public GetLocation(Activity a) {
    this.mActivity = a;
    }

    public void getYourLocation() {

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            gps = new GPSTracker(mActivity);

            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                Log.d("latitude",latitude + "");
                Log.d("longitude",longitude + "");
                try {
                    Geocoder geocoder = new Geocoder(mActivity.getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    //mText.setText(address+", "+country);
                    // mLa.setText(String.valueOf(latitude));
                    //mLo.setText(String.valueOf(longitude));
                    Toast.makeText(mActivity.getApplicationContext(), address+", "+country, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                gps.showSettingsAlert();
            }
        }
    }
}
