package com.example.foodorder.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class DataLocal {
    private static final String MY_DATA_LOCAL = "MY_DATA_LOCAL";
    private Context context;

    public DataLocal(Context context) {
        this.context = context;
    }

    public void putUser(String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_DATA_LOCAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }

    public String getUser(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_DATA_LOCAL, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public void deleteUser(String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_DATA_LOCAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(key);
        editor.apply();
    }
}
