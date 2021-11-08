package com.watchtrading.watchtrade.Utils;

import com.google.firebase.firestore.auth.User;
import com.watchtrading.watchtrade.Utils.RetrofitReq.SendChatImageReq;
import com.watchtrading.watchtrade.Utils.RetrofitReq.SendaddnewwatchReq;
import com.watchtrading.watchtrade.Utils.RetrofitReq.UpdateProfileRequest;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

import static com.watchtrading.watchtrade.Constants.APIContract.ADD_WATCH;
import static com.watchtrading.watchtrade.Constants.APIContract.SEND_FILES;
import static com.watchtrading.watchtrade.Constants.APIContract.SEND_FILES_Watch;
import static com.watchtrading.watchtrade.Constants.APIContract.SEND_MESSAGE;
import static com.watchtrading.watchtrade.Constants.APIContract.UPDATE_PROFILE;

public interface APIService {


    @POST(SEND_FILES)
    Call<ResponseBody> get_send_message(@Body SendChatImageReq sendChatImageReq);

    @POST(SEND_FILES)
    Call<SendChatImageReq> send_file(
                        @Body RequestBody file);

    @POST(ADD_WATCH)
    Call<SendaddnewwatchReq> send_file_watch(
            @Body RequestBody file);

    @POST(UPDATE_PROFILE)
    Call<UpdateProfileRequest> update_profile(
            @Body RequestBody file);


    @Multipart
    @POST(SEND_FILES)
    Call<SendChatImageReq> upload_file(
            @PartMap Map<String, RequestBody> map
    );

}
