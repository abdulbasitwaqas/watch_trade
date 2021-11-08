package com.watchtrading.watchtrade.Utils;

import android.content.Context;

import com.watchtrading.watchtrade.Models.ChatsModel;
import com.watchtrading.watchtrade.Models.ContactsModel;
import com.watchtrading.watchtrade.Models.DefaultSetting;
import com.watchtrading.watchtrade.Models.DefaultSettingModel;
import com.watchtrading.watchtrade.Models.ForumModel;
import com.watchtrading.watchtrade.Models.RatingListModel;
import com.watchtrading.watchtrade.Models.SearchModel;

import java.util.ArrayList;
import java.util.List;

public class WatchTradeSingleton {


    Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    private static final WatchTradeSingleton singletonInstance = new WatchTradeSingleton();

    private List<ForumModel> forumModelArrayList = new ArrayList<>();
    private List<SearchModel> searchModelArrayList = new ArrayList<>();
    private List<ContactsModel> contactsModelList = new ArrayList<>();
    private List<ChatsModel> chatsModelList = new ArrayList<>();
    private List<RatingListModel> ratingListModelList = new ArrayList<>();
    private List<String> brandsList = new ArrayList<>();
    private List<String> currencySymbol = new ArrayList<>();
    private List<String> continentList = new ArrayList<>();

    private List<String> africaList = new ArrayList<>();
    private List<String> asiaList = new ArrayList<>();
    private List<String> australiaList = new ArrayList<>();
    private List<String> europeliaList = new ArrayList<>();
    private List<String> northAmericaliaList = new ArrayList<>();
    private List<String> southAmericaliaList = new ArrayList<>();

    public WatchTradeSingleton() {
    }

    public static WatchTradeSingleton getSingletonInstance() {
        return singletonInstance;
    }

    public List<ForumModel> getForumModelArrayList() {
        return forumModelArrayList;
    }
    public void setForumModelArrayList(List<ForumModel> forumModelArrayList) {
        this.forumModelArrayList = forumModelArrayList;
    }

    public List<SearchModel> getSearchModelArrayList() {
        return searchModelArrayList;
    }
    public void setSearchModelArrayList(List<SearchModel> searchModelArrayList) {
        this.searchModelArrayList = searchModelArrayList;
    }

    public List<ContactsModel> getContactsModelList() {
        return contactsModelList;
    }
    public void setContactsModelList(List<ContactsModel> contactsModelList) {
        this.contactsModelList = contactsModelList;
    }

    public List<ChatsModel> getChatsModelList() {
        return chatsModelList;
    }
    public void setChatsModelList(List<ChatsModel> chatsModelList) {
        this.chatsModelList = chatsModelList;
    }

    public List<RatingListModel> getRaingModelList() {
        return ratingListModelList;
    }
    public void setRaingModelList(List<RatingListModel> chatsModelList) {
        this.ratingListModelList = ratingListModelList;
    }

    public List<String> getBrandsList() {
        return brandsList;
    }
    public void setBrandsList(List<String> brandsList) {
        this.brandsList = brandsList;
    }

    public List<String> getcurrencySymbol() {
        return currencySymbol;
    }
    public void setcurrencySymbol(List<String> currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public List<String> getContinentList() {
        return continentList;
    }
    public void setContinentList(List<String> continentList) {
        this.continentList = continentList;
    }

    public List<String> getafricaList() {
        return africaList;
    }
    public void setafricaList(List<String> africaList) {
        this.africaList = africaList;
    }




    public List<String> getasiaList() {
        return asiaList;
    }
    public void setasiaList(List<String> asiaList) {
        this.asiaList = asiaList;
    }




    public List<String> getaustraliaList() {
        return australiaList;
    }
    public void setaustraliaList(List<String> australiaList) {
        this.australiaList = australiaList;
    }




    public List<String> geteuropeliaList() {
        return europeliaList;
    }
    public void seteuropeliaList(List<String> europeliaList) {
        this.europeliaList = europeliaList;
    }




    public List<String> getnorthAmericaliaList() {
        return northAmericaliaList;
    }
    public void setnorthAmericaliaList(List<String> northAmericaliaList) {
        this.northAmericaliaList = northAmericaliaList;
    }




    public List<String> getsouthAmericaliaList() {
        return southAmericaliaList;
    }
    public void setsouthAmericaliaList(List<String> southAmericaliaList) {
        this.southAmericaliaList = southAmericaliaList;
    }

}
