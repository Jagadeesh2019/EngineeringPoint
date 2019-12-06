package com.example.myengineeringpoint.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myengineeringpoint.R;
import com.example.myengineeringpoint.models.HomeScreenModel;
import com.example.myengineeringpoint.utils.AppConstants;
import com.example.myengineeringpoint.utils.AppKeys;
import com.example.myengineeringpoint.utils.CommonUtils;
import com.example.myengineeringpoint.utils.FirebaseKeys;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class HomeActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private AppCompatTextView syllabus, qpapers, addmessage;
    private String syllabus_text,qpapers_text,addmessage_text;
    private CardView courseCard,qpapersCard;
    private CommonUtils commonUtils= new CommonUtils();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        syllabus = findViewById(R.id.syllabus_title);
        qpapers = findViewById(R.id.question_paper_title);
        addmessage = findViewById(R.id.additionalMessage_textView);
        courseCard = findViewById(R.id.courses_card);
        qpapersCard = findViewById(R.id.question_papers_card);

        getSupportActionBar().setTitle(AppConstants.app_title);

         HomeScreenModel homeScreenModelHere = (HomeScreenModel) getIntent().getSerializableExtra(FirebaseKeys.HOME_SCREEN_JSON_DATA);
         syllabus.setText(homeScreenModelHere.getSyllabus_title());
        qpapers.setText(homeScreenModelHere.getQuestion_papers_title());
        addmessage.setText(homeScreenModelHere.getAdditional_message());



        courseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commonUtils.getInternetStatus(HomeActivity.this)){
                    Intent intent = new Intent(HomeActivity.this, SelectBranchActivity.class);
                    intent.putExtra(AppKeys.KEY_TITLE,syllabus.getText().toString());
                    intent.putExtra(AppKeys.KEY_DATA,AppConstants.DATA_SYLLABUS_OF);
                    startActivity(intent);
                }else {
                    commonUtils.showEnableInternetShortToast(HomeActivity.this);
                }

            }
        });

        qpapersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commonUtils.getInternetStatus(HomeActivity.this)){
                    Intent intent = new Intent(HomeActivity.this, SelectBranchActivity.class);
                    intent.putExtra(AppKeys.KEY_TITLE,qpapers.getText().toString());
                    intent.putExtra(AppKeys.KEY_DATA,AppConstants.DATA_QUESTION_PAPERS_OF);
                    startActivity(intent);
                }else {
                    commonUtils.showEnableInternetShortToast(HomeActivity.this);
                }

            }
        });



    }

}