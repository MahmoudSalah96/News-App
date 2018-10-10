package com.example.wolverine.newsapp;

/**
 * Created by wolverine on 12/08/18.
 */

public class News {
    private String mTitle;
    private String mSectionName;
    private String mTimeDate;
    private String mUrl;
    private String mType;
    private String mAuthor;

    public News(String Title, String SectionName, String Author, String Type, String TimeDate, String Url) {
        this.mTitle = Title;
        this.mSectionName = SectionName;
        this.mTimeDate = TimeDate;
        this.mUrl = Url;
        this.mType = Type;
        this.mAuthor = Author;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public String getmTimeDate() {
        return mTimeDate;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmType() {
        return mType;
    }

    public String getmAuthor() {
        return mAuthor;
    }
}
