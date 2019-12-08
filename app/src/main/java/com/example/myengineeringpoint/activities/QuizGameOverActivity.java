package com.example.myengineeringpoint.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myengineeringpoint.R;

public class QuizGameOverActivity extends AppCompatActivity {

    AppCompatTextView gameOverText, numberOfQuestionsText, numberOfCorrectAnswersText, numberOfWrongAnswersText;
    AppCompatButton replayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game_over);

        gameOverText = findViewById(R.id.game_over_textView);
        numberOfQuestionsText = findViewById(R.id.total_questions_textView);
        numberOfCorrectAnswersText = findViewById(R.id.correct_answers_textView);
        numberOfWrongAnswersText = findViewById(R.id.wrong_answers_textView);
        replayButton = findViewById(R.id.replay_buttonView);

        numberOfQuestionsText.setText("Total Questions : "+ getIntent().getExtras().get("TotalQuestions").toString());
        numberOfCorrectAnswersText.setText("Total Correct Answers : "+ getIntent().getExtras().get("TotalCorrectAnswers").toString());
        numberOfWrongAnswersText.setText("Total Wrong Answers : "+ getIntent().getExtras().get("TotalWrongAnswers").toString());

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuizGameOverActivity.this,QuizHomeActivity.class));
                finish();
            }
        });

    }
}
