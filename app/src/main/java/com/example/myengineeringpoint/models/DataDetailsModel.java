package com.example.myengineeringpoint.models;

public class DataDetailsModel {

    private String subName;
    private String subPapersCount;

    public DataDetailsModel(String subName,String subPapersCount){
        this.subName = subName;
        this.subPapersCount = subPapersCount;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubPapersCount() {
        return subPapersCount;
    }

    public void setSubPapersCount(String subPapersCount) {
        this.subPapersCount = subPapersCount;
    }
}
