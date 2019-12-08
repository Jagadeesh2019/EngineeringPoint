package com.example.myengineeringpoint.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class QuizDataModel {

    private String id;
    private String question;
    private ArrayList<String> options;
    private String answer;
    private String answer_index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer_index() {
        return answer_index;
    }

    public void setAnswer_index(String answer_index) {
        this.answer_index = answer_index;
    }
}
