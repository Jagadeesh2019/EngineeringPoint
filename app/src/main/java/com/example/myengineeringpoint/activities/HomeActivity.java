package com.example.myengineeringpoint.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myengineeringpoint.R;
import com.example.myengineeringpoint.models.HomeScreenModel;
import com.example.myengineeringpoint.utils.AdvertisementUtils;
import com.example.myengineeringpoint.utils.AppConstants;
import com.example.myengineeringpoint.utils.AppKeys;
import com.example.myengineeringpoint.utils.CommonUtils;
import com.example.myengineeringpoint.utils.FirebaseKeys;
import com.google.android.gms.ads.AdView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;

public class HomeActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private AppCompatTextView syllabus, qpapers, addmessage,quiz;
    private String syllabus_text,qpapers_text,addmessage_text,quiz_text;
    private CardView courseCard,qpapersCard,quizCard;
    private CommonUtils commonUtils;
    private AdView bannerAdView;
    private AdvertisementUtils advertisementUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Create an Object of CommonUtils Class
        commonUtils= new CommonUtils(HomeActivity.this);
        //Create an Object of AdvertisementUtils
        advertisementUtils = new AdvertisementUtils(HomeActivity.this);


        quiz = findViewById(R.id.quiz_title);
        syllabus = findViewById(R.id.syllabus_title);
        qpapers = findViewById(R.id.question_paper_title);
        addmessage = findViewById(R.id.additionalMessage_textView);

        quizCard = findViewById(R.id.quiz_card);
        courseCard = findViewById(R.id.courses_card);
        qpapersCard = findViewById(R.id.question_papers_card);
        bannerAdView = findViewById(R.id.home_activity_bannerAdView);

        //load and show banner ad
        advertisementUtils.initializeBannerAd();
        advertisementUtils.loadBannerAd(bannerAdView);

        try {
            getSupportActionBar().setTitle(AppConstants.app_title);
        }catch (Exception e){
            Toast.makeText(HomeActivity.this,"Couldn't load ActivityTitle",Toast.LENGTH_SHORT).show();
        }

        mFirebaseRemoteConfig = commonUtils.setUpFireBaseRemoteConfig();
        Gson gson = new Gson();
        HomeScreenModel homeScreenModel = gson.fromJson(mFirebaseRemoteConfig.getString(FirebaseKeys.HOME_SCREEN_JSON_DATA),HomeScreenModel.class);

        quiz.setText(homeScreenModel.getQuiz_title());
        syllabus.setText(homeScreenModel.getSyllabus_title());
        qpapers.setText(homeScreenModel.getQuestion_papers_title());
        addmessage.setText(homeScreenModel.getAdditional_message());



        quizCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commonUtils.getInternetStatus(HomeActivity.this)){
                    Intent intent = new Intent(HomeActivity.this,QuizHomeActivity.class);
                    //intent.putExtra(AppKeys.KEY_TITLE,quiz.getText().toString());
                    startActivity(intent);
                }else {
                    commonUtils.showEnableInternetShortToast(HomeActivity.this);
                }
            }
        });


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