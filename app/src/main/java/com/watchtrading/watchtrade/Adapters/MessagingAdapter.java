package com.watchtrading.watchtrade.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.watchtrading.watchtrade.Activities.pdfpreview;
import com.watchtrading.watchtrade.Constants.APIContract;
import com.watchtrading.watchtrade.Models.MessagingModel;
import com.watchtrading.watchtrade.R;
import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Objects;

import static android.content.Context.DOWNLOAD_SERVICE;

public class MessagingAdapter extends RecyclerView.Adapter<MessagingAdapter.MyViewHolder> {
    private List<MessagingModel> messagingModelList;
    private Context context;
    private View view;
    private MessagingAdapter.MyViewHolder holder;
    private final LayoutInflater inflater;
    Intent intent,intent1;
    String imageurl,docsurl,xlsurl,zipurl,pdfurl;
    DownloadManager dm;
    String type;
    DownloadManager.Request request;
    private int viewT;
    private DeleteMessage deleteMessage;
// Intent intent=new Intent(context, pdfpreview.class);

    public MessagingAdapter(List<MessagingModel> messagingModelList, Context context, DeleteMessage deleteMessage) {
        this.messagingModelList = messagingModelList;
        this.context = context;
        this.deleteMessage = deleteMessage;
        inflater = LayoutInflater.from(context);
        intent=new Intent(context, pdfpreview.class);
        intent1=new Intent();
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.setAction(Intent.ACTION_VIEW);
        type = "application/*";
        dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);

    }

    /*@NonNull
    @Override
    public MessagingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.messaging_list, null);
        MessagingAdapter.MyViewHolder viewHolder = new MessagingAdapter.MyViewHolder(itemView);
        return viewHolder;
    }*/


    @Override
    public MessagingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        viewT=viewType;
        if (viewType == 0) {
            view = inflater.inflate(R.layout.sender_layout, parent, false);
        } else {
            view = inflater.inflate(R.layout.receiver_layout, parent, false);
        }
        holder = new MessagingAdapter.MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull MessagingAdapter.MyViewHolder holder, int position) {

        holder.timeTV.setText(messagingModelList.get(position).getTms());
        String seen = messagingModelList.get(position).getSeen();



        if (messagingModelList.get(position).getType().equals("file")) {
            String word = messagingModelList.get(position).getMsg();
            String word1 = messagingModelList.get(position).getMsg();
            word1=word.substring(word.length() - 4);
            word = word.substring(word.length() - 3);
            Log.d("Fileextensionnames",word);

            if (word.toString().equals("jpg")||word.toString().equals("png")) {
                holder.iamgeViewPic.setVisibility(View.VISIBLE);
                holder.messageTV.setVisibility(View.GONE);
                holder.pdfViewAdap.setVisibility(View.GONE);
                Glide.with(context)
                        .load(APIContract.MESSAGE_FILE_VIEWER+""+messagingModelList.get(position).getMsg())
                        . placeholder(R.drawable.app_icon)
                        .into(holder.iamgeViewPic);
                Log.d("allaboutpic",APIContract.MESSAGE_FILE_VIEWER+""+messagingModelList.get(position).getMsg());
            }
           else if (word1.toString().equals("docx")||word.toString().equals("doc")) {
                holder.iamgeViewPic.setVisibility(View.VISIBLE);
                holder.messageTV.setVisibility(View.GONE);
                holder.pdfViewAdap.setVisibility(View.GONE);
                holder.iamgeViewPic.setImageResource(R.drawable.docx);
                holder.iamgeViewPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        docsurl=APIContract.MESSAGE_FILE_VIEWER+""+messagingModelList.get(position).getMsg();
                        String PATH = Environment.getExternalStorageDirectory().toString()
                                + "/load";
                        Log.v("LOG_TAG", "PATH: " + PATH);
                        File file = new File(PATH);
                        file.mkdirs();
                        File outputFile = new File(file, messagingModelList.get(position).getMsg());
                        if(outputFile.exists()==false) {
                            downloadfile(docsurl, outputFile);
                        }
                        else
                        {

                            file = new File(PATH+"/"+messagingModelList.get(position).getMsg());
                            intent1.setDataAndType(Uri.fromFile(file), type);
                            context.startActivity(intent1);
                        }
                    }
                });
                Log.d("allaboutpic",APIContract.MESSAGE_FILE_VIEWER+""+messagingModelList.get(position).getMsg());
            }
            else if (word1.toString().equals("xlxs") ||word.toString().equals("xls")) {
                holder.iamgeViewPic.setVisibility(View.VISIBLE);
                holder.messageTV.setVisibility(View.GONE);
                holder.pdfViewAdap.setVisibility(View.GONE);
                holder.iamgeViewPic.setImageResource(R.drawable.zip);
                Log.d("allaboutpic",APIContract.MESSAGE_FILE_VIEWER+""+messagingModelList.get(position).getMsg());


                holder.iamgeViewPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        xlsurl= APIContract.MESSAGE_FILE_VIEWER+""+messagingModelList.get(position).getMsg();
                          String PATH = Environment.getExternalStorageDirectory().toString()
                                  + "/load";
                         Log.v("LOG_TAG", "PATH: " + PATH);
                        File file = new File(PATH);
                        file.mkdirs();
                        File outputFile = new File(file, messagingModelList.get(position).getMsg());
                        if(outputFile.exists()==false) {
                            downloadfile(xlsurl, outputFile);
                        }
                        else
                        {
                            file = new File(PATH+"/"+messagingModelList.get(position).getMsg());
                            intent1.setDataAndType(Uri.fromFile(file), type);
                            context.startActivity(intent1);
                        }
                    }
                });
            }
           else if (word.toString().equals("zip")) {
                holder.iamgeViewPic.setVisibility(View.VISIBLE);
                holder.messageTV.setVisibility(View.GONE);
                holder.pdfViewAdap.setVisibility(View.GONE);
                holder.iamgeViewPic.setImageResource(R.drawable.zip);
                Log.d("allaboutpic",APIContract.MESSAGE_FILE_VIEWER+""+messagingModelList.get(position).getMsg());


                holder.iamgeViewPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zipurl= APIContract.MESSAGE_FILE_VIEWER+""+messagingModelList.get(position).getMsg();
                        String PATH1 = Environment.getExternalStorageDirectory().toString()
                                + "/load"+messagingModelList.get(position).getMsg();

                            String PATH = Environment.getExternalStorageDirectory().toString()
                                    + "/load";
                            Log.v("LOG_TAG", "PATH: " + PATH);
                            File file = new File(PATH);
                            file.mkdirs();
                            File outputFile = new File(file, messagingModelList.get(position).getMsg());
                            if(outputFile.exists()==false) {
                                downloadfile(zipurl, outputFile);
                            }
                            else
                            {
                                file = new File(PATH+"/"+messagingModelList.get(position).getMsg());
                                intent1.setDataAndType(Uri.fromFile(file), type);
                                context.startActivity(intent1);
                            }



                    }
                });



            }

            else {
                holder.iamgeViewPic.setVisibility(View.GONE);
                holder.messageTV.setVisibility(View.GONE);
                holder.pdfViewAdap.setVisibility(View.VISIBLE);
                String pdf = APIContract.MESSAGE_FILE_VIEWER+""+messagingModelList.get(position).getMsg();
                holder.pdfViewAdap.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);

                holder.pdfViewAdap.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        intent.putExtra("pdfurl",APIContract.MESSAGE_FILE_VIEWER+""+messagingModelList.get(position).getMsg());
                        context.startActivity(intent);
                        return false;
                    }
                });



