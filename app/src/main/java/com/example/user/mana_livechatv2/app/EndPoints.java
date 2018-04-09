package com.example.user.mana_livechatv2.app;

/**
 * Created by user on 15/07/2016.
 */
public class EndPoints {

    // List URL
    public static final String BASE_URL = "http://202.56.170.37/mobile-agro/chatv2/v1";

    public static final String LOGIN = BASE_URL + "/user/login";
    public static final String REGISTER = BASE_URL + "/user/register";
    public static final String UPDATE_GCM = BASE_URL + "/user/_ID_";
    //public static final String CHAT_ROOMS = BASE_URL + "/chat_rooms";
    //public static final String CHAT_ROOM_SPECIFIC = BASE_URL + "/chat_room/_ID_";
    public static final String CHAT_ROOM_SPECIFIC = BASE_URL + "/chat_rooms/_ID_";
    public static final String CHAT_THREAD = BASE_URL + "/chat_room/_ID_";
    public static final String CHAT_ROOM_MESSAGE = BASE_URL + "/chat_room/_ID_/message";
    public static final String CHAT_ROOM_MAKE = BASE_URL + "/chat_room/new";
    public static final String GET_USER_BY_NAME = BASE_URL + "/userdata/_ID_";
    public static final String ALAMAT = BASE_URL + "/alamat";
    public static final String SAVE_SAPRO = BASE_URL + "/user/savesaprodi";
    public static final String GET_SAPRO = BASE_URL + "/user/getsaprodi";
    public static final String ADD_SAPRODI = BASE_URL + "/user/addsaprodi";


}