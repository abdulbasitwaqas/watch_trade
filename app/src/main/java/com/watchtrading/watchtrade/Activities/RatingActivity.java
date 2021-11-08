package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
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

public class RatingActivity extends AppCompatActivity implements IPresenter, RequestViews {
    private ImageView backBtnRA;
    private ImageView starOne, starTwo, starThree, starfour, starfive;
    private Button submitBtn;
    private EditText ratingMessage;
    private int ratingTV=0;
    private ProgressDialog mProgressDialog;

    private String st_rating, createuserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);


        backBtnRA=findViewById(R.id.backBtnRA);
        ratingMessage=findViewById(R.id.ratingMessage);

        createuserID = getIntent().getStringExtra("createuserID");


        starOne=findViewById(R.id.starOne);
        starTwo=findViewById(R.id.starTwo);
        starThree=findViewById(R.id.starThree);
        starfour=findViewById(R.id.starfour);
        starfive=findViewById(R.id.starfive);


        submitBtn=findViewById(R.id.submitBtn);

        starOne.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
        starTwo.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
        starThree.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
        starfour.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
        starfive.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));



        backBtnRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        starOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starOne.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starTwo.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
                starThree.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
                starfour.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
                starfive.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
                ratingTV = 1;
            }
        });
        starTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starOne.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starTwo.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starThree.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
                starfour.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
                starfive.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
                ratingTV = 2;
            }
        });


        starThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starOne.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starTwo.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starThree.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starfour.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
                starfive.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));

                ratingTV = 3;
            }
        });


        starfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starOne.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starTwo.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starThree.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starfour.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starfive.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));

                ratingTV = 4;
            }
        });


        starfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starOne.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starTwo.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starThree.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starfour.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));
                starfive.setBackground(getResources().getDrawable(R.drawable.ic_selected_star));

                ratingTV = 5;
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingMessage.getText().toString().trim().equals("")) {
                    ratingMessage.setError(getResources().getString(R.string._string_edit_text_hint_user_email));
                } else if (ratingTV==0) {
                    Toast.makeText(RatingActivity.this, ""+getResources().getString(R.string.star_rating), Toast.LENGTH_SHORT).show();
                } else if (createuserID.equals("")) {
                    Toast.makeText(RatingActivity.this, ""+getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                } else {
                    ratingRequest();
                }

            }
        });


    }


    private void ratingRequest() {


        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.ADD_RATING, creatStringObject(), "ratingRequest");
        Log.d("nueurl", "" + APIContract.ADD_RATING);
    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("ratinguserID");
        parmsModel.setParmsValue(SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);

        ParmsModel password = new ParmsModel();
        password.setParmsTag("createuserID");
        password.setParmsValue(""+createuserID);
        parmsList.add(password);

        ParmsModel ratingpoints = new ParmsModel();
        ratingpoints.setParmsTag("ratingpoints");
        ratingpoints.setParmsValue(""+ratingTV);
        parmsList.add(ratingpoints);

        ParmsModel reviewmessage = new ParmsModel();
        reviewmessage.setParmsTag("reviewmessage");
        reviewmessage.setParmsValue(""+ratingMessage.getText().toString().trim());
        parmsList.add(reviewmessage);

        return parmsList;
    }

    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**ratingRequestMsg", requestMessage);
        Log.d("**ratingRequestResp", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "ratingRequest":
                    ratingRequestPRocess(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ratingRequestPRocess(JSONObject jsonObject) {
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





    private void showProgress(boolean show) {

        if (show) {
            mProgressDialog = new ProgressDialog(RatingActivity.this,
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