package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.watchtrading.watchtrade.MailServer.GMailSender;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.AppController;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class OTPVarificationActivity extends AppCompatActivity implements RequestViews, IPresenter {

    @BindView(R.id.etOtpVarification)
    EditText etOtpVarification;
    @BindView(R.id.resendCodeTv)
    TextView resendCodeTv;
    @BindView(R.id.buttonSave)
    TextView buttonNext;


    CountDownTimer count;
    String emailAddress;

    private ProgressDialog mProgressDialog;

    private boolean isEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isEnglish = AppController.setLocale();
        AppController.updateResources(this, SharedPreferencesSotreData.getInstance().getUserLanguage());

        setContentView(R.layout.activity_o_t_p_varification);
        ButterKnife.bind(this);

        emailAddress = getIntent().getStringExtra("email");
        Log.d("*emailForgetPassword", ""+emailAddress);
       /* navigateTo = getIntent().getStringExtra("navigateTo");
        password = getIntent().getStringExtra("password");*/
        startTimer();

        new Thread() {
            public void run() {
                try {
                    sendEmail(emailAddress);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }.start();
    }

    @OnClick({R.id.resendCodeTv, R.id.buttonSave})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.resendCodeTv:
                Toast.makeText(OTPVarificationActivity.this, getResources().getString(R.string.new_code_email_instruction), Toast.LENGTH_SHORT).show();
                new Thread() {
                    public void run() {
                        try {
                            sendEmail(emailAddress);
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }
                    }
                }.start();
                startTimer();
                break;

            case R.id.buttonSave:
                if (TextUtils.isEmpty(etOtpVarification.getText().toString())) {
                    etOtpVarification.setError(getResources().getString(R.string.enter_otp));
                } else if (etOtpVarification.getText().toString().equals(SharedPreferencesSotreData.getInstance().getOTP())) {
                    Intent intent = new Intent(OTPVarificationActivity.this, PasswordResetActivity.class);
                    intent.putExtra("email", ""+emailAddress);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(OTPVarificationActivity.this, "Please Enter Correct OTP", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }




    private void sendEmail(String emailAddress) {
        try {
            GMailSender sender = new GMailSender("bestShape00@gmail.com", "bestshape1000");
            sender.sendMail(getResources().getString(R.string.otp_email_title),
                    getResources().getString(R.string.otp_email_body_1) + " \n" + getRandomNumberString() +
                            " \n" + getResources().getString(R.string.otp_email_body_2),
                    "bestShape00@gmail.com",
                    emailAddress);

        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
        Log.d("code_generate", SharedPreferencesSotreData.getInstance().getOTP());

    }
    private void startTimer() {

        resendCodeTv.setClickable(false);
        count = new CountDownTimer(60000, 1000) {


            public void onTick(long millisUntilFinished) {

                resendCodeTv.setText(getResources().getString(R.string.wait_for) + " " + (millisUntilFinished / 1000) + " " + getResources().getString(R.string._string_seconds));

            }

            public void onFinish() {
                resendCodeTv.setText(getResources().getString(R.string.resend_code));
                resendCodeTv.setClickable(true);
                SharedPreferencesSotreData.getInstance().setOTP(null);
            }
        }.start();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (count != null)
            count.cancel();
    }


    public String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        @SuppressLint("DefaultLocale")
        String OTP = String.format("%06d", rnd.nextInt(999999));
        SharedPreferencesSotreData.getInstance().setOTP(OTP);

        // this will convert any number sequence into 6 character.
        return OTP;
    }



    @Override
    public void getResponse(String response, String requestMessage) {

        Log.e("verify_response", response);
        try {

            JSONObject jsonObject = new JSONObject(response);
            if (requestMessage.equals("verifyAccount")) {
                if (jsonObject.getString("statusCode").equals("200")) {

                }


            }
        } catch (JSONException e) {
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

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferencesSotreData.getInstance().setContext(newBase);
        AppController.updateResources(newBase, SharedPreferencesSotreData.getInstance().getUserLanguage());
        super.attachBaseContext(newBase);
    }
}