package com.watchtrading.watchtrade.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefaultSettingModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("default_setting")
    @Expose
    private DefaultSetting defaultSetting;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DefaultSetting getDefaultSetting() {
        return defaultSetting;
    }

    public void setDefaultSetting(DefaultSetting defaultSetting) {
        this.defaultSetting = defaultSetting;
    }
}
