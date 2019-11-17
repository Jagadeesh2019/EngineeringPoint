package com.example.myengineeringpoint.utils;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class CommonUtils {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;


    public void setUpFirebaseRemoteConfig(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().
                setMinimumFetchIntervalInSeconds(0).
                build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }
}
