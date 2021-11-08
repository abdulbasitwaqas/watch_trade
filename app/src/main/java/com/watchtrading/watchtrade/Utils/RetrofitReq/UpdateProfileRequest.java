package com.watchtrading.watchtrade.Utils.RetrofitReq;

import okhttp3.RequestBody;

public class UpdateProfileRequest {
    private String userID;
    private String email;
    private String name;
    private String profile_1;
    private String profile_2;
    private String profile_4;
    private String profile_5;
    private String profile_6;
    private String profile_9;
    private String profile_10;
    private String profile_15;
    private RequestBody profile_image;

    public UpdateProfileRequest(String userID, String email, String name, String profile_1, String profile_2, String profile_4, String profile_5, String profile_6, String profile_9, String profile_10, String profile_15, RequestBody profile_image) {
        this.userID = userID;
        this.email = email;
        this.name = name;
        this.profile_1 = profile_1;
        this.profile_2 = profile_2;
        this.profile_4 = profile_4;
        this.profile_5 = profile_5;
        this.profile_6 = profile_6;
        this.profile_9 = profile_9;
        this.profile_10 = profile_10;
        this.profile_15 = profile_15;
        this.profile_image = profile_image;
    }
}
