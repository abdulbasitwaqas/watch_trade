package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Models.ProfileModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity implements IPresenter, RequestViews {
    private CircleImageView userImageCLSL;
    private Button messageBtn, rattingBtn, stockBtn, reportUserBtn, editProfIV;
    private TextView userNameTVUPA,userIDUPA, ratingTV, locationTVUPA, countryTVUPA, socialMediaTVUPA, vatTVUPA,
            dobTVUPA, phoneTVUPA, companyNameTVUPA;
    private ImageView addToContact, backBtnUPA, blockIV;
    private String userIntentID;
    private ProgressDialog mProgressDialog;
    private String uuID, name, ratingpoints;
    private View btLine;
    private CardView locationCV, countryCV, socialMediaLinksCV, vatNumCV, dobCV, phoneCV, companyNameCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        userIntentID = getIntent().getStringExtra("userID");
//        initMembers();








    }

    private void initMembers() {
        btLine=findViewById(R.id.btLine);
        userImageCLSL=findViewById(R.id.userImageCLSL);
        messageBtn=findViewById(R.id.messageBtn);
        rattingBtn=findViewById(R.id.rattingBtn);
        stockBtn=findViewById(R.id.stockBtn);
        reportUserBtn=findViewById(R.id.reportUserBtn);
        userNameTVUPA=findViewById(R.id.userNameTVUPA);
        userIDUPA=findViewById(R.id.userIDUPA);

        userIDUPA.setText(userIntentID);
        ratingTV=findViewById(R.id.ratingTV);
        vatTVUPA=findViewById(R.id.vatTVUPA);
        dobTVUPA=findViewById(R.id.dobTVUPA);
        phoneTVUPA=findViewById(R.id.phoneTVUPA);
        companyNameTVUPA=findViewById(R.id.companyNameTVUPA);
        locationTVUPA=findViewById(R.id.locationTVUPA);
        countryTVUPA=findViewById(R.id.countryTVUPA);
        socialMediaTVUPA=findViewById(R.id.socialMediaTVUPA);
        addToContact=findViewById(R.id.addToContact);
        editProfIV=findViewById(R.id.editProfIV);
        backBtnUPA=findViewById(R.id.backBtnUPA);
        blockIV=findViewById(R.id.blockIV);
        socialMediaLinksCV=findViewById(R.id.socialMediaLinksCV);
        countryCV=findViewById(R.id.countryCV);
        locationCV=findViewById(R.id.locationCV);
        vatNumCV=findViewById(R.id.vatNumCV);
        dobCV=findViewById(R.id.dobCV);
        phoneCV=findViewById(R.id.phoneCV);
        companyNameCV=findViewById(R.id.companyNameCV);

        backBtnUPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editProfIV.setVisibility(View.GONE);
        if (userIntentID.equals(""+SharedPreferencesSotreData.getInstance().getID())){
            addToContact.setVisibility(View.GONE);
            editProfIV.setVisibility(View.VISIBLE);
            blockIV.setVisibility(View.GONE);
            messageBtn.setVisibility(View.GONE);
            btLine.setVisibility(View.GONE);
            messageBtn.setEnabled(false);

            reportUserBtn.setText(getResources().getString(R.string.cannot_rate_yourself));
            reportUserBtn.setEnabled(false);

            vatTVUPA.setText(""+SharedPreferencesSotreData.getInstance().getVatNumber());
            dobTVUPA.setText(""+SharedPreferencesSotreData.getInstance().getDateOfBirth());
            phoneTVUPA.setText(""+SharedPreferencesSotreData.getInstance().getUserPhone());
            companyNameTVUPA.setText(""+SharedPreferencesSotreData.getInstance().getCompanyName());
            locationTVUPA.setText(""+SharedPreferencesSotreData.getInstance().getLocation());
            countryTVUPA.setText(""+SharedPreferencesSotreData.getInstance().getCountry());
            socialMediaTVUPA.setText(""+SharedPreferencesSotreData.getInstance().getSocialLinks());

            if (SharedPreferencesSotreData.getInstance().getVatNumber().equals("")){
                vatNumCV.setVisibility(View.GONE);
            }
            if (SharedPreferencesSotreData.getInstance().getDateOfBirth().equals("")){
                dobCV.setVisibility(View.GONE);
            }
            if (SharedPreferencesSotreData.getInstance().getUserPhone().equals("")){
                phoneCV.setVisibility(View.GONE);
            }
            if (SharedPreferencesSotreData.getInstance().getLocation().equals("")){
                locationCV.setVisibility(View.GONE);
            }
            if (SharedPreferencesSotreData.getInstance().getCompanyName().equals("")){
                companyNameCV.setVisibility(View.GONE);
            }
            if (SharedPreferencesSotreData.getInstance().getCountry().equals("")){
                countryCV.setVisibility(View.GONE);
            }
            if (SharedPreferencesSotreData.getInstance().getSocialLinks().equals("")){
                socialMediaLinksCV.setVisibility(View.GONE);
            }



            editProfIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                }
            });



        }




        blockIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockRequest();
            }
        });

        getProfileReq();
        getProfileImage();





        if (vatTVUPA.getText().toString().trim().equals("")){
            vatNumCV.setVisibility(View.GONE);
        }
        if (dobTVUPA.getText().toString().trim().equals("")){
            dobCV.setVisibility(View.GONE);
        }
        if (phoneTVUPA.getText().toString().trim().equals("")){
            phoneCV.setVisibility(View.GONE);
        }
        if (companyNameTVUPA.getText().toString().trim().equals("")){
            companyNameCV.setVisibility(View.GONE);
        }
        if (locationTVUPA.getText().toString().trim().equals("")){
            locationCV.setVisibility(View.GONE);
        }
        if (countryTVUPA.getText().toString().trim().equals("")){
            countryCV.setVisibility(View.GONE);
        }
        if (socialMediaTVUPA.getText().toString().trim().equals("")){
            socialMediaLinksCV.setVisibility(View.GONE);
        }


        addToContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToContactRequest();
            }
        });

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(UserProfileActivity.this,  ChattingActivity.class);
                intentt.putExtra("uuuID", ""+ uuID);
                intentt.putExtra("uuuName", ""+name);
                startActivity(intentt);
            }
        });

        stockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stocksIntent = new Intent(UserProfileActivity.this,  StocksActivity.class);
                stocksIntent.putExtra("createuserID", ""+ uuID);
                startActivity(stocksIntent);
            }
        });

        rattingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(UserProfileActivity.this,  RatingListActivity.class);
                intentt.putExtra("createuserID", ""+ uuID);
                intentt.putExtra("ratingpoints", ratingpoints);

                startActivity(intentt);
            }
        });

        reportUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(UserProfileActivity.this,  ProfileReportActivity.class);
                intentt.putExtra("createuserID", ""+ uuID);
                intentt.putExtra("createuserName", ""+name);
                startActivity(intentt);
            }
        });
    }

    private void blockRequest() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.BLOCK_UNBLACK, blockRequest(userIntentID), "block");
        Log.d("block_unblock:", "" + APIContract.BLOCK_UNBLACK);
    }

    private List<ParmsModel> blockRequest(String userID) {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel addModel = new ParmsModel();
        addModel.setParmsTag("type");
        addModel.setParmsValue("add");
        parmsList.add(addModel);

        ParmsModel cuidModel = new ParmsModel();
        cuidModel.setParmsTag("createuserID");
        cuidModel.setParmsValue(""+ SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(cuidModel);

        ParmsModel uidModel = new ParmsModel();
        uidModel.setParmsTag("blockuserID");
        uidModel.setParmsValue(""+userID);
        parmsList.add(uidModel);

        return parmsList;
    }


    private void addToContactRequest() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.ADD_TO_CONTACTS, creatStringObjects(), "addToConRequest");
        Log.d("ADD_TO_CONTACTS:::", "" + APIContract.ADD_TO_CONTACTS);
    }

    private List<ParmsModel> creatStringObjects() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("createuserID");
        parmsModel.setParmsValue(""+SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);

        ParmsModel userID = new ParmsModel();
        userID.setParmsTag("userID");
        userID.setParmsValue(""+userIntentID);
        parmsList.add(userID);

