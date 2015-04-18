package com.pankaj.chatapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.widget.ImageView;

public class ImageShowActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_show_layout);

		try {
			Intent intent = getIntent();
			Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
			ImageView imageView = (ImageView) findViewById(R.id.imageview_imageshow_image);
			imageView.setImageBitmap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
