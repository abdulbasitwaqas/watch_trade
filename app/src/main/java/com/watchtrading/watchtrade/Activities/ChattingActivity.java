package com.watchtrading.watchtrade.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.common.internal.service.Common;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.shockwave.pdfium.PdfDocument;
import com.watchtrading.watchtrade.Adapters.ChatsAdapter;
import com.watchtrading.watchtrade.Adapters.MessagingAdapter;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.MessagingModel;
import com.watchtrading.watchtrade.Models.ParmsModel;
import com.watchtrading.watchtrade.Presenter.Presenter;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.APIService;
import com.watchtrading.watchtrade.Utils.FilePickUtils;
import com.watchtrading.watchtrade.Utils.RetrofitReq.SendChatImageReq;
import com.watchtrading.watchtrade.Utils.ServerResponse;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;
import com.watchtrading.watchtrade.interfaces.IPresenter;
import com.watchtrading.watchtrade.interfaces.RequestViews;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import in.gauriinfotech.commons.Commons;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.watchtrading.watchtrade.Constants.APIContract.BASE_URL;

public class ChattingActivity extends AppCompatActivity implements IPresenter, RequestViews, OnPageChangeListener, MessagingAdapter.DeleteMessage   /*, OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener*/ {
    private static final String POST_FIELD = "";
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private RecyclerView chattingRV;
    private MessagingAdapter messagingAdapter;
    private List<MessagingModel> messagingModelList;
    private ProgressDialog mProgressDialog;
    private String uuuID, uuuName;
    private ImageView sendMsgBtn, chatMenuBtn, sendImgBtn, sendfileBtn;
    private EditText msgETCA;
    private TextView userNameCA;
    private Dialog showImageDialog, showImgDialog;
    private CircleImageView chatsUserTopIV;
    String path;
    int i=0;
    File file;
    private ImageView chatMessageIVCA, cameraIVCA, attachFileCA;
    private ConstraintLayout textMessageCL, imageCL, fileCL;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    private final static int CAMERA_RQ = 6969;
    private final static int GALLERY_RQ = 101;
    private final static int FILE_RQ = 102;
    private ImageView imageCA;
    private TextView pdfView;
    private boolean fromgallery;
    private Bitmap myBitmap;
    private Uri fileURI;
    private int pageNumber = 0;
    private String filePath;
    private File finalFile;

    private Uri picUri;
    private String currentDateTimeString;
    Path pdfFilePath = null;
//    private Dialog showImageDialog;
    private Retrofit retrofit;
    APIService service;
    private String pdfFileName;
    private int pageNum = 0;
    private String pdfPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        uuuID = getIntent().getStringExtra("uuuID");
        uuuName = getIntent().getStringExtra("uuuName");
//        Toast.makeText(this, ""+SharedPreferencesSotreData.getInstance().getID()+"     receiverID:"+uuuID, Toast.LENGTH_SHORT).show();

        currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        Log.d("*****uuID", "" + uuuID);

        initView();
        getMessagingRequest();

        messageThread();

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

