package com.amzi.cnews3.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class model_wallpapers implements Parcelable {


    public static final Creator<model_wallpapers> CREATOR = new Creator<model_wallpapers>() {
        public model_wallpapers createFromParcel(Parcel in) {
            return new model_wallpapers(in);
        }

        public model_wallpapers[] newArray(int size) {
            return new model_wallpapers[size];

        }
    };
    long wallpaperId;
    String tags;
    String isTop, isDownloaded;
    String category, subCategory;
    String link,downloadlink, isPremium;

    public model_wallpapers(Parcel in) {
        this.wallpaperId = in.readLong();
        this.isTop = in.readString();
        this.isPremium = in.readString();
        this.isDownloaded = in.readString();
        this.category = in.readString();
        this.subCategory = in.readString();
        this.link = in.readString();
        this.downloadlink = in.readString();
        this.tags = in.readString();

    }

    public model_wallpapers() {

    }

    public model_wallpapers(long wallpaperId, String isTop, String isPremium, String isDownloaded, String category, String subCategory, String link,String downloadlink,String tags) {
        this.wallpaperId = wallpaperId;
        this.isTop = isTop;
        this.isPremium = isPremium;
        this.isDownloaded = "no";
        this.category = category;
        this.subCategory = subCategory;
        this.link = link;
        this.downloadlink = downloadlink;
        this.tags = tags;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public long getWallpaperId() {
        return wallpaperId;
    }

    public void setWallpaperId(long wallpaperId) {
        this.wallpaperId = wallpaperId;
    }


    public String getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(String isPremium) {
        this.isPremium = isPremium;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(String isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDownloadlink() {
        return downloadlink;
    }

    public void setDownloadlink(String downloadlink) {
        this.downloadlink = downloadlink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(wallpaperId);
        parcel.writeString(isTop);
        parcel.writeString(isPremium);
        parcel.writeString(isDownloaded);
        parcel.writeString(category);
        parcel.writeString(subCategory);
        parcel.writeString(link);
        parcel.writeString(downloadlink);
        parcel.writeString(tags);

    }
}
