package com.watchtrading.watchtrade.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DefaultSetting {


    @SerializedName("allbrands")
    @Expose
    private List<String> allbrands = null;


    @SerializedName("stableversion")
    @Expose
    private String stableversion;
    @SerializedName("currency_symbol")
    @Expose
    private List<String> currencySymbol = null;
    @SerializedName("country_array")
    @Expose
    private List<String> countryArray = null;

    @SerializedName("lang")
    @Expose
    private Object lang;
    @SerializedName("restdata")
    @Expose
    private String restdata;



    public List<String> getAllbrands() {
        return allbrands;
    }

    public void setAllbrands(List<String> allbrands) {
        this.allbrands = allbrands;
    }







    public String getStableversion() {
        return stableversion;
    }

    public void setStableversion(String stableversion) {
        this.stableversion = stableversion;
    }

    public List<String> getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(List<String> currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public List<String> getCountryArray() {
        return countryArray;
    }

    public void setCountryArray(List<String> countryArray) {
        this.countryArray = countryArray;
    }



    public Object getLang() {
        return lang;
    }

    public void setLang(Object lang) {
        this.lang = lang;
    }

    public String getRestdata() {
        return restdata;
    }

    public void setRestdata(String restdata) {
        this.restdata = restdata;
    }
}
