package com.ae.apps.pnrstatus;

import com.ae.apps.pnrstatus.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity 
{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set the content view based on the about.xml file 
		setContentView(R.layout.about);
		
		// Read from the strings.xml file
		String appName = getResources().getString(R.string.app_name);
		String appVersion = getResources().getString(R.string.versionName);
		
		// Get hold of the TextViews and insert the values
		TextView appNameText = (TextView) findViewById(R.id.about_app_name);
		TextView appVersionText = (TextView) findViewById(R.id.about_app_version);
		
		appNameText.setText( appName);
		appVersionText.setText("Version : " +  appVersion );
	}

}
