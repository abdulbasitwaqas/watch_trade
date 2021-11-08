package com.watchtrading.watchtrade.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
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
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.APIService;
import com.watchtrading.watchtrade.Utils.RetrofitReq.SendaddnewwatchReq;
import com.watchtrading.watchtrade.Utils.RetrofitReq.UpdateProfileRequest;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

public class EditProfileActivity extends AppCompatActivity implements IPresenter, RequestViews {
    private final static int CAMERA_RQ = 6969;
    private Spinner privateMsgSpinner, countiesSpinner, deActivateSpinner;
    private EditText userFullNameETLA, userNameETLA, userEmailETLA, passwordETLA, vatNoETLA, phoneETLA, companyNameETLA, businessNameETLA, locationETLA, socialAccountsETLA;
    private Button updateBtn, cancelUpadteBtn;
    private ImageView closeEPA;
    private TextView dobETLA;
    private String[] privateMsgList = new String[]{"Enable", "Disable"};
    private String[] deactivateList = new String[]{"Yes", "No"};
    public String[] countries = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
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

    private ArrayAdapter<String> countriesAdapter, privateMsgAdapter, deactivateAccountAdapter;
    private static int countriesPos, privateMsgPos, deactivatePos = -1;
    private String st_country, st_privateMsg, st_deactivateMsg = "";
    private APIService service;
    private Retrofit retrofit;
    private ProgressDialog mProgressDialog;
    private CircleImageView addProfileIV;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };
    Uri picUri = null;
    private String filePath = "";
    private File finalFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initMembers();


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
    }

    @SuppressLint("WrongViewCast")
    private void initMembers() {
        addProfileIV = findViewById(R.id.addProfileIV);

        privateMsgSpinner = findViewById(R.id.privateMsgSpinner);
        countiesSpinner = findViewById(R.id.countiesSpinner);
        deActivateSpinner = findViewById(R.id.deActivateSpinner);


        userFullNameETLA = findViewById(R.id.userFullNameETLA);
        userNameETLA = findViewById(R.id.userNameETLA);
        userEmailETLA = findViewById(R.id.userEmailETLA);
        passwordETLA = findViewById(R.id.passwordETLA);
        vatNoETLA = findViewById(R.id.vatNoETLA);
        dobETLA = findViewById(R.id.dobETLA);
        phoneETLA = findViewById(R.id.phoneETLA);
        companyNameETLA = findViewById(R.id.companyNameETLA);
        businessNameETLA = findViewById(R.id.businessNameETLA);
        locationETLA = findViewById(R.id.locationETLA);
        socialAccountsETLA = findViewById(R.id.socialAccountsETLA);


        updateBtn = findViewById(R.id.updateBtn);
        cancelUpadteBtn = findViewById(R.id.cancelUpadteBtn);
        closeEPA = findViewById(R.id.closeEPA);


        userFullNameETLA.setText(SharedPreferencesSotreData.getInstance().getUserName());
        userNameETLA.setText(SharedPreferencesSotreData.getInstance().getUserName());
        userEmailETLA.setText(SharedPreferencesSotreData.getInstance().getEmail());
        passwordETLA.setText(SharedPreferencesSotreData.getInstance().getUserPassword());
        vatNoETLA.setText(SharedPreferencesSotreData.getInstance().getVatNumber());
        dobETLA.setText(SharedPreferencesSotreData.getInstance().getDateOfBirth());
        phoneETLA.setText(SharedPreferencesSotreData.getInstance().getUserPhone());
        companyNameETLA.setText(SharedPreferencesSotreData.getInstance().getCompanyName());
        businessNameETLA.setText(SharedPreferencesSotreData.getInstance().getBusinessName());
        locationETLA.setText(SharedPreferencesSotreData.getInstance().getLocation());
        socialAccountsETLA.setText(SharedPreferencesSotreData.getInstance().getSocialLinks());


        addProfileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermissions(EditProfileActivity.this, PERMISSIONS)) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(EditProfileActivity.this, "" + getResources().getString(R.string.allow_all_permissions), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    addImage();
                }
            }
        });


        closeEPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancelUpadteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        deactivateAccountAdapter = new ArrayAdapter<>(EditProfileActivity.this, R.layout.main_spinner_layout, deactivateList);
        privateMsgAdapter = new ArrayAdapter<>(EditProfileActivity.this, R.layout.main_spinner_layout, privateMsgList);
        countriesAdapter = new ArrayAdapter<>(EditProfileActivity.this, R.layout.main_spinner_layout, countries);

        countiesSpinner.setAdapter(countriesAdapter);
        privateMsgSpinner.setAdapter(privateMsgAdapter);
        deActivateSpinner.setAdapter(deactivateAccountAdapter);


        countiesSpinner.setSelection(countriesPos);
        privateMsgSpinner.setSelection(privateMsgPos);
        deActivateSpinner.setSelection(deactivatePos);


        countiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("selectedCountry", "" + countiesSpinner.getSelectedItem().toString());
                st_country = countiesSpinner.getSelectedItem().toString();

                countriesPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        privateMsgSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**privateMsgSpinner", "" + privateMsgSpinner.getSelectedItem().toString());
                st_privateMsg = privateMsgSpinner.getSelectedItem().toString();

                privateMsgPos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        deActivateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                Log.d("**deActivateSpinner", "" + deActivateSpinner.getSelectedItem().toString());
                st_deactivateMsg = deActivateSpinner.getSelectedItem().toString();

                deactivatePos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userNameETLA.getText().toString().equals("")) {
                    userNameETLA.setError(getResources().getString(R.string.enter_user_name));
                } else if (userEmailETLA.getText().toString().equals("")) {
                    userEmailETLA.setError(getResources().getString(R.string.enter_user_email));
                } else if (vatNoETLA.getText().toString().equals("")) {
                    vatNoETLA.setError(getResources().getString(R.string.vat_number_error));
                } else if (st_country.equals("")) {
                    Toast.makeText(EditProfileActivity.this, "" + getResources().getString(R.string.enter_country_error), Toast.LENGTH_SHORT).show();
                } else if (companyNameETLA.getText().toString().equals("")) {
                    companyNameETLA.setError(getResources().getString(R.string.company_name_error));
                } else if (businessNameETLA.getText().toString().equals("")) {
                    businessNameETLA.setError(getResources().getString(R.string.business_name_error));
                } else {
                    updateRequest();
                }

            }
        });


    }

    private void addImage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditProfileActivity.this);
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
        startActivityForResult(intent, CAMERA_RQ);


    }

    public void GalleryPic() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 101);
    }

    private void updateRequest() {

        if (filePath.equals("")) {
            Toast.makeText(this, "" + getResources().getString(R.string.select_img), Toast.LENGTH_SHORT).show();
        } else if (!filePath.equals("")) {

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("userID", "" + SharedPreferencesSotreData.getInstance().getID());

            builder.addFormDataPart("email", userEmailETLA.getText().toString().trim());
            builder.addFormDataPart("name", userNameETLA.getText().toString().trim());

            builder.addFormDataPart("profile_1", vatNoETLA.getText().toString().trim());
            builder.addFormDataPart("profile_2", dobETLA.getText().toString().trim());
            builder.addFormDataPart("profile_4", phoneETLA.getText().toString().trim());
            builder.addFormDataPart("profile_5", locationETLA.getText().toString().trim());
            builder.addFormDataPart("profile_6", st_country);
            builder.addFormDataPart("profile_9", companyNameETLA.getText().toString().trim());
            builder.addFormDataPart("profile_10", businessNameETLA.getText().toString().trim());
            builder.addFormDataPart("profile_15", socialAccountsETLA.getText().toString().trim());

            Bitmap bmp = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 30, bos);

            builder.addFormDataPart("profile_image", finalFile.getName(), RequestBody.create(MultipartBody.FORM, bos.toByteArray()));


            RequestBody requestBody = builder.build();
            Call<UpdateProfileRequest> call = service.update_profile(requestBody);
            call.enqueue(new Callback<UpdateProfileRequest>() {
                @Override
                public void onResponse(Call<UpdateProfileRequest> call, Response<UpdateProfileRequest> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            showProgress(false);

                            Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                            SharedPreferencesSotreData.getInstance().setVatNumber("" + vatNoETLA.getText().toString().trim());
                            SharedPreferencesSotreData.getInstance().setDateOfBirth("" + dobETLA.getText().toString().trim());
                            SharedPreferencesSotreData.getInstance().setLocation("" + locationETLA.getText().toString().trim());
                            SharedPreferencesSotreData.getInstance().setCompanyName("" + companyNameETLA.getText().toString().trim());
                            SharedPreferencesSotreData.getInstance().setBusinessName("" + businessNameETLA.getText().toString().trim());
                            SharedPreferencesSotreData.getInstance().setCountry("" + st_country);
                            SharedPreferencesSotreData.getInstance().setUserPhone("" + phoneETLA.getText().toString().trim());
                            SharedPreferencesSotreData.getInstance().setSocialLinks("" + socialAccountsETLA.getText().toString().trim());

                            finish();

                        }
                    } else {
                        showProgress(false);
                        Toast.makeText(getApplicationContext(), "problem image", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileRequest> call, Throwable t) {
                    showProgress(false);
                    Log.v("Response gotten is", t.getMessage());
                    Toast.makeText(getApplicationContext(), "problem uploading image " + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }


    @Override
    public void getResponse(String response, String requestMessage) {

    }

    @Override
    public void getError(VolleyError error) {

    }

    @Override
    public void getSuccessNetwork(NetworkResponse response, String requestMessage) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }


    private void showProgress(boolean show) {

        if (show) {
            mProgressDialog = new ProgressDialog(EditProfileActivity.this,
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


    public boolean hasPermissions(Context context, String... permissions) {
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

                    addProfileIV.setImageURI(picUri);
                    finalFile = new File(getRealPathFromURI(picUri));

                } else if (requestCode == CAMERA_RQ) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    addProfileIV.setImageBitmap(photo);

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