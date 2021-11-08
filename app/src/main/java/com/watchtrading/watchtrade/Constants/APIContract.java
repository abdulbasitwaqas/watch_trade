package com.watchtrading.watchtrade.Constants;

public interface APIContract {

    public String APP_NAME = "World Watch Trade";
    //POST
//    String BASE_URL = "http://watchapi.globeitclient.com/Api/";
    String BASE_URL = "https://worldtrade.app/api/Api/";
    String REGISTER = BASE_URL+"UserData/user_signup";  //email, password, name, fullname, profile_5 (UserLocation), profile_6   (country),  profile_10  (Company Details),   profile_9  (Business details),   profile_1   (Social Accounts) ,    fbtoken  (firebase token)
    String LOGIN = BASE_URL+"UserData/user_login";   //email, password, fbtoken(firebase_token)
    String ALL_BRANDS = BASE_URL + "NewWatch/allPostsbrand";    //brand
    String GET_PROFILE = BASE_URL + "UserData/getProfile";    //userID:59

    String UPDATE_PROFILE = BASE_URL + "UserData/updateprofile";
    String UPDATE_PASSWORD = BASE_URL + "UserData/updatepassword";
    String FORGET_PASSWORD = BASE_URL + "UserData/forgotpassword";    //userID:59, password:globitsols@gmail.com ,oldpassword:123456
    String IMAGE_URL = "https://worldtrade.app/uploads/images/";
    String USER_CHAT = BASE_URL + "NewWatch/getuserchatlist";
    String USER_RATING = BASE_URL + "NewWatch/getuserrating";

    //POST

    String UPDATE_WATCH = BASE_URL + "NewWatch/updatePost";
    String POST_REPORT = BASE_URL + "NewWatch/addpostreport"; /* createuserID:59   ,  postID:83   ,  post_reportmessage:asdf asdfa */
    String GET_CONTACT = BASE_URL + "NewWatch/getcontact";   //createuserID:59
    String ADD_TO_CONTACTS = BASE_URL + "NewWatch/addtocontact";   //createuserID:59 ,  userID:84
    String ADD_WATCH = BASE_URL + "NewWatch/addPost";
    String ADD_WATCH_Adnroid = BASE_URL + "NewWatch/addPostAndroid";
    String SEND_FILES_Watch = BASE_URL + "NewWatch/addPost";
    //GET
    String GET_ALL_POSTS = BASE_URL + "NewWatch/allPosts";
    String GET_DEFAULT_SETTINGS = BASE_URL + "NewWatch/defaultSetting";
    String USER_STOCK = BASE_URL + "NewWatch/userstock";    /*  createuserID: 59  */
    String GET_USER_MESSAGE = BASE_URL + "NewWatch/getusermessage";    /*  senduserID:84    ,    getuserID:80   */
    public  static final String SEND_MESSAGE = BASE_URL + "NewWatch/sendmessage";           /*  senduserID:84     ,    getuserID:80     ,      message:fine   */
    String SEND_FILES = BASE_URL + "NewWatch/sendmessagefile";           /*  senduserID:84     ,    getuserID:80     ,      message:fine   */
    String MESSAGE_SEEN = BASE_URL + "NewWatch/messageseen";          /*   mailID:23   , alertID:33   */
    String FOREX_CONVERTER = BASE_URL + "NewWatch/fxconverter";       /*   currency_from:USD ,  currency_to:PKR  ,  convert_amount:3 */
    String ADD_RATING = BASE_URL + "NewWatch/addrating";       /*   ratinguserID:"59"  ,  createuserID:"83"  ,  ratingpoints:5  ,  reviewmessage:""  */
    String ADD_RATING_REPORT = BASE_URL + "NewWatch/addratingreport";       /*   createuserID:"59"  ,  ratingID:"83"  ,  post_reportmessage:"" */
    String BLOCK_UNBLACK = BASE_URL + "NewWatch/blockUnblock";       /*   type:add/remove   ,   createuserID:59   ,    blockuserID:83 */
    String USER_PROFILE_REPORT = BASE_URL + "NewWatch/adduserreport";       /*  createuserID:59  ,  userID:23   ,  post_reportmessage:asdf asdfa */
    String USER_PROFILE_IMAGE = BASE_URL + "NewWatch/userimage";       /*  userID:38 */
    String DELETE_POST = BASE_URL + "NewWatch/deletePost";       /*  postID : 38 */
    String DELETE_CHAT = BASE_URL + "NewWatch/deletechat";       /*  createuserID:OTHERUSERID   ,   getuserID:LOGINUSERID */
    String DELETE_MESSAGE = BASE_URL + "NewWatch/deletemessage";       /*  createuserID:OTHERUSERID   ,   getuserID:LOGINUSERID   ,   messageID */


    String MESSAGE_FILE_VIEWER ="https://worldtrade.app/gem/ore/grupo/files/preview/";


    String SPANISH = "es";
     String IMAGE_URI_CAMERA = "image_uri_camera";
    String ENGLISH = "en";
    String Portuguese = "pt";
}
