package com.example.myengineeringpoint.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myengineeringpoint.R;
import com.example.myengineeringpoint.models.DataModel;
import com.example.myengineeringpoint.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.Map;

public class DataActivity extends AppCompatActivity {

    private AppCompatSpinner spinner_scheme,spinner_branch,spinner_sem;
    private String data,getDataButtonTitle;
    private AppCompatButton getDataButton;
    private ArrayList scheme_list;
    private ArrayList branch_list;
    private ArrayList sem_list;
    private ArrayAdapter schemeAdapter,branchAdapter,semAdapter;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private DataModel dataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = getIntent().getExtras().getString("title").replace(" ","");
        data = getIntent().getExtras().getString("data");
        getSupportActionBar().setTitle(title);
        dataModel = new DataModel();

        //CreateProgressDialog
        createProgressDialog();
        //CreateFireStoreInstance
        db = FirebaseFirestore.getInstance();
        //
        showProgressDialog();

        db.collection("branch_year_sem")
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


                                    dismissProgressDialog();

                                   setContentView(R.layout.activity_data);

                                   spinner_scheme = findViewById(R.id.select_scheme);
                                   spinner_branch = findViewById(R.id.select_branch);
                                   spinner_sem = findViewById(R.id.select_sem);
                                   getDataButton = findViewById(R.id.get_data_button);


                                   getDataButton.setText(dataModel.getGetDataButtonTitle());

                                   schemeAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,dataModel.getScheme_list());
                                   branchAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,dataModel.getBranch_list());
                                   semAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,dataModel.getSem_list());

                                   spinner_scheme.setAdapter(schemeAdapter);
                                   spinner_branch.setAdapter(branchAdapter);
                                   spinner_sem.setAdapter(semAdapter);


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


                                   getDataButton.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           final Intent intent = new Intent(DataActivity.this,DataDetailActivity.class);
                                           if((spinner_branch.getSelectedItemPosition()!=0 &&
                                                   spinner_scheme.getSelectedItemPosition()!=0 &&
                                                   spinner_sem.getSelectedItemPosition()!=0) ||
                                                   (spinner_sem.getVisibility()==View.GONE && spinner_scheme.getSelectedItemPosition()!=0)){


                                               android.app.AlertDialog.Builder alertDialog =
                                                       new android.app.AlertDialog.Builder(DataActivity.this);
                                               alertDialog.setCancelable(false);
                                               alertDialog.setTitle(AppConstants.SUBJECT_SELECT_DIALOG_HEADER+" "+data+" :");
                                               if(spinner_sem.getVisibility()!=View.GONE){
                                                   alertDialog.setMessage("Branch :"+dataModel.getBranch_list().get(spinner_branch.getSelectedItemPosition())
                                                           +"\n"+"Scheme :"+dataModel.getScheme_list().get(spinner_scheme.getSelectedItemPosition())
                                                           +"\n"+"Sem :"+dataModel.getSem_list().get(spinner_sem.getSelectedItemPosition()));

                                                   intent.putExtra("Branch",(String)dataModel.getBranch_list().get(spinner_branch.getSelectedItemPosition()));
                                                   intent.putExtra("Scheme",(String)dataModel.getScheme_list().get(spinner_scheme.getSelectedItemPosition()));
                                                   intent.putExtra("Sem",(String)dataModel.getSem_list().get(spinner_sem.getSelectedItemPosition()));

                                               }else{
                                                   alertDialog.setMessage("Branch :"+dataModel.getBranch_list().get(spinner_branch.getSelectedItemPosition())
                                                           +"\n"+"Scheme :"+dataModel.getScheme_list().get(spinner_scheme.getSelectedItemPosition()));

                                                   intent.putExtra("Branch",(String)dataModel.getBranch_list().get(spinner_branch.getSelectedItemPosition()));
                                                   intent.putExtra("Scheme",(String)dataModel.getScheme_list().get(spinner_scheme.getSelectedItemPosition()));
                                               }

                                               //Action on YES
                                               alertDialog.setPositiveButton(AppConstants.ALERT_DIALOG_BUTTON_YES, new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog, int which) {
                                                       //Fire FireBase Request
                                                       //For testing launch DataDetailActivity
                                                       startActivity(intent);


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


                                   //Toast.makeText(DataActivity.this,"Scheme List : "+scheme_list,Toast.LENGTH_LONG).show();
                               }
                           }else{
                               dismissProgressDialog();

                               Toast.makeText(DataActivity.this,
                                       "Error getting documents :"+task.getException(),Toast.LENGTH_SHORT).show();
                           }
                    }
                });



    }

    private void createProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data From Server");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
    }

    private void showProgressDialog(){
        progressDialog.show();
    }

    private void dismissProgressDialog(){
        progressDialog.dismiss();
    }

}
