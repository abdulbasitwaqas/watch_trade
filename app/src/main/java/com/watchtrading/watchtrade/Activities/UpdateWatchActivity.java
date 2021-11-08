
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

public class UpdateWatchActivity extends AppCompatActivity implements IPresenter, RequestViews {
    private EditText textTypeTV, brandNameTV, caseSizeTV, modelTV, priceTV, yearTV, subContinentTV, currenyTV, countryTV, watchBoxTV, watchPapersTV;
    private Button addWatchButton;
    private ImageView backBtnANW;
    private ProgressDialog mProgressDialog;
    private String postID,createuserID,postType,postmake,casesize,postmode,enterprice,postyear,postarea,currency,postcountry,watchbox,paperwatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_watch);




        textTypeTV = findViewById(R.id.textTypeTVUW);
        brandNameTV = findViewById(R.id.brandNameTVUW);
        caseSizeTV = findViewById(R.id.caseSizeTVUW);
        modelTV = findViewById(R.id.modelTVUW);
        priceTV = findViewById(R.id.priceTVUW);
        yearTV = findViewById(R.id.yearTVUW);
        subContinentTV = findViewById(R.id.subContinentTVUW);
        currenyTV = findViewById(R.id.currenyTVUW);
        countryTV = findViewById(R.id.countryTVUW);
        watchBoxTV = findViewById(R.id.watchBoxTVUW);
        watchPapersTV = findViewById(R.id.watchPapersTVUW);


        addWatchButton = findViewById(R.id.updateWatchButton);
        backBtnANW = findViewById(R.id.backBtnUW);

        backBtnANW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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

        textTypeTV.setText(""+postType);
        brandNameTV.setText(""+postmake);
        caseSizeTV.setText(""+casesize);
        modelTV.setText(""+postmode);
        priceTV.setText(""+enterprice);
        yearTV.setText(""+postyear);
        subContinentTV.setText(""+postarea);
        countryTV.setText(""+postcountry);
        watchBoxTV.setText(""+watchbox);
        watchPapersTV.setText(""+paperwatch);

        addWatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textTypeTV.getText().toString().trim().equalsIgnoreCase("")) {
                    textTypeTV.setError("Buy/Sell");
                } else if (brandNameTV.getText().toString().trim().equalsIgnoreCase("")) {
                    brandNameTV.setError("Please enter brand Name");
                } else if (caseSizeTV.getText().toString().trim().equalsIgnoreCase("")) {
                    caseSizeTV.setError("Please enter case size");
                } else if (modelTV.getText().toString().trim().equalsIgnoreCase("")) {
                    modelTV.setError("Please enter Model here");
                } else if (priceTV.getText().toString().trim().equalsIgnoreCase("")) {
                    priceTV.setError("Please enter price here");
                } else if (yearTV.getText().toString().trim().equalsIgnoreCase("")) {
                    yearTV.setError("Please add year here");
                } else if (subContinentTV.getText().toString().trim().equalsIgnoreCase("")) {
                    subContinentTV.setError("Please enter continent here");
                } else if (currenyTV.getText().toString().trim().equalsIgnoreCase("")) {
                    currenyTV.setError("Please add your currency type");
                } else if (countryTV.getText().toString().trim().equalsIgnoreCase("")) {
                    countryTV.setError("Please add your country name");
                } else if (watchBoxTV.getText().toString().trim().equalsIgnoreCase("")) {
                    watchBoxTV.setError("Do you have watch Box?");
                } else if (watchPapersTV.getText().toString().trim().equalsIgnoreCase("")) {
                    watchPapersTV.setError("Do you have watch papers?");
                } else {
                    addWatchRequest();
                }
            }
        });
    }

    private void addWatchRequest() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.UPDATE_WATCH, creatStringObject(), "updateWatch");
    }

    private List<ParmsModel> creatStringObject() {

        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel firstNameModel = new ParmsModel();
        firstNameModel.setParmsTag("postype");
        firstNameModel.setParmsValue("" + textTypeTV.getText().toString().trim());
        parmsList.add(firstNameModel);

        ParmsModel passwordModel = new ParmsModel();
        passwordModel.setParmsTag("postmake");
        passwordModel.setParmsValue("" + brandNameTV.getText().toString().trim());
        parmsList.add(passwordModel);

        ParmsModel phoneModel = new ParmsModel();
        phoneModel.setParmsTag("casesize");
        phoneModel.setParmsValue("" + caseSizeTV.getText().toString().trim());
        parmsList.add(phoneModel);

        ParmsModel phoneModelLocation = new ParmsModel();
        phoneModelLocation.setParmsTag("postmodel");
        phoneModelLocation.setParmsValue("" + modelTV.getText().toString().trim());
        parmsList.add(phoneModelLocation);

        ParmsModel phoneModelCountry = new ParmsModel();
        phoneModelCountry.setParmsTag("enterprice");
        phoneModelCountry.setParmsValue("" + priceTV.getText().toString().trim());
        parmsList.add(phoneModelCountry);

        ParmsModel phoneModelCD = new ParmsModel();
        phoneModelCD.setParmsTag("postyear");
        phoneModelCD.setParmsValue("" + yearTV.getText().toString().trim());
        parmsList.add(phoneModelCD);

        ParmsModel phoneModelBD = new ParmsModel();
        phoneModelBD.setParmsTag("postarea");
        phoneModelBD.setParmsValue("" + subContinentTV.getText().toString().trim());
        parmsList.add(phoneModelBD);

        ParmsModel phoneModelSML = new ParmsModel();
        phoneModelSML.setParmsTag("currency");
        phoneModelSML.setParmsValue("" + currenyTV.getText().toString().trim());
        parmsList.add(phoneModelBD);

        ParmsModel phoneModelPC = new ParmsModel();
        phoneModelPC.setParmsTag("postcountry");
        phoneModelPC.setParmsValue("" + countryTV.getText().toString().trim());
        parmsList.add(phoneModelPC);

        ParmsModel phoneModelWB = new ParmsModel();
        phoneModelWB.setParmsTag("watchbox");
        phoneModelWB.setParmsValue("" + watchBoxTV.getText().toString().trim());
        parmsList.add(phoneModelWB);

        ParmsModel phoneModelWP = new ParmsModel();
        phoneModelWP.setParmsTag("paperwatch");
        phoneModelWP.setParmsValue("" + watchPapersTV.getText().toString().trim());
        parmsList.add(phoneModelWP);

        ParmsModel phoneModelUID = new ParmsModel();
        phoneModelUID.setParmsTag("createuserID");
        phoneModelUID.setParmsValue(""+createuserID);
        parmsList.add(phoneModelUID);

        ParmsModel phoneModelPID = new ParmsModel();
        phoneModelPID.setParmsTag("postID");
        phoneModelPID.setParmsValue("" +postID);
        parmsList.add(phoneModelPID);

        return parmsList;
    }

    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**updateWatch", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "updateWatch":

                    updateWatch(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateWatch(JSONObject jsonObject) {
        boolean dataObject;
        try {

            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                finish();


            } else if (dataObject==false) {
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



    private void showProgress(boolean show) {

        if (show) {
            mProgressDialog = new ProgressDialog(UpdateWatchActivity.this,
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