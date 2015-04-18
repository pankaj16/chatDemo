package com.pankaj.chatapp.util;

import android.util.Log;

public class CommonUtilities {

	// Cloudinary
	public final static String 	CLOUD_NAME = "clairvoyant";
	public final static String API_KEY = "212473473824841";
	public final static String API_SECRET = "ptXZyXWy_00nYVhsyVfJTCo329U";
	public final static String publicID = "public_id";
	public final static String imageUrl = "url";
	public final static String imageName = "name";

	//Api Authorization
	public final static String AUTHORIZATION = "97c8683fbf7e335e7d2948372d956267";
	
	// public static final String URL_SignalR =
	// "http://friendchat-001-site1.myasp.net/signalr/";

	public static final String URL_SignalR = "http://chatdemo716-001-site1.myasp.net/signalr/";

	public static final String Host_Url = "http://androidapp.byethost10.com/task_manager/v1";

	public static String Query_String = "";

	public static String toUser = "";

	public static final String HubName = "UpdateLocationHub";

	public static final String Method_Name = "methodName";

	public static String ChatHead_Info = "";

	public static String ConnectedUser = "?";

	public static String DisConnectedUser = "?";

	public static final String Connected = "connected";

	public static final String DisConnected = "Disconnected";

	public static String Connected_Client_Identical_Previous = "";

	public static String DisConnected_Client_Identical_Previous = "";

	public static final String Broadcast_Action = "com.pankaj.chatdemo.BROADCAST";

	public static final String Broadcast_Action_Online = "com.pankaj.chatdemo.ONLINE_BROADCAST";

	public static final String Broadcast_Message1 = "message1";

	public static final String Broadcast_Message2 = "message2";

	public static final String Broadcast_ObjData = "objData";

	public static final String Broadcast_ObjReference = "objReference";

	public static final String Parameter1 = "param1";

	public static final String Parameter2 = "param2";

	public static final String Parameter3 = "param3";

	public static final String Parameter4 = "param4";

	public static final String OnReceive = "onReceive";

	public static final String Image_Upload = "upload_image";

	public static final String Image_get = "get_image";

	public static final String delete_image = "delete_image";

	public static final String Receive_Image = "receiveImage";

	public static final String Param_Image = "image";
	
	public static final String UPLOAD_IMAGE = "image";
	
	public static final String IMAGE_VIA_NAME = "image_via_name";
	
	public static final String IMAGE_DELETE = "image_delete";
	
	public static final String Image_Saved_Path_Directory = "ChatApp";

	public static final String SendReceiveImage_Receive = "receiveImage";

	public static final String SendReceiveImage_Send = "sendImage";

	public static final String Image_Upload_Message = "Image sent successfully";

	public static final String Successful_Image_Upload = "Uploaded";

	public static String Sender_Message_Client_Identical_Previous = "";

	public static String Sender_Message_Identical_Previous = "";

	public static String Sender_Image_Client_Identical_Previous = "";

	public static String Sender_Image_Identical_Previous = "";

	public static String Sender_Browser_Client_Identical_Previous = "";

	public static String Sender_Browser_Message_Identical_Previous = "";

	public static final String Image_Downloading = "Downloading";

	public static final String Image_Uploading = "Uploading";

	public static final String Image_View = "ViewImage";

	public static final String Image_Error = "ImagError";

	public String Save_WebServiceUrl = "saveWebserviceUrl";

	public String WebServiceUrl = "WebserviceUrl";

	public String Save_SignalRUrl = "saveSignalRUrl";

	public String SignalRUrl = "SignalRUrl";

	public static final String getExistingUser = "users_list";

	public static final String addUser = "users_list";

	public static final String updateUser = "users_list_update";

	public static final String getOnlineUserList = "users_list";

	public void printToLog(String className, String methodName, Exception error) {
		System.out.println("Class: " + className + "\n Method: " + methodName
				+ "\n Error: " + error.toString() + "\n Error Message: "
				+ error.getMessage());
		Log.d("Error", "Class: " + className + "\n Method: " + methodName
				+ "\n Error: " + error.toString() + "\n Error Message: "
				+ error.getMessage());
	}
}
