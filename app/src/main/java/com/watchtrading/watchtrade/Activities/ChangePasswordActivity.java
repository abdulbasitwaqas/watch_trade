package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChangePasswordActivity extends AppCompatActivity implements IPresenter, RequestViews {
    private EditText etOldPassword,etNewPassword,etConformPassword;
    private Button buttonUpdate;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        buttonUpdate=findViewById(R.id.buttonUpdate);
        etOldPassword=findViewById(R.id.etOldPassword);
        etNewPassword=findViewById(R.id.etNewPassword);
        etConformPassword=findViewById(R.id.etConformPassword);


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOldPassword.getText().toString().trim().equals("")) {
                    etOldPassword.setError(getResources().getString(R.string.old_password));
                } else if (etNewPassword.getText().toString().trim().equals("")) {
                    etNewPassword.setError(getResources().getString(R.string._string_new_password));
                } else if (etConformPassword.getText().toString().trim().equals("")) {
                    etConformPassword.setError(getResources().getString(R.string.confirm_password));
                }
                else if (!etNewPassword.getText().toString().trim().equals(""+etConformPassword.getText().toString().trim())){
                    etNewPassword.setError("Password not matching");
                    etConformPassword.setError("Password not matching");
                }
                else {
                    updatePasswordRequest();
                }
            }
        });

    }

    private void updatePasswordRequest() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.UPDATE_PASSWORD, creatStringObject(), "updatePass");
    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("userID");
        parmsModel.setParmsValue(SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);

        ParmsModel password = new ParmsModel();
        password.setParmsTag("password");
        password.setParmsValue(etNewPassword.getText().toString().trim());
        parmsList.add(password);

        ParmsModel oldpassword = new ParmsModel();
        oldpassword.setParmsTag("oldpassword");
        oldpassword.setParmsValue(etOldPassword.getText().toString().trim());
        parmsList.add(oldpassword);

        return parmsList;
    }

    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**updatePass", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "updatePass":

                    updatePass(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updatePass(JSONObject jsonObject) {
        boolean dataObject;
        try {

            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();

                finish();





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



    private void showProgress(boolean show) {

        if (show) {
            mProgressDialog = new ProgressDialog(ChangePasswordActivity.this,
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