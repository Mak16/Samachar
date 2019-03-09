package com.example.mahi.samachar;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {

    public String title;
    public String description;
    public String thumbnail;
    public String author;
    public String date;
    public String time;
    public String moreUrl;

    public Article(String title, String description, String thumbnail, String author, String date, String time, String moreUrl) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.author = author;
        this.date = date;
        this.time = time;
        this.moreUrl = moreUrl;
    }

    protected Article(Parcel in) {
        title = in.readString();
        description = in.readString();
        thumbnail = in.readString();
        author = in.readString();
        date = in.readString();
        time = in.readString();
        moreUrl = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(thumbnail);
        dest.writeString(author);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(moreUrl);
    }
}
