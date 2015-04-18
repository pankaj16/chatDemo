package com.pankaj.chatapp;

import com.pankaj.chatapp.util.CommonUtilities;
import com.utility.util.DisplayMessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

public class UpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			boolean isConnected = activeNetInfo != null
					&& activeNetInfo.isConnectedOrConnecting();
			if (isConnected){
				Intent broadcastIntent = new Intent(CommonUtilities.Broadcast_Action);

				// Attaching data to the intent
				broadcastIntent
						.putExtra("connect", "connect");
				// Sending the broadcast
				LocalBroadcastManager.getInstance(context).sendBroadcast(
						broadcastIntent);				
			}
			else{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}