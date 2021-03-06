package com.pedromoreirareisgmail.pchat.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.pedromoreirareisgmail.pchat.R;

import java.util.Objects;

public class Internet {

    public static Boolean temInternet(Context context){

        boolean temNet;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable()){

            temNet = true;

        }else{

            Toast.makeText(context,context.getString(R.string.toast_sem_internet), Toast.LENGTH_SHORT).show();
            temNet = false;
        }

        return temNet;
    }
}