package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
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

public class StocksActivity extends AppCompatActivity implements IPresenter, RequestViews, BrandAdapter.Clicks {
    private RecyclerView stocksRV;
    private ImageView backBtnSTA;
    private BrandAdapter brandAdapter;
    private ProgressDialog mProgressDialog;
    private String createuserID;
    private ArrayList<BrandModel> brandModelArrayList = new ArrayList<>();
    private Dialog blockDialog, showImageDialog;
    private RadioGroup radioGroupFilter;
    private RadioButton allCBAS, sellCBAS, buyCBAS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        backBtnSTA = findViewById(R.id.backBtnSTA);
        stocksRV = findViewById(R.id.stocksRV);
        radioGroupFilter = findViewById(R.id.radioGroupFilter);
        allCBAS = findViewById(R.id.allCBAS);
        sellCBAS = findViewById(R.id.sellCBAS);
        buyCBAS = findViewById(R.id.buyCBAS);

        createuserID = getIntent().getStringExtra("createuserID");

        stoacksRequest();


        backBtnSTA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        radioGroupFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.allCBAS:
                        filter("Sell","Buy");
                        break;
                    case R.id.sellCBAS:
                        filter("Sell","sell");
                        break;
                    case R.id.buyCBAS:
                        filter("Buy","buy");
                        break;

                }
            }
        });
    }

    private void stoacksRequest() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.USER_STOCK, creatStringObject(), "userStocks");
        Log.d("nueurl", "" + APIContract.USER_STOCK);

    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("createuserID");
        parmsModel.setParmsValue("" + createuserID);
        parmsList.add(parmsModel);

        return parmsList;
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
                case "block":
                    blockReqResponse(jsonObject);
                    break;
                case "deletePostReq":
                    deletePostResponse(jsonObject);
                    break;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void deletePostResponse(JSONObject jsonObject) {

        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
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


    private void blockReqResponse(JSONObject jsonObject) {

        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                if (blockDialog.isShowing()) {
                    blockDialog.dismiss();
                }
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


    private void userStocksReq(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
                JSONArray dataArray = jsonObject.getJSONArray("data");
                Log.d("**dataArray", "" + dataArray);
                for (int i = 0; i < dataArray.length(); i++) {
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
                brandAdapter = new BrandAdapter(brandModelArrayList, StocksActivity.this, StocksActivity.this);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(StocksActivity.this, 1);
                stocksRV.setLayoutManager(gridLayoutManager);
                stocksRV.setAdapter(brandAdapter);

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
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();

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
            mProgressDialog = new ProgressDialog(StocksActivity.this,
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
    public void addToContacts(int position) {

    }

    @Override
    public void message(int position) {

    }

    @Override
    public void block(int position, String userID) {

        blockDialog(userID);

    }

    @Override
    public void delete(int position, String postID) {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.DELETE_POST, deleteRequest(postID), "deletePostReq");
        Log.d("block_unblock:", "" + APIContract.BLOCK_UNBLACK);
    }

    @Override
    public void showImage(int position, String imageURL) {
        showImageDialog = new Dialog(this);
        showImageDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        showImageDialog.getWindow().setGravity(Gravity.CENTER);
        showImageDialog.setCancelable(true);
        showImageDialog.setContentView(R.layout.show_pic_layout);

        ImageView imageViewSPL = showImageDialog.findViewById(R.id.imageViewSPL);
        imageViewSPL.setOnTouchListener(new ImageMatrixTouchHandler(imageViewSPL.getContext()));
        Glide.with(StocksActivity.this)
                .load(imageURL)
                .placeholder(R.drawable.app_icon)
                .into(imageViewSPL);


        showImageDialog.show();
    }

    private List<ParmsModel> deleteRequest(String postID) {

        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel postIDModel = new ParmsModel();
        postIDModel.setParmsTag("postID");
        postIDModel.setParmsValue(postID);
        parmsList.add(postIDModel);

        return parmsList;
    }

    private void blockDialog(String userID) {
        blockDialog = new Dialog(this);
        blockDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        blockDialog.getWindow().setGravity(Gravity.CENTER);
        blockDialog.setCancelable(false);
        blockDialog.setContentView(R.layout.block_user_layout);

        Button confirmBlockBtn = blockDialog.findViewById(R.id.confirmBlockBtn);
        Button cancelBlockBtn = blockDialog.findViewById(R.id.cancelBlockBtn);

        confirmBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockAPIRequest(userID);
            }
        });
        cancelBlockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockDialog.dismiss();
            }
        });


        blockDialog.show();
    }

    private void blockAPIRequest(String userID) {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.BLOCK_UNBLACK, blockRequest(userID), "block");
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
        cuidModel.setParmsValue("" + SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(cuidModel);

        ParmsModel uidModel = new ParmsModel();
        uidModel.setParmsTag("blockuserID");
        uidModel.setParmsValue("" + userID);
        parmsList.add(uidModel);

        return parmsList;
    }


    public void filter(String type, String trype) {

        brandAdapter = new BrandAdapter(brandModelArrayList, StocksActivity.this, StocksActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(StocksActivity.this, 1);
        stocksRV.setLayoutManager(gridLayoutManager);
        stocksRV.setAdapter(brandAdapter);


        List<BrandModel> productByLocations = new ArrayList<>();
        for (int i = 0; i < brandModelArrayList.size(); i++) {
            if (brandModelArrayList.get(i).getPostype().toLowerCase().contains(type.toLowerCase()) || brandModelArrayList.get(i).getPostype().toLowerCase().contains(trype.toLowerCase())) {
                productByLocations.add(brandModelArrayList.get(i));
            }
        }
        brandAdapter.setProductList(productByLocations);
    }

}