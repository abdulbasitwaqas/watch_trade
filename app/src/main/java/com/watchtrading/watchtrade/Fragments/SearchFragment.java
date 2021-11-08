package com.watchtrading.watchtrade.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.watchtrading.watchtrade.Activities.ForumDetailActivity;
import com.watchtrading.watchtrade.Adapters.BrandAdapter;
import com.watchtrading.watchtrade.Adapters.ForumAdapter;
import com.watchtrading.watchtrade.Adapters.SearchAdapter;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.BrandModel;
import com.watchtrading.watchtrade.Models.ContinentModel;
import com.watchtrading.watchtrade.Models.CountriesModel;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SearchFragment extends Fragment implements IPresenter, RequestViews, SearchAdapter.Clicks {

    private Context context;
    private RecyclerView searchRV;
    private List<SearchModel> searchModelArrayList = new ArrayList<>();
    private SearchAdapter searchAdapter;
    private String model_st = "", continentID = "", continentName = "", countryID = "", countryName = "", year_st = "", box = "", paper = "", checkedBox = "",
            checkedPaper = "", st_modelName = "", st_caseSize = "", st_sell = "", st_buy = "";
    private Spinner brandSpinnerSF, yearSpinnerSF, continetSpinnerSF, countiesSpinnerSF;
    private CheckBox boxCheckBoxSF, paperCheckBoxSF;

    private ArrayList<BrandModel> brandModelArrayList = new ArrayList<>();
    private BrandAdapter brandAdapter;

    private ArrayList<ContinentModel> continentModelArrayList = new ArrayList<>();
    private ArrayList<CountriesModel> countriesModelArrayList = new ArrayList<>();

    private String[] continents = new String[]{"Africa", "North America", "Oceania", "Antarctica", "Europe", "Asia", "South America"};

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

    private String[] brands = new String[]{"Vintage", "Rolex", "Audimars Piguet", "Richard Milli", "Patek Phillippe", "Cartier", "Omega", "Breightling", "Vacheron Constantine", "Panerai",
            "IWC", "Tudor", "Assorted"};

    private String[] year = new String[]{"2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011",
            "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021"};

    static int selectedContinent = -1;
    static int selectedCountry = -1;
    static int modelPosition = -1;
    static int yearsPosition = -1;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter continentAdapter;
    private ArrayAdapter<String> countriesAdapter;
    private ArrayAdapter<String> yearAdapter;
    private LinearLayout searchingFilterCL;

    private SearchFragment searchFragment;
    private Button filterBtn;
    private ProgressDialog mProgressDialog;
    private Dialog blockDialog;
    private EditText modelET, caseSizeET;
    private CheckBox sellCBSF, buyCBSF;


    public SearchFragment(Context context) {
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchRV = view.findViewById(R.id.searchRV);


        initRecyclerView();

        brandSpinnerSF = view.findViewById(R.id.brandSpinnerSF);
        searchingFilterCL = view.findViewById(R.id.searchingFilterCL);
        searchingFilterCL.setVisibility(View.VISIBLE);
        yearSpinnerSF = view.findViewById(R.id.yearSpinnerSF);
        continetSpinnerSF = view.findViewById(R.id.continetSpinnerSF);
        countiesSpinnerSF = view.findViewById(R.id.countiesSpinnerSF);

        boxCheckBoxSF = view.findViewById(R.id.boxCheckBoxSF);
        paperCheckBoxSF = view.findViewById(R.id.paperCheckBoxSF);
        filterBtn = view.findViewById(R.id.filterBtn);
        modelET = view.findViewById(R.id.modelET);
        caseSizeET = view.findViewById(R.id.caseSizeET);
        sellCBSF = view.findViewById(R.id.sellCBSF);
        buyCBSF = view.findViewById(R.id.buyCBSF);


        adapter = new ArrayAdapter<>(context, R.layout.main_spinner_layout, brands);
        continentAdapter = new ArrayAdapter<>(context, R.layout.main_spinner_layout, continents);
        countriesAdapter = new ArrayAdapter<>(context, R.layout.main_spinner_layout, countries);
        yearAdapter = new ArrayAdapter<>(context, R.layout.main_spinner_layout, year);
//        yearAdapter = new ArrayAdapter<>(context, R.layout.main_spinner_layout, year);


        brandSpinnerSF.setAdapter(adapter);
        countiesSpinnerSF.setAdapter(countriesAdapter);
        yearSpinnerSF.setAdapter(yearAdapter);
        continetSpinnerSF.setAdapter(continentAdapter);


        brandSpinnerSF.setSelection(modelPosition);
        countiesSpinnerSF.setSelection(selectedCountry);
        yearSpinnerSF.setSelection(yearsPosition);
        continetSpinnerSF.setSelection(selectedContinent);


        String papers = "yes";
        String checked = "checked";
        String emptyy = "";

        boxCheckBoxSF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    box = papers;
                    checkedBox = checked;
                } else {
                    box = emptyy;
                    checkedBox = emptyy;
                }
            }
        });

        sellCBSF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    st_sell = "sell";
                } else {
                    st_sell = "";
                }
            }
        });

        buyCBSF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    st_buy = "buy";
                } else {
                    st_buy = "";
                }
            }
        });

        paperCheckBoxSF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    paper = papers;
                    checkedPaper = checked;
                } else {
                    paper = emptyy;
                    checkedPaper = emptyy;
                }
            }
        });


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
        yearSpinnerSF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**year", "" + yearSpinnerSF.getSelectedItem().toString());
                year_st = yearSpinnerSF.getSelectedItem().toString();

                yearsPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        continetSpinnerSF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("selectedContinent", "" + continetSpinnerSF.getSelectedItem().toString());
                continentName = continetSpinnerSF.getSelectedItem().toString();
                if (continetSpinnerSF.getSelectedItemPosition() == 0) {
                    countiesSpinnerSF.setVisibility(View.GONE);
                } else {
                    countiesSpinnerSF.setVisibility(View.VISIBLE);
                }

                selectedContinent = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        countiesSpinnerSF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("selectedCountry", "" + countiesSpinnerSF.getSelectedItem().toString());
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

                st_modelName = modelET.getText().toString().toString();
                st_caseSize = caseSizeET.getText().toString().toString();


                filter(model_st, box, checkedBox, paper, checkedPaper, countryName, st_modelName, st_caseSize, st_sell, st_buy);
            }
        });

        return view;
    }


    private void initRecyclerView() {
        searchAdapter = new SearchAdapter(WatchTradeSingleton.getSingletonInstance().getSearchModelArrayList(), this, context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        searchRV.setLayoutManager(gridLayoutManager);
        searchRV.setAdapter(searchAdapter);
    }

    public void filter(String model, String box, String checkedBox, String paper, String checkedPaper, String countryName, String st_modelName, String st_caseSize, String st_sell, String st_buy) {
        List<SearchModel> productByModel = new ArrayList<>();

        for (SearchModel searchModel : WatchTradeSingleton.getSingletonInstance().getSearchModelArrayList()) {
            if (searchModel.getPostmake().toLowerCase().toString().equals(model.toLowerCase().toString()) || model.toLowerCase().toString().equals("")) {

                if (searchModel.getWatchbox().toLowerCase().toString().equals(box.toLowerCase().toString()) || searchModel.getWatchbox().toLowerCase().toString().equals(checkedBox.toLowerCase().toString()) || box.toLowerCase().toString().equals("")) {
                    if (paper.toLowerCase().toString().equals("") ||searchModel.getPaperwatch().toLowerCase().toString().equals(paper.toLowerCase().toString()) ||searchModel.getPaperwatch().toLowerCase().toString().equals( checkedPaper.toLowerCase().toString())) {
                        if (countryName.toLowerCase().toString().equals("") || countryName.toLowerCase().toString().equals("country") || searchModel.getPostcountry().toLowerCase().toString().equals(countryName.toLowerCase().toString())) {
                            if (st_modelName.toLowerCase().toString().equals("") ||searchModel.getPostmodel().toLowerCase().toString().equals(st_modelName.toLowerCase().toString())) {
                                if (st_caseSize.toLowerCase().toString().equals("") || searchModel.getCasesize().toLowerCase().toString().equals(  st_caseSize.toLowerCase().toString())) {
                                    if (st_sell.toLowerCase().toString().equals("")||searchModel.getPostype().toLowerCase().toString().equals(st_sell.toLowerCase().toString())) {
                                        if (st_buy.toLowerCase().toString().equals("")|| searchModel.getPostype().toLowerCase().toString().equals(st_buy.toLowerCase().toString())) {
                                            productByModel.add(searchModel);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        searchAdapter.setProductList(productByModel);
        if (productByModel.size()==0){
            Toast.makeText(context, "No Data To Show", Toast.LENGTH_SHORT).show();
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
}