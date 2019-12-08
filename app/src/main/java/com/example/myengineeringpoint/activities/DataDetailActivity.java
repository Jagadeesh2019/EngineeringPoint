package com.example.myengineeringpoint.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myengineeringpoint.R;
import com.example.myengineeringpoint.models.DataDetailsModel;
import com.example.myengineeringpoint.utils.AdvertisementUtils;
import com.example.myengineeringpoint.utils.AppKeys;
import com.example.myengineeringpoint.utils.CommonUtils;
import com.example.myengineeringpoint.utils.FireBaseStorageFileName;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class DataDetailActivity extends AppCompatActivity {

    private ListView data_details_list_view;
    private MyAdapter data_details_list_adapter;
    public ArrayList<DataDetailsModel> dataDetailsArrayList;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private CommonUtils commonUtils;
    private FirebaseStorage firebaseStorage;
    private AdvertisementUtils advertisementUtils;
    private AdView bannerAdView;
    private StorageReference mStorageRef;
    private StorageReference ref,gsRef;
    public static String remoteJsonDataString="Default";
    private String branch,scheme,sem,resourceType,fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //BannerAd is placed
        //Create an Object of AdvertisementUtils
        advertisementUtils = new AdvertisementUtils(DataDetailActivity.this);
        advertisementUtils.initializeBannerAd();

        //Create an Object of CommonUtils
        commonUtils = new CommonUtils(DataDetailActivity.this);

        String activityTitle = "";
         branch = (String)getIntent().getExtras().get(AppKeys.KEY_BRANCH);
         scheme = (String)getIntent().getExtras().get(AppKeys.KEY_SCHEME);
         sem = (String)getIntent().getExtras().get(AppKeys.KEY_SEM);
         resourceType = (String)getIntent().getExtras().get(AppKeys.KEY_RESOURCE_TYPE);


        if(sem==null){
            activityTitle = branch+"/"+scheme;
        }else {
            activityTitle = branch+"/"+scheme+"/"+sem;
        }

        getSupportActionBar().setTitle(activityTitle);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //ShowProgressDialog
        commonUtils.showProgressDialog();


        mAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Get dataDetails json file from storage

                switch (branch.toLowerCase()){
                    case AppKeys.KEY_BRANCH_CSE :
                        fileName=FireBaseStorageFileName.VTU_ENGINEERING_CSE_DATA_JSON_FILE;
                        break;
                    case AppKeys.KEY_BRANCH_ISE :
                        fileName=FireBaseStorageFileName.VTU_ENGINEERING_ISE_DATA_JSON_FILE;
                        break;
                    case AppKeys.KEY_BRANCH_ECE :
                        fileName=FireBaseStorageFileName.VTU_ENGINEERING_ECE_DATA_JSON_FILE;
                        break;
                    case AppKeys.KEY_BRANCH_MECH :
                        fileName=FireBaseStorageFileName.VTU_ENGINEERING_MECH_DATA_JSON_FILE;
                        break;
                    case AppKeys.KEY_BRANCH_CIVIL :
                        fileName=FireBaseStorageFileName.VTU_ENGINEERING_CIVIL_DATA_JSON_FILE;
                        break;
                    case AppKeys.KEY_BRANCH_EEE :
                        fileName=FireBaseStorageFileName.VTU_ENGINEERING_EEE_DATA_JSON_FILE;
                        break;
                }

                fetchAndSetDataFromServer(branch,scheme,sem,fileName);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                commonUtils.dismissProgressDialog();
            }
        });

    }


    public void fetchAndSetDataFromServer(final String branch, final String scheme, final String sem, final String filename){

        final String remoteJsonDataPath = commonUtils.getGetBranchDataRemotePath(branch,filename);
        //Log.d("URLHere",remoteJsonDataPath);
//        firebaseStorage = FirebaseStorage.getInstance();
//             gsRef = firebaseStorage.getReferenceFromUrl("gs://myengineeringpoint.appspot.com/vtu_engineering_data/cse/cse_data.json");
        mStorageRef = firebaseStorage.getInstance().getReference();
        ref = mStorageRef.child(remoteJsonDataPath);
        try{
            final File localFile = File.createTempFile(AppKeys.KEY_TEMP_FILE,AppKeys.KEY_JSON_FORMAT);
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //set JSON String here
                    JSONParser parser = new JSONParser();
                    try{
                        FileReader reader = new FileReader(localFile);
                        JSONObject actualObject = (JSONObject)parser.parse(reader);
                        //Log.d("DataHere",actualObject.toString());
                       //Call set data
                        setDataToAdapters(actualObject,branch.toLowerCase(),scheme.toLowerCase(),sem.toLowerCase().replace(" ",""));

                    }catch (Exception e){
                        e.printStackTrace();
                       commonUtils.dismissProgressDialog();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    commonUtils.dismissProgressDialog();
                }
            });
        }catch (Exception e){
            commonUtils.dismissProgressDialog();
        }
    }


    public void setDataToAdapters(JSONObject dataObject,String branch,String scheme,String sem){

        dataDetailsArrayList = new ArrayList<DataDetailsModel>();

        setContentView(R.layout.activity_data_detail);

        bannerAdView = findViewById(R.id.dataDetail_bannerAdView);
//        AdSize adSize = new AdSize(300,100);
//        bannerAdView.setAdSize(adSize);
        advertisementUtils.loadBannerAd(bannerAdView);

        data_details_list_view = findViewById(R.id.data_details_list_view);


        //RetrieveDataFromObject
        JSONObject branchData = (JSONObject)dataObject.get(branch);
        JSONObject schemeData = (JSONObject)branchData.get(scheme);
        JSONArray semArray = (JSONArray)schemeData.get(sem);

        data_details_list_adapter = new MyAdapter();
        data_details_list_view.setAdapter(data_details_list_adapter);

        for(int i=0;i<semArray.size();i++){
            JSONObject innerObject = (JSONObject)semArray.get(i);
            DataDetailsModel dataDetailsModel = new DataDetailsModel((String)innerObject.get(DataDetailsModel.DataDetailsConstants.TITLE),
                    (String)innerObject.get(DataDetailsModel.DataDetailsConstants.SUB_CODE));
            dataDetailsArrayList.add(dataDetailsModel);
            data_details_list_adapter.notifyDataSetChanged();
        }

        commonUtils.dismissProgressDialog();
    }






    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return dataDetailsArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataDetailsArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final DataDetailsModel dataDetailsModel = dataDetailsArrayList.get(position);
            View v = getLayoutInflater().inflate(R.layout.data_detail_list_item,null);
            AppCompatTextView subject_textView = v.findViewById(R.id.subject_textView);
            AppCompatTextView subjectCode_textView = v.findViewById(R.id.subjectCode_textView);
            AppCompatButton get_papers_button = v.findViewById(R.id.get_papers_button);
            subject_textView.setText(dataDetailsModel.getSubName());
            subjectCode_textView.setText("Subject Code : "+dataDetailsModel.getSubCode());

            get_papers_button.setText("Get "+resourceType);
            get_papers_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(commonUtils.getInternetStatus(DataDetailActivity.this)){
                        Intent intent = new Intent(DataDetailActivity.this,GetPapersActivity.class);
                        //send subject code along with intent
                        intent.putExtra(AppKeys.KEY_SUBJECT_CODE,dataDetailsModel.getSubCode());
                        intent.putExtra(AppKeys.KEY_RESOURCE_TYPE,resourceType);
                        startActivity(intent);

                    }else {
                        commonUtils.showEnableInternetShortToast(DataDetailActivity.this);
                    }


                }
            });
            return v;
        }
    }
}




