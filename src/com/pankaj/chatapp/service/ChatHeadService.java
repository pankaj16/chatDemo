package com.pankaj.chatapp.service;

import com.pankaj.chatapp.R;
import com.pankaj.chatapp.util.CommonUtilities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatHeadService extends Service {
	String from, msg;
	String info;
	private WindowManager windowManager;
	private ImageView chatHead;
	private LinearLayout layout;
	private ImageView closeImageView;
	ImageView image;
	TextView text;
	WindowManager.LayoutParams params;
	private View mChatHead;

	@Override
	public IBinder onBind(Intent intent) {
		// Not used
		// from = intent.getStringExtra("From");
		// msg = intent.getStringExtra("Msg");
		try {
			info = intent.getStringExtra("info");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		chatHead = new ImageView(this);
		chatHead.setImageResource(R.drawable.ic_launcher);

		layout = new LinearLayout(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		mChatHead = inflater.inflate(R.layout.chathead_items_layout, null);
		image = (ImageView) mChatHead
				.findViewById(R.id.imageview_chathead_image);
		text = (TextView) mChatHead.findViewById(R.id.textview_chathead_text);
		closeImageView = (ImageView) mChatHead
				.findViewById(R.id.imageview_chathead_close);

		if (info != null) {
			;
			text.setText(info);
		} else {
			info = CommonUtilities.ChatHead_Info;
			text.setText(info);
			CommonUtilities.ChatHead_Info = "";// making it empty
		}

		params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 100;

		windowManager.addView(mChatHead, params);
		
		closeImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				windowManager.removeView(mChatHead);
				stopSelf();
			}
		});

		mChatHead.setOnTouchListener(new View.OnTouchListener() {
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = params.x;
					initialY = params.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					// Intent i = new
					// Intent(getBaseContext(),MainActivity.class);
					// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
					// Intent.FLAG_ACTIVITY_SINGLE_TOP);
					// i.setClass(getBaseContext(), MainActivity.class);
					// i.putExtra("Msg", msg);
					// i.putExtra("From", from);
					// startActivity(i);
					return true;
				case MotionEvent.ACTION_UP:
//					stopSelf();
					return true;
				case MotionEvent.ACTION_MOVE:
					params.x = initialX
							+ (int) (event.getRawX() - initialTouchX);
					params.y = initialY
							+ (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(mChatHead, params);
					// get hieght and width of screen
//					Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
//							.getDefaultDisplay();
//					int width = display.getWidth();
//					int height = display.getHeight();
//					Log.d("width", width + "");
//					if (width < params.x) {
//						stopSelf();
//					}
					return true;

				}
				return false;
			}

		});

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// from = intent.getStringExtra("From");
		// msg = intent.getStringExtra("Msg");
		try {
			//this method called after oncreate when startservice called
			//And if service is already running then only this method called
			info = intent.getStringExtra("info");
			updateView(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return startId;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mChatHead != null) {
			windowManager.removeView(mChatHead);
		}

	}

	public void updateView(String message) {
		try {
			text.setText(message);
			windowManager.updateViewLayout(mChatHead, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}