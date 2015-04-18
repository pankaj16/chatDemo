package com.pankaj.chatapp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import com.pankaj.chatapp.adapter.MessageListAdapter;
import com.pankaj.chatapp.entities.Location;
import com.pankaj.chatapp.entities.MessageList;
import com.pankaj.chatapp.service.ChatHeadService;
import com.pankaj.chatapp.service.DataEngine;
import com.pankaj.chatapp.service.MessageService;
import com.pankaj.chatapp.service.SignalR;
import com.pankaj.chatapp.util.ActivityManagerUtil;
import com.pankaj.chatapp.util.BackgroundNetworkNoLoading;
import com.pankaj.chatapp.util.CommonUtilities;
import com.pankaj.chatapp.util.ImageUtility;
import com.pankaj.chatapp.util.NotificationUtil;
import com.utility.util.ConnectionDetector;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity {
	Context context;
	SignalR objSignalR;

	TextView statusField;
	HubConnection connection;
	HubProxy hub;
	SubscriptionHandler subscriptionHandler;
	Button joinButton;
	Button sendButton;
	EditText joinEditText;
	EditText messEditText;
	ListView messageListView;
//	Button uploadImageButton;
	ImageView attachmentImageView;
	String message;
	public static ArrayList<MessageList> arrayList = new ArrayList<MessageList>();
	public static MessageListAdapter adapter;

	Intent mServiceIntent;
	MessageReceiver mReceiver;
	IntentFilter mFilter;

	MessageService objMessageService;
	Location objLocation;

	String typeString;
	String imageFileName;
	String usernameString;

	public HomeActivity(Context context, SignalR objSignalR) {
		this.context = context;
		this.objSignalR = objSignalR;
		objLocation = new Location();
	}
	
	public HomeActivity(Context context, SignalR objSignalR, String username) {
		this.context = context;
		this.objSignalR = objSignalR;
		this.usernameString = username;
		objLocation = new Location();
	}

	public void createLayout(boolean isNewUser) {
		LoginActivity.backPressStateString = "OnlineUser";
		
		LinearLayout baseLayout = (LinearLayout) ((LoginActivity) context)
				.findViewById(R.id.layout_login_base);
		baseLayout.setGravity(Gravity.BOTTOM);
		LinearLayout inflateLayout = (LinearLayout) ((LoginActivity) context)
				.getLayoutInflater().inflate(R.layout.home_layout, null);
		baseLayout.removeAllViews();
		baseLayout.addView(inflateLayout);
		((LoginActivity) context).getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		joinEditText = (EditText) ((LoginActivity) context)
				.findViewById(R.id.edittext_home_joinname);
		messEditText = (EditText) ((LoginActivity) context)
				.findViewById(R.id.edittext_home_message);
		joinButton = (Button) ((LoginActivity) context)
				.findViewById(R.id.button_home_join);
		sendButton = (Button) ((LoginActivity) context)
				.findViewById(R.id.button_home_send);
		messageListView = (ListView) ((LoginActivity) context)
				.findViewById(R.id.listview_home_list);
//		uploadImageButton = (Button) ((LoginActivity) context)
//				.findViewById(R.id.btn);
		attachmentImageView = (ImageView) ((LoginActivity) context).findViewById(R.id.imageview_login_attachment);
		attachmentImageView.setVisibility(View.VISIBLE);
		
		new Handler().post(new Runnable() {

			@Override
			public void run() {
				arrayList.clear();
				CommonUtilities.ConnectedUser = "";
				CommonUtilities.DisConnectedUser = "";
				adapter = new MessageListAdapter(context, arrayList, objSignalR);
				messageListView.setAdapter(adapter);
				messageListView.post(new Runnable() {

					@Override
					public void run() {
						messageListView.setSelection(adapter.getCount() - 1);

					}
				});

			}
		});

//		if (new ConnectionDetector(context).isConnectingToInternet()) {
//			if (!isNewUser) {
//				new BackgroundServiceWithoutProcess(context) {
//					@Override
//					protected Void doInBackground(Void... params) {
//						if (objSignalR != null) {
//							if (objSignalR.connection != null) {
//								objSignalR.connection.stop();
//							}
//						}
//
//						objSignalR.startSignalrConnection();
//						return null;
//					};
//
//					protected void onPostExecute(Void result) {
//						super.onPostExecute(result);
//
//					};
//
//				}.execute();
//			}
//		}

		sendButton.setEnabled(true);

		// startSignalrConnection();

		// Instantiating BroadcastReceiver
		mReceiver = new MessageReceiver();

		// Creating an IntentFilter with action
		mFilter = new IntentFilter(CommonUtilities.Broadcast_Action);

		// Registering BroadcastReceiver with this activity for the intent
		// filter
		LocalBroadcastManager.getInstance(
				((LoginActivity) context).getApplicationContext())
				.registerReceiver(mReceiver, mFilter);
		
		attachmentImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				((LoginActivity) context).startActivityForResult(i,
						LoginActivity.RESULT_LOAD_IMAGE);
			}
		});

