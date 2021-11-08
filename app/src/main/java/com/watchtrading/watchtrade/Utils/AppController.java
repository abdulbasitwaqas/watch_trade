package com.watchtrading.watchtrade.Utils;


import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import androidx.viewpager.widget.ViewPager;

import java.util.Locale;

import static com.watchtrading.watchtrade.Constants.APIContract.ENGLISH;


public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private Context mContext;
    public CustomViewPager viewPager_home;
    private static AppController mInstance;
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = this;
        SharedPreferencesSotreData.getInstance().setContext(this);
        /*Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(4)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);*/
     }

    public Context getContext() {
        return mContext;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


    public ViewPager getViewPager_home() {
        return viewPager_home;
    }

    public void setViewPager_home(CustomViewPager viewPager_home) {
        this.viewPager_home = viewPager_home;
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

}