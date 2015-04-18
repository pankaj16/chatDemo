package com.pankaj.chatapp.util;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class GetPhoneDetails {
	Context context;

	public GetPhoneDetails(Context context) {
		this.context = context;
	}

	public String getUser_Ref() {
		String imeiString = null;
		TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (tManager != null) {
			try {				
				// Getting IMEI Number of Devide
				imeiString = tManager.getDeviceId();
				if (imeiString != null || imeiString.length() > 0) {
					return imeiString;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (imeiString == null || imeiString.length() == 0) {
			try {
				imeiString = Secure.getString(context.getContentResolver(),
						Secure.ANDROID_ID);
				return imeiString;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