    private void initView() {
        chattingRV = findViewById(R.id.chattingRV);
        chatsUserTopIV = findViewById(R.id.chatsUserTopIV);
        sendMsgBtn = findViewById(R.id.sendMsgBtn);
        sendImgBtn = findViewById(R.id.sendImgBtn);
        sendfileBtn = findViewById(R.id.sendfileBtn);
        msgETCA = findViewById(R.id.msgETCA);
        userNameCA = findViewById(R.id.userNameCA);
        chatMenuBtn = findViewById(R.id.chatMenuBtn);
        attachFileCA = findViewById(R.id.attachFileCA);
        cameraIVCA = findViewById(R.id.cameraIVCA);
        chatMessageIVCA = findViewById(R.id.chatMessageIVCA);
        imageCL = findViewById(R.id.imageCL);
        fileCL = findViewById(R.id.fileCL);
        textMessageCL = findViewById(R.id.textMessageCL);
        imageCA = findViewById(R.id.imageCA);
        pdfView = findViewById(R.id.fileCA);


        cameraIVCA.setVisibility(View.VISIBLE);
        attachFileCA.setVisibility(View.VISIBLE);
        textMessageCL.setVisibility(View.VISIBLE);

        chatMessageIVCA.setVisibility(View.GONE);
        fileCL.setVisibility(View.GONE);
        imageCL.setVisibility(View.GONE);

        userNameCA.setText("" + uuuName);


        userImageRequest();


        chatMessageIVCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cameraIVCA.setVisibility(View.VISIBLE);
                attachFileCA.setVisibility(View.VISIBLE);
                textMessageCL.setVisibility(View.VISIBLE);

                chatMessageIVCA.setVisibility(View.GONE);
                fileCL.setVisibility(View.GONE);
                imageCL.setVisibility(View.GONE);

            }
        });


        cameraIVCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cameraIVCA.setVisibility(View.GONE);
                textMessageCL.setVisibility(View.GONE);
                fileCL.setVisibility(View.GONE);

                chatMessageIVCA.setVisibility(View.VISIBLE);
                imageCL.setVisibility(View.VISIBLE);
                attachFileCA.setVisibility(View.VISIBLE);

                if (!hasPermissions(ChattingActivity.this, PERMISSIONS)) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(ChattingActivity.this, "" + getResources().getString(R.string.allow_all_permissions), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    addVideo();
                }
            }
        });


        imageCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVideo();
            }
        });

        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//Toast.makeText(getApplicationContext(),"pdf selection",Toast.LENGTH_SHORT).show();
                imageCL.setVisibility(View.GONE);
                textMessageCL.setVisibility(View.GONE);
                fileCL.setVisibility(View.VISIBLE);

                chatMessageIVCA.setVisibility(View.VISIBLE);
                cameraIVCA.setVisibility(View.VISIBLE);
                attachFileCA.setVisibility(View.GONE);


//                openFile(finalFile);
                openDirectory(fileURI);
            }
        });


        attachFileCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageCL.setVisibility(View.GONE);
                textMessageCL.setVisibility(View.GONE);
                fileCL.setVisibility(View.VISIBLE);
//                Toast.makeText(getApplicationContext(),"waseem attace pdf selection",Toast.LENGTH_SHORT).show();
                chatMessageIVCA.setVisibility(View.VISIBLE);
                cameraIVCA.setVisibility(View.VISIBLE);
                attachFileCA.setVisibility(View.GONE);
                launchPicker();
            }
        });






        chatMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });


        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (msgETCA.getText().toString().trim().equals("")) {
                    msgETCA.setError(getResources().getString(R.string.type_message));
                } else {
                    Presenter iPresenter = new Presenter(ChattingActivity.this, ChattingActivity.this, ChattingActivity.this);
                    iPresenter.setPostMethod(APIContract.SEND_MESSAGE, creatStringObjectSendMsg(), "message");
                }
            }
        });

        sendImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                builder.addFormDataPart("senduserID", "" + SharedPreferencesSotreData.getInstance().getID())
                        .addFormDataPart("getuserID", "" + uuuID)
                        .addFormDataPart("file_send", "Yes");


                Bitmap bmp = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 30, bos);

                builder.addFormDataPart("message", finalFile.getName(), RequestBody.create(MultipartBody.FORM, bos.toByteArray()));


