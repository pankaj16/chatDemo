package com.pankaj.chatapp.service;

import microsoft.aspnet.signalr.client.Action;

//import com.pankaj.chatapp.MainActivity;
import com.pankaj.chatapp.util.CommonUtilities;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class MessageService extends IntentService{
	
//	public static MainActivity.SignalR objSignalR;
	
	public SignalR objSignalR;
	
	public MessageService(){
		super("Empty Constructor");		
	}

	public MessageService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	// This method is executed in background when this service is started 
	@Override
	protected void onHandleIntent(Intent intent) {
		
		/*
		 *
		 MainActivity myActivity = new MainActivity();
		objSignalR = myActivity.new SignalR();
		*
		*/
		
		//objSignalR = (Signal)intent.getExtras().get("signalr");
		objSignalR.startSignalrConnection();
		
	}
	
    
}
