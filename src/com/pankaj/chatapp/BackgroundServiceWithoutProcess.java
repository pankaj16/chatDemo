package com.pankaj.chatapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class BackgroundServiceWithoutProcess extends AsyncTask<Void, Void, Void> {

	Context context;

	public BackgroundServiceWithoutProcess(Context activity) {
		context = activity;

	}

	protected void doActionOnPostExecuteBeforeProgressDismiss() {

	}

	protected void doActionOnPostExecuteAfterProgressDismiss() {

	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		doActionOnPostExecuteBeforeProgressDismiss();

		doActionOnPostExecuteAfterProgressDismiss();
	}

	public void onPostExecuteDeveloperMethodForPublicAccess(Void result) {
		super.onPostExecute(result);
		doActionOnPostExecuteBeforeProgressDismiss();

		doActionOnPostExecuteAfterProgressDismiss();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected Void doInBackground(Void... params) {
		return null;
	}
	
	

}
