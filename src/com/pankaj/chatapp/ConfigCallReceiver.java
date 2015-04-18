package com.pankaj.chatapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConfigCallReceiver extends BroadcastReceiver {

	private static final String OUTGOING_CALL_ACTION = "android.intent.action.NEW_OUTGOING_CALL";
	private static final String INTENT_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";
	public static String phoneNumber = "";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction()
				.equals(ConfigCallReceiver.OUTGOING_CALL_ACTION)) {
			phoneNumber = intent.getExtras().getString(
					ConfigCallReceiver.INTENT_PHONE_NUMBER);
			if (phoneNumber.equals("*#*#7#")) {
				Intent i = new Intent(context, ConfigActivity.class);
				// chatHead.putExtra("Msg", msgBody);
				// chatHead.putExtra("From", msg_from);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				setResultData(null);
				context.startActivity(i);
			}
		}
	}

}
