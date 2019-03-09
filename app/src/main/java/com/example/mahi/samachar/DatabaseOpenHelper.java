package com.example.mahi.samachar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseOpenHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME="saved.db";
    public static final String TABLE_NAME="saved";
    public static final int DATABASE_VERSION=1;
    public static final String COLUMN_TITLE="title";
    public static final String COLUMN_AUTHOR="author";
    public static final String COLUMN_DATE="date";
    public static final String COLUMN_TIME="time";
    public static final String COLUMN_THUMBNAIL= "image";
    public static final String COLUMN_MORE="more";
    public static final String COLUMN_DESCRIPTION="description";

    public static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+
            COLUMN_TITLE+" TEXT UNIQUE,"+
            COLUMN_AUTHOR+" TEXT ,"+
            COLUMN_DATE+" TEXT,"+
            COLUMN_TIME+" TEXT,"+
            COLUMN_DESCRIPTION+" TEXT UNIQUE,"+
            COLUMN_THUMBNAIL+" TEXT,"+
            COLUMN_MORE+" TEXT)";


    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public static long insertData(Context context,Article article){
        SQLiteDatabase db=new DatabaseOpenHelper(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE,article.title);
        values.put(COLUMN_AUTHOR,article.author);
        values.put(COLUMN_DATE,article.date);
        values.put(COLUMN_TIME,article.time);
        values.put(COLUMN_DESCRIPTION,article.description);
        values.put(COLUMN_THUMBNAIL,article.thumbnail);
        values.put(COLUMN_MORE,article.moreUrl);
        long id=db.insert(TABLE_NAME,null,values);
        db.close();
        return id;
    }

    public static int deleteData(Context context,Article article)
    {
        SQLiteDatabase db=new DatabaseOpenHelper(context).getWritableDatabase();
        int n=db.delete(
                TABLE_NAME,
                COLUMN_TITLE+"=?"+
                        COLUMN_AUTHOR+"=?"+
                        COLUMN_DATE+"=?"+
                        COLUMN_TIME+"=?",
                new String[]{article.title,article.author,article.date,article.title}
        );
        db.close();
        return n;
    }

}
