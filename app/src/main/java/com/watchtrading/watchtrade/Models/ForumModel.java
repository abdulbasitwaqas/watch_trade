package com.watchtrading.watchtrade.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForumModel {
    @SerializedName("postID")
    @Expose
    String postID;
    @SerializedName("createuserID")
    @Expose
    String createuserID;

    @SerializedName("paperwatch")
    @Expose
    String paperwatch;


    @SerializedName("enterprice")
    @Expose
    String enterprice;

    @SerializedName("casesize")
    @Expose
    String casesize;

    @SerializedName("watchbox")
    @Expose
    String watchbox;


    @SerializedName("postype")
    @Expose
    String postype;

    @SerializedName("postmake")
    @Expose
    String postmake;

    @SerializedName("postyear")
    @Expose
    String postyear;


    @SerializedName("postcountry")
    @Expose
    String postcountry;

    @SerializedName("postaddress")
    @Expose
    String postaddress;

    @SerializedName("postmodel")
    @Expose
    String postmodel;

    @SerializedName("poststatus")
    @Expose
    String poststatus;

    @SerializedName("postcreated")
    @Expose
    String postcreated;


    @SerializedName("postedupdated")
    @Expose
    String postedupdated;

    String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getCreateuserID() {
        return createuserID;
    }

    public void setCreateuserID(String createuserID) {
        this.createuserID = createuserID;
    }

    public String getPaperwatch() {
        return paperwatch;
    }

    public void setPaperwatch(String paperwatch) {
        this.paperwatch = paperwatch;
    }

    public String getEnterprice() {
        return enterprice;
    }

    public void setEnterprice(String enterprice) {
        this.enterprice = enterprice;
    }

    public String getCasesize() {
        return casesize;
    }

    public void setCasesize(String casesize) {
        this.casesize = casesize;
    }

    public String getWatchbox() {
        return watchbox;
    }

    public void setWatchbox(String watchbox) {
        this.watchbox = watchbox;
    }

    public String getPostype() {
        return postype;
    }

    public void setPostype(String postype) {
        this.postype = postype;
    }

    public String getPostmake() {
        return postmake;
    }

    public void setPostmake(String postmake) {
        this.postmake = postmake;
    }

    public String getPostyear() {
        return postyear;
    }

    public void setPostyear(String postyear) {
        this.postyear = postyear;
    }

    public String getPostcountry() {
        return postcountry;
    }

    public void setPostcountry(String postcountry) {
        this.postcountry = postcountry;
    }

    public String getPostaddress() {
        return postaddress;
    }

    public void setPostaddress(String postaddress) {
        this.postaddress = postaddress;
    }

    public String getPostmodel() {
        return postmodel;
    }

    public void setPostmodel(String postmodel) {
        this.postmodel = postmodel;
    }

    public String getPoststatus() {
        return poststatus;
    }

    public void setPoststatus(String poststatus) {
        this.poststatus = poststatus;
    }

    public String getPostcreated() {
        return postcreated;
    }

    public void setPostcreated(String postcreated) {
        this.postcreated = postcreated;
    }

    public String getPostedupdated() {
        return postedupdated;
    }

    public void setPostedupdated(String postedupdated) {
        this.postedupdated = postedupdated;
    }
}
