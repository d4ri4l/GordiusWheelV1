package com.iskandar.gordiuswheel;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private final String SHARED_PREFS_FILE = "HMPrefs";
    private final String IDD="";

    private Context mContext;





    public Settings(Context context){
        mContext = context;
    }

    public Settings() {

    }

    private SharedPreferences getSettings(){
        return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
    }

    public String getUserEmail(){
        return getSettings().getString(IDD,"");
    }

    public void setUserEmail(String the){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putString(IDD, the);
        editor.commit();
    }


}
