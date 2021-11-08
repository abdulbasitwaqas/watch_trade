package com.watchtrading.watchtrade.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.firebase.database.snapshot.Index;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.watchtrading.watchtrade.Adapters.CustomAdapter;
import com.watchtrading.watchtrade.BuildConfig;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Fragments.ChatsFragment;
import com.watchtrading.watchtrade.Fragments.ContactsFragment;
import com.watchtrading.watchtrade.Fragments.ForumFragment;
import com.watchtrading.watchtrade.Fragments.SearchFragment;
import com.watchtrading.watchtrade.Fragments.StocksFragment;
import com.watchtrading.watchtrade.Models.BrandModel;
import com.watchtrading.watchtrade.Models.ForumModel;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Models.SearchModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.CurrencyHelper;
import com.watchtrading.watchtrade.Utils.CustomViewPager;
import com.watchtrading.watchtrade.Utils.LanguagesExtension;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.Utils.ViewPagerAdapter;
import com.watchtrading.watchtrade.Utils.WatchTradeSingleton;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IPresenter, RequestViews {

    private CustomViewPager viewPager;
    private TextView forumsMenu,chatsMenu,searchMenu,contactssMenu, stocksMenu;
  
    private ImageView menuMA, currencyExchangeIVMA;
    private Dialog dialog;
    private Spinner spinnerFrom, spinnerTo;

    private String currencyFrom, currencyTo;
    private int to = 0;
    private int from = 1;
    private Gson gson;
    private ProgressDialog mProgressDialog;
    private CurrencyHelper currencyHelper;
    List<com.watchtrading.watchtrade.Models.Currency> currencyList;
    private List<String> list;
    private String inputLanguageCode;
    private String outputLanguageCode;
    String languageInput = "";
    String languageOutput = "";
    private EditText exchangeToET;
    private TextView exchangeFromET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createGson();
        allPostsRequests();

        currencyHelper = new CurrencyHelper(this);
        currencyList = currencyHelper.getAndParseLanguaues();

        list = LanguagesExtension.Companion.getStringsArray(currencyList);

    }

    private void initMembers() {

        viewPager=findViewById(R.id.viewPager);
        forumsMenu=findViewById(R.id.forumsMenu);
        chatsMenu=findViewById(R.id.chatsMenu);
        searchMenu=findViewById(R.id.searchMenu);
        contactssMenu=findViewById(R.id.contactssMenu);
        stocksMenu=findViewById(R.id.stocksMenu);


        menuMA=findViewById(R.id.menuMA);
        currencyExchangeIVMA=findViewById(R.id.currencyExchangeIVMA);
        createViewPager();



        menuMA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        currencyExchangeIVMA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printDialogShow();
            }
        });



        forumsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                forumsMenu.setBackground(getResources().getDrawable(R.drawable.menu_selected_bg));
                chatsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                searchMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                contactssMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                stocksMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));

                forumsMenu.setTextColor(getResources().getColor(R.color.white));
                chatsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                searchMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                contactssMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                stocksMenu.setTextColor(getResources().getColor(R.color.dark_grey));
            }
        });
        chatsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                forumsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                chatsMenu.setBackground(getResources().getDrawable(R.drawable.menu_selected_bg));
                searchMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                contactssMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                stocksMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));


                forumsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                chatsMenu.setTextColor(getResources().getColor(R.color.white));
                searchMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                contactssMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                stocksMenu.setTextColor(getResources().getColor(R.color.dark_grey));
            }
        });
        searchMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
                forumsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                chatsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                searchMenu.setBackground(getResources().getDrawable(R.drawable.menu_selected_bg));
                contactssMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));

                forumsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                chatsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                searchMenu.setTextColor(getResources().getColor(R.color.white));
                contactssMenu.setTextColor(getResources().getColor(R.color.dark_grey));
            }
        });
        contactssMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
                forumsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                chatsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                searchMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                contactssMenu.setBackground(getResources().getDrawable(R.drawable.menu_selected_bg));
                stocksMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));

                forumsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                chatsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                stocksMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                searchMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                contactssMenu.setTextColor(getResources().getColor(R.color.white));
            }
        });
        stocksMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(4);

                chatsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                stocksMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                searchMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                contactssMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                stocksMenu.setBackground(getResources().getDrawable(R.drawable.menu_selected_bg));

                forumsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                chatsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                searchMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                contactssMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                stocksMenu.setTextColor(getResources().getColor(R.color.white));
            }
        });
    }

    private void createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    private void allPostsRequests() {
        Presenter presenter = new Presenter(this, this, this);
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


    private void createViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ForumFragment(MainActivity.this));
        adapter.addFrag(new ChatsFragment(MainActivity.this));
        adapter.addFrag(new SearchFragment(MainActivity.this));
        adapter.addFrag(new ContactsFragment(MainActivity.this));
        adapter.addFrag(new StocksFragment(MainActivity.this));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        setTabs();
    }

    private void setTabs() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(int i) {
                switch (i) {

                    case 0:
                        viewPager.setCurrentItem(0);
                        forumsMenu.setBackground(getResources().getDrawable(R.drawable.menu_selected_bg));
                        chatsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        searchMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        contactssMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        stocksMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));

                        forumsMenu.setTextColor(getResources().getColor(R.color.white));
                        chatsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        searchMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        contactssMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        stocksMenu.setTextColor(getResources().getColor(R.color.dark_grey));


                        break;

                    case 1:
                        viewPager.setCurrentItem(1);
                        forumsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        chatsMenu.setBackground(getResources().getDrawable(R.drawable.menu_selected_bg));
                        searchMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        contactssMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        stocksMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));

                        forumsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        chatsMenu.setTextColor(getResources().getColor(R.color.white));
                        searchMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        contactssMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        stocksMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        break;

                    case 2:
                        viewPager.setCurrentItem(2);
                        forumsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        chatsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        searchMenu.setBackground(getResources().getDrawable(R.drawable.menu_selected_bg));
                        contactssMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        stocksMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));

                        forumsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        chatsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        searchMenu.setTextColor(getResources().getColor(R.color.white));
                        contactssMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        stocksMenu.setTextColor(getResources().getColor(R.color.dark_grey));

                        break;

                    case 3:
                        viewPager.setCurrentItem(3);
                        forumsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        stocksMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        chatsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        searchMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        contactssMenu.setBackground(getResources().getDrawable(R.drawable.menu_selected_bg));

                        forumsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        stocksMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        chatsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        searchMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        contactssMenu.setTextColor(getResources().getColor(R.color.white));

                        break;

                    case 4:
                        viewPager.setCurrentItem(4);
                        forumsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        chatsMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        searchMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        contactssMenu.setBackground(getResources().getDrawable(R.drawable.menu_unselected_bg));
                        stocksMenu.setBackground(getResources().getDrawable(R.drawable.menu_selected_bg));

                        forumsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        chatsMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        searchMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        contactssMenu.setTextColor(getResources().getColor(R.color.dark_grey));
                        stocksMenu.setTextColor(getResources().getColor(R.color.white));

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }



    private void showPopupMenu(View view) {
        // inflate menu
        android.widget.PopupMenu popup = new android.widget.PopupMenu(MainActivity.this, view);
        MenuInflater inflater = popup.getMenuInflater();

        inflater.inflate(R.menu.main_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();

    }

    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**allPostsResponse", requestMessage);
        Log.d("**defaultSettings", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "allPostsResponse":
                    allPostsResponseProcess(jsonObject);
                    break;
                case "defaultSettings":
                    defaultSettingsResponse(jsonObject);
                    break;
                case "forexReq":
                    forexReqResponse(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void forexReqResponse(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
//                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                JSONObject currencyJsonData = jsonObject.getJSONObject("currencyJsonData");
                String converted_amount = currencyJsonData.getString("converted_amount");

                exchangeFromET.setText(""+converted_amount);

            }
            else if (dataObject==false) {
                String message = jsonObject.getString("message");
//                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void defaultSettingsResponse(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
//                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();

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

                    initMembers();
                }


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

    private class MyMenuItemClickListener implements android.widget.PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.profile:
                    Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                    intent.putExtra("userID", SharedPreferencesSotreData.getInstance().getID());
                    startActivity(intent);
                    break;


                case R.id.memberShip:
                    Uri uri = Uri.parse("https://worldtrade.app/member/");
                    Intent intentt = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intentt);
                    break;

              /*  case R.id.settings:
                    Intent settingIntent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(settingIntent);
                    break;*/

                case R.id.chats:
                    viewPager.setOffscreenPageLimit(1);
                    viewPager.setCurrentItem(1);
                    break;

                case R.id.my_forums:
                   Intent intentForums = new Intent(MainActivity.this, MyForumsActivity.class);
                    intentForums.putExtra("createuserID", ""+ SharedPreferencesSotreData.getInstance().getID());
                    startActivity(intentForums);
                    break;

                case R.id.my_posts:
                   Intent intent1 = new Intent(MainActivity.this, StocksActivity.class);
                    intent1.putExtra("createuserID", ""+ SharedPreferencesSotreData.getInstance().getID());
                    startActivity(intent1);
                    break;

                /*case R.id.privacy_policy:
                    break;*/

                case R.id.terms_and_conditions:
//                    Uri urii = Uri.parse("https://worldtrade.app/termcondition/#");
                    Intent termsIntent = new Intent(MainActivity.this, TermsAndConditionsActivity.class);
                    startActivity(termsIntent);

                    break;

                case R.id.sendToFriends:
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "World Watch Trade");
                        String shareMessage= "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch(Exception e) {
                        //e.toString();
                    }
                    break;

                case R.id.logout:

                    SharedPreferencesSotreData.getInstance().setEmail("");
                    SharedPreferencesSotreData.getInstance().setID("");
                    SharedPreferencesSotreData.getInstance().setOTP("");
                    SharedPreferencesSotreData.getInstance().setToken("");
                    SharedPreferencesSotreData.getInstance().setUserName("");
                    SharedPreferencesSotreData.getInstance().setIsPro("0");
                    SharedPreferencesSotreData.getInstance().setFCMToken("");

                    Intent intenttt = new Intent(MainActivity.this, LogInActivity.class);
                    startActivity(intenttt);
                    finish();

                    break;

            }
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.app_icon)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.close_msg))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();



        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("Are you sure, you want to close this App?");
        alertDialogBuilder.setTitle("Alert!");

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int arg1) {
                dialogInterface.dismiss();
                finishAffinity();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int arg1) {
                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.show();*/
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
        ImageView cancelDialogIV=dialog.findViewById(R.id.cancelDialogIV);
        Button convertBtn = dialog.findViewById(R.id.convertBtn);

        ArrayAdapter inputLanguageAdapter =
                new ArrayAdapter<>(this, R.layout.spinner_item_new, list);

        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),list);

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
        parmsModel.setParmsValue(""+inputLanguageCode);
        parmsList.add(parmsModel);

        ParmsModel password = new ParmsModel();
        password.setParmsTag("currency_to");
        password.setParmsValue(""+outputLanguageCode);
        parmsList.add(password);

        ParmsModel fbtoken  = new ParmsModel();
        fbtoken .setParmsTag("currency_amount");
        fbtoken .setParmsValue(""+exchangeToET.getText().toString());
        parmsList.add(fbtoken );

        return parmsList;
    }

    private void showProgress(boolean show) {

        if (show) {
            mProgressDialog = new ProgressDialog(MainActivity.this,
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