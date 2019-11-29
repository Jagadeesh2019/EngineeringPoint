package com.example.myengineeringpoint.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myengineeringpoint.R;
import com.example.myengineeringpoint.models.DataDetailsModel;
import com.example.myengineeringpoint.utils.AppConstants;
import com.example.myengineeringpoint.utils.CommonUtils;

import java.util.ArrayList;

public class DataDetailActivity extends AppCompatActivity {

    private ListView data_details_list_view;
    private MyAdapter data_details_list_adapter;
    public ArrayList<DataDetailsModel> dataDetailsArrayList;
    private ProgressDialog progressDialog;
    private CommonUtils commonUtils = new CommonUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String activityTitle = "";
        String branch = (String)getIntent().getExtras().get("Branch");
        String scheme = (String)getIntent().getExtras().get("Scheme");
        String sem = (String)getIntent().getExtras().get("Sem");
        if(sem==null){
            activityTitle = branch+"/"+scheme;
        }else {
            activityTitle = branch+"/"+scheme+"/"+sem;
        }

        getSupportActionBar().setTitle(activityTitle);

        setContentView(R.layout.activity_data_detail);

        data_details_list_view = findViewById(R.id.data_details_list_view);
        data_details_list_adapter = new MyAdapter();


        //setData
        DataDetailsModel detailsModel1 = new DataDetailsModel("Subject 1","Papers :35");
        DataDetailsModel detailsModel2 = new DataDetailsModel("Subject 2","Papers :34");
        DataDetailsModel detailsModel3 = new DataDetailsModel("Subject 3","Papers :54");
        DataDetailsModel detailsModel4 = new DataDetailsModel("Subject 4","Papers :21");
        DataDetailsModel detailsModel5 = new DataDetailsModel("Subject 5","Papers :55");
        DataDetailsModel detailsModel6 = new DataDetailsModel("Subject 6","Papers :34");
        DataDetailsModel detailsModel7 = new DataDetailsModel("Subject 7","Papers :45");
        DataDetailsModel detailsModel8 = new DataDetailsModel("Subject 8","Papers :32");

        dataDetailsArrayList = new ArrayList<DataDetailsModel>();

        dataDetailsArrayList.add(detailsModel1);
        dataDetailsArrayList.add(detailsModel2);
        dataDetailsArrayList.add(detailsModel3);
        dataDetailsArrayList.add(detailsModel4);
        dataDetailsArrayList.add(detailsModel5);
        dataDetailsArrayList.add(detailsModel6);
        dataDetailsArrayList.add(detailsModel7);
        dataDetailsArrayList.add(detailsModel8);

        data_details_list_adapter.notifyDataSetChanged();

        data_details_list_view.setAdapter(data_details_list_adapter);

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
            DataDetailsModel dataDetailsModel = dataDetailsArrayList.get(position);
            View v = getLayoutInflater().inflate(R.layout.data_detail_list_item,null);
            AppCompatTextView subject_textView = v.findViewById(R.id.subject_textView);
            AppCompatTextView papers_textView = v.findViewById(R.id.papers_textView);
            AppCompatButton get_papers_button = v.findViewById(R.id.get_papers_button);
            subject_textView.setText(dataDetailsModel.getSubName());
            papers_textView.setText(dataDetailsModel.getSubPapersCount());


            get_papers_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(commonUtils.getInternetStatus(DataDetailActivity.this)){
                        startActivity(new Intent(DataDetailActivity.this,GetPapersActivity.class));
                    }else {
                        commonUtils.showEnableInternetShortToast(DataDetailActivity.this);
                    }


                }
            });
            return v;
        }
    }
}




