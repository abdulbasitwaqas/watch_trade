package com.watchtrading.watchtrade.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.watchtrading.watchtrade.Activities.MainActivity;
import com.watchtrading.watchtrade.Activities.MyForumsActivity;
import com.watchtrading.watchtrade.Adapters.ForumAdapter;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ForumModel;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Models.SearchModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.Utils.WatchTradeSingleton;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ForumFragment extends Fragment implements IPresenter, RequestViews, ForumAdapter.ControlsAdapterlistener {

    Context context;
    private ArrayList<ForumModel> forumModelArrayList = new ArrayList<>();
    ForumAdapter forumAdapter;
    private RecyclerView recyclerView;
    private Gson gson;
    private ProgressDialog mProgressDialog;
    private ConstraintLayout openForumCL;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (isNetworkConnected())  {
                        allPostsRequestCalling();
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


    public ForumFragment(Context context) {
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        recyclerView=view.findViewById(R.id.forumRV);
        openForumCL=view.findViewById(R.id.openForumCL);

        initRecyclerView();

        openForumCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForums = new Intent(context, MyForumsActivity.class);
                intentForums.putExtra("createuserID", ""+ SharedPreferencesSotreData.getInstance().getID());
                startActivity(intentForums);
            }
        });




        return view;
    }

    private void initRecyclerView() {
        forumAdapter = new ForumAdapter( WatchTradeSingleton.getSingletonInstance().getBrandsList(), this, context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(forumAdapter);
    }

    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**addToConRequestMsg", requestMessage);
        Log.d("**addToConRequestRes", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "addToConRequest":
                    addToConRequest(jsonObject);
                    break;

                case "allPostsResponse":
                    allPostsResponseProcess(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void allPostsResponseProcess(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
//                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                JSONArray postsArray = jsonObject.getJSONArray("posts");
                Log.d("**posts", ""+postsArray);
                for (int i =0; i<postsArray.length(); i ++){
                    JSONObject orderObject = postsArray.getJSONObject(i);

                    Type postType = new TypeToken<List<ForumModel>>() {
                    }.getType();

                    WatchTradeSingleton.
                            getSingletonInstance().
                            setForumModelArrayList((List<ForumModel>) gson.fromJson(orderObject.getString("posts"), postType));
                    Log.d("***checkDataOrder", "" + gson.toJson(WatchTradeSingleton.getSingletonInstance().getForumModelArrayList()));

                    Type searchType = new TypeToken<List<SearchModel>>() {
                    }.getType();

                    WatchTradeSingleton.
                            getSingletonInstance().
                            setSearchModelArrayList((List<SearchModel>) gson.fromJson(orderObject.getString("posts"), searchType));


//                    initMembers();
                }


            }
            else if (dataObject==false) {
                String message = jsonObject.getString("message");
                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void allPostsRequestCalling() {
        Presenter presenter = new Presenter(this, getActivity(), this);
        presenter.setGetMethod(APIContract.GET_ALL_POSTS,"allPostsResponse");
    }

    private List<ParmsModel> cratePostParams() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();
        parmsModel.setParmsTag("createuserID");
        parmsModel.setParmsValue(""+ SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);


        return parmsList;
    }

    private void addToConRequest(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
//                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
            }
            else if (dataObject==false) {
                String message = jsonObject.getString("message");
                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
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
            mProgressDialog = new ProgressDialog(context,
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
    public void addToContacts(View v, int position) {

        addToContactRequest(position);

    }

    private void addToContactRequest(int position) {
        Presenter presenter = new Presenter(this, context, this);
        presenter.setPostMethod(APIContract.ADD_TO_CONTACTS, creatStringObject(position), "addToConRequest");
        Log.d("ADD_TO_CONTACTS:::", "" + APIContract.ADD_TO_CONTACTS);
    }
    private List<ParmsModel> creatStringObject(int position) {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("createuserID");
        parmsModel.setParmsValue(""+ SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);

        ParmsModel password = new ParmsModel();
        password.setParmsTag("userID");
        password.setParmsValue(""+WatchTradeSingleton.getSingletonInstance().getForumModelArrayList().get(position).getCreateuserID());
        parmsList.add(password);

//        Toast.makeText(context, ""+WatchTradeSingleton.getSingletonInstance().getForumModelArrayList().get(position).getCreateuserID(), Toast.LENGTH_SHORT).show();

        return parmsList;
    }
}