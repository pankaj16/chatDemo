package com.pankaj.chatapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.pankaj.chatapp.LoginActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter.LengthFilter;
import android.util.Log;

public class ImageUtility {

	Context context;

	public ImageUtility() {
	}

	public ImageUtility(Context context) {
		this.context = context;
	}

	public Bitmap drawableToBitmap(Drawable d) {
		Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
		return bitmap;
	}

	public Bitmap decodeFile(String path, int size) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			 o.inJustDecodeBounds = true; //this is for not assigning same bitmap and loading image in memory
			System.out.println(">>>Path");
			System.out.println(">>>Path=" + path);
			BitmapFactory.decodeFile(path, o);
			// The new size we want to scale to
			final int REQUIRED_SIZE = size;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			long length = 0;
			try {
				File file = new File(path);
				length = file.length();
				length = length / 1024; // gives length in kb
			} catch (Exception e) {
				System.out.println("File not found : " + e.getMessage() + e);
			}

			if (length > 300) {
				// while (o.outWidth / scale / 2 >= REQUIRED_SIZE
				// && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
				// scale *= 2;
				// }
				int width_tmp = o.outWidth, height_tmp = o.outHeight;
				while (true) {
					if (width_tmp / 2 < REQUIRED_SIZE
							&& height_tmp / 2 < REQUIRED_SIZE)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
			}

			if (length > 300) {
				// Decode with inSampleSize
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;
				return BitmapFactory.decodeFile(path, o2);
			}else{
				o.inJustDecodeBounds = false;
				o.inSampleSize = 1;
				o.inPreferredConfig = Bitmap.Config.ARGB_8888;
				return BitmapFactory.decodeFile(path, o);
			}
			
		} catch (Exception e) {
			System.out.println(">>>Exception>>>" + e.toString()
					+ ">>>Message>>>" + e.getMessage());
		}
		return null;

	}

	public void saveToCard(byte[] bytearray, String name) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytearray, 0,
				bytearray.length);
		saveToCard(bitmap, name);
	}

	public void saveToCard(Bitmap bitmap, String name) {

		try {
			File dir = new File(Environment.getExternalStorageDirectory() + "/"
					+ CommonUtilities.Image_Saved_Path_Directory + "/");

			if (!dir.exists()) {
				dir.mkdir();
			}

			File file = new File(dir, name + ".png");

			try {
				// if (!newfile.mkdir()) {
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				// }else{
				// newfile.createNewFile();
				// }

			} catch (IOException e) {
				e.printStackTrace();
			}

			OutputStream stream = null;
			try {
				stream = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			bitmap.compress(CompressFormat.PNG, 100, stream);
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			notifyMediaScannerService(context, file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void notifyMediaScannerService(Context context, String path) {
		MediaScannerConnection.scanFile(context, new String[] { path }, null,
				new MediaScannerConnection.OnScanCompletedListener() {
					public void onScanCompleted(String path, Uri uri) {
						Log.i("ExternalStorage", "Scanned " + path + ":");
						Log.i("ExternalStorage", "-> uri=" + uri);
					}
				});
	}

}
