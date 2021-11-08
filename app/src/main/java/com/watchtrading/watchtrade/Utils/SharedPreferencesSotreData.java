package com.watchtrading.watchtrade.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.watchtrading.watchtrade.Constants.APIContract;


public class SharedPreferencesSotreData {
    private static final SharedPreferencesSotreData instance = new SharedPreferencesSotreData();
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private double lat, lng;

    public static synchronized SharedPreferencesSotreData getInstance() {
        return instance;
    }


    public void setContext(Context context) {
        this.sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferencesSotreData(Context context) {
        this.sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferencesSotreData() {

    }

    public boolean setUserLanguage(String language) {
        spEditor = sp.edit();
        spEditor.putString("userLanguage", language);
        spEditor.commit();
        return true;
    }

    public String getUserLanguage() {
        return sp.getString("userLanguage", ""+ APIContract.ENGLISH);
    }



    public void setOTP(String otp){
        spEditor = sp.edit();
        spEditor.putString("otp", otp);
        spEditor.apply();
    }

    public String getOTP(){
        return sp.getString("otp", null);

    }


    public void setEmail(String language) {
        spEditor = sp.edit();
        spEditor.putString("userEmail", language);
        spEditor.apply();
    }

    public String getEmail() {
        return sp.getString("userEmail", "");
    }

    public void setUserPassword(String userPassword) {
        spEditor = sp.edit();
        spEditor.putString("userPassword", userPassword);
        spEditor.apply();
    }

    public String getUserPassword() {
        return sp.getString("userPassword", "");
    }



    public void setIsPro(String is_pro_member) {
        spEditor = sp.edit();
        spEditor.putString("is_pro_member", is_pro_member);
        spEditor.apply();
    }

    public String getIsPro() {
        return sp.getString("is_pro_member", "0");
    }

    public void setCreateProfileDate(String created) {
        spEditor = sp.edit();
        spEditor.putString("created", created);
        spEditor.apply();
    }

    public String getCreateProfileDate() {
        return sp.getString("created", "");
    }

    public void setID(String userID) {
        spEditor = sp.edit();
        spEditor.putString("userID", userID);
        spEditor.apply();
    }

    public String getID() {
        return sp.getString("userID", "");
    }


    public void setUserName(String userName) {
        spEditor = sp.edit();
        spEditor.putString("userName", userName);
        spEditor.apply();
    }

    public String getUserName() {
        return sp.getString("userName", "");
    }


    public void setToken(String token) {
        spEditor = sp.edit();
        spEditor.putString("token", token);
        spEditor.apply();
    }

    public String getToken() {
        return sp.getString("token", "");
    }



    public void setFCMToken(String fcmToken) {
        spEditor = sp.edit();
        spEditor.putString("fcmToken", fcmToken);
        spEditor.apply();
    }

    public String getFCMToken() {
        return sp.getString("fcmToken", "");
    }




    public void setValue(String pic) {
        spEditor = sp.edit();
        spEditor.putString("pic", pic);
        spEditor.apply();
    }
    public String getValue() {
        return sp.getString("pic", "");
    }





    public void setLocation(String location) {
        spEditor = sp.edit();
        spEditor.putString("location", location);
        spEditor.apply();
    }
    public String getLocation() {
        return sp.getString("location", "");
    }


    public void setCountry(String country) {
        spEditor = sp.edit();
        spEditor.putString("country", country);
        spEditor.apply();
    }
    public String getCountry() {
        return sp.getString("country", "");
    }

    public void setSocialLinks(String socialLinks) {
        spEditor = sp.edit();
        spEditor.putString("socialLinks", socialLinks);
        spEditor.apply();
    }
    public String getSocialLinks() {
        return sp.getString("socialLinks", "");
    }


    public void setUserPhone(String phone) {
        spEditor = sp.edit();
        spEditor.putString("phone", phone);
        spEditor.apply();
    }
    public String getUserPhone() {
        return sp.getString("phone", "");
    }
    public void setCompanyName(String CompanyName) {
        spEditor = sp.edit();
        spEditor.putString("companyName", CompanyName);
        spEditor.apply();
    }
    public String getCompanyName() {
        return sp.getString("companyName", "");
    }


    public void setBusinessName(String BusinessName) {
        spEditor = sp.edit();
        spEditor.putString("businessName", BusinessName);
        spEditor.apply();
    }
    public String getBusinessName() {
        return sp.getString("businessName", "");
    }



    public void setDateOfBirth(String dateOfBirth) {
        spEditor = sp.edit();
        spEditor.putString("dateOfBirth", dateOfBirth);
        spEditor.apply();
    }
    public String getDateOfBirth() {
        return sp.getString("dateOfBirth", "");
    }



    public void setVatNumber(String vatNumber) {
        spEditor = sp.edit();
        spEditor.putString("vatNumber", vatNumber);
        spEditor.apply();
    }
    public String getVatNumber() {
        return sp.getString("vatNumber", "");
    }




}
