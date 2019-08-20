package com.yalantis.ucrop.bean;

import java.io.Serializable;

/**
 * @auth fxl on 2019/8/20.
 */
public class CropBean implements Serializable {
    private boolean cropping;
    private int maxSize;
    private int maxWidth;

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public boolean isCropping() {
        return cropping;
    }

    public void setCropping(boolean cropping) {
        this.cropping = cropping;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public String toString() {
        return "CropBean{" +
                "cropping=" + cropping +
                ", maxSize=" + maxSize +
                ", maxWidth=" + maxWidth +
                '}';
    }
}
