package com.watchtrading.watchtrade;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;


import com.watchtrading.watchtrade.Utils.SharedPreferencesSotreData;

import java.util.Locale;

import static com.watchtrading.watchtrade.Constants.APIContract.ENGLISH;


public class AppController extends Application {

    private Context mContext;

    private static AppController mInstance;



    public void onCreate() {

        super.onCreate();
        mInstance = this;
        mContext = this;
        SharedPreferencesSotreData.getInstance().setContext(this);


    }
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public Context getContext() {
        return mContext;
    }



    public static boolean setLocale() {

        String lang = SharedPreferencesSotreData.getInstance().getUserLanguage();

        Locale current = AppController.getInstance().getResources().getConfiguration().locale;

        Locale myLocale = new Locale(lang);
        Resources res = AppController.getInstance().getResources();
        Configuration conf = res.getConfiguration();
        if (lang.equals(ENGLISH)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                conf.setLocale(myLocale);

                LocaleList localeList = new LocaleList(myLocale);
                LocaleList.setDefault(localeList);
                conf.setLocales(localeList);

                AppController.getInstance().createConfigurationContext(conf);

            } else {
                conf.locale = myLocale;
            }

            //   DisplayMetrics dm = res.getDisplayMetrics();
            //   res.updateConfiguration(conf, dm);
            //   conf.setLayoutDirection(new Locale(lang));
            return true;
        } else {
            //    conf.locale = Locale.US;
            //     DisplayMetrics dm = res.getDisplayMetrics();
            //    res.updateConfiguration(conf, dm);
            //    conf.setLayoutDirection(new Locale(lang));
            return false;
        }

    }


    public static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        context.createConfigurationContext(configuration);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferencesSotreData.getInstance().setContext(newBase);
        updateResources(newBase, SharedPreferencesSotreData.getInstance().getUserLanguage());
        super.attachBaseContext(newBase);
    }

}