//		uploadImageButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// Bitmap bitmap =
//				// BitmapFactory.decodeResource(context.getResources(),
//				// R.drawable.image);
//				// arrayList.add(new MessageList("image", bitmap));
//				// adapter.notifyDataSetChanged();
//				// messageListView.post(new Runnable() {
//				//
//				// @Override
//				// public void run() {
//				// messageListView.setSelection(adapter.getCount() - 1);
//				// }
//				// });
//
//				
//			}
//		});

		joinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (joinEditText.getText().toString().trim().equals("")) {
					showAlertDialog("Please enter a name.");
					return;
				}

				if (!new ConnectionDetector(context).isConnectingToInternet()) {
					new com.utility.util.DisplayMessage(context, "Error",
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

				objSignalR = new SignalR(context);

				new BackgroundNetwork(context) {
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

				if (!new ConnectionDetector(context).isConnectingToInternet()) {
					new com.utility.util.DisplayMessage(context, "Error",
							"Please ensure that you are connected to the internet");
					return;
				}

				try {
					ConnectionState objConnectionState = objSignalR.connection
							.getState();
					if (objConnectionState.toString().equalsIgnoreCase(
							"Disconnected")) {
						objSignalR.startSignalrConnection();
					}

					if (objSignalR.connection == null) {
						;
						showAlertDialog("Some problem, Please exit application and join again.");
						return;
					}

					invokeMethodOnServer();

				} catch (Exception e) {
					startingSignalR();
					Log.d("Exception-->HomeActivity-->SendButton.setOnClick",
							e.toString() + "\n Message:" + e.getMessage());
				}

			}
		});

	}

	public void invokeMethodOnServer() {
		if (messEditText.getText().toString().trim().equals("")) {
			showAlertDialog("Please enter a message to send.");
			return;
		}

		final Location obj = new Location();
		obj.client = CommonUtilities.Query_String;		
		obj.x = messEditText.getText().toString();
		obj.y = "message";

		((LoginActivity) context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				messEditText.setText("");
				updatingListWithLocation(obj);
			}
		});

		// try {
		View d = ((LoginActivity) context).getCurrentFocus();
		if (d == null)
			return;
		InputMethodManager inputManager = (InputMethodManager) ((LoginActivity) context)
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(d.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		
		obj.client = CommonUtilities.toUser;
		objSignalR.hub.invoke("updateLocation", obj);

		// Main.objSignalR.hub.invoke(
		// "updateLocation", obj).get();
		// MessageService.objSignalR.hub.invoke("updateLocation",
		// obj).get();

		// mServiceIntent = new Intent(getApplicationContext(),
		// MessageService.class);
		// mServiceIntent.putExtra("method", "message");
		// startService(mServiceIntent);
		// objSignalR.hub.invoke("updateLocation", obj).done(new Action<Void>()
		// {
		//
		// @Override
		// public void run(Void obj1) throws Exception {
		//
		// // final Location obj = new Location();
		// // obj.client = CommonUtilities.Query_String;
		// // obj.x = messEditText.getText().toString();
		// // obj.y = "";
		// ((LoginActivity) context).runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// messEditText.setText("");
		// updatingListWithLocation(obj);
		// }
		// });
		//
		// }
		// });
	}

	public void onSelectingImageFromGallery(String picturePath) {
		// TODO
		sendButton = (Button) ((LoginActivity) context)
				.findViewById(R.id.button_home_send);
		messageListView = (ListView) ((LoginActivity) context)
				.findViewById(R.id.listview_home_list);
//		uploadImageButton = (Button) ((LoginActivity) context)
//				.findViewById(R.id.btn);

		File file = new File(picturePath);
		imageFileName = file.getName();
		if (imageFileName.contains(".jpg")) {
			imageFileName = imageFileName.replace(".jpg", "");
		} else if (imageFileName.contains(".jpeg")) {
			imageFileName = imageFileName.replace(".jpeg", "");
		} else if (imageFileName.contains(".png")) {
			imageFileName = imageFileName.replace(".png", "");
		}
		Bitmap bitmap = new ImageUtility().decodeFile(picturePath, 400);
		arrayList.add(new MessageList("image", imageFileName, bitmap,
				"sendImage"));
		adapter.notifyDataSetChanged();
		messageListView.post(new Runnable() {

			@Override
			public void run() {
				messageListView.setSelection(adapter.getCount() - 1);
			}
		});
	}

	public void updatingListWithLocation(Location obj) {
		try {

			if (obj.client.equals(objLocation.client)) {
				if (!obj.x.equals(objLocation.x)) {
					objLocation.client = obj.client;
					objLocation.x = obj.x;
					arrayList.add(new MessageList("message", obj.client + ": "
							+ obj.x));
					// MessageListAdapter adapter = new MessageListAdapter(
					// context, arrayList);
					// messageListView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					messageListView.post(new Runnable() {

						@Override
						public void run() {
							messageListView.setSelection(adapter.getCount() - 1);
						}
					});
				}
			} else {
				// if (!obj.x.equals(objLocation.x)) {
				objLocation.client = obj.client;
				objLocation.x = obj.x;
				arrayList.add(new MessageList("message", obj.client + ": "
						+ obj.x));
				// MessageListAdapter adapter = new MessageListAdapter(
				// context, arrayList);
				// messageListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				messageListView.post(new Runnable() {

					@Override
					public void run() {
						messageListView.setSelection(adapter.getCount() - 1);
					}
				});
				// }
			}
		} catch (Exception e) {
			System.out.println("<<<Exception>>>" + e.toString() + "<<<>>>"
					+ e.getMessage());
		}
	}

	public void startingSignalR() {
		new BackgroundServiceWithoutProcess(context) {
			@Override
			protected Void doInBackground(Void... params) {
				if (objSignalR != null) {
					if (objSignalR.connection != null) {
						objSignalR.connection.stop();
					}
				}
				objSignalR.startSignalrConnection();
				return null;
			};

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				invokeMethodOnServer();
			};

		}.execute();
	}

	public void updatingListWithUser(String str) {
		try {
			String dateString = new SimpleDateFormat("HH:mm:ss")
					.format(new Date());
			str = str + " at " + dateString;
			arrayList.add(new MessageList("message", str));
			// MessageListAdapter adapter = new MessageListAdapter(
			// MainActivity.this, arrayList);
			// messageListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			messageListView.post(new Runnable() {

				@Override
				public void run() {
					messageListView.setSelection(adapter.getCount() - 1);
				}
			});
		} catch (Exception e) {
			System.out.println("<<<Exception>>>" + e.toString() + "<<<>>>"
					+ e.getMessage());
		}
	}

	public void updateListWithBroadcastLocation(final Location obj) {
		if (!new ActivityManagerUtil(context)
				.isActivityForeground("LoginActivity")) {
			// this is loginActivity because homeActivity is not activity in
			// real
			String msg = obj.client + ": " + obj.x;
			// new NotificationUtil(context).generateNotification(msg,
			// LoginActivity.class);
			Intent chatHead = new Intent(context, ChatHeadService.class);
			chatHead.putExtra("info", msg);
			CommonUtilities.ChatHead_Info = msg;
			context.startService(chatHead);
		}

		try {

			// runOnUiThread(new Runnable() {
			// @Override
			// public void run() {
			updatingListWithLocation(obj);
			// }
			// });

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

			if (!new ActivityManagerUtil(context)
					.isActivityForeground("LoginActivity")) {
				// this is loginActivity because homeActivity is not activity in
				// real
				String dateString = new SimpleDateFormat("HH:mm:ss")
						.format(new Date());
				String msg = str;
				msg = msg + " connected at " + dateString;
				// new NotificationUtil(context).generateNotification(msg,
				// LoginActivity.class);
			}

			final String msgOnList = str;
			((LoginActivity) context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					updatingListWithUser(msgOnList);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showAlertDialog(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

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

	public void getImageFromSender(final Location data) {
		arrayList.add(new MessageList(CommonUtilities.Param_Image, data.x,
				null, CommonUtilities.Receive_Image));
		adapter.notifyDataSetChanged();
		messageListView.post(new Runnable() {

			@Override
			public void run() {
				messageListView.setSelection(adapter.getCount() - 1);
			}
		});
		// new BackgroundNetworkNoLoading(context){
		// Bitmap bitmap;
		// protected Void doInBackground(Void[] params) {
		// DataEngine objDataEngine = new DataEngine(context);
		// bitmap = objDataEngine.getImage(data.x);
		// return null;
		// };
		//
		// protected void onPostExecute(Void result) {
		// super.onPostExecute(result);
		// if(bitmap != null){
		// arrayList.add(new MessageList("image", data.x,
		// bitmap,"receiveImage"));
		// adapter.notifyDataSetChanged();
		// }
		// };
		//
		// }.execute();
	}

	public void onConnectedShow(String str) {
		if (!new ActivityManagerUtil(context)
				.isActivityForeground("LoginActivity")) {
			// this is loginActivity because homeActivity is not activity in
			// real
			String dateString = new SimpleDateFormat("HH:mm:ss")
					.format(new Date());
			String msg = str;
			msg = msg + " at " + dateString;
			// new NotificationUtil(context).generateNotification(msg,
			// LoginActivity.class);
		}

		final String msgOnList = str;
		((LoginActivity) context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updatingListWithUser(msgOnList);
			}
		});
	}

	public void onDisconnectedShow(String str) {
		if (!new ActivityManagerUtil(context)
				.isActivityForeground("LoginActivity")) {
			// this is loginActivity because homeActivity is not activity in
			// real
			String dateString = new SimpleDateFormat("HH:mm:ss")
					.format(new Date());
			String msg = str;
			msg = msg + " at " + dateString;
			// new NotificationUtil(context).generateNotification(msg,
			// LoginActivity.class);
		}

		final String msgOnList = str;
		((LoginActivity) context).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				updatingListWithUser(msgOnList);
			}
		});
	}

	// Defining a BroadcastReceiver
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				//this is for the updateReceiver BroadcastReceiver class
				//detect n call this when user On the mobile data
				String name = intent.getStringExtra("connect");
				if (name.equalsIgnoreCase("connect")) {
					objSignalR.startSignalrConnection();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				String methodName = intent
						.getStringExtra(CommonUtilities.Broadcast_Message1);
				String data = intent
						.getStringExtra(CommonUtilities.Broadcast_Message2);
				if (methodName.equals(CommonUtilities.OnReceive)) {
					// if (!data.equals(CommonUtilities.Query_String)) {
					updatingListWithBroadcastUser(data);
					// }
				}
				if (methodName.equals(CommonUtilities.Connected)) {
					if (!CommonUtilities.ConnectedUser.equalsIgnoreCase(data)) {
						CommonUtilities.ConnectedUser = data;
						onConnectedShow(data + " connected");
					}
				}

				if (methodName.equals(CommonUtilities.DisConnected)) {
					if (LoginActivity.isBackPressed == false) {
						try {
							ConnectionState objConnectionState = objSignalR.connection
									.getState();
							if (objConnectionState.toString().equalsIgnoreCase(
									"Disconnected")) {
								objSignalR.startSignalrConnection();
							}
						} catch (Exception e) {
							new CommonUtilities().printToLog("HomeActivity",
									"Message Receiver - Disconnected", e);
						}
					} else {
						if (!CommonUtilities.DisConnectedUser
								.equalsIgnoreCase(data)) {
							CommonUtilities.DisConnectedUser = data;
							onDisconnectedShow(data + " disconnected");
						}
					}
				}

			} catch (Exception e) {
				new CommonUtilities().printToLog("HomeActivity",
						"Message Receiver-OnReceive", e);
			}

			try {
				Location data = (Location) intent.getExtras().get(
						CommonUtilities.Broadcast_ObjData);
				// check if data contains "message" or "image" string
				// data.y contains message string means its a message

				if (data.y.equalsIgnoreCase("message")) {
					if (CommonUtilities.Sender_Message_Client_Identical_Previous
							.equalsIgnoreCase(data.client)) {
						if (!CommonUtilities.Sender_Message_Identical_Previous
								.equalsIgnoreCase(data.x)) {
							CommonUtilities.Sender_Message_Client_Identical_Previous = data.client;
							CommonUtilities.Sender_Message_Identical_Previous = data.x;
							updateListWithBroadcastLocation(data);
						}
					} else {
						CommonUtilities.Sender_Message_Client_Identical_Previous = data.client;
						CommonUtilities.Sender_Message_Identical_Previous = data.x;
						updateListWithBroadcastLocation(data);
					}
					// updateListWithBroadcastLocation(data);
				} else if (data.y.equalsIgnoreCase("image")) {

					if (CommonUtilities.Sender_Image_Client_Identical_Previous
							.equals(data.client)) {
						if (!CommonUtilities.Sender_Image_Identical_Previous
								.equalsIgnoreCase(data.x)) {
							CommonUtilities.Sender_Image_Client_Identical_Previous = data.client;
							CommonUtilities.Sender_Image_Identical_Previous = data.x;
							getImageFromSender(data);
						}
					} else {
						CommonUtilities.Sender_Image_Client_Identical_Previous = data.client;
						CommonUtilities.Sender_Image_Identical_Previous = data.x;
						getImageFromSender(data);
					}
					// getImageFromSender(data);
				} else if (data.x.contains("#cd#")) {
					//this #cd# for testing from browser to send image to android client
					//with image name from server
					String temp = data.x.replace("#cd#", "");
					arrayList.add(new MessageList(CommonUtilities.Param_Image,
							temp, null, CommonUtilities.Receive_Image));
					adapter.notifyDataSetChanged();
					messageListView.post(new Runnable() {

						@Override
						public void run() {
							messageListView.setSelection(adapter.getCount() - 1);
						}
					});
				} else if (data.y.equalsIgnoreCase("ylocation")) {
					//this ylocation means simple message coming from browser
					if (CommonUtilities.Sender_Browser_Client_Identical_Previous
							.equals(data.client)) {
						if (!CommonUtilities.Sender_Browser_Message_Identical_Previous
								.equalsIgnoreCase(data.x)) {
							CommonUtilities.Sender_Browser_Client_Identical_Previous = data.client;
							CommonUtilities.Sender_Browser_Message_Identical_Previous = data.x;
							updateListWithBroadcastLocation(data);
						}
					} else {
						CommonUtilities.Sender_Browser_Client_Identical_Previous = data.client;
						CommonUtilities.Sender_Browser_Message_Identical_Previous = data.x;
						updateListWithBroadcastLocation(data);
					}
					// updateListWithBroadcastLocation(data);
				}

			} catch (Exception e) {
				new CommonUtilities().printToLog("HomeActivity",
						"Message Receiver - OnReceive", e);
			}
		}
	}
}
