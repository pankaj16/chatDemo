package com.pankaj.chatapp.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Base64EncodeDecode {

	public Bitmap StringToBitMap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String BitmapToString(Bitmap bitmap){
		try{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] bytearray = baos.toByteArray();

		// saveToCard(bytearray); //Method to save compressed image to check

		return Base64.encodeToString(bytearray,
				Base64.DEFAULT);
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
		
	}
}