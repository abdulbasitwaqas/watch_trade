package com.watchtrading.watchtrade.Utils.RetrofitReq;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SendChatImageReq {

//    private MultipartBody.Part message;
    private String senduserID;
    private String getuserID;
    private RequestBody file_send;
//    private String file_name;





    public SendChatImageReq(/*MultipartBody.Part message, */String senduserID, String getuserID, RequestBody file_send/*, String file_name*/ ) {
//        this.message = message;
        this.senduserID = senduserID;
        this.getuserID = getuserID;
        this.file_send = file_send;
//        this.file_name = file_name;


    }
}
