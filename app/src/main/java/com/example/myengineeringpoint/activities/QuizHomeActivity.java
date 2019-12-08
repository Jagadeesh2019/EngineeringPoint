package com.example.myengineeringpoint.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.myengineeringpoint.R;
import com.example.myengineeringpoint.models.QuizDataModel;
import com.example.myengineeringpoint.utils.AdvertisementUtils;
import com.example.myengineeringpoint.utils.AppKeys;
import com.example.myengineeringpoint.utils.CommonUtils;
import com.example.myengineeringpoint.utils.FireBaseStorageFileName;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

public class QuizHomeActivity extends AppCompatActivity {

    private AdView bannerAdView;
    private CommonUtils commonUtils;
    private AdvertisementUtils advertisementUtils;
    private AppCompatButton option1,option2,option3,option4;
    private LinearLayoutCompat optionsHolder,quizBoard,outerLayout;
    private AppCompatTextView question;
    private String answer,answer_index;
    private FirebaseStorage mFireBaseStorage;
    private StorageReference mStorageReference,childRef;
    private FirebaseAuth mAuth;
    private int index=0,correct=0,wrong=0;
    private JSONArray dataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commonUtils = new CommonUtils(QuizHomeActivity.this);

        advertisementUtils = new AdvertisementUtils(QuizHomeActivity.this);

        advertisementUtils.initializeBannerAd();

        //Start Progress Dialog
        commonUtils.showProgressDialog();


        setContentView(R.layout.activity_quiz_home);

        question = findViewById(R.id.questionView);
        optionsHolder = findViewById(R.id.optionsHolder);
        outerLayout = findViewById(R.id.super_linearLayout);
        quizBoard = findViewById(R.id.quiz_board_linearLayout);
        option1 = findViewById(R.id.option1View);
        option2 = findViewById(R.id.option2View);
        option3 = findViewById(R.id.option3View);
        option4 = findViewById(R.id.option4View);
        bannerAdView = findViewById(R.id.quiz_home_activity_bannerAdView);

        advertisementUtils.loadBannerAd(bannerAdView);


        //Get Data From FireBase DataBase

        //SigIn as anonymous user
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Toast.makeText(QuizHomeActivity.this,"User Id : "+mAuth.getCurrentUser().getUid(),Toast.LENGTH_SHORT).show();


