package com.watchtrading.watchtrade.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.cocosw.bottomsheet.BottomSheet;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.APIService;
import com.watchtrading.watchtrade.Utils.CurrencyHelper;
import com.watchtrading.watchtrade.Utils.ImageCompression;
import com.watchtrading.watchtrade.Utils.LanguagesExtension;
import com.watchtrading.watchtrade.Utils.RetrofitReq.SendChatImageReq;
import com.watchtrading.watchtrade.Utils.RetrofitReq.SendaddnewwatchReq;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.Utils.WatchTradeSingleton;
import com.watchtrading.watchtrade.interfaces.IConverter;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.watchtrading.watchtrade.Constants.APIContract.BASE_URL;

public class AddNewWatchActivity extends AppCompatActivity implements IPresenter, RequestViews, IConverter {
    private EditText caseSizeTV, modelTV, priceTV;
    private Button addWatchButton;
    private ImageView backBtnANW;

    private String st_brand, st_year, st_continent, st_currency, st_country, st_box, st_paper, st_type;
    private ProgressDialog mProgressDialog;
    private CircleImageView ivWatch;
    private Spinner brandsSpinnerANA, yearSpinnerANA, subContinentSpinnerANA, currenySpinnerANA, countrySpinnerANA;
    private CheckBox boxCBANA, paperCBANA;
    private ArrayAdapter continentAdapter, brandsAdapter, yearAdapter, currencyAdapter, countryAdapter;
    private String[] yearList = new String[]{"1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011",
            "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021"};
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
    private int modelPosition = -1;
    private boolean fromgallery;
    private File file;

    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };


    Uri picUri = null;
    private final static int CAMERA_RQ = 6969;
    private TextView textTypeTV;
    List<com.watchtrading.watchtrade.Models.Currency> currencyList;
    private List<String> list;
    private CurrencyHelper currencyHelper;


    BottomSheet.Builder builder;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
    byte[] resultByteArray;
    Bitmap bitmap = null;
    private APIService service;
    private Retrofit retrofit;
    private String filePath;
    private File finalFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_watch);

        st_type = getIntent().getStringExtra("type");




        okhttp3.OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        service = retrofit.create(APIService.class);

        textTypeTV = findViewById(R.id.textTypeTV);

        textTypeTV.setText(st_type);
        ivWatch = findViewById(R.id.ivWatch);

        caseSizeTV = findViewById(R.id.caseSizeTV);
        modelTV = findViewById(R.id.modelTV);
        priceTV = findViewById(R.id.priceTV);


        boxCBANA = findViewById(R.id.boxCBANA);
        paperCBANA = findViewById(R.id.paperCBANA);


        brandsSpinnerANA = findViewById(R.id.brandsSpinnerANA);
        yearSpinnerANA = findViewById(R.id.yearSpinnerANA);
        subContinentSpinnerANA = findViewById(R.id.subContinentSpinnerANA);
        currenySpinnerANA = findViewById(R.id.currenySpinnerANA);
        countrySpinnerANA = findViewById(R.id.countrySpinnerANA);


        addWatchButton = findViewById(R.id.addWatchButton);
        backBtnANW = findViewById(R.id.backBtnANW);
        backBtnANW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        currencyHelper = new CurrencyHelper(this);
        currencyList = currencyHelper.getAndParseLanguaues();

        list = LanguagesExtension.Companion.getStringsArray(currencyList);


        brandsAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, WatchTradeSingleton.getSingletonInstance().getBrandsList());
//        yearAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, yearList);
        yearAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, yearsRange("1950", "2021"));
