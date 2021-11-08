package com.watchtrading.watchtrade.Utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.watchtrading.watchtrade.Models.Currency;
import com.watchtrading.watchtrade.R;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class CurrencyHelper {
    private Context context;

    public CurrencyHelper(@NonNull Context context) {
        this.context = context;
    }

    public List<Currency> getAndParseLanguaues() throws RuntimeException {
        InputStream is = context.getResources().openRawResource(R.raw.currency);
        String s = "";
        try {
            s = IOUtils.toString(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is); // don't forget to close your streams
        }
        if (s.isEmpty()) {
            throw new RuntimeException("Failed to open resource!");
        }
        return Arrays.asList(new Gson().fromJson(s, Currency[].class));
    }

}
