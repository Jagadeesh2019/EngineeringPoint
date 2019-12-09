package com.example.myengineeringpoint.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myengineeringpoint.R;
import com.example.myengineeringpoint.models.HomeScreenModel;
import com.example.myengineeringpoint.utils.AppConstants;
import com.example.myengineeringpoint.utils.CommonUtils;
import com.example.myengineeringpoint.utils.FirebaseKeys;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;

public class SplashActivity extends AppCompatActivity {


    public FirebaseRemoteConfig mFirebaseRemoteConfig;
    public HomeScreenModel homeScreenModel;
    public AlertDialog.Builder alertBuilder;
    public boolean fetchStatus=true;
    public ConnectivityManager connectivityManager;
    public CommonUtils commonUtils;
    private FirebaseAuth mAuth;
    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;
    private boolean animationStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);


                mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().
                        setMinimumFetchIntervalInSeconds(1).
                        build();
                mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

                commonUtils = new CommonUtils(SplashActivity.this);


        commonUtils = new CommonUtils(SplashActivity.this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashActivityLead();
            }
        },2000);



    }



    private void splashActivityLead(){
        //Check Internet Connectivity
        if(commonUtils.getInternetStatus(SplashActivity.this)){
            //Toast.makeText(SplashActivity.this,"Internet True",Toast.LENGTH_SHORT).show();

            //Fetch and activate Remote configs

            if(fetchFirebaseConfigs()){
                //Toast.makeText(SplashActivity.this,"Fetch Firebase Configs True",Toast.LENGTH_SHORT).show();
                //Fetch the remoteconfig values and pass it to next(Home) activity
                try{
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }catch (Exception e){
                    showSomethingWentWrong();
                }
                //Toast.makeText(SplashActivity.this,home_screen_json_data,Toast.LENGTH_SHORT).show();

            }else {
                //Toast.makeText(SplashActivity.this,"Error : Fetch Firebase Configs",Toast.LENGTH_SHORT).show();
                //Error in fetch status
                //Build alert and Ask the users to try app after sometime
                showSomethingWentWrong();
            }


        }else {

            //alert dialog : NoInternetConnection

            android.app.AlertDialog.Builder alertDialog =
                    new android.app.AlertDialog.Builder(SplashActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle(AppConstants.ENABLE_INTERNET_CONNECTION_MESSAGE);
            //alertDialog.setMessage("");
            //Action on YES
            alertDialog.setPositiveButton(AppConstants.ALERT_DIALOG_BUTTON_YES, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Launch settings
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            //Action on NO
            alertDialog.setNegativeButton(AppConstants.ALERT_DIALOG_BUTTON_NO, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            android.app.AlertDialog alertDialog1 = alertDialog.create();
            alertDialog1.show();

        }
    }



    //fetch and activate FirebaseRemoteConfigs
    public boolean fetchFirebaseConfigs(){

        //Toast.makeText(SplashActivity.this,"Control in fetchFirebaseConfigs",Toast.LENGTH_SHORT).show();
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            //Toast.makeText(SplashActivity.this,"True : inner fetchFirebaseConfigs",Toast.LENGTH_SHORT).show();
                            //Log.d("Hello","True : inner fetchFirebaseConfigs");
                            fetchStatus=true;

                        } else {
                            //Toast.makeText(SplashActivity.this,"False : inner fetchFirebaseConfigs",Toast.LENGTH_SHORT).show();
                            fetchStatus=false;
                        }

                    }
                });

            if(fetchStatus){
                return true;
            }else {
                return false;
            }
    }



    public void showSomethingWentWrong(){
        android.app.AlertDialog.Builder firebaseConfigFethErrorDialog =
                new android.app.AlertDialog.Builder(SplashActivity.this);
        firebaseConfigFethErrorDialog.setCancelable(false);
        firebaseConfigFethErrorDialog.setTitle(AppConstants.SOMETHING_WENT_WRONG_MESSAGE);
        firebaseConfigFethErrorDialog.setPositiveButton(AppConstants.ALERT_DIALOG_BUTTON_OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Kill the app
                finish();
            }
        });
        //firebaseConfigFethErrorDialog.create().show();
    }




}
