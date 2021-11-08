package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportActivity extends AppCompatActivity implements IPresenter, RequestViews{
    private TextView makeTV, modelTV, locationTV, yearTV, priceTV, typeTV;
    private RadioGroup paperAndBoxRG;
    private CheckBox papersCB, boxCB;
    private String postID,createuserID,postType,postmake,casesize,postmode,enterprice,postyear,postarea,currency,postcountry,watchbox,paperwatch;
    private EditText reportET;
    private Button submitReviewBtn;
    private ImageView backBtnRA;
    private String report_st;
    private ProgressDialog mProgressDialog;
    private CircleImageView userImageCLSL;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportET=findViewById(R.id.reportET);
        submitReviewBtn=findViewById(R.id.submitReviewBtn);
        backBtnRA=findViewById(R.id.backBtnRA);
        userImageCLSL=findViewById(R.id.userImageCLSL);
        backBtnRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        submitReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportET.getText().toString().trim().equals("")) {
                    reportET.setError(getResources().getString(R.string.pleae_give_report));
                } else {
                    reportRequest();
                }

            }
        });




        typeTV=findViewById(R.id.typeTV);
        priceTV=findViewById(R.id.priceTV);
        makeTV=findViewById(R.id.makeTV);
        modelTV=findViewById(R.id.modelTV);
        locationTV=findViewById(R.id.locationTV);
        yearTV=findViewById(R.id.yearTV);

        paperAndBoxRG=findViewById(R.id.paperAndBoxRG);

        papersCB=findViewById(R.id.papersCB);
        boxCB=findViewById(R.id.boxCB);


        Intent intent = getIntent();
        postID =  intent.getStringExtra("postID");
        createuserID = intent.getStringExtra("createuserID");
        postType =    intent.getStringExtra("postType");
        postmake =    intent.getStringExtra("postmake");
        casesize =    intent.getStringExtra("casesize");
        postmode =    intent.getStringExtra("postmode");
        enterprice =    intent.getStringExtra("enterprice");
        postyear =    intent.getStringExtra("postyear");
        postarea =    intent.getStringExtra("postarea");
        postcountry =    intent.getStringExtra("postcountry");
        watchbox =    intent.getStringExtra("watchbox");
        paperwatch =    intent.getStringExtra("paperwatch");

        Log.d("**postType",""+postType);

        typeTV.setText(""+postType);
        makeTV.setText(""+postmake);
        priceTV.setText(""+enterprice);
        modelTV.setText(""+postmode);
        locationTV.setText(""+postarea);
        yearTV.setText(""+postyear);

        if (watchbox.equals("true")){
            boxCB.setChecked(true);
        }

        if (paperwatch.equals(true)){
            papersCB.setChecked(true);
        }


        Glide.with(ReportActivity.this)
                .load(APIContract.IMAGE_URL+postID+".jpg")
                .placeholder(R.drawable.app_icon)
                .into(userImageCLSL);
    }

    private void reportRequest() {

        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.POST_REPORT, creatStringObject(), "userReportRequest");
        Log.d("POST_REPORT : ", "" + APIContract.POST_REPORT);
    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("createuserID");
        parmsModel.setParmsValue(""+createuserID);
        parmsList.add(parmsModel);

        ParmsModel password = new ParmsModel();
        password.setParmsTag("postID");
        password.setParmsValue(""+postID);
        parmsList.add(password);

        ParmsModel post_reportmessage = new ParmsModel();
        post_reportmessage.setParmsTag("post_reportmessage");
        post_reportmessage.setParmsValue(reportET.getText().toString().trim());
        parmsList.add(post_reportmessage);

        return parmsList;
    }

    private void showProgress(boolean show) {
        if (show) {
            mProgressDialog = new ProgressDialog(ReportActivity.this,
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
                case "userReportRequest":
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