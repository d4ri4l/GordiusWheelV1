package com.iskandar.gordiuswheel;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    private final String SHARED_PREFS_FILE = "HMPrefs";
    private final int Theme1 = 0;

    private Context mContext;





    public Settings(Context context){
        mContext = context;
    }

    private SharedPreferences getSettings(){
        return mContext.getSharedPreferences(SHARED_PREFS_FILE, 0);
    }

    public int getUserEmail(){
        return getSettings().getInt(null,Theme1);
    }

    public void setUserEmail(int the){
        SharedPreferences.Editor editor = getSettings().edit();
        editor.putInt(null, Theme1);
        editor.commit();
    }


}
