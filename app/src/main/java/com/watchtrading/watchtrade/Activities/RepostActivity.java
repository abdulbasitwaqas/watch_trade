package com.watchtrading.watchtrade.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.watchtrading.watchtrade.Utils.RetrofitReq.SendaddnewwatchReq;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.interfaces.IConverter;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import static com.watchtrading.watchtrade.Activities.AddNewWatchActivity.hasPermissions;
import static com.watchtrading.watchtrade.Constants.APIContract.BASE_URL;

public class RepostActivity extends AppCompatActivity implements IPresenter, RequestViews, IConverter {
    private TextView caseSizeTV, modelTV;
    private EditText priceET, countrySpinnerANA;
    private Button addWatchButton;
    private ImageView backBtnANW;
    private String st_brand, st_year, st_continent, st_currency, st_country, st_box, st_paper, st_type;
    private ProgressDialog mProgressDialog;
    private CircleImageView ivWatch;
    private TextView brandsSpinnerANA, yearSpinnerANA, subContinentSpinnerANA;
    private CheckBox boxCBANA, paperCBANA;
    private Spinner currenySpinnerANA;
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


    private String filePath="";
    private File finalFile;


    private String currentDateTimeString;

    private String postID, createuserID, postType, postmake, casesize, postmode, enterprice, postyear, postarea, currency, postcountry, watchbox, paperwatch;

    private APIService service;
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repost);


        currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

        textTypeTV = findViewById(R.id.textTypeTV);
        ivWatch = findViewById(R.id.ivWatch);
        brandsSpinnerANA = findViewById(R.id.brandsSpinnerANA);
        caseSizeTV = findViewById(R.id.caseSizeTV);
        modelTV = findViewById(R.id.modelTV);
        priceET = findViewById(R.id.priceET);
        yearSpinnerANA = findViewById(R.id.yearSpinnerANA);
        currenySpinnerANA = findViewById(R.id.currenySpinnerANA);
        countrySpinnerANA = findViewById(R.id.countrySpinnerANA);
        boxCBANA = findViewById(R.id.boxCBANA);
        paperCBANA = findViewById(R.id.paperCBANA);


        addWatchButton = findViewById(R.id.addWatchButton);
        backBtnANW = findViewById(R.id.backBtnUW);

        backBtnANW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent intent = getIntent();

        postID = intent.getStringExtra("postID");
        createuserID = intent.getStringExtra("createuserID");
        postType = intent.getStringExtra("postType");
        postmake = intent.getStringExtra("postmake");
        casesize = intent.getStringExtra("casesize");
        postmode = intent.getStringExtra("postmode");
        enterprice = intent.getStringExtra("enterprice");
        postyear = intent.getStringExtra("postyear");
        postarea = intent.getStringExtra("postarea");
        postcountry = intent.getStringExtra("postcountry");
        watchbox = intent.getStringExtra("watchbox");
        paperwatch = intent.getStringExtra("paperwatch");

        textTypeTV.setText("" + postType);
        brandsSpinnerANA.setText("" + postmake);
        caseSizeTV.setText("" + casesize);
        modelTV.setText("" + postmode);

        yearSpinnerANA.setText("" + postyear);
        countrySpinnerANA.setText("" + postarea);


        Log.d("**areaaName", postarea + "       country:  " + postcountry);


        ivWatch = findViewById(R.id.ivWatch);

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


        ivWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermissions(RepostActivity.this, PERMISSIONS)) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(RepostActivity.this, "" + getResources().getString(R.string.allow_all_permissions), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    addVideo();
                }
            }
        });

        currencyHelper = new CurrencyHelper(this);
        currencyList = currencyHelper.getAndParseLanguaues();
        list = LanguagesExtension.Companion.getStringsArray(currencyList);
        currencyAdapter = new ArrayAdapter<>(RepostActivity.this, R.layout.main_spinner_layout, list);


        currenySpinnerANA.setAdapter(currencyAdapter);

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

        addWatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textTypeTV.getText().toString().trim().equalsIgnoreCase("")) {
                    textTypeTV.setError("Buy/Sell");
                } else if (brandsSpinnerANA.getText().toString().trim().equalsIgnoreCase("")) {
                    brandsSpinnerANA.setError("Please enter brand Name");
                } else if (caseSizeTV.getText().toString().trim().equalsIgnoreCase("")) {
                    caseSizeTV.setError("Please enter case size");
                } else if (modelTV.getText().toString().trim().equalsIgnoreCase("")) {
                    modelTV.setError("Please enter Model here");
                } else if (priceET.getText().toString().trim().equalsIgnoreCase("")) {
                    priceET.setError("Please enter price here");
                } else if (yearSpinnerANA.getText().toString().trim().equalsIgnoreCase("")) {
                    yearSpinnerANA.setError("Please add year here");
                } else if (countrySpinnerANA.getText().toString().trim().equalsIgnoreCase("")) {
                    countrySpinnerANA.setError("Please add your country name");
                } else {
                    addWatchRequest();
                }
            }
        });
    }

    private void addVideo() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RepostActivity.this);
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
      /*  Presenter iPresenter = new Presenter(RepostActivity.this, RepostActivity.this, RepostActivity.this);
        if (filePath != null) {
            try {
                iPresenter.setPostAddPostMethod(APIContract.ADD_WATCH, creatStringObject(), "updateWatch", "my_file", ".png", "" + currentDateTimeString, readBytes(picUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
        }*/