Log.d("allaboutpd",APIContract.MESSAGE_FILE_VIEWER+""+messagingModelList.get(position).getMsg());
            }
        }
        else if (messagingModelList.get(position).getType().equals("msg")){
            holder.iamgeViewPic.setVisibility(View.GONE);
            holder.pdfViewAdap.setVisibility(View.GONE);
            holder.messageTV.setVisibility(View.VISIBLE);
            holder.messageTV.setText(messagingModelList.get(position).getMsg());
        }

        if (messagingModelList.get(position).getSeen().toString().equals("1")) {
            holder.seenIV.setBackground(context.getResources().getDrawable(R.drawable.ic_seen));
        } else if (messagingModelList.get(position).getSeen().toString().equals("0")) {
            holder.seenIV.setBackground(context.getResources().getDrawable(R.drawable.ic_delivered_icon));
        }


        holder.deleteMsgBtn.setVisibility(View.GONE);

        if (messagingModelList.get(position).getReceiverID().equals(""+ SharedPreferencesSotreData.getInstance().getID())){
            holder.senderCL.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    holder.deleteMsgBtn.setVisibility(View.VISIBLE);
                    return false;
                }
            });
        }


        holder.deleteMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessage.delMessage(position, messagingModelList.get(position).getId());
            }
        });

        holder.iamgeViewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessage.showImage(position, messagingModelList.get(position).getMsg());
            }
        });


    }



    private void downloadfile(String imageurl,File outputFile) {
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try
        {
            URL url = new URL(imageurl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory().toString()
                    + "/load";
            Log.v("LOG_TAG", "PATH: " + PATH);

            //File file = new File(PATH);
            //file.mkdirs();
           // File outputFile = new File(file, option14[i].toString());
            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = c.getInputStream();

            byte[] buffer = new byte[4096];
            int len1 = 0;

            while ((len1 = is.read(buffer)) != -1)
            {
                fos.write(buffer, 0, len1);
            }

            fos.close();
            is.close();

            Toast.makeText(context, "File is ready Tap to open",
                    Toast.LENGTH_LONG).show();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return messagingModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return messagingModelList.get(position).getGravity();
    }

    @Override
    public int getItemViewType(int position) {
        return messagingModelList.get(position).getGravity();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTV, timeTV;
        private ImageView seenIV, iamgeViewPic, deleteMsgBtn;
        //private PDFView pdfViewAdap;
        WebView pdfViewAdap;
       private ConstraintLayout senderCL;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            seenIV = itemView.findViewById(R.id.seenIV);
            messageTV = itemView.findViewById(R.id.messageTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            iamgeViewPic = itemView.findViewById(R.id.iamgeViewPic);
            deleteMsgBtn = itemView.findViewById(R.id.deleteMsgBtn);
            senderCL = itemView.findViewById(R.id.senderCL);
            pdfViewAdap = itemView.findViewById(R.id.pdfViewAdap);
            pdfViewAdap.getSettings().setJavaScriptEnabled(true);


        }


    }


    private boolean IsBase64Encoded(String value) {
        try {
            byte[] decodedString = Base64.decode(value, Base64.DEFAULT);
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void setExistImage(ImageView imageView, String base64String) {
        if (!base64String.isEmpty()) {
            byte[] bytes = Base64.decode(base64String, Base64.DEFAULT);
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
    }


    public interface DeleteMessage {

        void delMessage( int position, String messageID);
        void showImage(int position, String imageURL);
    }
}
