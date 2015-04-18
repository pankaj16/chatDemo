package com.pankaj.chatapp.util;

import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public class ActivityManagerUtil {

	Context context;
	
	public ActivityManagerUtil(Context context) {
		this.context = context;
	}
	
	public boolean isActivityForeground(String activity){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		 List<ActivityManager.RunningTaskInfo> runningTaskInfo = am.getRunningTasks(1); 

		     ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
		     Log.d("cpt", "df");
		   if(componentInfo.getShortClassName().equals("."+activity)){ 
			   return true;
		   }
		return false;
		}

}
