package com.example.myengineeringpoint.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class CommonUtils {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private ConnectivityManager connectivityManager;


    public void setUpFireBaseRemoteConfig(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().
                setMinimumFetchIntervalInSeconds(0).
                build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }

    public boolean getInternetStatus(Context activityName){
        connectivityManager = (ConnectivityManager)activityName.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager!=null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null && networkInfo.isConnected()){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public void showEnableInternetShortToast(Context context){
        Toast.makeText(context,AppConstants.ENABLE_INTERNET_CONNECTION_MESSAGE,Toast.LENGTH_SHORT).show();
    }


    public void showEnableInternetPopup(){

    }
}

