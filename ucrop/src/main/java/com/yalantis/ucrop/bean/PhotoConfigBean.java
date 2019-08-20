package com.yalantis.ucrop.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PhotoConfigBean implements Parcelable, Serializable {

    private String file_path;

    private String saved_file_id;

    private String photo_id;

    private int width,height;

    private String photoType;

    private boolean crop;

    private boolean isLoading;

    private int maxSize;

    /**
     * 0成功返回1失败2第一次加载
     */
    private int type;

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType;
    }

    public String getSaved_file_id() {
        return saved_file_id;
    }

    public void setSaved_file_id(String saved_file_id) {
        this.saved_file_id = saved_file_id;
    }

    public boolean isCrop() {
        return crop;
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PhotoConfigBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.file_path);
        dest.writeString(this.saved_file_id);
        dest.writeString(this.photo_id);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.photoType);
        dest.writeByte(this.crop ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isLoading ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
    }

    protected PhotoConfigBean(Parcel in) {
        this.file_path = in.readString();
        this.saved_file_id = in.readString();
        this.photo_id = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.photoType = in.readString();
        this.crop = in.readByte() != 0;
        this.isLoading = in.readByte() != 0;
        this.type = in.readInt();
    }

    public static final Creator<PhotoConfigBean> CREATOR = new Creator<PhotoConfigBean>() {
        @Override
        public PhotoConfigBean createFromParcel(Parcel source) {
            return new PhotoConfigBean(source);
        }

        @Override
        public PhotoConfigBean[] newArray(int size) {
            return new PhotoConfigBean[size];
        }
    };

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
