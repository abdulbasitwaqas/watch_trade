package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordResetActivity extends AppCompatActivity implements RequestViews, IPresenter {

    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.etConformPassword)
    EditText etConformPassword;
    @BindView(R.id.buttonSave)
    TextView buttonSave;
    String emaiAddress = "";


    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        ButterKnife.bind(this);

        emaiAddress = getIntent().getStringExtra("email");
    }





    private void showProgress(boolean show) {
        if (show) {
            mProgressDialog = new ProgressDialog(this,R.style.CustomFontDialog);
            mProgressDialog.setMessage(getResources().getString(R.string.wait_for));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } else {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }


    @OnClick(R.id.buttonSave)
    public void onViewClicked() {

        if(verifyEditTexts()){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email",""+emaiAddress.toString().trim());
                jsonObject.put("password", ""+etConformPassword.getText().toString().trim());
                jsonObject.put("new_password", ""+etConformPassword.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            Presenter presenter = new Presenter(this, this, this);
            presenter.setPostMethodJsonObject(APIContract.FORGET_PASSWORD, jsonObject, "forgetPassword");

        }
    }


    private boolean verifyEditTexts() {

        if(TextUtils.isEmpty(etNewPassword.getText().toString())){
            etNewPassword.setError("Please enter you password here");
            return false;
        }else if(TextUtils.isEmpty(etConformPassword.getText().toString())){
            etConformPassword.setError("Please confirm your password");
            return false;
        }else if(!etNewPassword.getText().toString().equals(etConformPassword.getText().toString())){
            etNewPassword.setError("You must need to enter password here.");
            etConformPassword.setError("Please confirm your password.");

            return false;
        }else return true;
    }

    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**forgetPassword", requestMessage);
        Log.d("**forgetPassworddRes", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "forgetPassword":
                    if (jsonObject.getString("status").equals("200")) {
                        Toast.makeText(this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if (jsonObject.getInt("status") == 404){
                        Toast.makeText(this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else  {
                        Toast.makeText(PasswordResetActivity.this, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
                    }
                    break;


            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
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

