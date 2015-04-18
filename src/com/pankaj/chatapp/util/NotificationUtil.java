package com.pankaj.chatapp.util;

import java.util.Random;

import com.pankaj.chatapp.R;

import android.content.Context;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

public class NotificationUtil {
	Context context;

	public NotificationUtil(Context context) {
		this.context = context;
	}

	public void generateNotification(String message, Class cls) {
		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);

		Intent notificationIntent = new Intent(context, cls);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;

		// notification.sound = Uri.parse("android.resource://" +
		// context.getPackageName() + "your_sound_file_name.mp3");

		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(new Random(System.currentTimeMillis()).nextInt(), notification);

	}

}
