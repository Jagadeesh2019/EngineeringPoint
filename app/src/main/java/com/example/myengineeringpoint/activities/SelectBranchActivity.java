package com.example.myengineeringpoint.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myengineeringpoint.R;
import com.example.myengineeringpoint.models.DataModel;
import com.example.myengineeringpoint.utils.AdvertisementUtils;
import com.example.myengineeringpoint.utils.AppConstants;
import com.example.myengineeringpoint.utils.AppKeys;
import com.example.myengineeringpoint.utils.CommonUtils;
import com.example.myengineeringpoint.utils.FireStoreCollectionNames;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;


import java.util.ArrayList;
import java.util.Map;

public class SelectBranchActivity extends AppCompatActivity {

    private AppCompatSpinner spinner_scheme,spinner_branch,spinner_sem;
    private LinearLayoutCompat linearLayoutCompat;
    private String data,getDataButtonTitle;
    private AppCompatButton getDataButton;
    private ArrayAdapter schemeAdapter,branchAdapter,semAdapter;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private DataModel dataModel;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    private CommonUtils commonUtils;
    private AdView adView;
    private AdvertisementUtils advertisementUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(R.style.ActivityThemeRed);

        //Create an Object of commonUtils
         commonUtils = new CommonUtils(SelectBranchActivity.this);


        //Create an object of AdvertisementUtils by passing context to it
        advertisementUtils = new AdvertisementUtils(SelectBranchActivity.this);

        //Initialize Ad here
        advertisementUtils.initializeBannerAd();
        //LoadInterstitialAd
        advertisementUtils.loadInterstitialAd();

        //Setup Activity Data Firebase
        firebaseRemoteConfig = commonUtils.setUpFireBaseRemoteConfig();



        String title = getIntent().getExtras().getString(AppKeys.KEY_TITLE).replace(" ","");
        data = getIntent().getExtras().getString(AppKeys.KEY_DATA);

        getSupportActionBar().setTitle(title);
        dataModel = new DataModel();

        //Create and show progressDialog

        //Do not use getApplicationContext() while calling the dialog from other components of the project :
        //instead user ActivityName.this

        commonUtils.showProgressDialog();

        //CreateFireStoreInstance
        db = FirebaseFirestore.getInstance();


