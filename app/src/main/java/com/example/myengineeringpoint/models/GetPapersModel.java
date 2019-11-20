package com.example.myengineeringpoint.models;

public class GetPapersModel {

    private String subjectCode;
    private String paperName;
    private String paperSubName;

    public GetPapersModel(){

    }

    public GetPapersModel(String subjectCode, String paperName, String paperSubName) {
        this.subjectCode = subjectCode;
        this.paperName = paperName;
        this.paperSubName = paperSubName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getPaperSubName() {
        return paperSubName;
    }

    public void setPaperSubName(String paperSubName) {
        this.paperSubName = paperSubName;
    }
}
