package com.watchtrading.watchtrade.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;

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
import com.watchtrading.watchtrade.Adapters.ChatsAdapter;
import com.watchtrading.watchtrade.Adapters.ContactsAdapter;
import com.watchtrading.watchtrade.Adapters.ForumAdapter;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ChatsModel;
import com.watchtrading.watchtrade.Models.ContactsModel;
import com.watchtrading.watchtrade.Models.ForumModel;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.Utils.WatchTradeSingleton;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment implements IPresenter, RequestViews, ChatsAdapter.DeleteChat {

    private Context context;
    private ArrayList<ChatsModel> chatsModelArrayList = new ArrayList<>();
    private ChatsAdapter chatsAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog mProgressDialog;
    private Gson gson;

    public ChatsFragment(Context context) {
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
                        getChats();
                    }
                    else{
                        Toast.makeText(getActivity(), ""+getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    }

                }
            }, 500);
        }

    }

    private void getChats() {
        Presenter presenter = new Presenter(this, context, this);
        presenter.setPostMethod(APIContract.USER_CHAT, creatStringObject(), "chatReq");
        Log.d("chatReq:", "" + APIContract.USER_CHAT);
    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();
        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("userID");
        parmsModel.setParmsValue(""+ SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);

        return parmsList;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chats, container, false);


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();

        recyclerView=view.findViewById(R.id.chatsRV);

        return view;
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


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void getResponse(String response, String requestMessage) {

        Log.d("*chatReqMsg", requestMessage);
        Log.d("*chatReqResponse", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "chatReq":
                    chatReq(jsonObject);
                    break;
                case "delChat":
                    delChatRes(jsonObject);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void chatReq(JSONObject jsonObject) {

        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
//                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                Type chatsType = new TypeToken<List<ChatsModel>>() {
                }.getType();

                List<ChatsModel> contactsModelList = gson.fromJson(jsonObject.getString("messages"), chatsType);
                WatchTradeSingleton.getSingletonInstance().setChatsModelList(contactsModelList);
                recyclerView.setVisibility(View.VISIBLE);

            }
            else if (dataObject==false) {
                String message = jsonObject.getString("message");
                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.GONE);
            }
            else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


            chatsAdapter = new ChatsAdapter(WatchTradeSingleton.getSingletonInstance().getChatsModelList(), context, this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(chatsAdapter);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delChatRes(JSONObject jsonObject) {

        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                getChats();

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

    @Override
    public void delChat(int position, String userID) {
        Presenter presenter = new Presenter(this, context, this);
        presenter.setPostMethod(APIContract.DELETE_CHAT, deleteChatParams(userID), "delChat");
        Log.d("**delChat", "" + APIContract.DELETE_CHAT);
    }

    private List<ParmsModel> deleteChatParams(String userID) {

        List<ParmsModel> parmsList = new ArrayList<>();
        
        
        ParmsModel parmsModel = new ParmsModel();
        parmsModel.setParmsTag("getuserID");
        parmsModel.setParmsValue(""+ SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);
        
        ParmsModel creatUserIDM = new ParmsModel();
        creatUserIDM.setParmsTag("createuserID");
        creatUserIDM.setParmsValue(userID);
        parmsList.add(creatUserIDM);

        return parmsList;
    }
}