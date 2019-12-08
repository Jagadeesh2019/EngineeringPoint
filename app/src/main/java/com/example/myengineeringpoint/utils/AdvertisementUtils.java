package com.example.myengineeringpoint.utils;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AdvertisementUtils {
    private Context context;
    private InterstitialAd mInterstitialAd;

    public AdvertisementUtils(Context context){
        this.context = context;
    }


    public void initializeBannerAd(){
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //Initialization success
            }
        });
    }


    public void loadBannerAd(AdView viewId){
        AdView bannerAdView = viewId;
        AdRequest bannerAdRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(bannerAdRequest);
    }

    public void loadInterstitialAd(){
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest interstitialAdRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(interstitialAdRequest);
    }

    public void showInterstitialAd(){
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else {
            Toast.makeText(context,"Interstitial Ad is not loaded",Toast.LENGTH_SHORT).show();
        }
    }



}
