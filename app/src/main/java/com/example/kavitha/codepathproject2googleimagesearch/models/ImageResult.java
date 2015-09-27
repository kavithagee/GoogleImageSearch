package com.example.kavitha.codepathproject2googleimagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kavitha on 9/22/15.
 */
public class ImageResult implements Parcelable {
    private String fullUrl;
    private String thumbUrl;
    private String title;
    private int tbWidth;
    private int tbHeight;
    private int fullWidth;
    private int fullHeight;

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

    public int getTbWidth() {
        return tbWidth;
    }

    public int getTbHeight() {
        return tbHeight;
    }

    public int getFullWidth() {
        return fullWidth;
    }

    public int getFullHeight() {
        return fullHeight;
    }

    public ImageResult(JSONObject json, float logicalDensity) {

        try{
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
            this.tbHeight = (int) Math.ceil(json.getInt("tbHeight") * logicalDensity);
            this.tbWidth = (int) Math.ceil(json.getInt("tbWidth") * logicalDensity);
            this.fullHeight = json.getInt("height");
            this.fullWidth = json.getInt("width");
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ImageResult> fromJSONArray(JSONArray jsonArray, float logicalDensity) {
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                results.add(new ImageResult(jsonArray.getJSONObject(i), logicalDensity));
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullUrl);
        dest.writeString(this.thumbUrl);
        dest.writeString(this.title);
        dest.writeInt(this.tbWidth);
        dest.writeInt(this.tbHeight);
        dest.writeInt(this.fullWidth);
        dest.writeInt(this.fullHeight);
    }

    protected ImageResult(Parcel in) {
        this.fullUrl = in.readString();
        this.thumbUrl = in.readString();
        this.title = in.readString();
        this.tbWidth = in.readInt();
        this.tbHeight = in.readInt();
        this.fullWidth = in.readInt();
        this.fullHeight = in.readInt();
    }

    public static final Creator<ImageResult> CREATOR = new Creator<ImageResult>() {
        public ImageResult createFromParcel(Parcel source) {
            return new ImageResult(source);
        }

        public ImageResult[] newArray(int size) {
            return new ImageResult[size];
        }
    };
}
