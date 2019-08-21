package com.meicai.yunshan.picselected.bean;

/**
 * @auth fxl on 2019/8/21.
 */
public class PhotoBean {
    private int maxSize;
    private boolean isCropping;

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isCropping() {
        return isCropping;
    }

    public void setCropping(boolean cropping) {
        isCropping = cropping;
    }
}
