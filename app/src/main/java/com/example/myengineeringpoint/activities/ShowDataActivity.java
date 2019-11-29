package com.example.myengineeringpoint.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myengineeringpoint.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;


public class ShowDataActivity extends AppCompatActivity {

    private AppCompatImageView view_image_holder;
    private PDFView pdfView;
    private AppCompatTextView view_title_holder;
    private ProgressDialog progressDialog;
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorageRef;
    private StorageReference ref,gsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        view_title_holder = findViewById(R.id.view_title_holder);
        view_image_holder = findViewById(R.id.view_image_holder);
        pdfView = (PDFView) findViewById(R.id.pdfView);


        downloadQuestionPaper();

    }


    private void createProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading your file...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
    }

    private void showProgressDialog(){
        createProgressDialog();
        progressDialog.show();
    }

    private void dismissProgressDialog(){
        progressDialog.dismiss();
    }

    private void downloadQuestionPaper(){
        showProgressDialog();
        //Create FireStorage reference
        firebaseStorage = FirebaseStorage.getInstance();
        mStorageRef = firebaseStorage.getInstance().getReference();
        //gsRef = firebaseStorage.getReferenceFromUrl("gs://myengineeringpoint.appspot.com/vtu_engineering_question_papers/civil/scheme_2002/sem3/10CV32/june_2012_civil.pdf");
        //gs://myengineeringpoint.appspot.com/vtu_engineering_question_papers/civil/scheme_2002/sem3/10CV32
        ref = mStorageRef.child("/vtu_engineering_question_papers/civil/scheme_2002/sem3/10CV32/jan_2013_civil.pdf");
        try{
            final File localFile = File.createTempFile("qpapers","pdf");
            ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    pdfView.fromFile(localFile).load();
                    dismissProgressDialog();
               //Toast.makeText(ShowDataActivity.this,"Loading Success",Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Display our image to notify the user
                    dismissProgressDialog();
                    Toast.makeText(ShowDataActivity.this,"Loading Failure",Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            System.out.print("CreateTempFile Exception :"+e);
            dismissProgressDialog();
            Toast.makeText(ShowDataActivity.this,"Loading Failure",Toast.LENGTH_SHORT).show();
        }


    }

}
