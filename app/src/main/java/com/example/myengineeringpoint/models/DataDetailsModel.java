package com.example.myengineeringpoint.models;

public class DataDetailsModel {

    private String subName;
    private String subCode;

    public DataDetailsModel(String subName,String subCode){
        this.subName = subName;
        this.subCode = subCode;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }
}
