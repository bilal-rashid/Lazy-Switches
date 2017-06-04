package com.menthoven.arduinoandroid.utils;

import android.content.Context;

/**
 * Created by Bilal Rashid on 5/13/2017.
 */

public class AppUtils {
    public static void saveButtonIcon(Context context,String iconKey,int iconValue){
        PrefUtils.persistInt(context,iconKey,iconValue);
    }
    public static int getButtonIcon(Context context,String iconKey){
        return PrefUtils.getInt(context,iconKey,0);
    }

    public static void saveAlarmState(Context context,String key,String value){
        PrefUtils.persistString(context,key,value);
    }
    public static String getAlarmState(Context context,String key){
        return PrefUtils.getString(context,key);
    }
}
