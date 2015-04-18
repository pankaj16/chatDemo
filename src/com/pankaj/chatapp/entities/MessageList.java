package com.pankaj.chatapp.entities;

import java.io.Serializable;

import android.graphics.Bitmap;

public class MessageList implements Serializable {
	public String typeString;
	public String messageString;
	public Bitmap imageBitmap;
	public String sendReceiveString;

	public MessageList(String typeString, String messageString) {
		this.typeString = typeString;
		this.messageString = messageString;
	}

	public MessageList(String typeString, String messageString, Bitmap imageBitmap, String sendReceiveString) {
		this.typeString = typeString;
		this.messageString = messageString;
		this.imageBitmap = imageBitmap;
		this.sendReceiveString = sendReceiveString;
	}
	
	public String getSendReceiveString() {
		return sendReceiveString;
	}
	
	public void setSendReceiveString(String sendReceiveString) {
		this.sendReceiveString = sendReceiveString;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	public String getMessageString() {
		return messageString;
	}

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}

	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
	}
}
