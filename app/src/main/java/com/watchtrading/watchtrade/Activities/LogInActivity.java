package com.watchtrading.watchtrade.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Models.ProfileModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.RequestPermissionHandler.RequestPermissionHandler;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.Utils.WatchTradeSingleton;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LogInActivity extends AppCompatActivity implements IPresenter, RequestViews {
    private Button logInBtn, memberShipBtnLA;
    private TextView forgetPasswordBtn, signUpBtn;
    private EditText emailET, passwordET;
    private ProgressDialog mProgressDialog;
    private RequestPermissionHandler mRequestPermissionHandler;
    private boolean permissonCheck = false;

    String email, password;
    private String firebaseToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mRequestPermissionHandler = new RequestPermissionHandler();
        initMembers();

        Log.d("*defaultSetting",""+ WatchTradeSingleton.getSingletonInstance().getBrandsList().size());

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LogInActivity.this,
                new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        firebaseToken = instanceIdResult.getToken();

                        SharedPreferencesSotreData.getInstance().setFCMToken(""+firebaseToken);
                        Log.e("**fcmToken", ""+SharedPreferencesSotreData.getInstance().getFCMToken());

                    }
                });
    }


    private void initMembers() {
        logInBtn=findViewById(R.id.logInBtn);
        memberShipBtnLA=findViewById(R.id.membersShipBtnLA);
        emailET =findViewById(R.id.userEmailETLA);
        passwordET =findViewById(R.id.userPasswordETLA);
        forgetPasswordBtn=findViewById(R.id.forgetPasswordBtn);
        signUpBtn=findViewById(R.id.signUpBtn);

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permissonCheck) {
                    validate();
                } else {
                    loginRequest();
                }
            }
        });
        memberShipBtnLA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.worldtrade.watch/membership-application");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri urii = Uri.parse("https://worldtrade.app/signin/");
                Intent intent = new Intent(Intent.ACTION_VIEW, urii);
                startActivity(intent);
            }
        });
        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailET.getText().toString().trim().equals("")) {
                    Toast.makeText(LogInActivity.this, ""+getResources().getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
                } else {
                    Presenter presenter = new Presenter(LogInActivity.this, LogInActivity.this, LogInActivity.this);
                    presenter.setPostMethod(APIContract.FORGET_PASSWORD, forgetJSONPObject(), "forgetPassword");
                    Log.d("nueurl", "" + APIContract.FORGET_PASSWORD);
                }
            }
        });

    }

    private List<ParmsModel> forgetJSONPObject() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();
        parmsModel.setParmsTag("email");
        parmsModel.setParmsValue(emailET.getText().toString().trim());
        parmsList.add(parmsModel);

        return parmsList;
    }


    private void validate() {
        if (emailET.getText().toString().trim().equals("")) {
            emailET.setError(getResources().getString(R.string._string_edit_text_hint_user_email));
        } else if (passwordET.getText().toString().trim().equals("")) {
            passwordET.setError(getResources().getString(R.string._string_error_enter_password));
        } else {

            loginRequest();

        }
    }

    private void loginRequest() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.LOGIN, creatStringObject(), "loginRequest");
        Log.d("nueurl", "" + APIContract.LOGIN);
    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("email");
        parmsModel.setParmsValue(emailET.getText().toString().trim());
        parmsList.add(parmsModel);

        ParmsModel password = new ParmsModel();
        password.setParmsTag("password");
        password.setParmsValue(passwordET.getText().toString().trim());
        parmsList.add(password);

        ParmsModel fbtoken  = new ParmsModel();
        fbtoken .setParmsTag("fbtoken");
        fbtoken .setParmsValue(""+SharedPreferencesSotreData.getInstance().getFCMToken());
        parmsList.add(fbtoken );

        return parmsList;
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.app_icon)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.close_msg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }



    private void showProgress(boolean show) {

        if (show) {
            mProgressDialog = new ProgressDialog(LogInActivity.this,
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


    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**logInRequestMsg", requestMessage);
        Log.d("**logInResponse", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "loginRequest":
                    logInProcess(jsonObject);
                    break;
                case "forgetPassword":
                    forgetPasswordProcess(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void forgetPasswordProcess(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();

            }
            else if (dataObject==false) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logInProcess(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
//                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                JSONObject data = jsonObject.getJSONObject("data");
                String id= data.getString("id");
                String name= data.getString("name");
                String email= data.getString("email");
                String password= data.getString("pass");
                String created= data.getString("created");
                String is_pro_member= data.getString("is_pro_member");

                SharedPreferencesSotreData.getInstance().setToken(password);
                SharedPreferencesSotreData.getInstance().setID(id);
                SharedPreferencesSotreData.getInstance().setUserName(name);
                SharedPreferencesSotreData.getInstance().setEmail(email);
                SharedPreferencesSotreData.getInstance().setIsPro(is_pro_member);
                SharedPreferencesSotreData.getInstance().setCreateProfileDate(created);

                JSONArray jsonArray = jsonObject.getJSONArray("profile");
                for ( int i =0; i<jsonArray.length();i++){
                    JSONObject profileObj = jsonArray.getJSONObject(i);

                    ProfileModel profileModel = new ProfileModel();
                    profileModel.setName(profileObj.getString("name"));
                    profileModel.setV1(profileObj.getString("v1"));



                    if (profileModel.getName().equals("1")){
                        SharedPreferencesSotreData.getInstance().setVatNumber(""+profileModel.getV1());
                        Log.d("**userL","VAT Num: "+profileModel.getV1());
                    }
                    if (profileModel.getName().equals("2")){
                        SharedPreferencesSotreData.getInstance().setDateOfBirth(""+profileModel.getV1());
                        Log.d("**userL","DOB: "+profileModel.getV1());
                    }

                    if (profileModel.getName().equals("5")){
                        SharedPreferencesSotreData.getInstance().setLocation(""+profileModel.getV1());
                        Log.d("**userL","Location: "+profileModel.getV1());
                    }
                    if (profileModel.getName().equals("9")){
                        SharedPreferencesSotreData.getInstance().setCompanyName(""+profileModel.getV1());
                        Log.d("**userL","Company: "+profileModel.getV1());
                    }
                    if (profileModel.getName().equals("10")){
                        SharedPreferencesSotreData.getInstance().setBusinessName(""+profileModel.getV1());
                        Log.d("**userL","Business: "+profileModel.getV1());
                    }

                    if (profileModel.getName().equals("6")){
                        SharedPreferencesSotreData.getInstance().setCountry(""+profileModel.getV1());
                        Log.d("**userL","Country: "+profileModel.getV1());
                    }
                    if (profileModel.getName().equals("4")){
                        SharedPreferencesSotreData.getInstance().setUserPhone(""+profileModel.getV1());
                        Log.d("**userL","Phone Num: "+profileModel.getV1());
                    }
                    if (profileModel.getName().equals("15")){
                        SharedPreferencesSotreData.getInstance().setSocialLinks(""+profileModel.getV1());
                        Log.d("**userL","Social Links: "+profileModel.getV1());
                    }

                }
                
                if (SharedPreferencesSotreData.getInstance().getIsPro().equals(1)){
                    memberShipBtnLA.setVisibility(View.GONE);
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if (dataObject==false) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            }
            else {
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
}