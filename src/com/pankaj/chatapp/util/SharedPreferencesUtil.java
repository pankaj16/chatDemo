package com.pankaj.chatapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtil {
	Context context;
	SharedPreferences sharedPreferences;
	Editor editor;
	
	public SharedPreferencesUtil(Context context){
		this.context = context;
		sharedPreferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}
	
	public void putString(String key, String value){
		editor.putString(key, value);
		editor.commit();
	}
	
	public String getString(String key, String value){
		return sharedPreferences.getString(key, value);
	}
	
}