//        currencyAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, WatchTradeSingleton.getSingletonInstance().getcurrencySymbol());
        currencyAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, list);
        continentAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, WatchTradeSingleton.getSingletonInstance().getContinentList());
        countryAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, countries);


        brandsSpinnerANA.setAdapter(brandsAdapter);
        countrySpinnerANA.setAdapter(countryAdapter);
        yearSpinnerANA.setAdapter(yearAdapter);
        currenySpinnerANA.setAdapter(currencyAdapter);
        subContinentSpinnerANA.setAdapter(continentAdapter);


        addWatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textTypeTV.getText().toString().trim().equalsIgnoreCase("")) {
                    textTypeTV.setError("Buy/Sell");
                } else if (caseSizeTV.getText().toString().trim().equalsIgnoreCase("")) {
                    caseSizeTV.setError("Please enter case size");
                } else if (modelTV.getText().toString().trim().equalsIgnoreCase("")) {
                    modelTV.setError("Please enter Model here");
                } else if (priceTV.getText().toString().trim().equalsIgnoreCase("")) {
                    priceTV.setError("Please enter price here");
                } else {
                    addWatchRequest();
                }
            }
        });


        //spinners

        brandsSpinnerANA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**brandName", "" + brandsSpinnerANA.getSelectedItem().toString());
                st_brand = brandsSpinnerANA.getSelectedItem().toString();

                modelPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yearSpinnerANA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**brandName", "" + yearSpinnerANA.getSelectedItem().toString());
                st_year = yearSpinnerANA.getSelectedItem().toString();

                modelPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subContinentSpinnerANA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**brandName", "" + subContinentSpinnerANA.getSelectedItem().toString());
                st_continent = subContinentSpinnerANA.getSelectedItem().toString();



                modelPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        currenySpinnerANA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                Log.d("*currency", "" + currenySpinnerANA.getSelectedItem().toString());

                st_currency = currenySpinnerANA.getSelectedItem().toString();

                modelPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        countrySpinnerANA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**brandName", "" + countrySpinnerANA.getSelectedItem().toString());
                st_country = countrySpinnerANA.getSelectedItem().toString();


                modelPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ivWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermissions(AddNewWatchActivity.this, PERMISSIONS)) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(AddNewWatchActivity.this, "" + getResources().getString(R.string.allow_all_permissions), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    addVideo();
                }
            }
        });

    }


    private void addVideo() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddNewWatchActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_video_pic, null);
        Button take_pic = dialogView.findViewById(R.id.take_pic);
        Button gallery = dialogView.findViewById(R.id.gallery);

        dialogBuilder.setView(dialogView);

        TextView textVieww = (TextView) dialogView.findViewById(R.id.label_field);


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                TakeAPic();


            }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                GalleryPic();


            }
        });
    }

    public void TakeAPic() {

        /*ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        picUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        //  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_RQ);*/

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
        startActivityForResult(intent, CAMERA_RQ);


    }

    public void GalleryPic() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 101);
    }

    private void addWatchRequest() {


           //will be deleted if not work
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("createuserID", "" + SharedPreferencesSotreData.getInstance().getID());
        if (paperCBANA.isChecked()) {
            builder.addFormDataPart("paperwatch", "Yes");
        } else if (!paperCBANA.isChecked()) {
            builder.addFormDataPart("paperwatch", "No");
        }
        builder.addFormDataPart("enterprice",priceTV.getText().toString().trim());
        builder.addFormDataPart("currency",st_currency);
        builder.addFormDataPart("casesize",caseSizeTV.getText().toString().trim());
        if (boxCBANA.isChecked()) {
            builder.addFormDataPart("watchbox","Yes");
        } else {
            builder.addFormDataPart("watchbox","No");
        }
        builder.addFormDataPart("postype",textTypeTV.getText().toString().trim());
        builder.addFormDataPart("postmake",st_brand);
        builder.addFormDataPart("postyear",st_year);
        builder.addFormDataPart("postcountry",st_country);
        builder.addFormDataPart("postaddress",st_country);
        builder.addFormDataPart("postmodel",modelTV.getText().toString().trim());
//        builder.addFormDataPart("file_send", "Yes");

        Bitmap bmp = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, bos);

        builder.addFormDataPart("my_file", finalFile.getName(), RequestBody.create(MultipartBody.FORM, bos.toByteArray()));


