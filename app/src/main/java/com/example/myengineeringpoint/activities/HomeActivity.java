package com.example.myengineeringpoint.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myengineeringpoint.R;
import com.example.myengineeringpoint.models.HomeScreenModel;
import com.example.myengineeringpoint.utils.Constants;
import com.example.myengineeringpoint.utils.FirebaseKeys;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class HomeActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private AppCompatTextView syllabus, qpapers, addmessage;
    private String syllabus_text,qpapers_text,addmessage_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        syllabus = findViewById(R.id.syllabus_title);
        qpapers = findViewById(R.id.question_paper_title);
        addmessage = findViewById(R.id.additionalMessage_textView);


         HomeScreenModel homeScreenModelHere = (HomeScreenModel) getIntent().getSerializableExtra(FirebaseKeys.HOME_SCREEN_JSON_DATA);
         syllabus.setText(homeScreenModelHere.getSyllabus_title());
        qpapers.setText(homeScreenModelHere.getQuestion_papers_title());
        addmessage.setText(homeScreenModelHere.getAdditional_message());


    }

}