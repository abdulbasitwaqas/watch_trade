package com.watchtrading.watchtrade.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatsModel {


    @SerializedName("mesid")
    @Expose
    private String mesid;

    @SerializedName("gid")
    @Expose
    private String gid;
    @SerializedName("cuser")
    @Expose
    private String cuser;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("lcount")
    @Expose
    private String lcount;

    @SerializedName("img")
    @Expose
    private String img;

    public String getMesid() {
        return mesid;
    }

    public void setMesid(String mesid) {
        this.mesid = mesid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getCuser() {
        return cuser;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLcount() {
        return lcount;
    }

    public void setLcount(String lcount) {
        this.lcount = lcount;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