// If you want to use direct file then use this ( comment out the below part and comment the above part )

                //builder.addFormDataPart("featured_photo", featured_image.getName(), RequestBody.create(MultipartBody.FORM, featured_image));


                RequestBody requestBody = builder.build();

                Call<SendChatImageReq> sendChatImageReqCall = service.send_file(requestBody);

                sendChatImageReqCall.enqueue(new Callback<SendChatImageReq>() {
                    @Override
                    public void onResponse(Call<SendChatImageReq> call, Response<SendChatImageReq> response) {

                        Log.d("**response", "" + response.body().toString());
                        Log.d("**responseError", "" + response.errorBody());
                        getMessagingRequest();
                    }

                    @Override
                    public void onFailure(Call<SendChatImageReq> call, Throwable t) {
                        Toast.makeText(ChattingActivity.this, "Image not send", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        sendfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(ChattingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(ChattingActivity.this, "" + getResources().getString(R.string.allow_all_permissions), Toast.LENGTH_SHORT).show();
                    Log.d("***permissions", "permissions not granted");
                    return;
                }
                else {

                        if (path == null) {
                            Toast.makeText(ChattingActivity.this, "please select File ", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            showProgress(true);

                            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                            builder.addFormDataPart("senduserID", "" + SharedPreferencesSotreData.getInstance().getID())
                                    .addFormDataPart("getuserID", "" + uuuID)
                                    .addFormDataPart("file_send", "Yes");
                           builder.addFormDataPart("message", file.getName(), RequestBody.create(MultipartBody.FORM, file));
                            RequestBody requestBody = builder.build();

                            Map<String, RequestBody> map = new HashMap<>();
                            Call<SendChatImageReq> call = service.send_file(requestBody);
                            call.enqueue(new Callback<SendChatImageReq>() {
                                @Override
                                public void onResponse(Call<SendChatImageReq> call, Response<SendChatImageReq> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body() != null) {
                                            showProgress(false);

                                        }
                                    } else {
                                        showProgress(false);
                                        Toast.makeText(getApplicationContext(), "problem image", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<SendChatImageReq> call, Throwable t) {
                                    showProgress(false);
                                    Log.v("Response gotten is", t.getMessage());
                                    Toast.makeText(getApplicationContext(), "problem uploading image " + t.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

//                    } else {
//                        Toast.makeText(getApplicationContext(), "File not selected!", Toast.LENGTH_LONG).show();
//                    }
                }


            }
        });
    }

    private void userImageRequest() {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setGetMethod(APIContract.USER_PROFILE_IMAGE+"/"+uuuID, "getProfileImage");
        Log.d("getProfile : ", "" + APIContract.USER_PROFILE_IMAGE+"/"+uuuID);
    }


    private void openFile(File url) {

        try {

            Uri uri = Uri.fromFile(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".docs")) {
                // PDF file
                intent.setDataAndType(uri, "application/docs");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(uri, "application/zip");
            } else if (url.toString().contains(".rar")) {
                // RAR file
                intent.setDataAndType(uri, "application/x-rar-compressed");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application found which can open the file", Toast.LENGTH_SHORT).show();
        }
    }

    public void openDirectory(Uri uriToLoad) {

        Intent intent;
        if (Build.VERSION.SDK_INT >= 19) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setType("application/octet-stream");
        intent.setType("application/pdf");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);
        startActivityForResult(intent, FILE_RQ);


       /* Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);
        startActivityForResult(intent, FILE_RQ);*/
    }
    private void launchPicker() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(true)
                .withFilter(Pattern.compile(".*\\.*$"))
                .withTitle("Select PDF file")
                .start();
    }

    private List<ParmsModel> creatStringObjectSendImg() {


        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();
        parmsModel.setParmsTag("senduserID");
        parmsModel.setParmsValue("" + SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);

        ParmsModel getuserID = new ParmsModel();
        getuserID.setParmsTag("getuserID");
        getuserID.setParmsValue("" + uuuID);
        parmsList.add(getuserID);

        ParmsModel messageModel = new ParmsModel();
        messageModel.setParmsTag("file_send");
        messageModel.setParmsValue("Yes");
        parmsList.add(messageModel);

       /* ParmsModel fileNameModel = new ParmsModel();
        fileNameModel.setParmsTag("file_name");
        fileNameModel.setParmsValue(""+picUri.getPath());
        parmsList.add(fileNameModel);*/


        return parmsList;
    }


    private void addVideo() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ChattingActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_video_pic, null);
        Button take_pic = dialogView.findViewById(R.id.take_pic);
        Button gallery = dialogView.findViewById(R.id.gallery);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.show();

        take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                takeAPic();
            }
        });


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                galleryPic();
            }
        });
    }

    public void takeAPic() {

       /* ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        picUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        //  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_RQ);*/

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
        startActivityForResult(intent, CAMERA_RQ);


    }

    public void galleryPic() {


        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_RQ);
    }


    private List<ParmsModel> creatStringObjectSendMsg() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();
        parmsModel.setParmsTag("senduserID");
        parmsModel.setParmsValue(SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);

        ParmsModel getuserID = new ParmsModel();
        getuserID.setParmsTag("getuserID");
        getuserID.setParmsValue("" + uuuID);
        parmsList.add(getuserID);

        ParmsModel message = new ParmsModel();
        message.setParmsTag("message");
        message.setParmsValue("" + msgETCA.getText().toString().trim());
