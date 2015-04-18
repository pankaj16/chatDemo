package com.pankaj.chatapp.service;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.pankaj.chatapp.entities.MessageList;
import com.pankaj.chatapp.entities.ResponseResult;
import com.pankaj.chatapp.util.Base64EncodeDecode;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

public class DataParser {

	Context context;

	public String getStringFromInputStream(InputStream in) {

		if (in == null) {
			;
			return null;
		}

		StringBuffer out = new StringBuffer();
		byte[] b = new byte[1024];
		try {
			for (int i; (i = in.read(b)) != -1;) {
				out.append(new String(b, 0, i));
			}

			String responseString = out.toString();
			if (responseString != null) {
				;
				return out.toString();
			}

		} catch (IOException e) {
			System.out.println(">>> Exception >>> " + e + " >>> Message >>> "
					+ e.getMessage());
		}
		return null;
	}

	public Bitmap getImage(String jsonString) {
		JSONObject objJsonObject;

		try {
			objJsonObject = new JSONObject(jsonString);

			String encodedString = objJsonObject.getString("image");
			return new Base64EncodeDecode().StringToBitMap(encodedString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public Bitmap getImage(InputStream inputStream) {
		return getImage(getStringFromInputStream(inputStream));
	}

	public String deleteImage(String jsonString) {
		JSONObject objJsonObject;

		try {
			objJsonObject = new JSONObject(jsonString);
			String message = objJsonObject.getString("message");
			return message;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String deleteImage(InputStream inputStream) {
		return deleteImage(getStringFromInputStream(inputStream));
	}
	
	public String uploadImage(String jsonString) {
		JSONObject objJsonObject;

		try {
			objJsonObject = new JSONObject(jsonString);
			String message = objJsonObject.getString("error");
			return message;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public String uploadImage(InputStream inputStream) {
		return uploadImage(getStringFromInputStream(inputStream));
	}

	public ResponseResult addUserToList(String jsonString) {
		JSONObject objJsonObject;
		ResponseResult objResponseResult = new ResponseResult();
		try {
			objJsonObject = new JSONObject(jsonString);
			try {
				objResponseResult.Success = objJsonObject.getString("Success");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				objResponseResult.message = objJsonObject.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return objResponseResult;
	}

	public ResponseResult addUserToList(InputStream inputStream) {
		return addUserToList(getStringFromInputStream(inputStream));
	}
	
	public ResponseResult updateUserToList(String jsonString) {
		JSONObject objJsonObject;
		ResponseResult objResponseResult = new ResponseResult();
		try {
			objJsonObject = new JSONObject(jsonString);
			try {
				objResponseResult.Success = objJsonObject.getString("Success");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				objResponseResult.message = objJsonObject.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return objResponseResult;
	}

	public ResponseResult updateUserToList(InputStream inputStream) {
		return updateUserToList(getStringFromInputStream(inputStream));
	}
}
