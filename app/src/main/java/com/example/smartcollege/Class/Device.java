package com.example.smartcollege.Class;

public class Device {
    private String mImageURI;
    private String mName;
    private String mID;
    private String mStatus;

    public Device(String mImageURI, String mName, String mID, String mStatus) {
        this.mImageURI = mImageURI;
        this.mName = mName;
        this.mID = mID;
        this.mStatus = mStatus;
    }

    public String getmImageURI() {
        return mImageURI;
    }

    public String getmName() {
        return mName;
    }

    public String getmID() {
        return mID;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmImageURI(String mImageURI) {
        this.mImageURI = mImageURI;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
