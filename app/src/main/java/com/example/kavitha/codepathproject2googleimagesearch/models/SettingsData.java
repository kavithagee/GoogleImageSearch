package com.example.kavitha.codepathproject2googleimagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kavitha on 9/26/15.
 */
public class SettingsData implements Parcelable {
    private String imageSize;
    private String imageType;
    private String imageColor;
    private String searchWebsite;

    public SettingsData(String imageSize, String imageType, String imageColor, String searchWebsite) {
        this.imageSize = imageSize;
        this.imageType = imageType;
        this.imageColor = imageColor;
        this.searchWebsite = searchWebsite;
    }

    @Override
    public String toString() {
        return "SettingsData{" +
                "imageSize='" + imageSize + '\'' +
                ", imageType='" + imageType + '\'' +
                ", imageColor='" + imageColor + '\'' +
                ", searchWebsite='" + searchWebsite + '\'' +
                '}';
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public void setImageColor(String imageColor) {
        this.imageColor = imageColor;
    }

    public void setSearchWebsite(String searchWebsite) {
        this.searchWebsite = searchWebsite;
    }

    public String getImageSize() {
        return imageSize;
    }

    public String getImageType() {
        return imageType;
    }

    public String getImageColor() {
        return imageColor;
    }

    public String getSearchWebsite() {
        return searchWebsite;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageSize);
        dest.writeString(this.imageType);
        dest.writeString(this.imageColor);
        dest.writeString(this.searchWebsite);
    }

    protected SettingsData(Parcel in) {
        this.imageSize = in.readString();
        this.imageType = in.readString();
        this.imageColor = in.readString();
        this.searchWebsite = in.readString();
    }

    public static final Parcelable.Creator<SettingsData> CREATOR = new Parcelable.Creator<SettingsData>() {
        public SettingsData createFromParcel(Parcel source) {
            return new SettingsData(source);
        }

        public SettingsData[] newArray(int size) {
            return new SettingsData[size];
        }
    };
}
