package com.example.myengineeringpoint.models;

import java.io.Serializable;

public class HomeScreenModel implements Serializable {

    private String syllabus_title;
    private String question_papers_title;
    private String additional_message;
    private String quiz_title;

    public HomeScreenModel(){

    }

    public HomeScreenModel(String syllabus_title, String question_papers_title, String additional_message,String quiz_title) {
        this.syllabus_title = syllabus_title;
        this.question_papers_title = question_papers_title;
        this.additional_message = additional_message;
        this.quiz_title = quiz_title;
    }

    public String getSyllabus_title() {
        return syllabus_title;
    }

    public void setSyllabus_title(String syllabus_title) {
        this.syllabus_title = syllabus_title;
    }

    public String getQuestion_papers_title() {
        return question_papers_title;
    }

    public void setQuestion_papers_title(String question_papers_title) {
        this.question_papers_title = question_papers_title;
    }

    public String getAdditional_message() {
        return additional_message;
    }

    public void setAdditional_message(String additional_message) {
        this.additional_message = additional_message;
    }

    public String getQuiz_title() {
        return quiz_title;
    }

    public void setQuiz_title(String quiz_title) {
        this.quiz_title = quiz_title;
    }
}
