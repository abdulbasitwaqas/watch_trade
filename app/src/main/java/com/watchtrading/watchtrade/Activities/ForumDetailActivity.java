package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.watchtrading.watchtrade.Adapters.BrandAdapter;
import com.watchtrading.watchtrade.Adapters.ChatsAdapter;
import com.watchtrading.watchtrade.Adapters.CustomAdapter;
import com.watchtrading.watchtrade.Adapters.ForumAdapter;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.BrandModel;
import com.watchtrading.watchtrade.Models.ChatsModel;
import com.watchtrading.watchtrade.Models.ForumModel;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Models.SearchModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.CurrencyHelper;
import com.watchtrading.watchtrade.Utils.LanguagesExtension;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.Utils.WatchTradeSingleton;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ForumDetailActivity extends AppCompatActivity implements IPresenter, RequestViews, BrandAdapter.Clicks {


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
    private ArrayList<BrandModel> brandModelArrayList = new ArrayList<>();
    private BrandAdapter brandAdapter;
    private RecyclerView recyclerView;
    private ImageView backBtnFDA;
    private Spinner selectCountrySpinnerFDA;
    private TextView noDataToShowTV;
    private Dialog dialog, blockDialog, showImageDialog;
    private String inputLanguageCode;
    private String outputLanguageCode;
    String languageInput = "";
    String languageOutput = "";
    private EditText exchangeToET;
    private TextView exchangeFromET;


    static int selectedItem = -1;
    private ArrayAdapter<String> countryadpter;
    private String country_st;
    private String brandIntent;
    private Button sellBtn, buyBtn;
    private ProgressDialog mProgressDialog;
    private TextView headerTVFDA;
    private ConstraintLayout AFDCL;
    private ImageView currencyExchangeIVFDA;
    private Spinner spinnerFrom, spinnerTo;
    List<com.watchtrading.watchtrade.Models.Currency> currencyList;
    private List<String> list;
    private CurrencyHelper currencyHelper;
    private int to = 0;
    private int from = 1;
    private LinearLayout spinnerCLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);

        final Intent intent = getIntent();

        brandIntent = intent.getStringExtra("brand");
        brandDetailsRequest();


        noDataToShowTV = findViewById(R.id.noDataToShowTV);
        currencyExchangeIVFDA = findViewById(R.id.currencyExchangeIVFDA);
        selectCountrySpinnerFDA = findViewById(R.id.selectCountrySpinnerFDA);
        AFDCL = findViewById(R.id.AFDCL);
        spinnerCLL = findViewById(R.id.spinnerCLL);

        currencyHelper = new CurrencyHelper(this);
        currencyList = currencyHelper.getAndParseLanguaues();

        list = LanguagesExtension.Companion.getStringsArray(currencyList);

        currencyExchangeIVFDA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printDialogShow();
            }
        });

        recyclerView = findViewById(R.id.recyclerViewFDA);
        backBtnFDA = findViewById(R.id.backBtnFDA);
        sellBtn = findViewById(R.id.sellBtn);
        buyBtn = findViewById(R.id.buyBtn);
        headerTVFDA = findViewById(R.id.headerTVFDA);

        headerTVFDA.setText("" + brandIntent);


        recyclerView.setVisibility(View.GONE);
        noDataToShowTV.setVisibility(View.GONE);


        backBtnFDA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sellBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ForumDetailActivity.this, AddNewWatchActivity.class);
                intent1.putExtra("type", "Sell");
                startActivity(intent1);
            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumDetailActivity.this, AddNewWatchActivity.class);
                intent.putExtra("type", "Buy");
                startActivity(intent);
            }
        });

        countryadpter = new ArrayAdapter<>(ForumDetailActivity.this, R.layout.main_spinner_layout, countries);
        selectCountrySpinnerFDA.setAdapter(countryadpter);
        selectCountrySpinnerFDA.setSelection(selectedItem);


        selectCountrySpinnerFDA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**year", "" + selectCountrySpinnerFDA.getSelectedItem().toString());
                country_st = selectCountrySpinnerFDA.getSelectedItem().toString();
                selectedItem = position;
                filter(country_st);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void printDialogShow() {
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.currency_exchange_layout);

        exchangeToET = dialog.findViewById(R.id.exchangeToET);
        exchangeFromET = dialog.findViewById(R.id.exchangeFromET);
        spinnerTo = dialog.findViewById(R.id.exchangeToSpinner);
        spinnerFrom = dialog.findViewById(R.id.exchangeFromSpinner);
        ImageView cancelDialogIV = dialog.findViewById(R.id.cancelDialogIV);
        Button convertBtn = dialog.findViewById(R.id.convertBtn);

        ArrayAdapter inputLanguageAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item_new, list);

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), list);

        spinnerTo.setAdapter(customAdapter);
        spinnerFrom.setAdapter(customAdapter);

        inputLanguageAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

        spinnerTo.setSelection(to);
        spinnerFrom.setSelection(from);

        cancelDialogIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        convertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertReq();
            }
        });


        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                inputLanguageCode = currencyList.get(position).getCode();
                languageInput = currencyList.get(position).getName();

                to = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
