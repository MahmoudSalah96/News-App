package com.example.wolverine.newsapp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wolverine on 12/08/18.
 */

public final class Utils {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int ReadTimeOut = 10000;
    private static final int ConnectTimeOut = 15000;
    private static String AuthorName;

    public Utils() {
    }

    public static List<News> featchNewsData(String RequedtedUrl,Context mContext) {

        URL url = createUrl(RequedtedUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makehttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem Making The HTTP Request", e);
        }

        List<News> newses = extractFeature(jsonResponse,mContext);
        return newses;
    }

    private static List<News> extractFeature(String NewsJson, Context mContext) {
        if (TextUtils.isEmpty(NewsJson)) {
            return null;
        }

        List<News> newses = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(NewsJson);
            JSONObject newsesopject = jsonResponse.getJSONObject("response");
            JSONArray newsesArray = newsesopject.getJSONArray("results");

            for (int i = 0; i < newsesArray.length(); i++) {
                JSONObject currentNews = newsesArray.getJSONObject(i);

                String theTitle = currentNews.getString("webTitle");
                try{
                    JSONArray tags = currentNews.getJSONArray("tags");
                    JSONObject firstTag = tags.getJSONObject(0);
                    AuthorName = firstTag.getString("webTitle");
                }
                catch(Exception e){
                    AuthorName = mContext.getString(R.string.AuthorName);
                }
                String sectionname = currentNews.getString("sectionName");
                String type = currentNews.getString("type");
                String dateTime = currentNews.getString("webPublicationDate");
                String url = currentNews.getString("webUrl");

                News news = new News(theTitle, sectionname, AuthorName, type, dateTime, url);
                newses.add(news);
            }
        } catch (JSONException e) {
            Log.e("UTILS", "extractFeature: problem parse the News", e);
        }
        return newses;
    }

    private static URL createUrl(String stringurl) {
        URL url = null;
        try {
            url = new URL(stringurl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "createUrl:problem Building The URL .", e);
        }
        return url;
    }

    private static String makehttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream stream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(ReadTimeOut);
            urlConnection.setConnectTimeout(ConnectTimeOut);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = urlConnection.getInputStream();
                jsonResponse = readFromStream(stream);
            } else {
                Log.e(LOG_TAG, "ERROR Resposne Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (stream != null) {
                stream.close();
            }
        }
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
