package com.pankaj.chatapp;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.pankaj.chatapp.entities.Location;
import com.pankaj.chatapp.entities.ResponseResult;
import com.pankaj.chatapp.entities.User;
import com.pankaj.chatapp.service.DataEngine;
import com.pankaj.chatapp.service.RestfulWebService;
import com.pankaj.chatapp.service.SignalR;
import com.pankaj.chatapp.util.CommonUtilities;
import com.pankaj.chatapp.util.GetPhoneDetails;
import com.pankaj.chatapp.util.SharedPreferencesUtil;
import com.utility.util.ConnectionDetector;
import com.utility.util.DisplayMessage;

import android.R.bool;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	public static int RESULT_LOAD_IMAGE = 1;

	public SignalR objSignalR;
	public EditText userNameEditText;
	Button joinButton;
	ImageView attachmentImageView;
	Context context;
	HomeActivity objHomeActivity;
	OnlineUsersList objOnlineUsersList;
	public static boolean isBackPressed = false;
	public static String backPressStateString = "Login";

	String userRefString;

	// SharedPreferences sharedPreferences;
	// SharedPreferences.Editor editorSEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		context = LoginActivity.this;
		objSignalR = new SignalR(context);
		objHomeActivity = new HomeActivity(context, objSignalR);
		objOnlineUsersList = new OnlineUsersList(context, objSignalR);

		userNameEditText = (EditText) findViewById(R.id.edittext_login_username);
		joinButton = (Button) findViewById(R.id.button_login_join);
		attachmentImageView = (ImageView) findViewById(R.id.imageview_login_attachment);
		// sharedPreferences = getSharedPreferences("info",
		// Context.MODE_PRIVATE);

		CommonUtilities.toUser = "";

		String user = new SharedPreferencesUtil(LoginActivity.this).getString(
				"username", "");
		if (!user.equals("")) {
			CommonUtilities.Query_String = user;
			// new HomeActivity(context, objSignalR).createLayout(false);
			new OnlineUsersList(context, objSignalR).createLayout(false);
		} else {

			getExistingUser();
		}

		joinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (userNameEditText.getText().toString().trim().equals("")) {
					new DisplayMessage(context, "Error",
							"Please enter a username.");
					return;
				}

				if (!new ConnectionDetector(context).isConnectingToInternet()) {
					new com.utility.util.DisplayMessage(context, "Error",
							"Please ensure that you are connected to the internet");
					return;
				}

				// String userRefString = new
				// GetPhoneDetails(LoginActivity.this)
				// .getUser_Ref();
				if (userRefString != null) {
					User objUser = new User();
					objUser.user_ref = userRefString;
					objUser.username = userNameEditText.getText().toString();

					final ArrayList<NameValuePair> nvpList = new ArrayList<NameValuePair>();
					nvpList.add(new BasicNameValuePair("username",
							userNameEditText.getText().toString()));
					nvpList.add(new BasicNameValuePair("user_ref",
							userRefString));

					// new RestfulWebService(LoginActivity.this) {
					// public void onSuccess(String data,
					// com.restservice.HttpResponse response) {
					//
					// new SharedPreferencesUtil(LoginActivity.this).putString(
					// "username", userNameEditText.getText().toString());
					// CommonUtilities.Query_String = userNameEditText.getText()
					// .toString();
					// doLogin();
					// };
					// }.serviceCall(CommonUtilities.addUser, "", nvpList);

					new BackgroundNetwork(LoginActivity.this) {
						ResponseResult objResponseResult = new ResponseResult();

						protected Void doInBackground(Void[] params) {
							DataEngine objDataEngine = new DataEngine(
									LoginActivity.this);
							objResponseResult = objDataEngine
									.addUserToList(nvpList);
							return null;
						};

						protected void onPostExecute(Void result) {
							super.onPostExecute(result);
							if (objResponseResult.Success
									.equalsIgnoreCase("true")) {
								new SharedPreferencesUtil(LoginActivity.this)
										.putString("username", userNameEditText
												.getText().toString());
								CommonUtilities.Query_String = userNameEditText
										.getText().toString();
								doLogin();
							} else {
								new DisplayMessage(LoginActivity.this, "Error",
										objResponseResult.message);
							}
						};
					}.execute();
				}

			}
		});
	}

	public void doLogin() {

		new BackgroundNetwork(context) {
			protected Void doInBackground(Void[] params) {
				objSignalR.startSignalrConnection();
				return null;
			};

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);

				// objHomeActivity.createLayout(true);
				objOnlineUsersList.createLayout(false);
			};
		}.execute();
	}

	@Override
	protected void onResume() {
		isBackPressed = false;
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		if (backPressStateString.equalsIgnoreCase("OnlineUser")) {
			objOnlineUsersList.createLayout(false);
		} else {
			isBackPressed = true;
			updateUser();
			if (objSignalR != null) {
				if (objSignalR.connection != null) {
					// objSignalR.connection.stop();
					objSignalR.connection.disconnect();
					objSignalR.connection.stop();
					
				}
			}
			finish();
		}
		// super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		if (objSignalR != null) {
			if (objSignalR.connection != null) {
				// objSignalR.connection.stop();
				objSignalR.connection.disconnect();
				objSignalR.connection.stop();
				
			}
		}
		updateUser();
		finish();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			objHomeActivity.onSelectingImageFromGallery(picturePath);
		}
	}

	private void getExistingUser() {
		userRefString = new GetPhoneDetails(LoginActivity.this).getUser_Ref();
		if (userRefString != null) {
			new RestfulWebService(LoginActivity.this) {
				public void onSuccess(String data,
						com.restservice.HttpResponse response) {
					String username = "";

					JSONObject json;
					try {
						json = new JSONObject(data);
						username = json.getString("username");
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (!username.equals("")) {
						Resources res = getResources();
						String text = String
								.format(res.getString(R.string.existing_user),
										username);

						Toast.makeText(LoginActivity.this, text,
								Toast.LENGTH_LONG).show();
						CommonUtilities.Query_String = username;
						new SharedPreferencesUtil(LoginActivity.this)
								.putString("username", username);
						// new HomeActivity(context, objSignalR)
						// .createLayout(true);
						doLogin();
					}
				};
			}.getUserName(CommonUtilities.getExistingUser,
					userRefString, null);
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
}