        //Set Data to Spinners from remoteConfigs
        db.collection(FireStoreCollectionNames.BRANCH_YEAR_SEM)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if(task.isSuccessful()){
                               for (QueryDocumentSnapshot document : task.getResult()) {
                                   Map mDocumentData = document.getData();
                                   dataModel.setBranch_list((ArrayList)mDocumentData.get(DataModel.BRANCH_KEY));
                                   dataModel.setScheme_list((ArrayList)mDocumentData.get(DataModel.SCHEME_KEY));
                                   dataModel.setSem_list((ArrayList)mDocumentData.get(DataModel.SEM_KEY));
                                   dataModel.setGetDataButtonTitle((String)mDocumentData.get(DataModel.BUTTON_TITLE_KEY));

                                   setContentView(R.layout.activity_select_branch);

                                   linearLayoutCompat = findViewById(R.id.outer_linear_layout);
                                   spinner_scheme = findViewById(R.id.select_scheme);
                                   spinner_branch = findViewById(R.id.select_branch);
                                   spinner_sem = findViewById(R.id.select_sem);
                                   getDataButton = findViewById(R.id.get_data_button);
                                   adView = findViewById(R.id.bannerAdView);

                                   //Load BannerAd Here
                                   advertisementUtils.loadBannerAd(adView);

                                   commonUtils.dismissProgressDialog();

                                   getDataButton.setText(dataModel.getGetDataButtonTitle());

                                   schemeAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,dataModel.getScheme_list());
                                   branchAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,dataModel.getBranch_list());
                                   semAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,dataModel.getSem_list());

                                   spinner_scheme.setAdapter(schemeAdapter);
                                   spinner_branch.setAdapter(branchAdapter);
                                   spinner_sem.setAdapter(semAdapter);


                                   //Branch Spinner OnClickListener
                                   spinner_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                       @Override
                                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                           if(position==1 || position==2){
                                                  spinner_sem.setVisibility(View.GONE);
                                           }else {
                                               spinner_sem.setVisibility(View.VISIBLE);
                                           }
                                       }

                                       @Override
                                       public void onNothingSelected(AdapterView<?> parent) {

                                       }
                                   });


                                   //GetData Onclick Listener
                                   getDataButton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           final Intent intent = new Intent(SelectBranchActivity.this,DataDetailActivity.class);
                                           if((spinner_branch.getSelectedItemPosition()!=0 &&
                                                   spinner_scheme.getSelectedItemPosition()!=0 &&
                                                   spinner_sem.getSelectedItemPosition()!=0) ||
                                                   (spinner_sem.getVisibility()==View.GONE && spinner_scheme.getSelectedItemPosition()!=0)){


                                               android.app.AlertDialog.Builder alertDialog =
                                                       new android.app.AlertDialog.Builder(SelectBranchActivity.this);
                                               alertDialog.setCancelable(false);
                                               alertDialog.setTitle(AppConstants.SUBJECT_SELECT_DIALOG_HEADER+" "+data+" :");

                                               if(spinner_sem.getVisibility()!=View.GONE){
                                                   //Here P-Cycle or C-Cycle is not selected
                                                   alertDialog.setMessage("Branch :"+dataModel.getBranch_list().get(spinner_branch.getSelectedItemPosition())
                                                           +"\n"+"Scheme :"+dataModel.getScheme_list().get(spinner_scheme.getSelectedItemPosition())
                                                           +"\n"+"Sem :"+dataModel.getSem_list().get(spinner_sem.getSelectedItemPosition()));

                                                   intent.putExtra(AppKeys.KEY_BRANCH,(String)dataModel.getBranch_list().get(spinner_branch.getSelectedItemPosition()));
                                                   intent.putExtra(AppKeys.KEY_SCHEME,(String)dataModel.getScheme_list().get(spinner_scheme.getSelectedItemPosition()));
                                                   intent.putExtra(AppKeys.KEY_SEM,(String)dataModel.getSem_list().get(spinner_sem.getSelectedItemPosition()));

                                               }else{
                                                   alertDialog.setMessage("Branch :"+dataModel.getBranch_list().get(spinner_branch.getSelectedItemPosition())
                                                           +"\n"+"Scheme :"+dataModel.getScheme_list().get(spinner_scheme.getSelectedItemPosition()));

                                                   intent.putExtra(AppKeys.KEY_BRANCH,(String)dataModel.getBranch_list().get(spinner_branch.getSelectedItemPosition()));
                                                   intent.putExtra(AppKeys.KEY_SCHEME,(String)dataModel.getScheme_list().get(spinner_scheme.getSelectedItemPosition()));
                                               }

                                               //Action on YES
                                               alertDialog.setPositiveButton(AppConstants.ALERT_DIALOG_BUTTON_YES, new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       //Fire FireBase Request
                                                       //For testing launch DataDetailActivity
                                                       if(commonUtils.getInternetStatus(SelectBranchActivity.this)){
                                                           if(data.equalsIgnoreCase(AppConstants.DATA_SYLLABUS_OF)){
                                                               intent.putExtra(AppKeys.KEY_RESOURCE_TYPE,AppConstants.SYLLABUS);
                                                           }else if(data.equalsIgnoreCase(AppConstants.DATA_QUESTION_PAPERS_OF)){
                                                               intent.putExtra(AppKeys.KEY_RESOURCE_TYPE,AppConstants.PAPERS);
                                                           }
                                                           startActivity(intent);

                                                       }else {
                                                           commonUtils.showEnableInternetShortToast(SelectBranchActivity.this);
                                                       }


                                                   }
                                               });
                                               //Action on NO
                                               alertDialog.setNegativeButton(AppConstants.ALERT_DIALOG_BUTTON_NO, new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       dialog.dismiss();
                                                   }
                                               });
                                               android.app.AlertDialog alertDialog1 = alertDialog.create();
                                               alertDialog1.show();

                                           }else{
                                               Toast.makeText(getApplicationContext(),AppConstants.PLEASE_SELECT_ALL_THE_FIELDS,Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });


                                   //Toast.makeText(SelectBranchActivity.this,"Scheme List : "+scheme_list,Toast.LENGTH_LONG).show();
                               }
                           }else{
                               commonUtils.dismissProgressDialog();

                               Toast.makeText(SelectBranchActivity.this,
                                       "Error getting documents :"+task.getException(),Toast.LENGTH_SHORT).show();
                           }
                    }
                });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        advertisementUtils.showInterstitialAd();
    }
}
