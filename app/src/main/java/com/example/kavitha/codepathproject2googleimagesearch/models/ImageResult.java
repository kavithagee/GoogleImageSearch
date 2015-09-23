package com.example.kavitha.codepathproject2googleimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kavitha on 9/22/15.
 */
public class ImageResult {
    private String fullUrl;
    private String thumbUrl;
    private String title;

//    GET THUMBNAIL and IMAGE ht and wd

    public String getFullUrl() {
        return fullUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getTitle() {
        return title;
    }

    public ImageResult(JSONObject json) {
        try{
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray jsonArray) {
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                results.add(new ImageResult(jsonArray.getJSONObject(i)));
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