                mStorageReference = mFireBaseStorage.getInstance().getReference();
                childRef = mStorageReference.child(FireBaseStorageFileName.VTU_QUIZ_DATA+"/"+
                        FireBaseStorageFileName.VTU_ENGINEERING_QUIZ_DATA_JSON_FILE);
            try{
                final File localFile = File.createTempFile(AppKeys.KEY_TEMP_FILE,AppKeys.KEY_JSON_FORMAT);
                childRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        JSONParser jsonParser = new JSONParser();
                        try{
                            FileReader reader = new FileReader(localFile);
                            JSONObject actualObject = (JSONObject)jsonParser.parse(reader);
                            //Toast.makeText(QuizHomeActivity.this,"QuizData is :"+actualObject.toString(),Toast.LENGTH_SHORT).show();

                            //Call Set Remote Data to Views
                            dataArray = (JSONArray) actualObject.get("data");
                            //Index should be fetched from FireStore
                            takeRemoteData();

                        }catch (Exception e){
                            System.out.print("JSONFileReader Exception");
                            commonUtils.dismissProgressDialog();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QuizHomeActivity.this,"GetFile Failure",Toast.LENGTH_SHORT).show();
                        commonUtils.dismissProgressDialog();
                    }
                });
            }catch (Exception e){
                System.out.print("FileReading Exception");
                commonUtils.dismissProgressDialog();
            }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuizHomeActivity.this,"Firebase Anonymous Login Failure",Toast.LENGTH_SHORT).show();
                commonUtils.dismissProgressDialog();
            }
        });




        //Start Quiz Data

        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callQuizEvaluator(option1);
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callQuizEvaluator(option2);
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callQuizEvaluator(option3);
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callQuizEvaluator(option4);
            }
        });


    }

    private void takeRemoteData(){
        Gson gson = new Gson();
        //Update index from fireBase
        QuizDataModel quizDataModel = gson.fromJson(dataArray.get(index).toString(),QuizDataModel.class);
        if(index<dataArray.size()){
            setQuestionData(quizDataModel);
        }else {
            Toast.makeText(QuizHomeActivity.this,"All are Done",Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(QuizHomeActivity.this,"Id is :"+quizDataModel.getOptions().toString(),Toast.LENGTH_SHORT).show();

    }

    private void setQuestionData(QuizDataModel questionData){
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //clearAllFields();
        disableAllButtons();
        fadeInAnimation(quizBoard);
        question.setText(questionData.getQuestion());
        option1.setText(questionData.getOptions().get(0));
        option2.setText(questionData.getOptions().get(1));
        option3.setText(questionData.getOptions().get(2));
        option4.setText(questionData.getOptions().get(3));
        answer = questionData.getAnswer();
        answer_index = questionData.getAnswer_index();
        commonUtils.dismissProgressDialog();
        enabledAllButtons();
    }

    private void callQuizEvaluator(AppCompatButton optionButton){
        disableAllButtons();
        int mAnswerIndex = Integer.parseInt(answer_index);
         if(optionButton.getText().toString().equalsIgnoreCase(answer)){
            optionButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.correct_answer_green_button_background));
            correct++;
             //Increment the question_index
             index++;
        }else {
             wrong++;
            optionButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.wrong_answer_red_button_background));
            final AppCompatButton actualAnswerButton = (AppCompatButton) optionsHolder.getChildAt(mAnswerIndex);
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        actualAnswerButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.correct_answer_green_button_background));
                    }
                },200);
            }catch (Exception e){
                actualAnswerButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.correct_answer_green_button_background));
                wrong++;
            }
             //Increment the question_index
             index++;
        }

         //Toast.makeText(QuizHomeActivity.this,"Question Index is: "+index,Toast.LENGTH_SHORT).show();
        //Call TakeRemoteData method
        if(index<dataArray.size()){
           clearAllFields();
        }else {
            outerLayout.setClickable(false);
            //Toast.makeText(QuizHomeActivity.this,"All Questions are Done",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(QuizHomeActivity.this,QuizGameOverActivity.class);
            intent.putExtra("TotalQuestions",dataArray.size());
            intent.putExtra("TotalCorrectAnswers",correct);
            intent.putExtra("TotalWrongAnswers",wrong);
            startActivity(intent);
            finish();
        }
    }

    private void clearAllFields(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                option1.setBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_white_corenerd_background));
                option2.setBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_white_corenerd_background));
                option3.setBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_white_corenerd_background));
                option4.setBackgroundDrawable(getResources().getDrawable(R.drawable.spinner_white_corenerd_background));
                takeRemoteData();
            }
        },1500);

        //enabledAllButtons();

    }

    public static void setAlphaAnimation(View v) {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha",  1f, .3f);
        fadeOut.setDuration(2000);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", .3f, 1f);
        fadeIn.setDuration(2000);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadeIn).after(fadeOut);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mAnimationSet.start();
            }
        });
        mAnimationSet.start();
    }

    private void fadeInAnimation(View v){
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, "alpha", .0f, 1f);
        fadeIn.setDuration(2000);
        fadeIn.start();

    }

    private void fadeOutAnimation(View v){
        Toast.makeText(QuizHomeActivity.this,"Control in FadeOutAnimation",Toast.LENGTH_SHORT).show();
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha",  1f, .3f);
        fadeOut.setDuration(2000);
        fadeOut.start();
    }

    private void disableAllButtons(){
        option1.setClickable(false);
        option2.setClickable(false);
        option3.setClickable(false);
        option4.setClickable(false);
    }

    private void enabledAllButtons(){
        option1.setClickable(true);
        option2.setClickable(true);
        option3.setClickable(true);
        option4.setClickable(true);
    }
}
