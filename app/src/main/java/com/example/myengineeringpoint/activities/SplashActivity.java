package com.example.myengineeringpoint.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Window;

import com.example.myengineeringpoint.R;
import com.example.myengineeringpoint.models.HomeScreenModel;
import com.example.myengineeringpoint.utils.AppConstants;
import com.example.myengineeringpoint.utils.FirebaseKeys;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {


    public FirebaseRemoteConfig mFirebaseRemoteConfig;
    public HomeScreenModel homeScreenModel;
    public AlertDialog.Builder alertBuilder;
    public boolean fetchStatus=true;
    public ConnectivityManager connectivityManager;



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


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashActivityLead();
            }
        },2000);




    }


    private void splashActivityLead(){
        //Check Internet Connectivity
        if(getInternetStatus()){
            //Toast.makeText(SplashActivity.this,"Internet True",Toast.LENGTH_SHORT).show();

            //Fetch and activate Remote configs

            if(fetchFirebaseConfigs()){
                //Toast.makeText(SplashActivity.this,"Fetch Firebase Configs True",Toast.LENGTH_SHORT).show();
                //Fetch the remoteconfig values and pass it to next(Home) activity
                try{
                    String home_screen_json_data = mFirebaseRemoteConfig.getString(FirebaseKeys.HOME_SCREEN_JSON_DATA);
                    JSONParser parser = new JSONParser();
                    JSONObject home_Screen_data_object = (JSONObject)parser.parse(home_screen_json_data);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    HomeScreenModel homeScreenModel = new HomeScreenModel();
                    homeScreenModel.setAdditional_message((String)home_Screen_data_object.get(AppConstants.ADDITIONAL_MESSAGE));
                    homeScreenModel.setQuestion_papers_title((String)home_Screen_data_object.get(AppConstants.QUESTION_PAPERS_TITLE));
                    homeScreenModel.setSyllabus_title((String)home_Screen_data_object.get(AppConstants.SYLLABUS_TITLE));
                    intent.putExtra(FirebaseKeys.HOME_SCREEN_JSON_DATA,homeScreenModel);
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
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            //Action Neutral Button : CANCEL

//                    alertDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
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



    public boolean getInternetStatus(){
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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

}
