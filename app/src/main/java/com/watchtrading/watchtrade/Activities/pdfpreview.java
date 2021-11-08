package com.watchtrading.watchtrade.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.watchtrading.watchtrade.R;

public class pdfpreview extends AppCompatActivity {
Bundle bundle;
WebView pdfViewAdap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfpreview);
        bundle= getIntent().getExtras();
        pdfViewAdap = (WebView) findViewById(R.id.pdfViewAdap);
        pdfViewAdap.getSettings().setJavaScriptEnabled(true);
        String pdf = bundle.getString("pdfurl");
        pdfViewAdap.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);


    }
}