package com.pankaj.chatapp.adapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import com.cloudinary.Cloudinary;
import com.google.gson.Gson;
import com.pankaj.chatapp.ImageShowActivity;
import com.pankaj.chatapp.R;
import com.pankaj.chatapp.entities.Images;
import com.pankaj.chatapp.entities.Location;
import com.pankaj.chatapp.entities.MessageList;
import com.pankaj.chatapp.service.DataEngine;
import com.pankaj.chatapp.service.RestfulWebService;
import com.pankaj.chatapp.service.SignalR;
import com.pankaj.chatapp.util.BackgroundNetworkNoLoading;
import com.pankaj.chatapp.util.CommonUtilities;
import com.pankaj.chatapp.util.ImageUtility;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MessageListAdapter extends BaseAdapter {

	Context context;
	ArrayList<MessageList> messageLists;
	LayoutInflater inflater;
	String typeString;
	SignalR objSignalR;
	
	String deleteImageResult;

	public MessageListAdapter(Context context, ArrayList<MessageList> list,
			SignalR objSignalR) {
		this.context = context;
		messageLists = list;
		this.objSignalR = objSignalR;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return messageLists.size();
	}

	@Override
	public Object getItem(int position) {
		return messageLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// View row = convertView;
		// if (row == null) {
		// row = inflater.inflate(R.layout.message_list_item, parent, false);
		//
		// }
		//
		// try {
		// TextView messageTextView = (TextView) row
		// .findViewById(R.id.textview_message);
		//
		// messageTextView.setText(arrayList.get(position));
		// } catch (Exception e) {
		// System.out.println(">>>Exception " + e + "  >>> Message : "
		// + e.getMessage());
		// }

		ViewHolder viewHolder = null;
		typeString = messageLists.get(position).getTypeString();

		// if (convertView == null) {

		if (typeString.equalsIgnoreCase("message")) {
			convertView = inflater.inflate(R.layout.message_list_item, null);
		} else if (typeString.equalsIgnoreCase("image")) {
			convertView = inflater.inflate(
					R.layout.message_list_progress_layout, null);
		}

		if (typeString.equalsIgnoreCase("message")) {
			TextView messageTextView = (TextView) convertView
					.findViewById(R.id.textview_message);
			viewHolder = new ViewHolder(messageTextView);
		} else if (typeString.equalsIgnoreCase("image")) {
			ImageView image = (ImageView) convertView
					.findViewById(R.id.imageview_messagelist_image);
			ProgressBar progress = (ProgressBar) convertView
					.findViewById(R.id.progress_messagelist_progress);
			TextView text = (TextView) convertView
					.findViewById(R.id.textview_messagelist_filename);
			viewHolder = new ViewHolder(image, progress, text);
		}

		convertView.setTag(viewHolder);

		// } else {
		// viewHolder = (ViewHolder) convertView.getTag();
		// }

		if (typeString.equalsIgnoreCase("message")) {
			viewHolder.getText().setText(
					messageLists.get(position).getMessageString());
		} else if (typeString.equalsIgnoreCase("image")) {
			// viewHolder.getImage().setImageBitmap(messageLists.get(position).getImageBitmap());
			final ImageView view = viewHolder.image;
			final ProgressBar progress = viewHolder.getProgress();
			final TextView text = viewHolder.getText();

			if (messageLists.get(position).sendReceiveString
					.equalsIgnoreCase(CommonUtilities.SendReceiveImage_Send)) {
				if (messageLists.get(position).messageString
						.equalsIgnoreCase(CommonUtilities.Image_Uploading)) {
					text.setText("Uploading...");
					progress.setVisibility(View.VISIBLE);
					view.setEnabled(false);
					view.setImageBitmap(messageLists.get(position).imageBitmap);
				} else if (messageLists.get(position).messageString
						.equalsIgnoreCase(CommonUtilities.Successful_Image_Upload)) {
					view.setEnabled(false);
					progress.setVisibility(View.GONE);
					text.setText(CommonUtilities.Image_Upload_Message);
					view.setImageBitmap(messageLists.get(position).imageBitmap);
				} else if (messageLists.get(position).messageString
						.equalsIgnoreCase(CommonUtilities.Image_Error)) {
					text.setText("Can't upload something is wrong. \n Click on image again to upload.");
					view.setEnabled(true);
					view.setImageBitmap(messageLists.get(position).imageBitmap);
				} else {
					text.setText("Click on image to upload");
					view.setImageBitmap(messageLists.get(position).imageBitmap);
				}

			} else {

				if (messageLists.get(position).messageString
						.equalsIgnoreCase(CommonUtilities.Image_View)) {
					// text.setText(Html
					// .fromHtml("Click on image to View <br> Image saved in ChatApp folder"));
					text.setText("Click on image to View \nImage saved in ChatApp folder");
					try {
						view.setImageBitmap(messageLists.get(position).imageBitmap);
					} catch (Exception e) {
						Log.d("ExceptionLog", "-->MessageListAdapter-->getview"
								+ e.toString() + ", Message;" + e.getMessage());
					}
				} else {

					if (messageLists.get(position).messageString
							.equalsIgnoreCase(CommonUtilities.Image_Downloading)) {
						text.setText("Downloading...");
						view.setEnabled(false);
						progress.setVisibility(View.VISIBLE);
					} else if (messageLists.get(position).messageString
							.equalsIgnoreCase(CommonUtilities.Image_Error)) {
						view.setEnabled(true);
						text.setText("Can't download something is wrong. \n Click on image again to download.");
					} else {
						text.setText("Click on image to download");
					}

				}
			}

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					progress.setVisibility(View.VISIBLE);
					// new CountDownTimer(5000,1000) {
					//
					// @Override
					// public void onTick(long millisUntilFinished) {
					// // TODO Auto-generated method stub
					//
					// }
					//
					// @Override
					// public void onFinish() {
					// progress.setVisibility(View.GONE);
					// view.setImageBitmap(messageLists.get(position).imageBitmap);
					// view.setVisibility(View.VISIBLE);
					// text.setText(messageLists.get(position).messageString);
					// String encodedString = new
					// Base64EncodeDecode().BitmapToString(messageLists.get(position).imageBitmap);
					// sendImage(encodedString);
					// }
					// }.start();

					// view.setVisibility(View.VISIBLE);
					ImageView view = (ImageView) v;

					String tempType = messageLists.get(position).sendReceiveString;
					if (tempType
							.equalsIgnoreCase(CommonUtilities.SendReceiveImage_Send)) {
						if (messageLists.get(position).messageString
								.equals(CommonUtilities.Successful_Image_Upload)) {
							text.setEnabled(false);
						} else {
							text.setText("Uploading...");
							view.setEnabled(false);
							Bitmap bitmap = messageLists.get(position).imageBitmap;
							uploadImageToCloudinary(bitmap, position,
									messageLists.get(position).messageString,
									progress, text, view);
							// sendImage(position,
							// messageLists.get(position).messageString,
							// encodedString, progress, text, view, bitmap);
							messageLists.remove(position);
							messageLists.add(position, new MessageList("image",
									CommonUtilities.Image_Uploading, bitmap,
									CommonUtilities.SendReceiveImage_Send));
							notifyDataSetChanged();
						}
					} else {
						// ViewImage is assigned from getImageFromServer
						// method background network on post
						if (messageLists.get(position).messageString
								.equalsIgnoreCase(CommonUtilities.Image_View)) {
							progress.setVisibility(View.GONE);
							Intent intent = new Intent(context,
									ImageShowActivity.class);
							// imagebitmap is assigned from getImageFromServer
							// method background network on post
							intent.putExtra("BitmapImage",
									messageLists.get(position).imageBitmap);
							context.startActivity(intent);
						} else {
							text.setText("Downloading...");
							getLinkFromServer(position,
									messageLists.get(position).messageString,
									progress, text, view);

							messageLists.remove(position);
							Drawable d = context.getResources().getDrawable(
									R.drawable.upload_image_jpeg_icon);
							Bitmap bitmap = new ImageUtility()
									.drawableToBitmap(d);
							messageLists.add(position, new MessageList("image",
									CommonUtilities.Image_Downloading, bitmap,
									CommonUtilities.SendReceiveImage_Receive));
							notifyDataSetChanged();
						}
					}
				}
			});
		}

		return convertView;
	}

	public void sendImage(final int position, final String imageName,
			String urlString, final ProgressBar progressBar,
			final TextView textview, final ImageView imageView,
			final Bitmap bitmap, final String imageId) {

		urlString = urlString.replace("\\/", "\\");
		try {
			byte[] data = urlString.getBytes("UTF-8");
			String encodedUrl = Base64.encodeToString(data, Base64.DEFAULT);
			Calendar c = Calendar.getInstance();
			int hourOfDay, minute, month, year, day;

			day = c.get(Calendar.DAY_OF_MONTH);
			month = c.get(Calendar.MONTH);
			month++;
			year = c.get(Calendar.YEAR);
			hourOfDay = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);

			String date_time;
			if (minute / 10 <= 0) {
				date_time = day + "/" + month + "/" + year + " " + hourOfDay
						+ ":0" + minute;
			} else {
				date_time = day + "/" + month + "/" + year + " " + hourOfDay
						+ ":" + minute;
			}

			ArrayList<NameValuePair> arrayList = new ArrayList<NameValuePair>();
			String tempImageName = imageName;
			tempImageName = tempImageName.replace(" ", "_");
			arrayList.add(new BasicNameValuePair("name", tempImageName));
			arrayList.add(new BasicNameValuePair("publicId", imageId));
			arrayList.add(new BasicNameValuePair("url", encodedUrl));
			arrayList.add(new BasicNameValuePair("created_at", date_time));

			saveImageToDB(arrayList, position, imageName, urlString,
					progressBar, textview, imageView, bitmap, imageId);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveImageToDB(final ArrayList<NameValuePair> arrayList,
			final int position, final String imageName, final String urlString,
			final ProgressBar progressBar, final TextView textview,
			final ImageView imageView, final Bitmap bitmap, final String imageId) {
		new BackgroundNetworkNoLoading(context) {

			String tempString;

			protected Void doInBackground(Void... params) {

				// old code
				// ArrayList<NameValuePair> nvpArrayList = new
				// ArrayList<NameValuePair>();
				// nvpArrayList
				// .add(new BasicNameValuePair("image", encodedString));
				// nvpArrayList.add(new BasicNameValuePair("name", imageName));

				DataEngine objDataEngine = new DataEngine(context);
				tempString = objDataEngine.uploadImage(arrayList);

				System.out.println("");
				return null;
			};

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);

				try {
					if (tempString.equalsIgnoreCase("false")) {
						imageView.setEnabled(false);
						textview.setText(tempString);
						if (objSignalR == null) {
							objSignalR = new SignalR(context);
							objSignalR.startSignalrConnection();
						}
						Location obj = new Location();
						// obj.client = CommonUtilities.Query_String;
						obj.client = CommonUtilities.toUser;
						String tempImageName = imageName;
						tempImageName = tempImageName.replace(" ", "_");
						obj.x = tempImageName;
						obj.y = "image";
						objSignalR.hub.invoke("updateLocation", obj);

						messageLists.remove(position);
						messageLists.add(position, new MessageList("image",
								CommonUtilities.Successful_Image_Upload,
								bitmap, CommonUtilities.SendReceiveImage_Send));
						notifyDataSetChanged();

					} else {
						messageLists.remove(position);
						messageLists.add(position, new MessageList("image",
								CommonUtilities.Image_Error, bitmap,
								CommonUtilities.SendReceiveImage_Send));
						notifyDataSetChanged();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					messageLists.remove(position);
					messageLists.add(position, new MessageList("image",
							CommonUtilities.Image_Error, bitmap,
							CommonUtilities.SendReceiveImage_Send));
					notifyDataSetChanged();
				}
			};
		}.execute();
	}

	private void getLinkFromServer(final int position, final String nameString,
			final ProgressBar progress, final TextView text,
			final ImageView view) {

		new RestfulWebService(context) {
			public void onSuccess(String data,
					com.restservice.HttpResponse response) {

				try {
					Images obj = new Gson().fromJson(data, Images.class);
					getImageFromServer(obj.getUrl(), position, obj.getName(),
							obj.getPublicId(), progress, text, view);
				} catch (Exception e) {
					Toast.makeText(context, e.toString(), Toast.LENGTH_LONG)
							.show();
					e.printStackTrace();
				}
			};

			public void onError(String message,
					com.restservice.HttpResponse respon) {
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
			};
		}.serviceCall(CommonUtilities.IMAGE_VIA_NAME, nameString);
	}

	public void getImageFromServer(String urlString, final int position,
			final String nameString, final String publicId,
			final ProgressBar progress, final TextView text,
			final ImageView view) {
		Log.d("GetImageFromServer", "Inside Method");

		try {
			byte[] data = Base64.decode(urlString, Base64.DEFAULT);
			urlString = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		new RestfulWebService(context) {
			public void onSuccess(Object data,
					com.restservice.HttpResponse response) {
				if (data != null) {
					InputStream inputStream = (InputStream) data;
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					if (bitmap != null) {
						// change sendReceive value from messagelist to open
						// image
						// in new Activity
						deleteImage(nameString, publicId);
						messageLists.remove(position);
						messageLists.add(position, new MessageList("image",
								CommonUtilities.Image_View, bitmap,
								CommonUtilities.SendReceiveImage_Receive));
						view.setImageBitmap(bitmap);
						new ImageUtility(context)
								.saveToCard(bitmap, nameString);

						// messageLists.get(position).setImageBitmap(bitmap);
						notifyDataSetChanged();

						// text.setOnClickListener(new OnClickListener() {
						//
						// @Override
						// public void onClick(View v) {
						//
						// }
						// });
					} else {
						messageLists.remove(position);
						Drawable d = context.getResources().getDrawable(
								R.drawable.upload_image_jpeg_icon);
						Bitmap bmp = new ImageUtility().drawableToBitmap(d);
						messageLists.add(position, new MessageList("image",
								CommonUtilities.Image_Error, bmp,
								CommonUtilities.SendReceiveImage_Receive));
						notifyDataSetChanged();
					}
				}
			};
		}.getImage(urlString);

		// new BackgroundNetworkNoLoading(context) {
		// Bitmap bitmap;
		//
		// protected Void doInBackground(Void... params) {
		// DataEngine objDataEngine = new DataEngine(context);
		// bitmap = objDataEngine.getImage(nameString);
		// return null;
		// };
		//
		// protected void onPostExecute(Void result) {
		// super.onPostExecute(result);
		// progress.setVisibility(View.GONE);
		// if (bitmap != null) {
		// // change sendReceive value from messagelist to open image
		// // in new Activity
		// deleteImage(nameString);
		// messageLists.remove(position);
		// messageLists.add(position, new MessageList("image",
		// CommonUtilities.Image_View, bitmap,
		// CommonUtilities.SendReceiveImage_Receive));
		// view.setImageBitmap(bitmap);
		// new ImageUtility().saveToCard(bitmap, nameString);
		// // messageLists.get(position).setImageBitmap(bitmap);
		// notifyDataSetChanged();
		//
		// // text.setOnClickListener(new OnClickListener() {
		// //
		// // @Override
		// // public void onClick(View v) {
		// //
		// // }
		// // });
		// } else {
		// messageLists.remove(position);
		// Drawable d = context.getResources().getDrawable(
		// R.drawable.upload_image_jpeg_icon);
		// Bitmap bitmap = new ImageUtility().drawableToBitmap(d);
		// messageLists.add(position, new MessageList("image",
		// CommonUtilities.Image_Error, bitmap,
		// CommonUtilities.SendReceiveImage_Receive));
		// notifyDataSetChanged();
		// }
		// };
		//
		// }.execute();
	}

	public void deleteImage(final String imageName, final String publicId) {

		new BackgroundNetworkNoLoading(context) {

			String result = null;

			protected Void doInBackground(Void... params) {
				try {
					Map<String, String> config = new HashMap<String, String>();
					config.put("cloud_name", CommonUtilities.CLOUD_NAME);
					config.put("api_key", CommonUtilities.API_KEY);
					config.put("api_secret", CommonUtilities.API_SECRET);
					Cloudinary cloudinary = new Cloudinary(config);
					result = cloudinary.uploader().destroy(publicId, config)
							.toString();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Void arg) {
				super.onPostExecute(arg);
				try {
					if (result != null) {
						JSONObject jsonObject;
						jsonObject = new JSONObject(result);
						if (jsonObject.getString("result").equalsIgnoreCase(
								"ok")) {
							new BackgroundNetworkNoLoading(context) {
								protected Void doInBackground(Void... params) {
									DataEngine objDataEngine = new DataEngine(
											context);
									deleteImageResult = objDataEngine
											.deleteImage(publicId);
									return null;
								};

								protected void onPostExecute(Void result) {
									super.onPostExecute(result);
									Log.d("***ImageDeleteREsult***", deleteImageResult);
								};
							}.execute();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.execute();

	}

	private void uploadImageToCloudinary(final Bitmap bitmap,
			final int position, final String imageName,
			final ProgressBar progressBar, final TextView textview,
			final ImageView view) {
		new BackgroundNetworkNoLoading(context) {
			Cloudinary cloudinary;
			String data = "";

			protected Void doInBackground(Void... params) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 100, bos);
				byte[] bitmapdata = bos.toByteArray();
				ByteArrayInputStream inputStream = new ByteArrayInputStream(
						bitmapdata);

				Map<String, String> config = new HashMap<String, String>();
				config.put("cloud_name", CommonUtilities.CLOUD_NAME);
				config.put("api_key", CommonUtilities.API_KEY);
				config.put("api_secret", CommonUtilities.API_SECRET);

				try {
					cloudinary = new Cloudinary(config);
					data = cloudinary.uploader()
							.upload(inputStream, Cloudinary.emptyMap())
							.toString();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;

			};

			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				try {
					if (!data.equals("") && data != null) {
						JSONObject objJsonObject = new JSONObject(data);
						sendImage(position, imageName,
								objJsonObject.getString("url"), progressBar,
								textview, view, bitmap,
								objJsonObject.getString("public_id"));
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(context, "Error Occur while uploading",
							Toast.LENGTH_SHORT).show();
				}

			};

		}.execute();

	}

	public class ViewHolder {
		TextView text;
		ImageView image;
		ProgressBar progress;

		public ViewHolder(TextView text) {
			this.text = text;
		}

		public ViewHolder(ImageView image, ProgressBar progress, TextView text) {
			this.image = image;
			this.progress = progress;
			this.text = text;
		}

		public TextView getText() {
			return text;
		}

		public void setText(TextView text) {
			this.text = text;
		}

		public ImageView getImage() {
			return image;
		}

		public void setImage(ImageView image) {
			this.image = image;
		}

		public ProgressBar getProgress() {
			return progress;
		}

		public void setProgress(ProgressBar progress) {
			this.progress = progress;
		}
	}
}
