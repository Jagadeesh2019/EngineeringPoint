package com.example.myengineeringpoint.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.myengineeringpoint.activities.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.security.Key;

import androidx.annotation.NonNull;

public class CommonUtils {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private ConnectivityManager connectivityManager;
    private ProgressDialog progressDialog;
    private StorageReference mStorageRef,ref;
    private FirebaseStorage firebaseStorage;
    public String remoteJsonDataString;
    private boolean fetchStatus;
    private Context context;

    public CommonUtils(Context context){
        this.context = context;
    }


    public FirebaseRemoteConfig setUpFireBaseRemoteConfig(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().
                setMinimumFetchIntervalInSeconds(0).
                build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        if(fetchFireBaseConfigs()){

        }else {

        }
        return mFirebaseRemoteConfig;
    }


    //fetch and activate FirebaseRemoteConfigs
    public boolean fetchFireBaseConfigs(){

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if(task.isSuccessful()){
                            fetchStatus=true;
                        }else {
                            fetchStatus=false;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                fetchStatus=false;
            }
        });


        if(fetchStatus){
            return true;
        }else {
            return false;
        }

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

    public String getGetBranchDataRemotePath(final String branch, final String filename){
        return "/"+ FireBaseStorageFileName.VTU_ENGINEERING_DATA+"/"+branch.toLowerCase()+"/"+filename;
    }

    public String getVtuEngineeringQuestionPapersRemotePath
            (final String branch, final String scheme, final String sem,final String subjectCode,
             final String resourceType,final String fileName){

        return "/"+FireBaseStorageFileName.VTU_ENGINEERING_QUESTION_PAPERS+
                "/"+branch.toLowerCase()+"/"+"scheme_"+scheme+
                "/"+sem.toLowerCase()+"/"+subjectCode.toUpperCase()+
                "/"+resourceType.toLowerCase()+"/"+fileName;
    }


    private void createProgressDialog(){
        progressDialog = new ProgressDialog(this.context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading you data..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
    }

    public void showProgressDialog(){
        createProgressDialog();
        progressDialog.show();
    }

    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }


}

