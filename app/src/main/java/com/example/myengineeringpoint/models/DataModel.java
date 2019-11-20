package com.example.myengineeringpoint.models;

import java.util.ArrayList;

public class DataModel {

    public static final String BRANCH_KEY="branch";
    public static final String SCHEME_KEY="scheme";
    public static final String SEM_KEY="sem";
    public static final String BUTTON_TITLE_KEY="button_title";

    private String getDataButtonTitle;
    private ArrayList scheme_list,branch_list,sem_list;

    public DataModel(){

    }

    public String getGetDataButtonTitle() {
        return getDataButtonTitle;
    }

    public void setGetDataButtonTitle(String getDataButtonTitle) {
        this.getDataButtonTitle = getDataButtonTitle;
    }

    public ArrayList getScheme_list() {
        return scheme_list;
    }

    public void setScheme_list(ArrayList scheme_list) {
        this.scheme_list = scheme_list;
    }

    public ArrayList getBranch_list() {
        return branch_list;
    }

    public void setBranch_list(ArrayList branch_list) {
        this.branch_list = branch_list;
    }

    public ArrayList getSem_list() {
        return sem_list;
    }

    public void setSem_list(ArrayList sem_list) {
        this.sem_list = sem_list;
    }
}
