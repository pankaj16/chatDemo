package com.pankaj.chatapp;

import com.pankaj.chatapp.util.CommonUtilities;
import com.pankaj.chatapp.util.SharedPreferencesUtil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ConfigActivity extends Activity {

	Button webServiceButton;
	Button signalRButton;
	EditText webServiceEditText;
	EditText signalREditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_activity);
		
		webServiceButton = (Button)findViewById(R.id.button_config_webservice);
		signalRButton = (Button)findViewById(R.id.button_config_signalr);
		webServiceEditText = (EditText)findViewById(R.id.editext_config_webservice);
		signalREditText = (EditText)findViewById(R.id.editext_config_signalr);
		
		webServiceButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SharedPreferencesUtil(ConfigActivity.this).putString(new CommonUtilities().Save_WebServiceUrl, "true");
				
				new SharedPreferencesUtil(ConfigActivity.this).putString(new CommonUtilities().WebServiceUrl, webServiceEditText.getText().toString().trim());
				
			}
		});
		
		signalRButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SharedPreferencesUtil(ConfigActivity.this).putString(new CommonUtilities().Save_SignalRUrl, "true");
				
				new SharedPreferencesUtil(ConfigActivity.this).putString(new CommonUtilities().SignalRUrl, signalREditText.getText().toString().trim());
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}

}
