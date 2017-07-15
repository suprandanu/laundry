package com.alfian.android.laundry.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alfian on 15/02/2017.
 */
public class Responses {
    @SerializedName("status")
    String status;

    public String getStatus() {
        return status;
    }
}
