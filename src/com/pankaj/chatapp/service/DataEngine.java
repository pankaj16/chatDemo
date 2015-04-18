package com.pankaj.chatapp.service;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import com.pankaj.chatapp.LoginActivity;
import com.pankaj.chatapp.entities.MessageList;
import com.pankaj.chatapp.entities.ResponseResult;
import com.pankaj.chatapp.util.CommonUtilities;
import com.pankaj.chatapp.util.SharedPreferencesUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class DataEngine {

	Context context;	
	String urlString;
	
	
	public DataEngine(Context context) {
		this.context = context;
		
		String isSavedUrl = new SharedPreferencesUtil(context).getString(new CommonUtilities().Save_WebServiceUrl, "false");
		if(isSavedUrl.equalsIgnoreCase("true")){
			urlString = new SharedPreferencesUtil(context).getString(new CommonUtilities().WebServiceUrl, "");
		}else{
			urlString = CommonUtilities.Host_Url;
		}
	}

	public Bitmap getImage(String imageName) {
		urlString = urlString + "/"+ CommonUtilities.Image_get +"/"+imageName;
		RestfulWebService restfulWebService = new RestfulWebService();
		DataParser dataParser = new DataParser();
		return dataParser.getImage(restfulWebService.get(urlString));
	}
	
	public String deleteImage(String imageName) {
//		urlString = urlString + "/"+ CommonUtilities.Image_delete +"/"+imageName;
		urlString = urlString + "/"+ CommonUtilities.IMAGE_DELETE +"/"+imageName;
		RestfulWebService restfulWebService = new RestfulWebService();
		DataParser dataParser = new DataParser();
		return dataParser.deleteImage(restfulWebService.get(urlString));
	}
	
	public String uploadImage(ArrayList<NameValuePair> nvp) {
		//old code without cloudinary
//		urlString = urlString + "/"+ CommonUtilities.Image_Upload;
		
		urlString = urlString + "/"+ CommonUtilities.UPLOAD_IMAGE;
		RestfulWebService restfulWebService = new RestfulWebService();
		DataParser dataParser = new DataParser();
		return dataParser.uploadImage(restfulWebService.post(urlString,nvp));
	}
	
	public ResponseResult addUserToList(ArrayList<NameValuePair> nvp) {
		urlString = urlString + "/"+ CommonUtilities.addUser;
		RestfulWebService restfulWebService = new RestfulWebService();
		DataParser dataParser = new DataParser();
		return dataParser.addUserToList(restfulWebService.post(urlString,nvp));
	}
	
	public ResponseResult updateUserToList(ArrayList<NameValuePair> nvp) {
		urlString = urlString + "/"+ CommonUtilities.updateUser;
		RestfulWebService restfulWebService = new RestfulWebService();
		DataParser dataParser = new DataParser();
		return dataParser.updateUserToList(restfulWebService.post(urlString,nvp));
	}
	
}