//                Log.d("**name", "" + spinnerFrom.getSelectedItem().toString());
//                currencyFrom = spinnerTo.getSelectedItem().toString();
                outputLanguageCode = currencyList.get(position).getCode();
                languageOutput = currencyList.get(position).getName();


                from = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        dialog.show();
    }

    private void convertReq() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.FOREX_CONVERTER, createForexCon(), "forexReq");
        Log.d("**forexURL", "" + APIContract.FOREX_CONVERTER);
    }

    private List<ParmsModel> createForexCon() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("currency_from");
        parmsModel.setParmsValue("" + inputLanguageCode);
        parmsList.add(parmsModel);

        ParmsModel password = new ParmsModel();
        password.setParmsTag("currency_to");
        password.setParmsValue("" + outputLanguageCode);
        parmsList.add(password);

        ParmsModel fbtoken = new ParmsModel();
        fbtoken.setParmsTag("convert_amount");
        fbtoken.setParmsValue("" + exchangeToET.getText().toString());
        parmsList.add(fbtoken);

        return parmsList;
    }

    private void brandDetailsRequest() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.ALL_BRANDS, creatStringObject(), "forumDetailsReq");
        Log.d("all_brands:", "" + APIContract.ALL_BRANDS);
    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();
        parmsModel.setParmsTag("brand");
        parmsModel.setParmsValue(brandIntent);
        parmsList.add(parmsModel);
        ParmsModel createuserIDModel = new ParmsModel();
        createuserIDModel.setParmsTag("createuserID");
        createuserIDModel.setParmsValue(SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(createuserIDModel);


        return parmsList;
    }


    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**forumDetailsReq", requestMessage);
        Log.d("**forumDetailsReq", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "forumDetailsReq":
                    forumDetailsProcess(jsonObject);
                    break;

                case "forexReq":
                    forexReqResponse(jsonObject);
                    break;

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

    private void forexReqResponse(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                JSONObject currencyJsonData = jsonObject.getJSONObject("currencyJsonData");
                String converted_amount = currencyJsonData.getString("converted_amount");

                exchangeFromET.setText("" + converted_amount);


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

    private void forumDetailsProcess(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
                String message = jsonObject.getString("message");
//                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                JSONArray postsArray = jsonObject.getJSONArray("posts");
                Log.d("**posts", "" + postsArray);
                for (int i = 0; i < postsArray.length(); i++) {
                    JSONObject orderObject = postsArray.getJSONObject(i);
                    JSONArray jsonArray = orderObject.getJSONArray("posts");
                    if (jsonArray.equals("[]") || jsonArray.length() == 0) {

                        recyclerView.setVisibility(View.GONE);
                        AFDCL.setVisibility(View.VISIBLE);
                        spinnerCLL.setVisibility(View.GONE);
                        noDataToShowTV.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        AFDCL.setVisibility(View.VISIBLE);
                        spinnerCLL.setVisibility(View.VISIBLE);
                        noDataToShowTV.setVisibility(View.GONE);


                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject postObject = jsonArray.getJSONObject(j);

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
                        brandAdapter = new BrandAdapter(brandModelArrayList, ForumDetailActivity.this, ForumDetailActivity.this);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(ForumDetailActivity.this, 1);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setAdapter(brandAdapter);
                    }


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


    public void filter(String country) {

        brandAdapter = new BrandAdapter(brandModelArrayList, ForumDetailActivity.this, ForumDetailActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ForumDetailActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(brandAdapter);


        List<BrandModel> productByLocations = new ArrayList<>();
        for (int i = 0; i < brandModelArrayList.size(); i++) {
            if (brandModelArrayList.get(i).getPostcountry().toLowerCase().contains(country.toLowerCase())  ||brandModelArrayList.get(i).getPostaddress().toLowerCase().contains(country.toLowerCase()) || country.toLowerCase().contains("country")) {
                productByLocations.add(brandModelArrayList.get(i));
            }
        }
        brandAdapter.setProductList(productByLocations);
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
            mProgressDialog = new ProgressDialog(ForumDetailActivity.this,
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
    public void delete(int position, String userID) {

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
        Glide.with(ForumDetailActivity.this)
                .load(imageURL)
                .placeholder(R.drawable.app_icon)
                .into(imageViewSPL);


        showImageDialog.show();
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


    @Override
    protected void onResume() {
        super.onResume();
//        brandDetailsRequest();
    }
}