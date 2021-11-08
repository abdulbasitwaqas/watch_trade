package com.watchtrading.watchtrade.interfaces;


import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

public interface IPresenter {
    public void getResponse(String response, String requestMessage);
    public void getError(VolleyError error);
    public void getSuccessNetwork(NetworkResponse response, String requestMessage);
}
