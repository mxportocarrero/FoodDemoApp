package com.example.fooddemoapp.Models;

public class RestCategory {
    private String mCategoryId;

    private String mCategoryName;

    private String mCategoryDesc;

    private String mCategoryForegroundColor;

    private String mCategoryImgUrl;

    RestCategory(){

    }

    public RestCategory(String mCategoryId, String mCategoryName, String mCategoryDesc, String mCategoryForegroundColor, String mCategoryImgUrl) {
        this.mCategoryId = mCategoryId;
        this.mCategoryName = mCategoryName;
        this.mCategoryDesc = mCategoryDesc;
        this.mCategoryForegroundColor = mCategoryForegroundColor;
        this.mCategoryImgUrl = mCategoryImgUrl;
    }

    public String getmCategoryId() {
        return mCategoryId;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public String getmCategoryDesc() {
        return mCategoryDesc;
    }

    public String getmCategoryForegroundColor() {
        return mCategoryForegroundColor;
    }

    public String getmCategoryImgUrl() {
        return mCategoryImgUrl;
    }
}
