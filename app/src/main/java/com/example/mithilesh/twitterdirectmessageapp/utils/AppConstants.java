package com.example.mithilesh.twitterdirectmessageapp.utils;


public class AppConstants {

    public static String SHARED_PREFERENCE_NAME = "direct_message_shared_preference";
    public static String SHARED_PREFERENCE_LAST_MESSAGE_TIME = "shared_preference_last_message_time";

    public static class IntentKey {
        public static final String EXTRA_DATA = "extra_data";

        public static final String USER = "USER";
        public static String USER_NAME = "USER_NAME";
    }

    public static class Screens {
        public static final int SCREEN_LOGIN = 100;
        public static final int SCREEN_FRIEND_LIST = 101;
        public static final int SCREEN_CHAT = 102;
    }

    public static class Notifications {
        public static final String NOTIFICATION_KEY = "NOTIFICATION";
        public static final String NOTIFICATION_TYPE_KEY = "TYPE";
        public static final String NOTIFICATION_TYPE_NEW_MESSAGE = "NEW_MESSAGE";
    }
}
