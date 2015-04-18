package com.pankaj.chatapp.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonElement;
import com.pankaj.chatapp.BackgroundServiceWithoutProcess;
import com.pankaj.chatapp.LoginActivity;
import com.pankaj.chatapp.OnlineUsersList;
import com.pankaj.chatapp.entities.Location;
import com.pankaj.chatapp.util.CommonUtilities;
import com.pankaj.chatapp.util.DisplayMessage;
import com.pankaj.chatapp.util.SharedPreferencesUtil;
import com.utility.util.MainActivity;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.ErrorCallback;
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
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class SignalR implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -601659301481387561L;
	Context context;
	public HubConnection connection;
	public HubProxy hub;
	SubscriptionHandler subscriptionHandler;

	public SignalR(Context context) {
		this.context = context;
	}

	public void joinUser(Location obj) {
		hub.invoke("updateLocation", obj).done(new Action<Void>() {

			@Override
			public void run(Void obj) throws Exception {
				// TODO Auto-generated method stub

			}
		});
	}

	public void startSignalrConnection() {
		Platform.loadPlatformComponent(new AndroidPlatformComponent());
		// Change to the IP address and matching port of your SignalR
		// server.
		// String host = "http://192.168.1.150:8080/";

		String host;

		String isSavedSignalR = new SharedPreferencesUtil(context).getString(
				new CommonUtilities().Save_SignalRUrl, "false");
		if (isSavedSignalR.equalsIgnoreCase("true")) {
			host = new SharedPreferencesUtil(context).getString(
					new CommonUtilities().SignalRUrl, "");
		} else {
			host = CommonUtilities.URL_SignalR; // need to put signalr/
												// for making
												// query string to work
		}

		Logger log1 = new Logger() {

			@Override
			public void log(String message, LogLevel level) {
				// TODO Auto-generated method stub
				// Log.e("Message", message);
			}
		};

		String queryString = "clientName=" + CommonUtilities.Query_String;
		connection = new HubConnection(host, queryString, false, log1);
		// connection = new HubConnection(host);

		hub = connection.createHubProxy(CommonUtilities.HubName);
		hub.subscribe(context);

		connection.received(new MessageReceivedHandler() {

			@Override
			public void onMessageReceived(JsonElement json) {
				// TODO Auto-generated method stub
				// System.out.println(json.toString());

				try {
					// Log.d("JSON", json.toString());
					// JSONObject jobj = new JSONObject(json.toString());
					// JSONArray array = jobj.getJSONArray("A");
					// // jobj = array.getJSONObject(0);
					// // String str = jobj.toString();
					// final String str = array.getString(0);
					// if (str.contains("\"client\":")) {
					// return;
					// }
					// showAlertDialog(str);

					// sendBroadcast(CommonUtilities.OnReceive,
					// json.toString());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

		connection.error(new ErrorCallback() {

			@Override
			public void onError(Throwable error) {
				// new DisplayMessage(context, "Error", error.toString());

				// Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
				Log.d("Error", "Error");
			}
		});

		connection.closed(new Runnable() {

			@Override
			public void run() {
				try {
					// new DisplayMessage(context, "Message", "Closed");
					// Toast.makeText(context, "Closed",
					// Toast.LENGTH_LONG).show();
					Log.d("Message", "Closed");
				} catch (Exception e) {

				}

			}
		});

		connection.reconnected(new Runnable() {

			@Override
			public void run() {
				// new DisplayMessage(context, "Message", "Reconnected");
				// Toast.makeText(context, "Reconnected",
				// Toast.LENGTH_LONG).show();
				Log.d("Message", "Reconnected");
			}
		});

		connection.reconnecting(new Runnable() {

			@Override
			public void run() {
				// new DisplayMessage(context, "Message", "Reconnecting...");
				// Toast.makeText(context, "Reconnecting",
				// Toast.LENGTH_LONG).show();
				Log.d("Message", "Reconnecting");
			}
		});

		hub.on("connectNotify", new SubscriptionHandler1<String>() {
			@Override
			public void run(String user) {
				// Since we are updating the UI,
				// we need to use a handler of the UI thread.
				// final String userString = user;
				// final String msgString = msg;
				Log.e("LocationUpdate", "running...");
				// if (!CommonUtilities.ConnectedUser.equalsIgnoreCase(user)) {

//				sendBroadcast(CommonUtilities.Connected, user);
				checkIfUserAlreadyInList(user);
				// CommonUtilities.ConnectedUser = user;
				// }
				// try {
				//
				// runOnUiThread(new Runnable(){
				// @Override
				// public void run(){
				// updatingList(obj);
				// }
				// });
				//
				// } catch (Exception e) {
				// System.out.println("<<<Exception>>>" + e.toString()
				// + "<<<>>>" + e.getMessage());
				// }
			}
		}, String.class);

		hub.on("disconnectNotify", new SubscriptionHandler1<String>() {
			@Override
			public void run(String user) {

				// Since we are updating the UI,
				// we need to use a handler of the UI thread.
				// final String userString = user;
				// final String msgString = msg;
				Log.e("LocationUpdate", "running...");
				// if (!CommonUtilities.DisConnectedUser.equalsIgnoreCase(user))
				// {
//				sendBroadcast(CommonUtilities.DisConnected, user);
				sendOnlineDeleteBroadcast(user);

				// CommonUtilities.DisConnectedUser = user;
				// }
				// try {
				//
				// runOnUiThread(new Runnable(){
				// @Override
				// public void run(){
				// updatingList(obj);
				// }
				// });
				//
				// } catch (Exception e) {
				// System.out.println("<<<Exception>>>" + e.toString()
				// + "<<<>>>" + e.getMessage());
				// }
			}
		}, String.class);

		hub.on("LocationUpdateNotification",
				new SubscriptionHandler1<Location>() {
					@Override
					public void run(final Location obj) {
						// Since we are updating the UI,
						// we need to use a handler of the UI thread.
						// final String userString = user;
						// final String msgString = msg;
						Log.e("LocationUpdate", "running...");
						try {

							sendBroadcast(obj);

						} catch (Exception e) {
							System.out.println("<<<Exception>>>" + e.toString()
									+ "<<<>>>" + e.getMessage());
						}
					}
				}, Location.class);

		SignalRFuture<Void> awaitConnection = connection.start();

		try {
			awaitConnection.get();
		} catch (InterruptedException e) {
			System.out.println("<<<Exception>>>" + e.toString() + "<<<>>>"
					+ e.getMessage());
		} catch (ExecutionException e) {
			System.out.println("<<<Exception>>>" + e.toString() + "<<<>>>"
					+ e.getMessage());
		}

	}

	public void sendBroadcast(String methodName, String data) {
		if (methodName.equalsIgnoreCase(CommonUtilities.DisConnected)) {
			if (data.equalsIgnoreCase(CommonUtilities.Query_String)) {
				updateUser();
			}
		}

		// Creating an intent for broadcastreceiver
		Intent broadcastIntent = new Intent(CommonUtilities.Broadcast_Action);

		// Attaching data to the intent
		broadcastIntent
				.putExtra(CommonUtilities.Broadcast_Message1, methodName);
		broadcastIntent.putExtra(CommonUtilities.Broadcast_Message2, data);
		// Sending the broadcast
		LocalBroadcastManager.getInstance(context).sendBroadcast(
				broadcastIntent);
	}

	public void sendBroadcast(Location data) {
		if (data.client.equalsIgnoreCase(CommonUtilities.toUser)) {
			// Creating an intent for broadcastreceiver
			Intent broadcastIntent = new Intent(
					CommonUtilities.Broadcast_Action);

			// Attaching data to the intent
			broadcastIntent.putExtra(CommonUtilities.Broadcast_ObjData, data);

			// Sending the broadcast
			LocalBroadcastManager.getInstance(context).sendBroadcast(
					broadcastIntent);
		}
	}

	public void updateUser() {
		final ArrayList<NameValuePair> nvpList = new ArrayList<NameValuePair>();
		nvpList.add(new BasicNameValuePair("username",
				CommonUtilities.Query_String));
		nvpList.add(new BasicNameValuePair("status", "offline"));

		new BackgroundServiceWithoutProcess(context) {
			@Override
			protected Void doInBackground(Void... params) {

				DataEngine objDataEngine = new DataEngine(context);
				objDataEngine.updateUserToList(nvpList);
				return null;
			};

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);

			};

		}.execute();
	}

	public void checkIfUserAlreadyInList(String user) {

		if (!CommonUtilities.Connected_Client_Identical_Previous
				.equalsIgnoreCase(user) && !user.equalsIgnoreCase(CommonUtilities.Query_String)) {
			
			if (!isUserExist(user)) {
				CommonUtilities.Connected_Client_Identical_Previous = user;
				// Creating an intent for broadcastreceiver
				Intent broadcastIntent = new Intent(
						CommonUtilities.Broadcast_Action_Online);

				// Attaching data to the intent
				broadcastIntent.putExtra(CommonUtilities.Broadcast_Message1,
						CommonUtilities.Connected);
				broadcastIntent.putExtra(CommonUtilities.Broadcast_Message2,
						user);
				// Sending the broadcast
				LocalBroadcastManager.getInstance(context).sendBroadcast(
						broadcastIntent);
			}
		}
	}

	public void sendOnlineDeleteBroadcast(String user) {
		if (!CommonUtilities.DisConnected_Client_Identical_Previous
				.equalsIgnoreCase(user)) {
			CommonUtilities.DisConnected_Client_Identical_Previous = user;
			// Creating an intent for broadcastreceiver
			Intent broadcastIntent = new Intent(
					CommonUtilities.Broadcast_Action_Online);

			// Attaching data to the intent
			broadcastIntent.putExtra(CommonUtilities.Broadcast_Message1,
					CommonUtilities.Connected);
			broadcastIntent.putExtra(CommonUtilities.Broadcast_Message2, user);
			// Sending the broadcast
			LocalBroadcastManager.getInstance(context).sendBroadcast(
					broadcastIntent);
		}
	}

	public boolean isUserExist(String user) {
		for (int i = 0; i < OnlineUsersList.userArrayList.size(); i++) {
			if (user.equalsIgnoreCase(OnlineUsersList.userArrayList.get(i).username)) {
				return true;
			}
		}
		return false;
	}

}