package com.watchtrading.watchtrade.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.watchtrading.watchtrade.Activities.LogInActivity;
import com.watchtrading.watchtrade.Adapters.ChatsAdapter;
import com.watchtrading.watchtrade.Adapters.ContactsAdapter;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ChatsModel;
import com.watchtrading.watchtrade.Models.ContactsModel;
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

public class ContactsFragment extends Fragment implements IPresenter, RequestViews, ContactsAdapter.BlockClick {

    private Context context;
    private ArrayList<ContactsModel> contactsModelArrayList = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private RecyclerView recyclerView;
    private ImageView menuCF;
    private ProgressDialog mProgressDialog;
    private Gson gson;

    public ContactsFragment(Context context) {
        this.context = context;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                        if (isNetworkConnected())  {
                            GET_CONTACT();
                        }
                        else{
                            Toast.makeText(getActivity(), ""+getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    }

                }
            }, 500);
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

//        menuCF = view.findViewById(R.id.menuCF);
        recyclerView = view.findViewById(R.id.contactsRV);




        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();


        return view;
    }


    private void GET_CONTACT() {


        Presenter presenter = new Presenter(this, context, this);
        presenter.setPostMethod(APIContract.GET_CONTACT, creatStringObject(), "getContactRequest");
        Log.d("get_contact::", "" + APIContract.GET_CONTACT);
    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();
        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("createuserID");
        parmsModel.setParmsValue(""+SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);

        return parmsList;
    }

    @Override
    public void getResponse(String response, String requestMessage) {

        Log.d("**contactReqMsg", requestMessage);
        Log.d("**contactReqResponse", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "getContactRequest":
                    getContacts(jsonObject);
                    break;

                case "unblock":
                    unblockReqResponse(jsonObject);
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private void unblockReqResponse(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
//                String message = jsonObject.getString("message");
                Toast.makeText(context, "User Unblocked Successfully", Toast.LENGTH_SHORT).show();
                GET_CONTACT();
            }
            else if (dataObject == false) {
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

    private void getContacts(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
//                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                Type contactsType = new TypeToken<List<ContactsModel>>() {
                }.getType();

                List<ContactsModel> contactsModelList = gson.fromJson(jsonObject.getString("data"), contactsType);
                WatchTradeSingleton.getSingletonInstance().setContactsModelList(contactsModelList);
                Log.d("contactsModelList", ""+WatchTradeSingleton.getSingletonInstance().getContactsModelList());
//                Toast.makeText(context, "Contact List Loaded", Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.VISIBLE);

            }
            else if (dataObject==false) {
                String message = jsonObject.getString("message");
                recyclerView.setVisibility(View.GONE);
                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


            contactsAdapter = new ContactsAdapter(WatchTradeSingleton.getSingletonInstance().getContactsModelList(), context, this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(contactsAdapter);


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


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void block(int position, String userID) {

        Presenter presenter = new Presenter(this, context, this);
        presenter.setPostMethod(APIContract.BLOCK_UNBLACK, blockRequest(userID), "unblock");
        Log.d("block_unblock:", "" + APIContract.BLOCK_UNBLACK);

    }

    private List<ParmsModel> blockRequest(String userID) {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel addModel = new ParmsModel();
        addModel.setParmsTag("type");
        addModel.setParmsValue("remove");
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

    @Override
    public void onResume() {
        super.onResume();
//        GET_CONTACT();
    }
}