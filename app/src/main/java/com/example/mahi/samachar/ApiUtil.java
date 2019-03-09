package com.example.mahi.samachar;

import android.app.admin.DeviceAdminInfo;
import android.appwidget.AppWidgetProviderInfo;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class ApiUtil {
    private ApiUtil(){}

    public static final String BASE_API="https://newsapi.org/v2/top-headlines";
    public static final String BASE_API_TYPE="https://newsapi.org/v2/everything";
    public static final String QUERY_PARAMETER="q";
    public static final String API="apiKey";
    public static final String API_KEY="474ba80c72a24b1cb8d5849debca924f";
    public static final String TITLE="title";
    public static final String DESCRIPTION="description";
    public static final String THUMBNAIL="urlToImage";
    public static final String ARTICLES="articles";
    public static final String AUTHOR="author";
    public static final String PUBLISHED_AT="publishedAt";
    public static final String MORE_URL="url";
    public static final String COUNTRY="country";
    public static final String CATEGORY="category";
    public static final String TOTAL_RESULTS="totalResults";
    public static final String TYPE="type";

    public static String getJSONresult(Uri uri) {

        URL url = null;
        HttpURLConnection connection = null;
        Scanner scanner = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url != null) {
            try {
                connection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(connection==null){
                    return null;
                }else {
                    scanner = new Scanner(connection.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(scanner!=null) {
                scanner.useDelimiter("//A");
                if (scanner.hasNext()) {
                    return scanner.next();
                } else {
                    return null;
                }
            }else{
                return null;
            }
        }
        else {
            return null;
        }
    }

    public static Uri getQueryUri(String query){
        Uri uri = null;
        uri = Uri.parse(BASE_API).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER,query)
                .appendQueryParameter(API, API_KEY)
                .build();
        return uri;
    }

    public static Uri getQueryUri(String type,String country,String category,String query){
        Uri uri = null;
        if(type.equals("top-headlines")||type.equals("")) {
            uri = Uri.parse(BASE_API).buildUpon()
                    .appendQueryParameter(COUNTRY, country)
                    .appendQueryParameter(CATEGORY, category)
                    .appendQueryParameter(QUERY_PARAMETER, query)
                    .appendQueryParameter(API, API_KEY)
                    .build();
        }else if(type.equals("everything")){
            uri = Uri.parse(BASE_API_TYPE).buildUpon()
                    .appendQueryParameter(QUERY_PARAMETER, query)
                    .appendQueryParameter(API, API_KEY)
                    .build();

        }
        return uri;
    }

    public static ArrayList<Article> getData(String result) throws JSONException {
        if(result!=null) {
            JSONObject jsonData = new JSONObject(result);
            JSONArray articles = jsonData.getJSONArray(ARTICLES);
            ArrayList<Article> arrayList = new ArrayList<>();
            String title = "";
            String description = "";
            String thumbnail = "";
            JSONObject currentObj;
            String author = "";
            String publishedAt = "";
            String date = "", time = "", moreUrl = "";
            int totalNumberOfArticles = jsonData.getInt(TOTAL_RESULTS);
            int numberOfArticles = articles.length();
            if(numberOfArticles==0){
                MainActivity.pages=totalNumberOfArticles;
            }
            else {
                MainActivity.pages = (totalNumberOfArticles / numberOfArticles);
            }
            if(MainActivity.pages<=0){
                MainActivity.pages=1;
            }
            for (int i = 0; i < numberOfArticles; i++) {

                currentObj = articles.getJSONObject(i);
                title = currentObj.getString(TITLE);
                description = currentObj.getString(DESCRIPTION);
                thumbnail = currentObj.getString(THUMBNAIL);
                author = currentObj.getString(AUTHOR);
                publishedAt = currentObj.getString(PUBLISHED_AT);
                String at[] = publishedAt.split("T");
                moreUrl = currentObj.getString(MORE_URL);
                if (at.length == 2) {
                    date = at[0];
                    time = at[1];
                }
                Article article = new Article(
                        title,
                        description,
                        thumbnail,
                        author,
                        date,
                        time.substring(0, 8),
                        moreUrl
                );
                arrayList.add(article);
            }
            return arrayList;
        }else {
            return null;
        }
    }
}
