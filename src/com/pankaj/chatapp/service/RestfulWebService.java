package com.pankaj.chatapp.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import com.pankaj.chatapp.entities.User;
import com.pankaj.chatapp.util.CommonUtilities;
import com.restservice.Http;
import com.restservice.HttpFactory;
import com.restservice.NetworkError;
import com.restservice.ResponseHandler;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class RestfulWebService {

	String authorizationString = CommonUtilities.AUTHORIZATION;

	// ProcessLoaderIndication processDialog;
	ProgressDialog progressDialog;
	Context context;

	public RestfulWebService(Context context) {

		this.context = context;
		// processDialog=new ProcessLoaderIndication(context);
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("Loading...");
	}

	public RestfulWebService() {

	}

	public InputStream post(String url,
			ArrayList<NameValuePair> nameValuePairList) {
		int timeout = 30000;
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Authorization", authorizationString);
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter("http.socket.timeout", timeout);
		httpClient = new DefaultHttpClient();
		StatusLine statusLine;
		AbstractHttpEntity abstractHttpEntity;
		try {
			abstractHttpEntity = new UrlEncodedFormEntity(nameValuePairList,
					HTTP.UTF_8);
			abstractHttpEntity
					.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
			abstractHttpEntity.setContentEncoding("UTF-8");
			httpPost.setEntity(abstractHttpEntity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream inputStream = httpEntity.getContent();
			// Log.d(">>> InputStream >>>", inputStream.toString());
			statusLine = httpResponse.getStatusLine();
			Log.d(">>> Server Response >>>", statusLine.toString());

			// responseCode = httpResponse.getStatusLine().getStatusCode();
			// Log.d(">>> Server Response Code >>>",
			// String.valueOf(responseCode));
			return inputStream;
		} catch (UnsupportedEncodingException e) {
			// this.error = e.getMessage();
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// this.error = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			// this.error = e.getMessage();
			e.printStackTrace();
		}
		return null;
	}

	public InputStream put(String url,
			ArrayList<NameValuePair> nameValuePairList) {
		int timeout = 30000;
		HttpPut httpPut = new HttpPut(url);
		httpPut.addHeader("Authorization", authorizationString);
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter("http.socket.timeout", timeout);
		httpClient = new DefaultHttpClient();
		StatusLine statusLine;
		AbstractHttpEntity abstractHttpEntity;
		try {
			abstractHttpEntity = new UrlEncodedFormEntity(nameValuePairList,
					HTTP.UTF_8);
			abstractHttpEntity
					.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
			abstractHttpEntity.setContentEncoding("UTF-8");
			httpPut.setEntity(abstractHttpEntity);
			HttpResponse httpResponse = httpClient.execute(httpPut);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream inputStream = httpEntity.getContent();
			// Log.d(">>> InputStream >>>", inputStream.toString());
			statusLine = httpResponse.getStatusLine();
			Log.d(">>> Server Response >>>", statusLine.toString());

			// responseCode = httpResponse.getStatusLine().getStatusCode();
			// Log.d(">>> Server Response Code >>>",
			// String.valueOf(responseCode));
			return inputStream;
		} catch (UnsupportedEncodingException e) {
			// this.error = e.getMessage();
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// this.error = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			// this.error = e.getMessage();
			e.printStackTrace();
		}
		return null;
	}

	public InputStream get(String url) {
		int timeout = 30000;
		InputStream inputStream = null;

		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter("http.socket.timeout", timeout);
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Authorization", authorizationString);
		// HttpClient httpClient1 = new DefaultHttpClient();
		// HttpContext localContext = new BasicHttpContext();
		// HttpGet httpGet1 = new
		// HttpGet("http://192.168.1.25:100/dpk/api/comptime");
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet,
					localContext);
			HttpEntity httpEntity = httpResponse.getEntity();
			inputStream = httpEntity.getContent();
			StatusLine statusLine = httpResponse.getStatusLine();
			Log.d(">>> Server Response >>>", statusLine.toString());

			int responseCode = httpResponse.getStatusLine().getStatusCode();
			Log.d(">>> Server Response Code >>>", String.valueOf(responseCode));
			return inputStream;
		} catch (UnsupportedEncodingException e) {
			// this.error = e.getMessage();
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// this.error = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			// this.error = e.getMessage();
			e.printStackTrace();
		}
		return null;
	}

	public void onSuccess(String data, com.restservice.HttpResponse response) {

	}

	public void onSuccess(Object data, com.restservice.HttpResponse response) {

	}

	public void onSuccess(User[] data, com.restservice.HttpResponse response) {

	}

	public void onComplete() {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	public void onError(String message, com.restservice.HttpResponse respon) {

	}

	public void onFailure(NetworkError error) {

	}

	public void getUserName(String resourceString, String extraParam,
			Object object) {
		String urlString = CommonUtilities.Host_Url + "/" + resourceString
				+ "/" + extraParam;
		getExistingUserList(urlString);

	}

	public void serviceCall(String resourceString, String extraParam) {
		String urlString = CommonUtilities.Host_Url + "/" + resourceString
				+ "/" + extraParam;
		if (resourceString.equalsIgnoreCase(CommonUtilities.IMAGE_VIA_NAME)) {
			getImageLink(urlString);
		}
	}

	public void serviceCallWithObject(String resourceString, String extraParam,
			Object object) {

		String urlString = CommonUtilities.Host_Url + "/" + resourceString
				+ extraParam;

		// if(resourceString.equals(CommonUtilities.getExistingUser)){
		// getExistingUserList(urlString);
		// }else
		if (resourceString.equals(CommonUtilities.getOnlineUserList)) {
			getOnlineUserList(urlString);
		}
	}

	public void serviceCall(String resourceString, String extraParam,
			ArrayList<NameValuePair> nvpList) {
		String urlString = CommonUtilities.Host_Url + "/" + resourceString
				+ extraParam;

		if (resourceString.equals(CommonUtilities.addUser)) {
			addUserToList(urlString, nvpList);
		}
	}

	private void getExistingUserList(String url) {
		progressDialog.show();
		Http http = HttpFactory.create(this.context);
		http.get(url).header("Authorization", authorizationString)
				.handler(new ResponseHandler<String>() {
					@Override
					public void success(String data,
							com.restservice.HttpResponse response) {
						onSuccess(data, response);
						super.success(data, response);
					}

					@Override
					public void complete() {
						// TODO Auto-generated method stub
						onComplete();
						super.complete();
					}

					@Override
					public void error(String message,
							com.restservice.HttpResponse response) {
						// TODO Auto-generated method stub
						onError(message, response);
						super.error(message, response);
					}

					@Override
					public void failure(NetworkError error) {
						// TODO Auto-generated method stub
						onFailure(error);
						super.failure(error);
					}

				}).send();

	}

	private void addUserToList(String url, ArrayList<NameValuePair> nvpList) {
		progressDialog.show();
		Http http = HttpFactory.create(this.context);
		http.post(url).data(nvpList)
				.contentType("application/x-www-form-urlencoded")
				.header("Authorization", authorizationString)
				.handler(new ResponseHandler<String>() {
					@Override
					public void success(String data,
							com.restservice.HttpResponse response) {
						onSuccess(data, response);
						super.success(data, response);
					}

					@Override
					public void complete() {
						onComplete();
						super.complete();
					}

					@Override
					public void error(String message,
							com.restservice.HttpResponse response) {
						// TODO Auto-generated method stub
						onError(message, response);
						super.error(message, response);
					}

					@Override
					public void failure(NetworkError error) {
						// TODO Auto-generated method stub
						onFailure(error);
						super.failure(error);
					}

				}).send();

	}

	private void getOnlineUserList(String url) {
		progressDialog.show();
		Http http = HttpFactory.create(this.context);
		http.get(url).header("Authorization", authorizationString)
				.handler(new ResponseHandler<String>() {
					@Override
					public void success(String data,
							com.restservice.HttpResponse response) {

						onSuccess(data, response);
						super.success(data, response);
					}

					@Override
					public void complete() {
						// TODO Auto-generated method stub
						onComplete();
						super.complete();
					}

					@Override
					public void error(String message,
							com.restservice.HttpResponse response) {
						// TODO Auto-generated method stub
						onError(message, response);
						super.error(message, response);
					}

					@Override
					public void failure(NetworkError error) {
						// TODO Auto-generated method stub
						onFailure(error);
						super.failure(error);
					}

				}).send();

	}

	private void getImageLink(String url) {
		Http http = HttpFactory.create(this.context);
		http.get(url).header("Authorization", authorizationString)
				.handler(new ResponseHandler<String>() {
					@Override
					public void success(String data,
							com.restservice.HttpResponse response) {

						onSuccess(data, response);
						super.success(data, response);
					}

					@Override
					public void complete() {
						// TODO Auto-generated method stub
						onComplete();
						super.complete();
					}

					@Override
					public void error(String message,
							com.restservice.HttpResponse response) {
						// TODO Auto-generated method stub
						onError(message, response);
						super.error(message, response);
					}

					@Override
					public void failure(NetworkError error) {
						// TODO Auto-generated method stub
						onFailure(error);
						super.failure(error);
					}

				}).send();

	}

	public void getImage(String url) {
		Http http = HttpFactory.create(this.context);
		http.get(url).handler(new ResponseHandler<InputStream>() {
			@Override
			public void success(InputStream data,
					com.restservice.HttpResponse response) {

				onSuccess(data, response);
				super.success(data, response);
			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				onComplete();
				super.complete();
			}

			@Override
			public void error(String message,
					com.restservice.HttpResponse response) {
				// TODO Auto-generated method stub
				onError(message, response);
				super.error(message, response);
			}

			@Override
			public void failure(NetworkError error) {
				// TODO Auto-generated method stub
				onFailure(error);
				super.failure(error);
			}

		}).send();

	}
}
