package com.gpsy.config;

public class MailMessageConfiguration {

    public static final String EMAIL_ADDRESS_TO_SEND = "dkoplenski@gmail.com";
    public static final String SCHEDULING_CRON = "0 0 7 ? * MON";//every monday at 7:00
    public static final String SUBJECT = "GPSY App: New recommended set of tracks on your Spotify!";
    public static final String WELCOME_MESSAHE = "Hello,";
    //main message in service SpotifyHandleService
    public static final String GOODBYE_MESSAGE = "ENJOY! See you next week!";
    public static final String ANCHOR_BUTTON_LINK = "https://www.spotify.com/pl/";
    public static final String COMPANY_NAME = "Daniel Koplenski GPSYApp";

}
