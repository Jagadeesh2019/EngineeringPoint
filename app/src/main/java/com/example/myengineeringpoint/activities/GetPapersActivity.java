package com.example.myengineeringpoint.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myengineeringpoint.R;
import com.example.myengineeringpoint.models.GetPapersModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class GetPapersActivity extends AppCompatActivity {

    private ListView hold_papers_listView;
    public ArrayList<GetPapersModel> getPapersModelArrayList;
    public MyAdapter myAdapter;
    private ProgressDialog progressDialog;
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorageRef;
    private StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_papers);

        hold_papers_listView = findViewById(R.id.hold_papers_listView);

        getPapersModelArrayList = new ArrayList<>();
        GetPapersModel getPapersModel1 = new GetPapersModel("06CS32","VTU Electronic circuits DEC 2010 Question Paper",
                "Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel2 = new GetPapersModel("06CS32","VTU Electronic circuits DEC 2011 Question Paper",
                "Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel3 = new GetPapersModel("06CS32","VTU Electronic circuits JAN 2008 Question Paper","Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel4 = new GetPapersModel("10CS32","VTU Electronic circuits JAN 2009 Question Paper","Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel5 = new GetPapersModel("15CS32","VTU Electronic circuits JAN 2010 Question Paper","Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel6 = new GetPapersModel("17CS32","VTU Electronic circuits JULY 2008 Question Paper","Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel7 = new GetPapersModel("02CS32","VTU Electronic circuits JULY 2009 Question Paper","Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel8 = new GetPapersModel("06CS32","VTU Electronic circuits JULY 2011 Question Paper","Electronic circuits Previous Question Papers");
        GetPapersModel getPapersModel9 = new GetPapersModel("06CS32","VTU Electronic Circuits JAN 2013 Question Paper","Electronic circuits Previous Question Papers");

        getPapersModelArrayList.add(getPapersModel1);
        getPapersModelArrayList.add(getPapersModel2);
        getPapersModelArrayList.add(getPapersModel3);
        getPapersModelArrayList.add(getPapersModel4);
        getPapersModelArrayList.add(getPapersModel5);
        getPapersModelArrayList.add(getPapersModel6);
        getPapersModelArrayList.add(getPapersModel7);
        getPapersModelArrayList.add(getPapersModel8);
        getPapersModelArrayList.add(getPapersModel9);

        myAdapter = new MyAdapter();
        myAdapter.notifyDataSetChanged();
        hold_papers_listView.setAdapter(myAdapter);

    }

    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return getPapersModelArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return getPapersModelArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GetPapersModel getPapersModel = getPapersModelArrayList.get(position);
            View v = getLayoutInflater().inflate(R.layout.hold_papers_list_item,null);
            AppCompatTextView subject_code_textView = v.findViewById(R.id.papers_item_ll_banner);
            AppCompatTextView paper_title_textView = v.findViewById(R.id.papers_item_ll_paper_title);
            AppCompatTextView paper_sub_title_textView = v.findViewById(R.id.papers_item_ll_paper_sub_title);
            AppCompatButton paper_download_button = v.findViewById(R.id.papers_item_card_ll_download_button);
            subject_code_textView.setText(getPapersModel.getSubjectCode());
            paper_title_textView.setText(getPapersModel.getPaperName());
            paper_sub_title_textView.setText(getPapersModel.getPaperSubName());

            paper_download_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadQuestionPaper();
                }
            });
            return v;
        }
    }


    private void createProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Downloading your file...");
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
        mStorageRef = firebaseStorage.getInstance().getReference();
        ref = mStorageRef.child("/vtu_engineering_question_papers/civil/scheme_2002/sem3/10CV32/jan_2013_civil.pdf");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFiles(GetPapersActivity.this,"jan_2013_civil",".pdf",DIRECTORY_DOWNLOADS,url);
                dismissProgressDialog();
                Toast.makeText(GetPapersActivity.this,"Download Success",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dismissProgressDialog();
                Toast.makeText(GetPapersActivity.this,"Download Failure ",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void downloadFiles(Context context,String fileName,String fileExtension,String destinationDirectory,String url){
        DownloadManager downloadManager= (DownloadManager)context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationDirectory,fileName+fileExtension);

        downloadManager.enqueue(request);
    }

}
