package com.watchtrading.watchtrade.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.watchtrading.watchtrade.Utils.RetrofitReq.SendChatImageReq;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.watchtrading.watchtrade.Constants.APIContract.BASE_URL;

public class APIManager {

    private static final String TAG = "APIManager";
    private static final APIManager instance = new APIManager();
    private Retrofit retrofit;


    public interface Callback {
        void onResult(boolean z, String response);
    }

    public static APIManager getInstance() {
        return instance;
    }

    private APIManager() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

//        httpClient.addNetworkInterceptor(new AddHeaderInterceptor());

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }




    public void setSendMessage(final Callback callback, SendChatImageReq sendChatImageReq) {
        APIService service = retrofit.create(APIService.class);
        Call<ResponseBody> result = service.get_send_message(sendChatImageReq);
        sendRequest(callback, result);
    }


    private void sendRequest(final Callback callback, Call<ResponseBody> result) {

        result.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {

                Log.e("response :", "...." + response.toString());
                Log.e("apiurl :", "...." + response.raw().request().url());

                if (response.body() != null) {
                    try {
                        if (callback != null) {
                            String serverResponse = response.body().string();
                            logLargeString(serverResponse);
                            callback.onResult(true, "" + serverResponse);
                        }
                    } catch (IOException e) {
                        Log.e("Error", "" +e.toString());

                        e.printStackTrace();
                        if (callback != null)
                            callback.onResult(false, "Error");
                    }
                } else {
                    if (callback != null)
                        callback.onResult(false, "" + response.errorBody().toString());
                    Log.e("Error", "" + response.errorBody().contentType().toString().toString());

                    try {
                        Log.e("Response", "...." + response.errorBody().string());
                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                t.fillInStackTrace();
                if (callback != null) {
                    callback.onResult(false, "" + t.getMessage());
                }
                Log.e("response :", "...." + t.toString());

            }

        });
    }



    public void logLargeString(String str) {
        if(str.length() > 3000) {
            Log.i("Response", str.substring(0, 3000));
            logLargeString(str.substring(3000));
        } else {
            Log.i("Response", ""+str); // continuation
        }
    }



}
