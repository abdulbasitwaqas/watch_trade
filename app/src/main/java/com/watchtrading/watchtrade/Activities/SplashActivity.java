package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.DefaultSetting;
import com.watchtrading.watchtrade.Models.DefaultSettingModel;
import com.watchtrading.watchtrade.Models.SearchModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.Utils.WatchTradeSingleton;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements IPresenter, RequestViews {
    String firebaseToken = "";
    private ProgressDialog mProgressDialog;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();


        getDefaultSettings();
    }

    private void getDefaultSettings() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setGetMethod(APIContract.GET_DEFAULT_SETTINGS, "defaultSetting");
        Log.d("defaultSettAPI:", "" + APIContract.GET_DEFAULT_SETTINGS);
    }


    @Override
    public void getResponse(String response, String requestMessage) {


        Log.d("**defaultSettingMsg", requestMessage);
        Log.d("**defaultSettingRes", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "defaultSetting":
                    defaultSetting(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void defaultSetting(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
                String message = jsonObject.getString("message");
//                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();

                JSONObject default_setting = jsonObject.getJSONObject("default_setting");
                JSONArray allbrands = default_setting.getJSONArray("allbrands");
                JSONArray currency_symbol = default_setting.getJSONArray("currency_symbol");
                JSONArray country_array = default_setting.getJSONArray("country_array");
                JSONObject state_array = default_setting.getJSONObject("state_array");

                JSONArray africaArray = state_array.getJSONArray("Africa");
                JSONArray asiaArray = state_array.getJSONArray("Asia");
                JSONArray australiaArray = state_array.getJSONArray("Australia");
                JSONArray europeArray = state_array.getJSONArray("Europe");
                JSONArray northamericaArray = state_array.getJSONArray("northamerica");
                JSONArray SouthamericaArray = state_array.getJSONArray("Southamerica");


                Log.d("*allBrands",""+allbrands);

                Type searchType = new TypeToken<List<String>>() {}.getType();
                WatchTradeSingleton.
                        getSingletonInstance().
                        setBrandsList(gson.fromJson(default_setting.getString("allbrands"), searchType));



                Type currencySymbolType = new TypeToken<List<String>>() {}.getType();
                WatchTradeSingleton.
                        getSingletonInstance().
                        setcurrencySymbol(gson.fromJson(default_setting.getString("currency_symbol"), currencySymbolType));


                Type continentType = new TypeToken<List<String>>() {}.getType();
                WatchTradeSingleton.
                        getSingletonInstance().
                        setContinentList(gson.fromJson(default_setting.getString("country_array"), continentType));





                Type africaType = new TypeToken<List<String>>() {}.getType();
                WatchTradeSingleton.
                        getSingletonInstance().
                        setafricaList(gson.fromJson(state_array.getString("Africa"), africaType));



                Type asiaType = new TypeToken<List<String>>() {}.getType();
                WatchTradeSingleton.
                        getSingletonInstance().
                        setasiaList(gson.fromJson(state_array.getString("Asia"), asiaType));




                Type australiaType = new TypeToken<List<String>>() {}.getType();
                WatchTradeSingleton.
                        getSingletonInstance().
                        setaustraliaList(gson.fromJson(state_array.getString("Australia"), australiaType));




                Type europeType = new TypeToken<List<String>>() {}.getType();
                WatchTradeSingleton.
                        getSingletonInstance().
                        seteuropeliaList(gson.fromJson(state_array.getString("Europe"), europeType));




                Type northamericaType = new TypeToken<List<String>>() {}.getType();
                WatchTradeSingleton.
                        getSingletonInstance().
                        setnorthAmericaliaList(gson.fromJson(state_array.getString("northamerica"), northamericaType));



                Type SouthamericaType = new TypeToken<List<String>>() {}.getType();
                WatchTradeSingleton.
                        getSingletonInstance().
                        setsouthAmericaliaList(gson.fromJson(state_array.getString("Southamerica"), SouthamericaType));






                splashThread();


            } else if (dataObject == false) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getError(VolleyError error) {

    }

    @Override
    public void getSuccessNetwork(NetworkResponse response, String requestMessage) {

    }

    @Override
    public void showProgress() {
        showProgress(true);

    }

    @Override
    public void hideProgress() {
        showProgress(false);

    }

    private void splashThread() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    if (SharedPreferencesSotreData.getInstance().getToken().length() >= 6) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, LogInActivity.class));
                        finish();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, 500);
    }


    private void showProgress(boolean show) {

        if (show) {
            mProgressDialog = new ProgressDialog(SplashActivity.this,
                    R.style.CustomFontDialog);
            mProgressDialog.setMessage(getResources().getString(R.string.string_message_wait));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } else {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }
}