//Toast.makeText(getApplicationContext(), ""+message,Toast.LENGTH_SHORT).show();
        parmsList.add(message);

        return parmsList;
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


    private void showPopupMenu(View view) {
        android.widget.PopupMenu popup = new android.widget.PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();

        inflater.inflate(R.menu.chat_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }




/*

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();

        printBookmarksTree(pdfView.getTableOfContents(), "-");
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", filePath, page + 1, pageCount));
    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

*/

    private class MyMenuItemClickListener implements android.widget.PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.block:
                    blockAPIRequest(uuuID);
                    break;
                case R.id.userProfileCM:
                    Intent profIntent = new Intent(ChattingActivity.this, UserProfileActivity.class);
                    profIntent.putExtra("userID", "" + uuuID);
                    startActivity(profIntent);
                    break;

                case R.id.unblock:
                    unblockAPIRequest(uuuID);
                    break;

            }
            return false;
        }


    }


    private void blockAPIRequest(String userID) {
        Presenter presenter = new Presenter(ChattingActivity.this, ChattingActivity.this, ChattingActivity.this);
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

    private void unblockAPIRequest(String createuserID) {
        Presenter presenter = new Presenter(ChattingActivity.this, ChattingActivity.this, ChattingActivity.this);
        presenter.setPostMethod(APIContract.BLOCK_UNBLACK, unBlockRequest(createuserID), "unblock");
        Log.d("block_unblock:", "" + APIContract.BLOCK_UNBLACK);
    }

    private List<ParmsModel> unBlockRequest(String userID) {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel addModel = new ParmsModel();
        addModel.setParmsTag("type");
        addModel.setParmsValue("remove");
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


    private void messageThread() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    getMessagingRequest();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, 10000);
    }


    private void getMessagingRequest() {
        Presenter iPresenter = new Presenter(this, this, this);
        iPresenter.setPostMethod(APIContract.GET_USER_MESSAGE, creatStringObject(), "userMessages");
    }

    private List<ParmsModel> creatStringObject() {
        List<ParmsModel> parmsList = new ArrayList<>();

        ParmsModel parmsModel = new ParmsModel();

        parmsModel.setParmsTag("senduserID");
        parmsModel.setParmsValue("" + uuuID);
        parmsList.add(parmsModel);

        ParmsModel password = new ParmsModel();
        password.setParmsTag("getuserID");
        password.setParmsValue("" + SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(password);

        return parmsList;
    }


    @Override
    public void getResponse(String response, String requestMessage) {
        Log.e("**getResponse", response);
        try {

            JSONObject jsonObject = new JSONObject(response);
            switch (requestMessage) {
                case "userMessages":
                    getMessagingProcess(jsonObject);
                    break;
                case "message":
                    sendMessagingProcess(jsonObject);
                    break;

                case "block":
                    blockReqResponse(jsonObject);
                    break;
                case "deleteMsg":
                    deleteMsgResponse(jsonObject);
                    break;

                case "unblock":
                    unblockReqResponse(jsonObject);
                    break;


                case "getProfileImage":
                    getProfileImg(jsonObject);
                    break;


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteMsgResponse(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject==true) {
                String message = jsonObject.getString("message");
                Toast.makeText(ChattingActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                getMessagingRequest();

            }
            else if (dataObject==false) {
                String message = jsonObject.getString("message");
                Toast.makeText(ChattingActivity.this, "" + message, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(ChattingActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProfileImg(JSONObject jsonObject) {

        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
//            Toast.makeText(this, "PROFILE IMAGE", Toast.LENGTH_SHORT).show();
            if (dataObject==true) {
                String message = jsonObject.getString("image");
                Log.d("*imageURL",""+message);

                Glide.with(this)
                        .load(message)
                        .placeholder(R.drawable.app_icon)
                        .into(chatsUserTopIV);

                chatsUserTopIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showImageDialog = new Dialog(ChattingActivity.this);
                        showImageDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        showImageDialog.getWindow().setGravity(Gravity.CENTER);
                        showImageDialog.setCancelable(true);
                        showImageDialog.setContentView(R.layout.show_pic_layout);

                        ImageView imageViewSPL = showImageDialog.findViewById(R.id.imageViewSPL);

                        imageViewSPL.setOnTouchListener(new ImageMatrixTouchHandler(imageViewSPL.getContext()));

                        Glide.with(ChattingActivity.this)
                                .load(message)
                                .placeholder(R.drawable.app_icon)
                                .into(imageViewSPL);


                        showImageDialog.show();
                    }
                });
            }
            else if (dataObject==false) {
//                String message = jsonObject.getString("message");
                Toast.makeText(this, "Image file corrupted", Toast.LENGTH_SHORT).show();
                Glide.with(this)
                        .load("false")
                        .placeholder(R.drawable.app_icon)
                        .into(chatsUserTopIV);
            }
            else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unblockReqResponse(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "User Unblocked Successfully", Toast.LENGTH_SHORT).show();
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

    private void blockReqResponse(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, "User blocked Successfully", Toast.LENGTH_SHORT).show();
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


    private void sendMessagingProcess(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
                String message = jsonObject.getString("message");
//                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                getMessagingRequest();
                msgETCA.setText("");

            } else if (dataObject == false) {
                String message = jsonObject.getString("message");
//                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMessagingProcess(JSONObject jsonObject) {
        boolean dataObject;
        try {
            dataObject = jsonObject.getBoolean("status");
            if (dataObject == true) {
                String message = jsonObject.getString("message");
//                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                JSONArray messagesArray = jsonObject.getJSONArray("messages");
                messagingModelList = new ArrayList<>();
                for (int i = 0; i < messagesArray.length(); i++) {
                    JSONObject messageObj = messagesArray.getJSONObject(i);
                    MessagingModel messagingModel = new MessagingModel();
                    String id = messageObj.getString("id");
                    String gid = messageObj.getString("gid");
                    String type = messageObj.getString("type");
                    String xtra = messageObj.getString("xtra");
                    String msg = messageObj.getString("msg");
                    String tms = messageObj.getString("tms");
                    String seen = messageObj.getString("seen");
                    String senderID = messageObj.getString("senderID");
                    String receiverID = messageObj.getString("receiverID");
                    Log.d("**senReceivID", senderID + "           ReceiverID: " + receiverID);

                    messagingModel.setId(id);
                    messagingModel.setGid(gid);
                    messagingModel.setType(type);
                    messagingModel.setXtra(xtra);
                    messagingModel.setMsg(msg);
                    messagingModel.setTms(tms);
                    messagingModel.setSeen(seen);
                    messagingModel.setSenderID(senderID);
                    messagingModel.setReceiverID(receiverID);

                    if (senderID.equals("" + SharedPreferencesSotreData.getInstance().getID())) {
                        messagingModel.setGravity(1);
                    } else {
                        messagingModel.setGravity(0);
                    }

                    messagingModelList.add(messagingModel);

                }

                messagingAdapter = new MessagingAdapter(messagingModelList, this, this);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                chattingRV.setLayoutManager(gridLayoutManager);
                chattingRV.setAdapter(messagingAdapter);
                chattingRV.smoothScrollToPosition(messagingModelList.size() - 1);

//                picUri = null;
//                imageCA.setImageURI(picUri);

            } else if (dataObject == false) {
                String message = jsonObject.getString("message");
//                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
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
//        showProgress(true);
    }

    @Override
    public void hideProgress() {
//        showProgress(false);
    }


    private void showProgress(boolean show) {

        if (show) {
            mProgressDialog = new ProgressDialog(ChattingActivity.this,
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
//            Toast.makeText(getApplicationContext(),"waseem file is received"+" result code:"+resultCode+" Request code:"+requestCode+" data:"+data ,Toast.LENGTH_SHORT).show();
            if (resultCode == RESULT_OK) {
                if (requestCode == GALLERY_RQ) {

                    picUri = data.getData();
                    filePath = getPath(picUri);

                    imageCA.setImageURI(picUri);
                    finalFile = new File(getRealPathFromURI(picUri));
                    Log.d("**galleryPath", finalFile.getAbsolutePath()+" "+filePath + "  :image Path     ....." + picUri.getPath());

                }
                else if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {

                    path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    file = new File(path);
                    displayFromFile(file);
                    if (path != null) {
                        Log.d("Path: ", path);
                        pdfPath = path;
                        file = new File(pdfPath);
                        pdfView.setText(file.getAbsolutePath());
                    }
                } else if (requestCode == CAMERA_RQ) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imageCA.setImageBitmap(photo);

                    picUri = getImageUri(getApplicationContext(), photo);
                    filePath = getPath(picUri);

                    finalFile = new File(getRealPathFromURI(picUri));

                    Log.d("**fileName", "" + finalFile);
                    Log.d("**fileName", "" + filePath);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void displayFromFile(File file) {


        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
        Log.d("**fileuri", "" + uri);
        filePath = getFileName(uri);
        Log.d("**fileurifile", "" + filePath);
        Log.d("**fileuri", "file :" + file.getPath());
        file.mkdirs();


    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
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

    @Override
    public void delMessage(int position, String messageID) {
        Presenter presenter = new Presenter(this, this, this);
        presenter.setPostMethod(APIContract.DELETE_MESSAGE, deleteMessageParams(messageID) , "deleteMsg");
        Log.d("getProfile : ", "" + APIContract.DELETE_MESSAGE);
    }

    @Override
    public void showImage(int position, String imageURL) {
        showImgDialog = new Dialog(this);
        showImgDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        showImgDialog.getWindow().setGravity(Gravity.CENTER);
        showImgDialog.setCancelable(true);
        showImgDialog.setContentView(R.layout.show_pic_layout);

        ImageView imageViewSPL = showImgDialog.findViewById(R.id.imageViewSPL);

        Glide.with(ChattingActivity.this)
                .load(APIContract.MESSAGE_FILE_VIEWER+""+imageURL)
                .placeholder(R.drawable.app_icon)
                .into(imageViewSPL);
        imageViewSPL.setOnTouchListener(new ImageMatrixTouchHandler(imageViewSPL.getContext()));

        showImgDialog.show();
    }

    private List<ParmsModel> deleteMessageParams(String messageID) {
        List<ParmsModel> parmsList = new ArrayList<>();


        ParmsModel parmsModel = new ParmsModel();
        parmsModel.setParmsTag("getuserID");
        parmsModel.setParmsValue(""+ SharedPreferencesSotreData.getInstance().getID());
        parmsList.add(parmsModel);

        ParmsModel creatUserIDM = new ParmsModel();
        creatUserIDM.setParmsTag("createuserID");
        creatUserIDM.setParmsValue(uuuID);
        parmsList.add(creatUserIDM);

        ParmsModel messageIDModel = new ParmsModel();
        messageIDModel.setParmsTag("messageID");
        messageIDModel.setParmsValue(messageID);
        parmsList.add(messageIDModel);

        return parmsList;
    }

}


