package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.watchtrading.watchtrade.Adapters.RatingListAdapter;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Models.RatingListModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RatingListActivity extends AppCompatActivity implements IPresenter, RequestViews, RatingListAdapter.Clicks {
    private RecyclerView ratingListRV;
    private ImageView backBtnRLA;
    private String ratingUserID ="", ratingpoints="";
    private Gson gson;
    private RatingListAdapter ratingListAdapter;
    private Dialog dialog;
    private EditText reviewET;
    private ProgressDialog mProgressDialog;
    private List<RatingListModel> ratingListModelList = new ArrayList<>();
    private TextView averageRattingTV;
    private ImageView starOne, starTwo, starThree, starfour, starfive;
    private Button submitBtn;
    private EditText ratingMessage;
    private int ratingTV=0;
    private ConstraintLayout ratingLayout;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_list);


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();



        ratingUserID = getIntent().getStringExtra("createuserID");
        ratingpoints = getIntent().getStringExtra("ratingpoints");



//        Toast.makeText(this, ""+ratingpoints, Toast.LENGTH_SHORT).show();


        ratingListRV = findViewById(R.id.ratingListRV);
        averageRattingTV = findViewById(R.id.averageRattingTV);

        averageRattingTV.setText(ratingpoints+"/5");

        backBtnRLA = findViewById(R.id.backBtnRLA);
        ratingLayout = findViewById(R.id.ratingLayout);





        backBtnRLA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userRatingRequest();


        if (SharedPreferencesSotreData.getInstance().getID().equals(""+ratingUserID)){
            ratingLayout.setVisibility(View.GONE);
        }
        else {
            ratingLayout.setVisibility(View.VISIBLE);
        }


        starOne=findViewById(R.id.starOne);
        starTwo=findViewById(R.id.starTwo);
        starThree=findViewById(R.id.starThree);
        starfour=findViewById(R.id.starfour);
        starfive=findViewById(R.id.starfive);


        submitBtn=findViewById(R.id.submitBtn);
        ratingMessage=findViewById(R.id.ratingMessage);

        starOne.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
        starTwo.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
        starThree.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
        starfour.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));
        starfive.setBackground(getResources().getDrawable(R.drawable.ic_star_unselcted));



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
                    Toast.makeText(RatingListActivity.this, ""+getResources().getString(R.string.star_rating), Toast.LENGTH_SHORT).show();
                } else if (ratingUserID.equals("")) {
                    Toast.makeText(RatingListActivity.this, ""+getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                } else {
                    ratingRequest();
                }

            }
        });


    }

    private void ratingRequest() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.ADD_RATING, creatRatingReqObject(), "ratingRequest");
        Log.d("nueurl", "" + APIContract.ADD_RATING);
    }

    private List<ParmsModel> creatRatingReqObject() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("ratinguserID");
        parmsModel.setParmsValue(ratingUserID);
        parmsList.add(parmsModel);

        ParmsModel password = new ParmsModel();
        password.setParmsTag("createuserID");
        password.setParmsValue(""+SharedPreferencesSotreData.getInstance().getID());
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

    private void userRatingRequest() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.USER_RATING, creatStringObjects(), "userRating");
        Log.d("userRating", "" + APIContract.USER_RATING);
    }

    private List<ParmsModel> creatStringObjects() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("ratinguserID");
        parmsModel.setParmsValue(""+ ratingUserID);
        parmsList.add(parmsModel);

        return parmsList;
    }

    @Override
    public void getResponse(String response, String requestMessage) {

        Log.d("**userRatingMsg", requestMessage);
        Log.d("**userRatingResp", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "userRating":
                    userRating(jsonObject);
                    break;
                case "reviewSubmitReq":
                    reviewSubmitResp(jsonObject);
                    break;

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

    private void reviewSubmitResp(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
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

    private void userRating(JSONObject jsonObject) {

        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
                JSONArray jsonArray = jsonObject.getJSONArray("reviews");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject reviewObject = jsonArray.getJSONObject(i);
                    JSONArray postsArray = reviewObject.getJSONArray("posts");

                    for (int j = 0; j < postsArray.length(); j++) {
                        JSONObject postsObject = postsArray.getJSONObject(j);

                        RatingListModel ratingListModel = new RatingListModel();


                        String ratingID = postsObject.getString("ratingID");
                        String createuserID = postsObject.getString("createuserID");
                        String ratinguserID = postsObject.getString("ratinguserID");
                        String ratingpoints = postsObject.getString("ratingpoints");
                        String reviewmessage = postsObject.getString("reviewmessage");
                        String created = postsObject.getString("created");
                        String name = postsObject.getString("name");

                        ratingListModel.setRatingID(ratingID);
                        ratingListModel.setCreateuserID(createuserID);
                        ratingListModel.setRatinguserID(ratinguserID);
                        ratingListModel.setRatingpoints(ratingpoints);
                        ratingListModel.setReviewmessage(reviewmessage);
                        ratingListModel.setCreated(created);
                        ratingListModel.setName(name);

                        ratingListModelList.add(ratingListModel);


//                    }


                /*    Type ratingType = new TypeToken<List<RatingListModel>>() {
                    }.getType();
                    List<RatingListModel> ratingListModelList = gson.fromJson(reviewObject.getString("posts"), ratingType);
                    Log.d("*posts", ""+gson.fromJson(reviewObject.getString("posts"), ratingType));
                    Log.d("*watchposts", ""+WatchTradeSingleton.getSingletonInstance().getRaingModelList());
                    WatchTradeSingleton.getSingletonInstance().setRaingModelList(ratingListModelList);

*/

//                        ratingListAdapter = new RatingListAdapter(WatchTradeSingleton.getSingletonInstance().getRaingModelList(), this, this);
//                        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
//                        ratingListRV.setLayoutManager(gridLayoutManager);
//                        ratingListRV.setAdapter(ratingListAdapter);

                    }
                }
            } else if (dataObject == false) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            ratingListAdapter = new RatingListAdapter(ratingListModelList, this, this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
            ratingListRV.setLayoutManager(gridLayoutManager);
            ratingListRV.setAdapter(ratingListAdapter);

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

    @Override
    public void ratingReview(View view, String ratindID) {


        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.review_layout);
        reviewET = dialog.findViewById(R.id.reviewETT);
        ImageView cancelDialogIVV = dialog.findViewById(R.id.cancelDialogIVV);
        Button submitReviewBtn = dialog.findViewById(R.id.submitReviewBtn);

        cancelDialogIVV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        submitReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reviewET.equals("")){
                    reviewET.setError(getResources().getString(R.string.review_message));
                }
                else {
                    reviewSubmitReq(ratindID);
                }
            }
        });


        dialog.show();
    }

    @Override
    public void ratingData(int totalRating, int totalPersons) {
        Log.d("**ratings", +totalRating+"    :TOTAL RATING"+"                             totalPersons:     "+totalPersons);
    }

    private void reviewSubmitReq(String ratindID) {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.ADD_RATING_REPORT, creatStringObject(ratindID), "reviewSubmitReq");
        Log.d("addRatingR :", "" + APIContract.ADD_RATING_REPORT);
    }

    private List<ParmsModel> creatStringObject(String ratindID) {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("createuserID");
        parmsModel.setParmsValue(SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);

        ParmsModel password = new ParmsModel();
        password.setParmsTag("ratingID");
        password.setParmsValue(""+ratindID);
        parmsList.add(password);

        ParmsModel ratingpoints = new ParmsModel();
        ratingpoints.setParmsTag("post_reportmessage");
        ratingpoints.setParmsValue(""+reviewET.getText().toString().trim());
        parmsList.add(ratingpoints);


        return parmsList;
    }
    private void showProgress(boolean show) {

        if (show) {
            mProgressDialog = new ProgressDialog(RatingListActivity.this,
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