package com.gpsy.config;

public class ConstantsForConfiguration {

    //*For Thymeleaf and mailService
    public static final String EMAIL_ADDRESS_TO_SEND = "dkoplenski@gmail.com";
    public static final String SUBJECT = "GPSY App: New recommended set of tracks on your Spotify!";
    public static final String WELCOME_MESSAHE = "Hello,";
    public static final String GOODBYE_MESSAGE = "ENJOY! See you next week!";
    public static final String ANCHOR_BUTTON_LINK = "https://www.spotify.com/pl/";
    public static final String COMPANY_NAME = "Daniel Koplenski GPSYApp";

    //*For scheduling
    //every monday at 7:00
    public static final String SCHEDULING_CRON = "0 0 7 ? * MON";
    //save to db recently played tracks every 2,5 h(1 track 3 min, max tracks 50 -> 150min)
    public static final int SCHEDULED_FETCHING_DATA_FROM_SPOTIFY_API = 9000000;

}
