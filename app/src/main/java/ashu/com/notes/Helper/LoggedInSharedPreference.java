package ashu.com.notes.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LoggedInSharedPreference {
    public static final String LOGGED_IN_PREF = "LoggedInStatus";
    public static final String GOOGLE_USER_ID = "GoogleUser";

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static void setLoggedInStatus(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.apply();
    }

    public static boolean getLoggedInStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    public static void setGoogleUserId(Context context, String email) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(GOOGLE_USER_ID, email);
        editor.apply();
    }

    public static String getGoogleUserId(Context context) {
        return getPreferences(context).getString(GOOGLE_USER_ID, "");
    }

}
