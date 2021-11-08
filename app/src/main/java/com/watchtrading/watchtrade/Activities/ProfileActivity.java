package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.watchtrading.watchtrade.Adapters.BrandAdapter;
import com.watchtrading.watchtrade.Adapters.StocksAdapter;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.BrandModel;
import com.watchtrading.watchtrade.Models.ParmsModel;
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

public class ProfileActivity extends AppCompatActivity implements IPresenter, RequestViews, StocksAdapter.ClicksStocks{
    private RecyclerView recyclerView;
    private List<BrandModel> brandModelArrayList = new ArrayList<>();
    private StocksAdapter stocksAdapter;
    private ImageView changePasswordIV;
    private CircleImageView userIV;
    private ProgressDialog mProgressDialog;
    private TextView userNameHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        recyclerView=findViewById(R.id.recyclerViewBDA);
        userIV=findViewById(R.id.userIV);
        changePasswordIV=findViewById(R.id.changePasswordIV);
        userNameHeader=findViewById(R.id.userNameHeader);

        userNameHeader.setText(SharedPreferencesSotreData.getInstance().getUserName());


        changePasswordIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        Glide.with(ProfileActivity.this)
                .load(APIContract.USER_PROFILE_IMAGE + "/"+SharedPreferencesSotreData.getInstance().getID())
                .placeholder(R.drawable.app_icon)
                .into(userIV);

        Log.d("*userID",""+APIContract.USER_PROFILE_IMAGE + "/"+SharedPreferencesSotreData.getInstance().getID());

        stoacksRequest();
    }

    private void stoacksRequest() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.USER_STOCK, creatStringObject(), "userStocks");
        Log.d("userStocks:  ", "" + APIContract.USER_STOCK);

    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();
        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("createuserID");
        parmsModel.setParmsValue(""+ SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);

        return parmsList;
    }


    @Override
    public void repost(int position) {

    }

    @Override
    public void block(int position) {

    }

    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**userStocksMsg", requestMessage);
        Log.d("userStocksResp", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "userStocks":
                    userStocksReq(jsonObject);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void userStocksReq(JSONObject jsonObject) {

        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                Log.d("**dataArray", ""+dataArray);
                for (int i =0; i<dataArray.length(); i ++){
                    JSONObject postObject = dataArray.getJSONObject(i);

                    BrandModel brandModel = new BrandModel();
                    brandModel.setPostID(postObject.getString("postID"));
                    brandModel.setCreateuserID(postObject.getString("createuserID"));
                    brandModel.setPaperwatch(postObject.getString("paperwatch"));
                    brandModel.setEnterprice(postObject.getString("enterprice"));
                    brandModel.setCasesize(postObject.getString("casesize"));
                    brandModel.setWatchbox(postObject.getString("watchbox"));
                    brandModel.setPostype(postObject.getString("postype"));
                    brandModel.setPostmake(postObject.getString("postmake"));
                    brandModel.setPostyear(postObject.getString("postyear"));
                    brandModel.setPostcountry(postObject.getString("postcountry"));
                    brandModel.setPostaddress(postObject.getString("postaddress"));
                    brandModel.setPostmodel(postObject.getString("postmodel"));
                    brandModel.setPoststatus(postObject.getString("poststatus"));
                    brandModel.setFile_name(postObject.getString("file_name"));
                    brandModel.setPostcreated(postObject.getString("postcreated"));
                    brandModel.setPostedupdated(postObject.getString("postedupdated"));

                    brandModelArrayList.add(brandModel);
                }
                stocksAdapter = new StocksAdapter(brandModelArrayList, ProfileActivity.this, ProfileActivity.this);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(ProfileActivity.this, 1);
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.setAdapter(stocksAdapter);
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
        Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();

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
            mProgressDialog = new ProgressDialog(ProfileActivity.this,
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