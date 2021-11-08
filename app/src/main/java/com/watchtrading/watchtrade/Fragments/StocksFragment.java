package com.watchtrading.watchtrade.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.watchtrading.watchtrade.Activities.ForumDetailActivity;
import com.watchtrading.watchtrade.Activities.StocksActivity;
import com.watchtrading.watchtrade.Adapters.BrandAdapter;
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

public class StocksFragment extends Fragment implements IPresenter, RequestViews, BrandAdapter.Clicks{

    private RecyclerView stocksRV;
    private BrandAdapter brandAdapter;
    private ProgressDialog mProgressDialog;
    private ArrayList<BrandModel> brandModelArrayList = new ArrayList<>();
    private Dialog blockDialog, showImageDialog;
    private RadioGroup radioGroupFilter;
    private RadioButton allCBAS, sellCBAS, buyCBAS;
    private Context context;
    private LinearLayout filterASLL;

    public StocksFragment(Context context) {
        this.context=context;
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (isNetworkConnected())  {
                        stoacksRequest();
                    }
                    else{
                        Toast.makeText(getActivity(), ""+getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    }

                }
            }, 500);
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_stocks, container, false);

        stocksRV = view.findViewById(R.id.stocksRV);
        radioGroupFilter = view.findViewById(R.id.radioGroupFilter);
        filterASLL = view.findViewById(R.id.filterASLL);
        allCBAS = view.findViewById(R.id.allCBAS);
        sellCBAS = view.findViewById(R.id.sellCBAS);
        buyCBAS = view.findViewById(R.id.buyCBAS);

        allCBAS.setChecked(true);


        radioGroupFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.allCBAS:
                        filter("Sell", "Buy");
                        break;
                    case R.id.sellCBAS:
                        filter("Sell", "sell");
                        break;
                    case R.id.buyCBAS:
                        filter("Buy","buy");
                        break;
                }
            }
        });

//        stoacksRequest();
        return view;
    }




    private void stoacksRequest() {
        Presenter presenter = new Presenter(this, context, this);
        presenter.setPostMethod(APIContract.USER_STOCK, creatStringObject(), "userStocks");
        Log.d("user_stocks:", "" + APIContract.USER_STOCK);

    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel createuserID = new ParmsModel();
        createuserID.setParmsTag("createuserID");
        createuserID.setParmsValue(""+SharedPreferencesSotreData.getInstance().getID());
        Log.d("**body", ""+createuserID.getParmsTag()+"--"+createuserID.getParmsValue());
        parmsList.add(createuserID);

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
                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                stoacksRequest();

            } else if (dataObject == false) {
                String message = jsonObject.getString("message");
                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                if (blockDialog.isShowing()) {
                    blockDialog.dismiss();
                }
            } else if (dataObject == false) {
                String message = jsonObject.getString("message");
                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
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
                brandModelArrayList.clear();
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

                brandAdapter = new BrandAdapter(brandModelArrayList, this, getActivity());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
                stocksRV.setLayoutManager(gridLayoutManager);
                stocksRV.setAdapter(brandAdapter);

                if (brandModelArrayList.size()==0){
                    stocksRV.setVisibility(View.GONE);
                    filterASLL.setVisibility(View.GONE);
                    Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                } else{
                    stocksRV.setVisibility(View.VISIBLE);
                    filterASLL.setVisibility(View.VISIBLE);
                }



            } else if (dataObject == false) {
                String message = jsonObject.getString("message");
                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getError(VolleyError error) {
        Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();

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
            mProgressDialog = new ProgressDialog(getActivity(),
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
        Presenter presenter = new Presenter(this, context, this);
        presenter.setPostMethod(APIContract.DELETE_POST, deleteRequest(postID), "deletePostReq");
        Log.d("block_unblock:", "" + APIContract.BLOCK_UNBLACK);

    }

    @Override
    public void showImage(int position, String imageURL) {
        showImageDialog = new Dialog(context);
        showImageDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        showImageDialog.getWindow().setGravity(Gravity.CENTER);
        showImageDialog.setCancelable(true);
        showImageDialog.setContentView(R.layout.show_pic_layout);

        ImageView imageViewSPL = showImageDialog.findViewById(R.id.imageViewSPL);

        Glide.with(getActivity())
                .load(imageURL)
                .placeholder(R.drawable.app_icon)
                .into(imageViewSPL);
        imageViewSPL.setOnTouchListener(new ImageMatrixTouchHandler(imageViewSPL.getContext()));

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
        blockDialog = new Dialog(context);
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
        Presenter presenter = new Presenter(this, context, this);
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

        brandAdapter = new BrandAdapter(brandModelArrayList, this, context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        stocksRV.setLayoutManager(gridLayoutManager);
        stocksRV.setAdapter(brandAdapter);


        List<BrandModel> productByLocations = new ArrayList<>();
        for (int i = 0; i < brandModelArrayList.size(); i++) {
            if (brandModelArrayList.get(i).getPostype().toString().equals("" + type)||brandModelArrayList.get(i).getPostype().toString().equals("" + trype)) {
                productByLocations.add(brandModelArrayList.get(i));
            }
        }
        brandAdapter.setProductList(productByLocations);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isNetworkConnected())  {
            stoacksRequest();
        }
        else{
            Toast.makeText(getActivity(), ""+getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }
}