//        builder.addFormDataPart("my_file", finalFile.getName(), RequestBody.create(MultipartBody.FORM, finalFile));
            RequestBody requestBody = builder.build();
            Call<SendaddnewwatchReq> call = service.send_file_watch(requestBody);
            call.enqueue(new Callback<SendaddnewwatchReq>() {
                @Override
                public void onResponse(Call<SendaddnewwatchReq> call, Response<SendaddnewwatchReq> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            showProgress(false);
                            Log.d("**response", "" + response.body().toString());
                            Log.d("**responseError", "" + response.errorBody());
                            finish();

                        }
                    } else {
                        showProgress(false);
                        Toast.makeText(getApplicationContext(), "problem image", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SendaddnewwatchReq> call, Throwable t) {
                    showProgress(false);
                    Log.v("Response gotten is", t.getMessage());
                    Toast.makeText(getApplicationContext(), "problem uploading image " + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        //Presenter presenter = new Presenter(this, this, this);
        //presenter.setPostMethod(APIContract.ADD_WATCH, creatStringObject(), "addNewWatch");
    }

    private List<ParmsModel> creatStringObject() {

        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel fileNameModel = new ParmsModel();
        fileNameModel.setParmsTag("my_file");
       fileNameModel.setParmsValue("image/png;base64,"+imagetoBase64());
        parmsList.add(fileNameModel);

        ParmsModel firstNameModel = new ParmsModel();
        firstNameModel.setParmsTag("postype");
        firstNameModel.setParmsValue(textTypeTV.getText().toString().trim());
        parmsList.add(firstNameModel);

        ParmsModel passwordModel = new ParmsModel();
        passwordModel.setParmsTag("postmake");
        passwordModel.setParmsValue(st_brand);
        parmsList.add(passwordModel);
        //ParmsModel android = new ParmsModel();
      //  passwordModel.setParmsTag("android");
       // passwordModel.setParmsValue("true");
        //parmsList.add(android);

        ParmsModel phoneModel = new ParmsModel();
        phoneModel.setParmsTag("casesize");
        phoneModel.setParmsValue(caseSizeTV.getText().toString().trim());
        parmsList.add(phoneModel);

        ParmsModel postAddressModel = new ParmsModel();
        postAddressModel.setParmsTag("postaddress");
        postAddressModel.setParmsValue(st_country);
        parmsList.add(postAddressModel);

        ParmsModel phoneModelLocation = new ParmsModel();
        phoneModelLocation.setParmsTag("postmodel");
        phoneModelLocation.setParmsValue(modelTV.getText().toString().trim());
        parmsList.add(phoneModelLocation);

        ParmsModel poststatusModel = new ParmsModel();
        poststatusModel.setParmsTag("poststatus");
        poststatusModel.setParmsValue("1");
        parmsList.add(poststatusModel);

        ParmsModel phoneModelCountry = new ParmsModel();
        phoneModelCountry.setParmsTag("enterprice");
        phoneModelCountry.setParmsValue(priceTV.getText().toString().trim());
        parmsList.add(phoneModelCountry);

        ParmsModel phoneModelCD = new ParmsModel();
        phoneModelCD.setParmsTag("postyear");
        phoneModelCD.setParmsValue(st_year);
        parmsList.add(phoneModelCD);

        /*ParmsModel phoneModelBD = new ParmsModel();
        phoneModelBD.setParmsTag("postarea");
        phoneModelBD.setParmsValue(" ");
        parmsList.add(phoneModelBD);*/

        ParmsModel phoneModelSML = new ParmsModel();
        phoneModelSML.setParmsTag("currency");
        phoneModelSML.setParmsValue(st_currency);
        parmsList.add(phoneModelSML);

        ParmsModel phoneModelPC = new ParmsModel();
        phoneModelPC.setParmsTag("postcountry");
        phoneModelPC.setParmsValue(st_country);
//        Toast.makeText(AddNewWatchActivity.this, ""+st_country.toString().trim(), Toast.LENGTH_SHORT).show();
        Log.d("*countryName",""+st_country);
        parmsList.add(phoneModelPC);

        ParmsModel watchModelWB = new ParmsModel();
        watchModelWB.setParmsTag("watchbox");
        if (boxCBANA.isChecked()) {
            watchModelWB.setParmsValue("Yes");
        } else {
            watchModelWB.setParmsValue("No");
        }
        parmsList.add(watchModelWB);


        ParmsModel paperModel = new ParmsModel();
        paperModel.setParmsTag("paperwatch");
        if (paperCBANA.isChecked()) {
            paperModel.setParmsValue("Yes");
        } else if (!paperCBANA.isChecked()) {
            paperModel.setParmsValue("No");
        }
        parmsList.add(paperModel);


        ParmsModel phoneModelUID = new ParmsModel();
        phoneModelUID.setParmsTag("createuserID");
        phoneModelUID.setParmsValue("" + SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(phoneModelUID);

        Log.d("*addNewReq",""+parmsList);

        return parmsList;
    }

    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**addNewWatch", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {

                case "addNewWatch":
                    addNewWatch(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addNewWatch(JSONObject jsonObject) {
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
            mProgressDialog = new ProgressDialog(AddNewWatchActivity.this,
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


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {


            if (resultCode == RESULT_OK) {
                if (requestCode == 101) {


                    picUri = data.getData();
                    filePath = getPath(picUri);

                    ivWatch.setImageURI(picUri);
                    finalFile = new File(getRealPathFromURI(picUri));

                } else if (requestCode == CAMERA_RQ) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    ivWatch.setImageBitmap(photo);

                    picUri = getImageUri(getApplicationContext(), photo);
                    filePath = getPath(picUri);

                    finalFile = new File(getRealPathFromURI(picUri));

                    Log.d("**fileName", "" + finalFile);
                    Log.d("**fileName", "" + filePath);

                }
            }

        } catch (Exception ex) {
            Log.e("Ex_OnActivityResult", ex.toString());
        }
    }



   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CROP_CAMERA_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    //bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(AddNewWatchActivity.this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            try {
                                // bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                                file = new File(imagePath);

                                Log.e("image", imagePath);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CROP_GALLERY_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    bm = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(AddNewWatchActivity.this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Log.e("image", imagePath);
                            try {
                                file = new File(imagePath);


                                Log.e("image", imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            if (picUri != null) {
                picUri = Uri.parse(SharedPreferencesSotreData.getInstance().getValue());
                startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(SharedPreferencesSotreData.getInstance().getValue());
                startCropping(picUri, CROP_CAMERA_IMAGE);
            }
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri tempUri = data.getData();
                Log.e("front tempUri", "" + tempUri);
                if (tempUri != null) {
                    startCropping(tempUri, CROP_GALLERY_IMAGE);
                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }


    }

    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(AddNewWatchActivity.this, AddNewWatchActivity.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }*/


    @Override
    public void getConvertedFile(File file) {
        try {
            this.file = file;
            if (file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                ivWatch.setImageBitmap(myBitmap);

            }
            Log.e("EXc", imagetoBase64());

            showProgress(false);
        } catch (Exception e) {
            Log.e("EXc", e.toString());
            Toast.makeText(AddNewWatchActivity.this, "Image not supported", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String imagetoBase64(){
        if(finalFile!=null){
            Bitmap bm = BitmapFactory.decodeFile(finalFile.getPath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            Log.d("ffff",encodedImage);
            return encodedImage;
        }
        return "";

    }


    public static List<String> yearsRange(String startYear, String endYear) {
        int cur = Integer.parseInt(startYear);
        int stop = Integer.parseInt(endYear);
        List<String> list = new ArrayList<String>();
        while (cur <= stop) {
            list.add(String.valueOf(stop--));
        }
        return list;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }



    //for bitmap
    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    //for File
    private String encodeImage(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }




    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}







































































/*
package com.watchtrading.watchtrade.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.cocosw.bottomsheet.BottomSheet;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.CurrencyHelper;
import com.watchtrading.watchtrade.Utils.ImageCompression;
import com.watchtrading.watchtrade.Utils.LanguagesExtension;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.Utils.WatchTradeSingleton;
import com.watchtrading.watchtrade.VolleyMultipartReq.VolleyMultipartRequest;
import com.watchtrading.watchtrade.interfaces.IConverter;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddNewWatchActivity extends AppCompatActivity implements IPresenter, RequestViews, IConverter {
    private EditText caseSizeTV, modelTV, priceTV;
    private Button addWatchButton;
    private ImageView backBtnANW;
    private String st_brand, st_year, st_continent, st_currency, st_country, st_box, st_paper, st_type;
    private ProgressDialog mProgressDialog;
    private CircleImageView ivWatch;
    private Spinner brandsSpinnerANA, yearSpinnerANA, subContinentSpinnerANA, currenySpinnerANA, countrySpinnerANA;
    private CheckBox boxCBANA, paperCBANA;
    private ArrayAdapter continentAdapter, brandsAdapter, yearAdapter, currencyAdapter, countryAdapter;
    private String[] yearList = new String[]{"1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011",
            "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021"};
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
    private int modelPosition = -1;
    private boolean fromgallery;
    private File file;

    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };


    Uri picUri = null;
    private final static int CAMERA_RQ = 6969;
    private TextView textTypeTV;
    List<com.watchtrading.watchtrade.Models.Currency> currencyList;
    private List<String> list;
    private CurrencyHelper currencyHelper;
    private String currentDateTimeString;


    BottomSheet.Builder builder;
    int PICK_FROM_CAMERA = 1, PICK_FROM_GALLERY = 2;
    int CROP_CAMERA_IMAGE = 3, CROP_GALLERY_IMAGE = 4;
    String imageName;
    String pathOfImage;
    Bitmap bm;
    ImageCompression imageCompression;
    byte[] resultByteArray;
    Bitmap bitmap = null;


    private String filePath;
    private File finalFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_watch);

        st_type = getIntent().getStringExtra("type");
        currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

        textTypeTV = findViewById(R.id.textTypeTV);

        textTypeTV.setText(st_type);
        ivWatch = findViewById(R.id.ivWatch);

        caseSizeTV = findViewById(R.id.caseSizeTV);
        modelTV = findViewById(R.id.modelTV);
        priceTV = findViewById(R.id.priceTV);



        Log.d("**createUSERID","USER ID :   "+SharedPreferencesSotreData.getInstance().getID());


        boxCBANA = findViewById(R.id.boxCBANA);
        paperCBANA = findViewById(R.id.paperCBANA);


        brandsSpinnerANA = findViewById(R.id.brandsSpinnerANA);
        yearSpinnerANA = findViewById(R.id.yearSpinnerANA);
        subContinentSpinnerANA = findViewById(R.id.subContinentSpinnerANA);
        currenySpinnerANA = findViewById(R.id.currenySpinnerANA);
        countrySpinnerANA = findViewById(R.id.countrySpinnerANA);


        addWatchButton = findViewById(R.id.addWatchButton);
        backBtnANW = findViewById(R.id.backBtnANW);
        backBtnANW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        currencyHelper = new CurrencyHelper(this);
        currencyList = currencyHelper.getAndParseLanguaues();

        list = LanguagesExtension.Companion.getStringsArray(currencyList);


        brandsAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, WatchTradeSingleton.getSingletonInstance().getBrandsList());
//        yearAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, yearList);
        yearAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, yearsRange("1950", "2021"));
//        currencyAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, WatchTradeSingleton.getSingletonInstance().getcurrencySymbol());
        currencyAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, list);
        continentAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, WatchTradeSingleton.getSingletonInstance().getContinentList());
        countryAdapter = new ArrayAdapter<>(AddNewWatchActivity.this, R.layout.main_spinner_layout, countries);


        brandsSpinnerANA.setAdapter(brandsAdapter);
        countrySpinnerANA.setAdapter(countryAdapter);
        yearSpinnerANA.setAdapter(yearAdapter);
        currenySpinnerANA.setAdapter(currencyAdapter);
        subContinentSpinnerANA.setAdapter(continentAdapter);


        addWatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textTypeTV.getText().toString().trim().equalsIgnoreCase("")) {
                    textTypeTV.setError("Buy/Sell");
                } else if (caseSizeTV.getText().toString().trim().equalsIgnoreCase("")) {
                    caseSizeTV.setError("Please enter case size");
                } else if (modelTV.getText().toString().trim().equalsIgnoreCase("")) {
                    modelTV.setError("Please enter Model here");
                } else if (priceTV.getText().toString().trim().equalsIgnoreCase("")) {
                    priceTV.setError("Please enter price here");
                } else {
                    addWatchRequest();
                }
            }
        });


        //spinners

        brandsSpinnerANA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**brandName", "" + brandsSpinnerANA.getSelectedItem().toString());
                st_brand = brandsSpinnerANA.getSelectedItem().toString();

                modelPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        yearSpinnerANA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**brandName", "" + yearSpinnerANA.getSelectedItem().toString());
                st_year = yearSpinnerANA.getSelectedItem().toString();

                modelPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subContinentSpinnerANA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**brandName", "" + subContinentSpinnerANA.getSelectedItem().toString());
                st_continent = subContinentSpinnerANA.getSelectedItem().toString();



                modelPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        currenySpinnerANA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                Log.d("*currency", "" + currenySpinnerANA.getSelectedItem().toString());

                st_currency = currenySpinnerANA.getSelectedItem().toString();

                modelPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        countrySpinnerANA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**brandName", "" + countrySpinnerANA.getSelectedItem().toString());
                st_country = countrySpinnerANA.getSelectedItem().toString();


                modelPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ivWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermissions(AddNewWatchActivity.this, PERMISSIONS)) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(AddNewWatchActivity.this, "" + getResources().getString(R.string.allow_all_permissions), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    addVideo();
                }
            }
        });

    }


    private void addVideo() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddNewWatchActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_video_pic, null);
        Button take_pic = dialogView.findViewById(R.id.take_pic);
        Button gallery = dialogView.findViewById(R.id.gallery);

        dialogBuilder.setView(dialogView);

        TextView textVieww = (TextView) dialogView.findViewById(R.id.label_field);


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                TakeAPic();


            }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                GalleryPic();


            }
        });
    }

    public void TakeAPic() {

        */
/*ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        picUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        //  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_RQ);*//*


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
        startActivityForResult(intent, CAMERA_RQ);


    }

    public void GalleryPic() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 101);
    }

    private void addWatchRequest() {
        Presenter iPresenter = new Presenter(AddNewWatchActivity.this, AddNewWatchActivity.this, AddNewWatchActivity.this);
        if (filePath != null) {
            try {
                iPresenter.setPostAddPostMethod(APIContract.ADD_WATCH, newPostStringObject(), "addNewWatch", "my_file", ".jpg", "" + currentDateTimeString, readBytes(picUri), this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
        }
//        Presenter presenter = new Presenter(this, this, this);
//        presenter.setPostAddPostMethod(APIContract.ADD_WATCH, creatStringObject(), "addNewWatch");
    }

    public byte[] readBytes(Uri uri) throws IOException {
        // this dynamically extends to take the bytes you read
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    private List<ParmsModel> newPostStringObject() {

        List<ParmsModel> parmsList = new ArrayList<>();


        ParmsModel createUserUID = new ParmsModel();
        createUserUID.setParmsTag("createuserID");
        createUserUID.setParmsValue(""+SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(createUserUID);



        ParmsModel paperModel = new ParmsModel();
        paperModel.setParmsTag("paperwatch");
        if (paperCBANA.isChecked()) {
            paperModel.setParmsValue("Yes");
        } else if (!paperCBANA.isChecked()) {
            paperModel.setParmsValue("No");
        }
        parmsList.add(paperModel);





        ParmsModel phoneModelCountry = new ParmsModel();
        phoneModelCountry.setParmsTag("enterprice");
        phoneModelCountry.setParmsValue(priceTV.getText().toString().trim());
        parmsList.add(phoneModelCountry);



        ParmsModel phoneModel = new ParmsModel();
        phoneModel.setParmsTag("casesize");
        phoneModel.setParmsValue(caseSizeTV.getText().toString().trim());
        parmsList.add(phoneModel);



        ParmsModel watchModelWB = new ParmsModel();
        watchModelWB.setParmsTag("watchbox");
        if (boxCBANA.isChecked()) {
            watchModelWB.setParmsValue("Yes");
        } else {
            watchModelWB.setParmsValue("No");
        }
        parmsList.add(watchModelWB);




        ParmsModel firstNameModel = new ParmsModel();
        firstNameModel.setParmsTag("postype");
        firstNameModel.setParmsValue(textTypeTV.getText().toString().trim());
        parmsList.add(firstNameModel);

        ParmsModel passwordModel = new ParmsModel();
        passwordModel.setParmsTag("postmake");
        passwordModel.setParmsValue(st_brand);
        parmsList.add(passwordModel);



        ParmsModel phoneModelCD = new ParmsModel();
        phoneModelCD.setParmsTag("postyear");
        phoneModelCD.setParmsValue(st_year);
        parmsList.add(phoneModelCD);

        ParmsModel phoneModelPC = new ParmsModel();
        phoneModelPC.setParmsTag("postcountry");
        phoneModelPC.setParmsValue(st_country);
        parmsList.add(phoneModelPC);


        ParmsModel phoneModelBD = new ParmsModel();
        phoneModelBD.setParmsTag("postaddress");
        phoneModelBD.setParmsValue("");
        parmsList.add(phoneModelBD);

        ParmsModel phoneModelLocation = new ParmsModel();
        phoneModelLocation.setParmsTag("postmodel");
        phoneModelLocation.setParmsValue(modelTV.getText().toString().trim());
        parmsList.add(phoneModelLocation);

        ParmsModel poststatusModel = new ParmsModel();
        poststatusModel.setParmsTag("poststatus");
        poststatusModel.setParmsValue("1");
        parmsList.add(poststatusModel);

        ParmsModel phoneModelSML = new ParmsModel();
        phoneModelSML.setParmsTag("currency");
        phoneModelSML.setParmsValue(st_currency);
        parmsList.add(phoneModelSML);





//        ParmsModel fileNameModel = new ParmsModel();
//        fileNameModel.setParmsTag("my_file");
//        fileNameModel.setParmsValue("image/png;base64,"+imagetoBase64());
//        parmsList.add(fileNameModel);



        Log.d("*addNewReq",""+SharedPreferencesSotreData.getInstance().getID());

        return parmsList;
    }

    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**addNewWatch", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {

                case "addNewWatch":
                    addNewWatch(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addNewWatch(JSONObject jsonObject) {
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
            mProgressDialog = new ProgressDialog(AddNewWatchActivity.this,
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


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {


            if (resultCode == RESULT_OK) {
                if (requestCode == 101) {


                    picUri = data.getData();
                    filePath = getPath(picUri);

                    ivWatch.setImageURI(picUri);

                } else if (requestCode == CAMERA_RQ) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    ivWatch.setImageBitmap(photo);

                    picUri = getImageUri(getApplicationContext(), photo);
                    filePath = getPath(picUri);

                    finalFile = new File(getRealPathFromURI(picUri));

                    Log.d("**fileName", "" + finalFile);
                    Log.d("**fileName", "" + filePath);

                }
            }
            */
/*if (requestCode == CAMERA_RQ) {

                if (resultCode == RESULT_OK) {


                    if (data != null) {


                        picUri = data.getData();
                        if (picUri != null) {
                            if (Build.VERSION.SDK_INT < 11) {
                                file = new File(getRealPathFromURI_BelowAPI11(getApplicationContext(), picUri));
                            } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT <= 19) {
                                file = new File(getRealPathFromURI_API11to19(getApplicationContext(), picUri));
                            } else if (Build.VERSION.SDK_INT > 19) {
                                file = new File(getRealPathFromURI_API11to19(getApplicationContext(), picUri));
                            }

                            picUri = Uri.fromFile(file);

                            if (file.exists()) {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

//                                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                            ivWatch.setImageBitmap(myBitmap);
//                            scaleDown(myBitmap,100f, true);

                                ivWatch.setImageBitmap(scaleDown(myBitmap, 10, true));
                            }
                            fromgallery = false;


                        }

                    }
                    *//*
*/
/*showProgress(true);
                    fromgallery = false;
                    new ImageConverterTask(AddNewWatchActivity.this,
                            picUri, false, AddNewWatchActivity.this).execute();
                    ivWatch.setImageBitmap(scaleDown(myBitmap, 10, true));
                    bitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();*//*
*/
/*

                }
            }

            if (requestCode == 101) {


                if (data != null) {


                    picUri = data.getData();
                    if (picUri != null) {
                        if (Build.VERSION.SDK_INT < 11) {
                            file = new File(getRealPathFromURI_BelowAPI11(getApplicationContext(), picUri));
                        } else if (Build.VERSION.SDK_INT >= 11 && Build.VERSION.SDK_INT <= 19) {
                            file = new File(getRealPathFromURI_API11to19(getApplicationContext(), picUri));
                        } else if (Build.VERSION.SDK_INT > 19) {
                            file = new File(getRealPathFromURI_API11to19(getApplicationContext(), picUri));
                        }

                        picUri = Uri.fromFile(file);

                        if (file.exists()) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

//                            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                            ivWatch.setImageBitmap(myBitmap);
//                            scaleDown(myBitmap,100f, true);

                            ivWatch.setImageBitmap(scaleDown(myBitmap, 10, true));
                        }
                        fromgallery = true;


                    }

                }
            }*//*

        } catch (Exception ex) {
            Log.e("Ex_OnActivityResult", ex.toString());
        }
    }



   */
/* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CROP_CAMERA_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    //bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(AddNewWatchActivity.this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            try {
                                // bitmap = MediaStore.Images.Media.getBitmap(SaveDetailsActivityNew.this.getContentResolver(), resultUri);
                                file = new File(imagePath);

                                Log.e("image", imagePath);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CROP_GALLERY_IMAGE) {
            if (data != null) {
                picUri = Uri.parse(data.getExtras().getString("resultUri"));
                try {
                    bm = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    pathOfImage = picUri.getPath();
                    imageCompression = new ImageCompression(AddNewWatchActivity.this);
                    imageCompression.execute(pathOfImage);
                    imageCompression.setOnTaskFinishedEvent(new ImageCompression.AsyncResponse() {
                        @Override
                        public void processFinish(String imagePath) {
                            Log.e("image", imagePath);
                            try {
                                file = new File(imagePath);


                                Log.e("image", imagePath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK) {
            if (picUri != null) {
                picUri = Uri.parse(SharedPreferencesSotreData.getInstance().getValue());
                startCropping(picUri, CROP_CAMERA_IMAGE);
            } else {
                picUri = Uri.parse(SharedPreferencesSotreData.getInstance().getValue());
                startCropping(picUri, CROP_CAMERA_IMAGE);
            }
        }
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK) {
            try {
                Uri tempUri = data.getData();
                Log.e("front tempUri", "" + tempUri);
                if (tempUri != null) {
                    startCropping(tempUri, CROP_GALLERY_IMAGE);
                } else {

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }


    }

    public void startCropping(Uri uri, int requestCode) {
        Intent intent = new Intent(AddNewWatchActivity.this, AddNewWatchActivity.class);
        intent.putExtra("imageUri", uri.toString());
        intent.putExtra("requestCode", requestCode);
        startActivityForResult(intent, requestCode);
    }*//*



    @Override
    public void getConvertedFile(File file) {
        try {
            this.finalFile = file;
            if (file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                ivWatch.setImageBitmap(myBitmap);

            }
            Log.e("EXc", imagetoBase64());

            showProgress(false);
        } catch (Exception e) {
            Log.e("EXc", e.toString());
            Toast.makeText(AddNewWatchActivity.this, "Image not supported", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String imagetoBase64(){
        if(finalFile!=null){
            Bitmap bm = BitmapFactory.decodeFile(finalFile.getPath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage;
        }
        return "";

    }


    public static List<String> yearsRange(String startYear, String endYear) {
        int cur = Integer.parseInt(startYear);
        int stop = Integer.parseInt(endYear);
        List<String> list = new ArrayList<String>();
        while (cur <= stop) {
            list.add(String.valueOf(stop--));
        }
        return list;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }



    //for bitmap
    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    //for File
    private String encodeImage(String path)
    {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(imagefile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }




    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }



    private void uploadBitmap(final Bitmap bitmap) {


    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}*/
