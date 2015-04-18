package com.pankaj.chatapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DisplayMessage {

	
	public DisplayMessage(Context context, String titleString, String messageString) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
		dlgAlert.setTitle(titleString);
		dlgAlert.setMessage(messageString);
		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}


	public DisplayMessage(Context context, String titleString, int messageId) {
		AlertDialog.Builder dlgAlert = new AlertDialog.Builder(context);
		dlgAlert.setTitle(titleString);
		dlgAlert.setMessage(messageId);
		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}

}