//        Presenter presenter = new Presenter(RepostActivity.this, RepostActivity.this, RepostActivity.this);
//        presenter.setPostMethod(APIContract.ADD_WATCH, creatStringObject(), "updateWatch");


        if (filePath.equals("")) {
            Toast.makeText(this, "" + getResources().getString(R.string.select_img), Toast.LENGTH_SHORT).show();
        }
        else if (!filePath.equals("")) {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("createuserID", "" + SharedPreferencesSotreData.getInstance().getID());
            if (paperCBANA.isChecked()) {
                builder.addFormDataPart("paperwatch", "Yes");
            } else if (!paperCBANA.isChecked()) {
                builder.addFormDataPart("paperwatch", "No");
            }
            builder.addFormDataPart("enterprice", priceET.getText().toString().trim());
            builder.addFormDataPart("currency", st_currency);
            builder.addFormDataPart("casesize", caseSizeTV.getText().toString().trim());
            if (boxCBANA.isChecked()) {
                builder.addFormDataPart("watchbox", "Yes");
            } else {
                builder.addFormDataPart("watchbox", "No");
            }
            builder.addFormDataPart("postype", textTypeTV.getText().toString().trim());
            builder.addFormDataPart("postmake", brandsSpinnerANA.getText().toString().trim());
            builder.addFormDataPart("postyear", yearSpinnerANA.getText().toString().trim());
            builder.addFormDataPart("postcountry", countrySpinnerANA.getText().toString().trim());
            builder.addFormDataPart("postaddress", countrySpinnerANA.getText().toString().trim());
            builder.addFormDataPart("postmodel", modelTV.getText().toString().trim());
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

        }


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

    private List<ParmsModel> creatStringObject() {

        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel fileNameModel = new ParmsModel();
        fileNameModel.setParmsTag("my_file");
        fileNameModel.setParmsValue("image/png;base64," + imagetoBase64());
        parmsList.add(fileNameModel);

        ParmsModel firstNameModel = new ParmsModel();
        firstNameModel.setParmsTag("postype");
        firstNameModel.setParmsValue("" + textTypeTV.getText().toString().trim());
        parmsList.add(firstNameModel);

        ParmsModel passwordModel = new ParmsModel();
        passwordModel.setParmsTag("postmake");
        passwordModel.setParmsValue("" + brandsSpinnerANA.getText().toString().trim());
        parmsList.add(passwordModel);

        ParmsModel phoneModel = new ParmsModel();
        phoneModel.setParmsTag("casesize");
        phoneModel.setParmsValue("" + caseSizeTV.getText().toString().trim());
        parmsList.add(phoneModel);

        ParmsModel phoneModelLocation = new ParmsModel();
        phoneModelLocation.setParmsTag("postmodel");
        phoneModelLocation.setParmsValue("" + modelTV.getText().toString().trim());
        parmsList.add(phoneModelLocation);

        ParmsModel phoneModelCountry = new ParmsModel();
        phoneModelCountry.setParmsTag("enterprice");
        phoneModelCountry.setParmsValue("" + priceET.getText().toString().trim());
        parmsList.add(phoneModelCountry);

        ParmsModel phoneModelCD = new ParmsModel();
        phoneModelCD.setParmsTag("postyear");
        phoneModelCD.setParmsValue("" + yearSpinnerANA.getText().toString().trim());
        parmsList.add(phoneModelCD);

        ParmsModel postareaModelBD = new ParmsModel();
        postareaModelBD.setParmsTag("postarea");
        postareaModelBD.setParmsValue("" + countrySpinnerANA.getText().toString().trim());
        parmsList.add(postareaModelBD);

        ParmsModel currencyModelSML = new ParmsModel();
        currencyModelSML.setParmsTag("currency");
        currencyModelSML.setParmsValue(st_currency);
        parmsList.add(currencyModelSML);

        ParmsModel phoneModelPC = new ParmsModel();
        phoneModelPC.setParmsTag("postcountry");
        phoneModelPC.setParmsValue("" + countrySpinnerANA.getText().toString().trim());
        parmsList.add(phoneModelPC);

        ParmsModel watchBoxModel = new ParmsModel();
        watchBoxModel.setParmsTag("watchbox");
        if (boxCBANA.isChecked()) {
            watchBoxModel.setParmsValue("Yes");
        } else {
            watchBoxModel.setParmsValue("No");
        }
        parmsList.add(watchBoxModel);

        ParmsModel paperModel = new ParmsModel();
        paperModel.setParmsTag("paperwatch");
        if (paperCBANA.isChecked()) {
            paperModel.setParmsValue("Yes");
        } else {
            paperModel.setParmsValue("No");
        }
        parmsList.add(paperModel);

        ParmsModel phoneModelUID = new ParmsModel();
        phoneModelUID.setParmsTag("createuserID");
        phoneModelUID.setParmsValue("" + createuserID);
        parmsList.add(phoneModelUID);


        return parmsList;






      /*  List<ParmsModel> parmsList = new ArrayList<>();

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
        passwordModel.setParmsValue(postmake);
        parmsList.add(passwordModel);

        ParmsModel phoneModel = new ParmsModel();
        phoneModel.setParmsTag("casesize");
        phoneModel.setParmsValue(caseSizeTV.getText().toString().trim());
        parmsList.add(phoneModel);

        ParmsModel phoneModelLocation = new ParmsModel();
        phoneModelLocation.setParmsTag("postmodel");
        phoneModelLocation.setParmsValue(modelTV.getText().toString().trim());
        parmsList.add(phoneModelLocation);

        ParmsModel phoneModelCountry = new ParmsModel();
        phoneModelCountry.setParmsTag("enterprice");
        phoneModelCountry.setParmsValue(priceET.getText().toString());
        parmsList.add(phoneModelCountry);

        ParmsModel phoneModelCD = new ParmsModel();
        phoneModelCD.setParmsTag("postyear");
        phoneModelCD.setParmsValue(postyear);
        parmsList.add(phoneModelCD);

        *//*ParmsModel phoneModelBD = new ParmsModel();
        phoneModelBD.setParmsTag("postarea");
        phoneModelBD.setParmsValue(" ");
        parmsList.add(phoneModelBD);*//*

        ParmsModel phoneModelSML = new ParmsModel();
        phoneModelSML.setParmsTag("currency");
        phoneModelSML.setParmsValue(st_currency);
        parmsList.add(phoneModelSML);

        ParmsModel phoneModelPC = new ParmsModel();
        phoneModelPC.setParmsTag("postcountry");
        phoneModelPC.setParmsValue(postcountry);
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

        return parmsList;*/
    }

    private String imagetoBase64() {
        if (finalFile != null) {
            Bitmap bm = BitmapFactory.decodeFile(finalFile.getPath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            return encodedImage;
        }
        return "";

    }

    @Override
    public void getResponse(String response, String requestMessage) {
        Log.d("**updateWatch", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "updateWatch":

                    updateWatch(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateWatch(JSONObject jsonObject) {
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


    private void showProgress(boolean show) {

        if (show) {
            mProgressDialog = new ProgressDialog(RepostActivity.this,
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
    public void getConvertedFile(File file) {
        try {
            this.finalFile = file;
            if (file.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                ivWatch.setImageBitmap(myBitmap);

            }
            Log.e("**base64", imagetoBase64());

            showProgress(false);
        } catch (Exception e) {
            Log.e("**base64", e.toString());
            Toast.makeText(RepostActivity.this, "Image not supported", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}