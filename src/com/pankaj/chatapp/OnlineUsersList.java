package com.pankaj.chatapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.pankaj.chatapp.HomeActivity.MessageReceiver;
import com.pankaj.chatapp.adapter.OnlineUserBaseAdapter;
import com.pankaj.chatapp.entities.Location;
import com.pankaj.chatapp.service.DataEngine;
import com.pankaj.chatapp.service.RestfulWebService;
import com.pankaj.chatapp.service.SignalR;
import com.pankaj.chatapp.util.CommonUtilities;
import com.pankaj.chatapp.util.SharedPreferencesUtil;
import com.utility.util.ConnectionDetector;
import com.utility.util.DisplayMessage;
import com.pankaj.chatapp.entities.User;

public class OnlineUsersList {

	Context context;
	SignalR objSignalR;
	HomeActivity objHomeActivity;

	ListView onlineUserListView;
	public static ArrayList<User> userArrayList;
	OnlineUserBaseAdapter adapter;

	Intent mServiceIntent;
	OnlineReceiver mReceiver;
	IntentFilter mFilter;
	
	Button refereshButton;

	public OnlineUsersList(Context context, SignalR objSignalR) {
		this.context = context;
		this.objSignalR = objSignalR;
		userArrayList = new ArrayList<User>();

		// Instantiating BroadcastReceiver
		mReceiver = new OnlineReceiver();

		// Creating an IntentFilter with action
		mFilter = new IntentFilter(CommonUtilities.Broadcast_Action_Online);

		// Registering BroadcastReceiver with this activity for the intent
		// filter
		LocalBroadcastManager.getInstance(
				((LoginActivity) context).getApplicationContext())
				.registerReceiver(mReceiver, mFilter);
	}

	public void createLayout(boolean isNewUser) {
		LoginActivity.backPressStateString = "Login";

		LinearLayout baseLayout = (LinearLayout) ((LoginActivity) context)
				.findViewById(R.id.layout_login_base);
		baseLayout.setGravity(Gravity.TOP);
		LinearLayout inflateLayout = (LinearLayout) ((LoginActivity) context)
				.getLayoutInflater().inflate(R.layout.online_users_list_layout,
						null);
		baseLayout.removeAllViews();
		baseLayout.addView(inflateLayout);
		
		ImageView attachmentImageView = (ImageView) ((LoginActivity) context).findViewById(R.id.imageview_login_attachment);
		attachmentImageView.setVisibility(View.INVISIBLE);

		refereshButton = (Button) ((LoginActivity) context).findViewById(R.id.button_online_users_list_referesh);
		onlineUserListView = (ListView) ((LoginActivity) context)
				.findViewById(R.id.listview_online_userlist_list);

		refereshButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				updateUserList();
			}
		});
		
		onlineUserListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				String username = userArrayList.get(position).username;
				CommonUtilities.toUser = username;
				objHomeActivity = new HomeActivity(context, objSignalR,
						username);
				objHomeActivity.createLayout(false);
			}
		});

		adapter = new OnlineUserBaseAdapter(context, userArrayList);
		onlineUserListView.setAdapter(adapter);
		
		//Adding/Updating users to list
		updateUserList();
		
		if (new ConnectionDetector(context).isConnectingToInternet()) {
			if (!isNewUser) {
				final ArrayList<NameValuePair> nvpList = new ArrayList<NameValuePair>();
				nvpList.add(new BasicNameValuePair("username",
						CommonUtilities.Query_String));
				nvpList.add(new BasicNameValuePair("status", "online"));

				new BackgroundServiceWithoutProcess(context) {
					@Override
					protected Void doInBackground(Void... params) {
						if (objSignalR != null) {
							if (objSignalR.connection != null) {
								objSignalR.connection.stop();
							}
						}

						objSignalR.startSignalrConnection();

						DataEngine objDataEngine = new DataEngine(context);
						objDataEngine.updateUserToList(nvpList);
						return null;
					};

					protected void onPostExecute(Void result) {
						super.onPostExecute(result);

					};

				}.execute();
			}
		}

		

	}
	
	private void updateUserList(){
		new RestfulWebService(context) {
			public void onSuccess(String data,
					com.restservice.HttpResponse response) {
				try {
					JSONObject jsonObject = new JSONObject(data);
					String str = jsonObject.getString("users");
					JSONArray jsonArray = new JSONArray(str);

					// calling server for user list then remove previous one
					userArrayList.clear();
					for (int i = 0; i < jsonArray.length(); i++) {
						String currentItem = jsonArray.getJSONObject(i)
								.toString();
						User objUser = new Gson().fromJson(currentItem,
								User.class);
						if (!objUser.username
								.equalsIgnoreCase(CommonUtilities.Query_String)) {
							userArrayList.add(objUser);
						}
					}

//					adapter = new OnlineUserBaseAdapter(context, userArrayList);
					adapter.notifyDataSetChanged();

				} catch (Exception e) {
					e.printStackTrace();
				}
			};

		}.serviceCallWithObject(CommonUtilities.getOnlineUserList, "", null);
	}

	// Defining a BroadcastReceiver
	public class OnlineReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
//				String methodName = intent
//						.getStringExtra(CommonUtilities.Broadcast_Message1);
//				String data = intent
//						.getStringExtra(CommonUtilities.Broadcast_Message2);
//				if (methodName.equalsIgnoreCase(CommonUtilities.Connected)) {
//					if (!CommonUtilities.Connected_Client_Identical_Previous
//							.equalsIgnoreCase(data) && !data.equalsIgnoreCase(CommonUtilities.Query_String)) {
//						CommonUtilities.Connected_Client_Identical_Previous = data;
//						User objUser = new User();
//						objUser.username = data;
//						userArrayList.add(objUser);
//						adapter.notifyDataSetChanged();
//					}
//				} else {
//					for (int i = 0; i < userArrayList.size(); i++) {
//						if (data.equalsIgnoreCase(userArrayList.get(i).username)) {
//							userArrayList.remove(i);
//							break;
//						}
//						adapter.notifyDataSetChanged();
//					}
//				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
