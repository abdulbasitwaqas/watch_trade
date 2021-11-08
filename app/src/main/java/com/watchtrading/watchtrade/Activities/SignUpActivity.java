package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class SignUpActivity extends AppCompatActivity implements IPresenter, RequestViews {
    private Spinner countiesSpinner;
        public String[] countriesList = new String[]{"Country*","Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
                "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria",
                "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
                "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana",
                "Brazil", "British Indian Ocean Territory", "British Virgin Islands", "Brunei", "Bulgaria",
                "Burkina Faso", "Burma (Myanmar)", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde",
                "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island",
                "Cocos (Keeling) Islands", "Colombia", "Comoros", "Cook Islands", "Costa Rica",
                "Croatia", "Cuba", "Cyprus", "Czech Republic", "Democratic Republic of the Congo",
                "Denmark", "Djibouti", "Dominica", "Dominican Republic",
                "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
                "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Polynesia",
                "Gabon", "Gambia", "Gaza Strip", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece",
                "Greenland", "Grenada", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana",
                "Haiti", "Holy See (Vatican City)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India",
                "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Ivory Coast", "Jamaica",
                "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kosovo", "Kuwait",
                "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein",
                "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia",
                "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mayotte", "Mexico",
                "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco",
                "Mozambique", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia",
                "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea",
                "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama",
                "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Islands", "Poland",
                "Portugal", "Puerto Rico", "Qatar", "Republic of the Congo", "Romania", "Russia", "Rwanda",
                "Saint Barthelemy", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin",
                "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines", "Samoa", "San Marino",
                "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone",
                "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Korea",
                "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland",
                "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau",
                "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands",
                "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "US Virgin Islands", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam",
                "Wallis and Futuna", "West Bank", "Yemen", "Zambia", "Zimbabwe"};


    static int selectedItem = -1;
    private ArrayAdapter<String> adapter;
    private String country_st;



    String fullName,userEmail,userName, userLocation,companyDetails_st, businessDetails_st, socialAccounts_st, password;
    ProgressDialog mDialog;
    private boolean isEnglish;

    private EditText userFullNameETLA, userEmailETLA,userNameETLA, addressETLA, companyDetailsETLA, businessDetailsETLA, socialAccountsETLA, passwordETLA;
    private Button signUpBtn, logInBtnSUA;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        countiesSpinner=findViewById(R.id.countiesSpinner);

        signUpBtn=findViewById(R.id.signUpBtn);
        logInBtnSUA=findViewById(R.id.logInBtnSUA);


        logInBtnSUA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userFullNameETLA=findViewById(R.id.userFullNameETLA);
        userEmailETLA =findViewById(R.id.userEmailETLA);
        userNameETLA=findViewById(R.id.userNameETLA);
        addressETLA=findViewById(R.id.locationETLA);
        companyDetailsETLA=findViewById(R.id.companyDetailsETLA);
        businessDetailsETLA=findViewById(R.id.businessDetailsETLA);
        socialAccountsETLA=findViewById(R.id.socialAccountsETLA);
        passwordETLA=findViewById(R.id.passwordETLA);



        adapter = new ArrayAdapter<>(SignUpActivity.this, R.layout.main_spinner_layout, countriesList);



        countiesSpinner.setAdapter(adapter);


        countiesSpinner.setSelection(selectedItem);

        countiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**countrySignup", "" + countiesSpinner.getSelectedItem().toString());
                country_st = countiesSpinner.getSelectedItem().toString();
                selectedItem = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });



        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRegister();
            }
        });

    }
    private void userRegister() {
        fullName = userFullNameETLA.getText().toString().trim();
        userEmail = userEmailETLA.getText().toString().trim();
        userName = userNameETLA.getText().toString().trim();
        userLocation = addressETLA.getText().toString().trim();
        companyDetails_st = companyDetailsETLA.getText().toString().trim();
        businessDetails_st = businessDetailsETLA.getText().toString().trim();
        socialAccounts_st = socialAccountsETLA.getText().toString().trim();
        password = passwordETLA.getText().toString().trim();

        if (userFullNameETLA.getText().toString().trim().equalsIgnoreCase("")) {
            userFullNameETLA.setError("Please enter Full Name");
        } else if (userEmailETLA.getText().toString().trim().equalsIgnoreCase("")) {
            userEmailETLA.setError("Please enter your email");
        } else if (userNameETLA.getText().toString().trim().equalsIgnoreCase("")) {
            userNameETLA.setError("Please enter username here");
        } else if (addressETLA.getText().toString().trim().equalsIgnoreCase("")) {
            addressETLA.setError("Please enter Location here");
        } else if (companyDetailsETLA.getText().toString().trim().equalsIgnoreCase("")) {
            companyDetailsETLA.setError("Please enter company details here");
        } else if (businessDetailsETLA.getText().toString().trim().equalsIgnoreCase("")) {
            businessDetailsETLA.setError("Please business details here");
        } else if (socialAccountsETLA.getText().toString().trim().equalsIgnoreCase("")) {
            socialAccountsETLA.setError("Please enter social accounts here");
        } else if (passwordETLA.getText().toString().trim().equalsIgnoreCase("")) {
            passwordETLA.setError("Please enter valid password");
        }
        else if (password.length() < 6) {
            Toast.makeText(SignUpActivity.this, "Password must be greater then 6 digit", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            registerRequestAPI();

        }

        }

    private void registerRequestAPI() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.REGISTER, creatStringObject(), "registerReq");
    }

    private List<ParmsModel> creatStringObject() {

        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel firstNameModel = new ParmsModel();
        firstNameModel.setParmsTag("email");
        firstNameModel.setParmsValue(userEmail);
        parmsList.add(firstNameModel);

        ParmsModel passwordModel = new ParmsModel();
        passwordModel.setParmsTag("password");
        passwordModel.setParmsValue(password);
        parmsList.add(passwordModel);

        ParmsModel phoneModel = new ParmsModel();
        phoneModel.setParmsTag("name");
        phoneModel.setParmsValue(userName);
        parmsList.add(phoneModel);

        ParmsModel fullname = new ParmsModel();
        fullname.setParmsTag("fullname");
        fullname.setParmsValue(""+fullName);
        parmsList.add(fullname);

        ParmsModel phoneModelLocation = new ParmsModel();
        phoneModelLocation.setParmsTag("profile_5");
        phoneModelLocation.setParmsValue(""+userLocation);
        parmsList.add(phoneModelLocation);

        ParmsModel phoneModelCountry = new ParmsModel();
        phoneModelCountry.setParmsTag("profile_6");
        phoneModelCountry.setParmsValue(""+country_st);
        parmsList.add(phoneModelCountry);

        ParmsModel phoneModelCD = new ParmsModel();
        phoneModelCD.setParmsTag("profile_10");
        phoneModelCD.setParmsValue(""+companyDetails_st);
        parmsList.add(phoneModelCD);

        ParmsModel phoneModelBD = new ParmsModel();
        phoneModelBD.setParmsTag("profile_9");
        phoneModelBD.setParmsValue(""+businessDetails_st);
        parmsList.add(phoneModelBD);

        ParmsModel phoneModelSML = new ParmsModel();
        phoneModelSML.setParmsTag("profile_1");
        phoneModelSML.setParmsValue(""+socialAccounts_st);
        parmsList.add(phoneModelBD);

        ParmsModel fbtoken  = new ParmsModel();
        fbtoken .setParmsTag("fbtoken");
        fbtoken .setParmsValue(""+ SharedPreferencesSotreData.getInstance().getFCMToken());
        parmsList.add(fbtoken );

        return parmsList;
    }


    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**registerRequest", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "registerReq":

                    registerReq(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void registerReq(JSONObject jsonObject) {
        boolean dataObject;
        try {

            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "Register Successfully!" , Toast.LENGTH_SHORT).show();
               JSONObject data = jsonObject.getJSONObject("data");
                String id= data.getString("id");
                String name= data.getString("name");
                String email= data.getString("email");
                String password= data.getString("pass");


                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();





            } else if (dataObject == false) {
                String message = jsonObject.getString("validation_error");
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
            mProgressDialog = new ProgressDialog(SignUpActivity.this,
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