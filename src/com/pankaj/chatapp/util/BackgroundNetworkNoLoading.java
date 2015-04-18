package com.pankaj.chatapp.util;

import android.content.Context;
import android.os.AsyncTask;

public class BackgroundNetworkNoLoading extends AsyncTask<Void, Void, Void> {

//	private ProgressDialog dialog;
	Context context;

	public BackgroundNetworkNoLoading(Context activity) {
		context = activity;
//		dialog = new ProgressDialog(context);
	}

	protected void doActionOnPostExecuteBeforeProgressDismiss() {

	}

	protected void doActionOnPostExecuteAfterProgressDismiss() {

	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		doActionOnPostExecuteBeforeProgressDismiss();
//		if (dialog.isShowing()) {
//			dialog.dismiss();
//		}
		doActionOnPostExecuteAfterProgressDismiss();
	}

	public void onPostExecuteDeveloperMethodForPublicAccess(Void result) {
		super.onPostExecute(result);
		doActionOnPostExecuteBeforeProgressDismiss();
//		if (dialog.isShowing()) {
//			dialog.dismiss();
//		}
		doActionOnPostExecuteAfterProgressDismiss();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
//		this.dialog.setMessage("Please Wait");
//		this.dialog.show();
//		this.dialog.setCancelable(false);
	}

	@Override
	protected Void doInBackground(Void... params) {
		return null;
	}
}