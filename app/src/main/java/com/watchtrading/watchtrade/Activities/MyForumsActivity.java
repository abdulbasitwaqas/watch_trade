package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.watchtrading.watchtrade.Adapters.SearchAdapter;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Models.SearchModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.Utils.WatchTradeSingleton;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyForumsActivity extends AppCompatActivity implements IPresenter, RequestViews, SearchAdapter.Clicks {
    private ImageView backBtnMFA;
    private Spinner brandSpinnerSF, countiesSpinnerSF;
    private Button filterBtn;
    private RecyclerView forumsRV;
    private Dialog blockDialog;

    static int selectedCountry = -1;
    static int modelPosition = -1;

    public String[] countries = new String[]{"Country", "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
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

    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> countriesAdapter;
    private ProgressDialog mProgressDialog;
    private SearchAdapter searchAdapter;
    private String model_st = "", countryName = "";
    private Button sellBtn, buyBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_forums);

        initMembers();
    }

    private void initMembers() {
        backBtnMFA = findViewById(R.id.backBtnMFA);
        brandSpinnerSF = findViewById(R.id.brandSpinnerSF);
        countiesSpinnerSF = findViewById(R.id.countiesSpinnerSF);
        filterBtn = findViewById(R.id.filterBtn);
        forumsRV = findViewById(R.id.forumsRV);
        sellBtn = findViewById(R.id.sellBtn);
        buyBtn = findViewById(R.id.buyBtn);

        initRecyclerView();


        backBtnMFA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new ArrayAdapter<>(MyForumsActivity.this, R.layout.main_spinner_layout, WatchTradeSingleton.getSingletonInstance().getBrandsList());
        countriesAdapter = new ArrayAdapter<>(MyForumsActivity.this, R.layout.main_spinner_layout, countries);

        brandSpinnerSF.setAdapter(adapter);
        countiesSpinnerSF.setAdapter(countriesAdapter);

        brandSpinnerSF.setSelection(modelPosition);
        countiesSpinnerSF.setSelection(selectedCountry);


        brandSpinnerSF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**brandName", "" + brandSpinnerSF.getSelectedItem().toString());
                model_st = brandSpinnerSF.getSelectedItem().toString();

                modelPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        countiesSpinnerSF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**year", "" + countiesSpinnerSF.getSelectedItem().toString());
                countryName = countiesSpinnerSF.getSelectedItem().toString();

                selectedCountry = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(model_st, countryName);
            }
        });



        sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MyForumsActivity.this, AddNewWatchActivity.class);
                intent1.putExtra("type", "Sell");
                startActivity(intent1);
            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyForumsActivity.this, AddNewWatchActivity.class);
                intent.putExtra("type", "Buy");
                startActivity(intent);
            }
        });
    }
    private void initRecyclerView() {
        searchAdapter = new SearchAdapter(WatchTradeSingleton.getSingletonInstance().getSearchModelArrayList(), this, MyForumsActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyForumsActivity.this, 1);
        forumsRV.setLayoutManager(gridLayoutManager);
        forumsRV.setAdapter(searchAdapter);
    }

    public void filter(String model, String countryName) {
        List<SearchModel> productByModel = new ArrayList<>();
        for (SearchModel searchModel : WatchTradeSingleton.getSingletonInstance().getSearchModelArrayList()) {
            if (model.toLowerCase().contains(searchModel.getPostmake().toLowerCase()) || model.equals("")) {
                if (countryName.toLowerCase().equals("country") ||countryName.toLowerCase().contains(searchModel.getPostaddress().toString().toLowerCase())) {
                    productByModel.add(searchModel);
                }
            }
        }
        searchAdapter.setProductList(productByModel);
        if (productByModel.size() == 0) {
            Toast.makeText(MyForumsActivity.this, "No Data To Show", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void message(int position) {

    }

    @Override
    public void block(int position, String userID) {

        blockDialog(userID);

    }

    private void blockDialog(String userID) {
        blockDialog = new Dialog(MyForumsActivity.this);
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


    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("*blockReq", requestMessage);
        Log.d("*blockResponse", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {

                case "block":
                    blockReqResponse(jsonObject);
                    break;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void blockReqResponse(JSONObject jsonObject) {

        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
                String message = jsonObject.getString("message");
                Toast.makeText(MyForumsActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                if (blockDialog.isShowing()) {
                    blockDialog.dismiss();
                }
            } else if (dataObject == false) {
                String message = jsonObject.getString("message");
                Toast.makeText(MyForumsActivity.this, "" + message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyForumsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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
            mProgressDialog = new ProgressDialog(MyForumsActivity.this,
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