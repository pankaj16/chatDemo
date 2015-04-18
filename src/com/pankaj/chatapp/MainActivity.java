package com.pankaj.chatapp;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.pankaj.chatapp.R;
import com.pankaj.chatapp.adapter.MessageListAdapter;
import com.pankaj.chatapp.entities.*;
import com.pankaj.chatapp.service.MessageService;
import com.pankaj.chatapp.service.SignalR;
import com.pankaj.chatapp.util.ActivityManagerUtil;
import com.pankaj.chatapp.util.CommonUtilities;
import com.pankaj.chatapp.util.NotificationUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.utility.util.*;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import android.net.Credentials;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint.Join;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView statusField;
	HubConnection connection;
	HubProxy hub;
	SubscriptionHandler subscriptionHandler;
	Button joinButton;
	Button sendButton;
	EditText joinEditText;
	EditText messEditText;
	ListView messageListView;
	String userName;
	String message;
	ArrayList<String> arrayList = new ArrayList<String>();
	MessageListAdapter adapter;

	Intent mServiceIntent;
	MessageReceiver mReceiver;
	IntentFilter mFilter;

	MessageService objMessageService;
	SignalR objSignalR;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		joinEditText = (EditText) findViewById(R.id.edittext_joinname);
		messEditText = (EditText) findViewById(R.id.edittext_message);
		joinButton = (Button) findViewById(R.id.button_join);
		sendButton = (Button) findViewById(R.id.button_send);
		messageListView = (ListView) findViewById(R.id.listview_list);

		// if (Build.VERSION.SDK_INT >= 19) {
		// messageListView.setFastScrollAlwaysVisible(true);
		// }

		new Handler().post(new Runnable() {

			@Override
			public void run() {
				arrayList.add("");
				adapter = new MessageListAdapter(MainActivity.this, arrayList);
				messageListView.setAdapter(adapter);
			}
		});

		sendButton.setEnabled(false);

		// startSignalrConnection();

		// Instantiating BroadcastReceiver
		mReceiver = new MessageReceiver();

		// Creating an IntentFilter with action
		mFilter = new IntentFilter(CommonUtilities.Broadcast_Action);

		// Registering BroadcastReceiver with this activity for the intent
		// filter
		LocalBroadcastManager.getInstance(getApplicationContext())
				.registerReceiver(mReceiver, mFilter);

		joinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (joinEditText.getText().toString().trim().equals("")) {
					showAlertDialog("Please enter a name.");
					return;
				}

				if (!new ConnectionDetector(MainActivity.this)
						.isConnectingToInternet()) {
					new com.utility.util.DisplayMessage(MainActivity.this,
							"Error",
							"Please ensure that you are connected to the internet");
					return;
				}

				CommonUtilities.Query_String = joinEditText.getText()
						.toString();
				// Creating an intent service
				// mServiceIntent = new Intent(getApplicationContext(),
				// MessageService.class);
				// mServiceIntent.putExtra("method", "join");
				// startService(mServiceIntent);
				// objMessageService = new MessageService();
				// objMessageService.startService(mServiceIntent);

				objSignalR = new SignalR(MainActivity.this);

				

				

				new BackgroundNetwork(MainActivity.this){
					protected Void doInBackground(Void[] params) {
						objSignalR.startSignalrConnection();
						return null;						
					};
					
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						sendButton.setEnabled(true);
						joinButton.setEnabled(false);
						joinEditText.setEnabled(false);
					};
				}.execute();
				//
				// View d = getCurrentFocus();
				//
				// if (d == null)
				// return;
				// InputMethodManager inputManager = (InputMethodManager)
				// getSystemService(Context.INPUT_METHOD_SERVICE);
				// inputManager.hideSoftInputFromWindow(d.getWindowToken(),
				// InputMethodManager.HIDE_NOT_ALWAYS);
				//
				// new Handler().post(new Runnable() {
				//
				// @Override
				// public void run() {
				//
				// }
				// });
				//
				// sendButton.setEnabled(true);
				// joinButton.setEnabled(false);
				// joinEditText.setEnabled(false);

			}
		});

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(objSignalR.connection == null){
					;
					showAlertDialog("Some problem, Please exit application and join again.");
					return;
				}
				
				if (messEditText.getText().toString().trim().equals("")) {
					showAlertDialog("Please enter a message to send.");
					return;
				}

				if (!new ConnectionDetector(MainActivity.this)
						.isConnectingToInternet()) {
					new com.utility.util.DisplayMessage(MainActivity.this,
							"Error",
							"Please ensure that you are connected to the internet");
					return;
				}
				
				
				Location obj = new Location();
				obj.client = joinEditText.getText().toString();
				obj.x = messEditText.getText().toString();
				obj.y = "";

				// try {
				View d = getCurrentFocus();
				if (d == null)
					return;
				InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(d.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

				// Main.objSignalR.hub.invoke(
				// "updateLocation", obj).get();
				// MessageService.objSignalR.hub.invoke("updateLocation",
				// obj).get();

				// mServiceIntent = new Intent(getApplicationContext(),
				// MessageService.class);
				// mServiceIntent.putExtra("method", "message");
				// startService(mServiceIntent);
				objSignalR.hub.invoke("updateLocation", obj).done(
						new Action<Void>() {

							@Override
							public void run(Void obj1) throws Exception {

								final Location obj = new Location();
								obj.client = joinEditText.getText().toString();
								obj.x = messEditText.getText().toString();
								obj.y = "";

								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										messEditText.setText("");
										updatingListWithLocation(obj);
									}
								});

							}
						});
			}
		});

	}

	public void updatingListWithLocation(Location obj) {
		try {
			arrayList.add(obj.client + ": " + obj.x);
			// MessageListAdapter adapter = new MessageListAdapter(
			// MainActivity.this, arrayList);
			// messageListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			System.out.println("<<<Exception>>>" + e.toString() + "<<<>>>"
					+ e.getMessage());
		}
	}

	public void updatingListWithUser(String str) {
		try {
			String dateString = new SimpleDateFormat("HH:mm:ss")
					.format(new Date());
			str = str + " connected at " + dateString;
			arrayList.add(str);
			// MessageListAdapter adapter = new MessageListAdapter(
			// MainActivity.this, arrayList);
			// messageListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		} catch (Exception e) {
			System.out.println("<<<Exception>>>" + e.toString() + "<<<>>>"
					+ e.getMessage());
		}
	}

	public void updateListWithBroadcastLocation(final Location obj) {
		if (!new ActivityManagerUtil(MainActivity.this)
				.isActivityForeground("MainActivity")) {
			String msg = obj.client + ": " + obj.x;
			new NotificationUtil(MainActivity.this).generateNotification(msg,
					MainActivity.class);
		}

		try {

//			runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
					updatingListWithLocation(obj);
//				}
//			});

		} catch (Exception e) {
			System.out.println("<<<Exception>>>" + e.toString() + "<<<>>>"
					+ e.getMessage());
		}
	}

	public void updatingListWithBroadcastUser(String str) {
		try {
			Log.d("JSON", str);
			JSONObject jobj = new JSONObject(str);
			JSONArray array = jobj.getJSONArray("A");
			// jobj = array.getJSONObject(0);
			// String str = jobj.toString();
			str = array.getString(0);
			if (str.contains("\"client\":")) {
				return;
			}
			// showAlertDialog(str);

			if (!new ActivityManagerUtil(MainActivity.this)
					.isActivityForeground("MainActivity")) {
				String dateString = new SimpleDateFormat("HH:mm:ss")
						.format(new Date());
				String msg = str;
				msg = msg + " connected at " + dateString;
				new NotificationUtil(MainActivity.this).generateNotification(
						msg, MainActivity.class);
			}

			final String msgOnList = str;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updatingListWithUser(msgOnList);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		 if (objSignalR.connection != null) {
		 objSignalR.connection.stop();
		 }
		 finish();
		super.onBackPressed();
	}
	

	public void showAlertDialog(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
				.create();

		// Setting Dialog Title
		alertDialog.setTitle("Message");

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// if(status != null)
		// // Setting alert dialog icon
		// alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	public void UpdateStatus(String status) {
		Handler handler = new Handler();
		final String fStatus = status;
		handler.post(new Runnable() {
			@Override
			public void run() {
				// statusField.setText( fStatus );
				System.out.println("Status: " + fStatus);
			}
		});

	}

	// Defining a BroadcastReceiver
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(context != null || intent != null){
				return;
			}
			
			try {
				String methodName = intent
						.getStringExtra(CommonUtilities.Broadcast_Message1);
				String data = intent
						.getStringExtra(CommonUtilities.Broadcast_Message2);
				if (methodName.equals(CommonUtilities.OnReceive)) {
//					if (!data.equals(CommonUtilities.Query_String)) {
						updatingListWithBroadcastUser(data);
//					}
				}
			} catch (Exception e) {
				new CommonUtilities().printToLog("MainActivity",
						"Message Receiver-OnReceive", e);
			}

			try {
				Location data = (Location) intent.getExtras().get(
						CommonUtilities.Broadcast_ObjData);
				updateListWithBroadcastLocation(data);
			} catch (Exception e) {
				new CommonUtilities().printToLog("MainActivity",
						"Message Receiver - OnReceive", e);
			}
		}
	}

}
