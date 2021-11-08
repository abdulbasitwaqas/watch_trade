package com.watchtrading.watchtrade.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingListModel {


    @SerializedName("ratingID")
    @Expose
    private String ratingID;
    @SerializedName("createuserID")
    @Expose
    private String createuserID;
    @SerializedName("ratinguserID")
    @Expose
    private String ratinguserID;
    @SerializedName("ratingpoints")
    @Expose
    private String ratingpoints;
    @SerializedName("reviewmessage")
    @Expose
    private Object reviewmessage;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }

    public String getCreateuserID() {
        return createuserID;
    }

    public void setCreateuserID(String createuserID) {
        this.createuserID = createuserID;
    }

    public String getRatinguserID() {
        return ratinguserID;
    }

    public void setRatinguserID(String ratinguserID) {
        this.ratinguserID = ratinguserID;
    }

    public String getRatingpoints() {
        return ratingpoints;
    }

    public void setRatingpoints(String ratingpoints) {
        this.ratingpoints = ratingpoints;
    }

    public Object getReviewmessage() {
        return reviewmessage;
    }

    public void setReviewmessage(Object reviewmessage) {
        this.reviewmessage = reviewmessage;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
