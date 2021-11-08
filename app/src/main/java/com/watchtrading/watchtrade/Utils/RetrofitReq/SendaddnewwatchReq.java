package com.watchtrading.watchtrade.Utils.RetrofitReq;

import android.util.Log;

import java.io.File;

import okhttp3.RequestBody;

public class SendaddnewwatchReq {

//    private MultipartBody.Part message;
    private String senduserID;
    private String getuserID;
    private RequestBody file_send;
    private String createuserID;
    private String paperwatch;
    private String enterprice;
    private String currency;
    private String casesize;
    private String watchbox;
    private String postype;
    private String postmake;
    private String postyear;
    private String postcountry;
    private String postaddress;
    private String postmodel;
//    private File my_file ;

//    private String file_name;





    public SendaddnewwatchReq(/*MultipartBody.Part message, */String createuserID,
                                                              String paperwatch,
                                                              String enterprice,
                                                              String currency,
                                                              String casesize,
                                                              String watchbox,
                                                              String postype,
                                                              String postmake,
                                                              String postyear,
                                                              String postcountry,
                                                              String postaddress,
                                                              String postmodel,
                                                              RequestBody file_send) {

//        this.message = message;
        this.createuserID=createuserID;
        this.paperwatch=paperwatch;
        this.enterprice=enterprice;
        this.currency=currency;
        this.casesize=casesize;
        this.watchbox=watchbox;
        this.postype=postype;
        this.postmake=postmake;
        this.postyear=postyear;
        this.postcountry=postcountry;
        this.postaddress=postaddress;
        this.postmodel=postmodel;
        this.file_send=file_send;
//        this.file_name = file_name;
        Log.d("information",createuserID+" "+paperwatch+" "+enterprice+" "+" "+currency+" "+casesize+" "+watchbox+" "+postype+" "+postmake+" "+postyear+" "+postcountry+" "+postaddress+" "+postmodel);

    }
}
