package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileReportActivity extends AppCompatActivity implements IPresenter, RequestViews {
    private TextView reportHeaderTV;
    private ImageView backBtnPRA;
    private EditText reportETPRA;
    private Button reportBtnPRA, cancelReportBtnPRA;
    private String userName, userID;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_report);

        userID = getIntent().getStringExtra("createuserID");
        userName = getIntent().getStringExtra("createuserName");

        initMembers();
    }

    private void initMembers() {

        cancelReportBtnPRA=findViewById(R.id.cancelReportBtnPRA);
        reportBtnPRA=findViewById(R.id.reportBtnPRA);
        reportETPRA=findViewById(R.id.reportETPRA);
        backBtnPRA=findViewById(R.id.backBtnPRA);
        reportHeaderTV=findViewById(R.id.reportHeaderTV);
        reportHeaderTV.setText(userName);

        backBtnPRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancelReportBtnPRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reportBtnPRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportETPRA.getText().toString().trim().equals("")) {
                    reportETPRA.setError(getResources().getString(R.string.pleae_give_report));
                } else {
                    reportRequest();
                }
            }
        });



    }



    private void reportRequest() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.USER_PROFILE_REPORT, creatStringObject(), "userProfRepRequest");
        Log.d("POST_REPORT : ", "" + APIContract.USER_PROFILE_REPORT);
    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();
        parmsModel.setParmsTag("createuserID");
        parmsModel.setParmsValue(""+userID);
        parmsList.add(parmsModel);

        ParmsModel userIDModel = new ParmsModel();
        userIDModel.setParmsTag("userID");
        userIDModel.setParmsValue(""+ SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(userIDModel);

        ParmsModel post_reportmessage = new ParmsModel();
        post_reportmessage.setParmsTag("post_reportmessage");
        post_reportmessage.setParmsValue(reportETPRA.getText().toString().trim());
        parmsList.add(post_reportmessage);

        return parmsList;
    }

    private void showProgress(boolean show) {
        if (show) {
            mProgressDialog = new ProgressDialog(ProfileReportActivity.this,
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
        Log.d("**userReportMsg", requestMessage);
        Log.d("**userReportResponse", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "userProfRepRequest":
                    userReportRequest(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void userReportRequest(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                finish();
            }
            else if (dataObject==false) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, ""+getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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