//        Toast.makeText(UserProfileActivity.this, ""+userID, Toast.LENGTH_SHORT).show();

        return parmsList;
    }

    private void getProfileReq() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.GET_PROFILE, creatStringObject(), "getProfile");
        Log.d("getProfile : ", "" + APIContract.GET_PROFILE);
    }
    private void getProfileImage() {

    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("userID");
        parmsModel.setParmsValue(""+ userIntentID);
        parmsList.add(parmsModel);

        return parmsList;
    }


    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**getProfileMsg", requestMessage);
        Log.d("**getProfileResp", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "getProfile":
                    getProfile(jsonObject);
                    break;
                case "getProfileImage":
                    getProfileImg(jsonObject);
                    break;

                case "addToConRequest":
                    addToConRequest(jsonObject);
                    break;
                case "block":
                    blockReqResponse(jsonObject);
                    break;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void getProfileImg(JSONObject jsonObject) {

        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
//            Toast.makeText(this, "PROFILE IMAGE", Toast.LENGTH_SHORT).show();
            if (dataObject==true) {
                String message = jsonObject.getString("image");
                Log.d("*imageURL",""+message);

                Glide.with(this)
                        .load(message)
                        .placeholder(R.drawable.app_icon)
                        .into(userImageCLSL);
            }
            else if (dataObject==false) {
//                String message = jsonObject.getString("message");
                Toast.makeText(this, "Image file corrupted", Toast.LENGTH_SHORT).show();
                Glide.with(this)
                        .load("false")
                        .placeholder(R.drawable.app_icon)
                        .into(userImageCLSL);
            }
            else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void blockReqResponse(JSONObject jsonObject) {

        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
//                Toast.makeText(this, "" +message, Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "User blocked Successfully", Toast.LENGTH_SHORT).show();
            }
            else if (dataObject==false) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" +message, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToConRequest(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();

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

    private void getProfile(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject userdata = data.getJSONObject("userdata");

                uuID = userdata.getString("id");
                name = userdata.getString("name");

                JSONObject ratingdata = data.getJSONObject("ratingdata");

                JSONArray jsonArray = data.getJSONArray("profiledate");
                for ( int i =0; i<jsonArray.length();i++){
                    JSONObject profileObj = jsonArray.getJSONObject(i);

                    ProfileModel profileModel = new ProfileModel();
                    profileModel.setName(profileObj.getString("name"));
                    profileModel.setV1(profileObj.getString("v1"));



                    if (profileModel.getName().equals("1")){
                        vatTVUPA.setText(profileModel.getV1());
                        if (profileModel.getV1().equals("")){
                            vatNumCV.setVisibility(View.GONE);
                        }
                    }

                    if (profileModel.getName().equals("2")){
                        dobTVUPA.setText(profileModel.getV1());
                        if (profileModel.getV1().equals("")){
                            dobCV.setVisibility(View.GONE);
                        }
                    }

                    if (profileModel.getName().equals("4")){
                        phoneTVUPA.setText(profileModel.getV1());
                        phoneCV.setVisibility(View.VISIBLE);
                        if (profileModel.getV1().equals("")){
                            phoneCV.setVisibility(View.VISIBLE);
                        }
                    }





                    if (profileModel.getName().equals("5")){
                        locationTVUPA.setText(""+profileModel.getV1());
                        if (profileModel.getV1().equals("")){
                            locationCV.setVisibility(View.GONE);
                        }
                    }
                    if (profileModel.getName().equals("6")){
                        countryTVUPA.setText(""+profileModel.getV1());
                        if (profileModel.getV1().equals("")){
                            countryCV.setVisibility(View.GONE);
                        }
                    }

                    if (profileModel.getName().equals("9")){
                        companyNameTVUPA.setText(profileModel.getV1());
                        SharedPreferencesSotreData.getInstance().setCompanyName(""+profileModel.getV1());
                        if (profileModel.getV1().equals("")){
                            companyNameCV.setVisibility(View.GONE);
                        }
                    }

                    if (profileModel.getName().equals("15")){
                        socialMediaTVUPA.setText(""+profileModel.getV1());
                        if (profileModel.getV1().equals("")){
                            socialMediaLinksCV.setVisibility(View.GONE);
                        }
                    }
                }


                ratingpoints = ratingdata.getString("ratingpoints");
                if (ratingpoints.equals("")||ratingpoints.equals("")){
                    ratingTV.setText("0");
                }
                else {
                    ratingTV.setText(""+ratingpoints.toString().trim());
                }
                Presenter presenter = new Presenter(this, this, this);
                presenter.setGetMethod(APIContract.USER_PROFILE_IMAGE+"/"+uuID, "getProfileImage");
                Log.d("getProfile : ", "" + APIContract.USER_PROFILE_IMAGE+"/"+uuID);


                userNameTVUPA.setText(""+name.toString().trim());

                Log.d("**USERID",""+APIContract.USER_PROFILE_IMAGE + "/"+uuID);

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

    private void showProgress(boolean show) {

        if (show) {
            mProgressDialog = new ProgressDialog(UserProfileActivity.this,
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
    protected void onResume() {
        initMembers();
        super.onResume();

    }
}