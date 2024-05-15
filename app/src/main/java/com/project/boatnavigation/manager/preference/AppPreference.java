package com.project.boatnavigation.manager.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.project.boatnavigation.appconstant.Constant;


public class AppPreference implements Constant {


    public static final String MOBILE_NUMBER = "MOBILE_NUMBER";

    public static void saveMobileNumber(String mobNumber, Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(
                APPPREFERENCES, Context.MODE_PRIVATE);
        Editor editor = sharedpreferences.edit();
        editor.putString(MOBILE_NUMBER, mobNumber);
        editor.commit();
    }

    public static String getMobileNumber(Context activity) {
        SharedPreferences sharedpreferences = activity.getSharedPreferences(
                APPPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences.getString(MOBILE_NUMBER, "0");
    }

}
