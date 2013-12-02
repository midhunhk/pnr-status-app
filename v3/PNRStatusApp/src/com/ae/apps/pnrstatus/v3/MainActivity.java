/*
 * Copyright 2013 Midhun Harikumar
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ae.apps.pnrstatus.v3;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ae.apps.pnrstatus.adapters.SectionsPagerAdapter;
import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.ae.apps.pnrstatus.fragments.PnrStatusFragment;
import com.ae.apps.pnrstatus.managers.DataManager;
import com.ae.apps.pnrstatus.service.IStatusService;
import com.ae.apps.pnrstatus.service.StatusServiceFactory;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.DialogUtils;
import com.ae.apps.pnrstatus.utils.Utils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;

/**
 * The Main Activity for the project
 * 
 * @author Midhun
 * 
 */
public class MainActivity extends FragmentActivity implements PnrStatusFragment.OnCheckStatusListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every loaded fragment in memory.
	 * If this becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter	mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager				mViewPager;

	private DataManager				mDataManager;
	private Handler					handler;
	private final int				SETTINGS_REQUEST	= 1001;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the datamanager instance here
		mDataManager = new DataManager(this);

		// Create the adapter that will return a fragment for each sections
		mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Show the middle one by default
		mViewPager.setCurrentItem(1);

		// Create a new Handler object
		handler = new Handler();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			// Start the Settings Activity
			startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_REQUEST);
			return true;
		case R.id.menu_licence:
			// Show the License as a Dialog
			DialogUtils.showLicenseDialog(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void checkStatus(final PNRStatusVo pnrStatusVo, final ProgressBar progressBar) {
		boolean isInternetAvailable = Utils.isInternetAvailable(getApplicationContext());
		if (isInternetAvailable) {
			try {
				if (progressBar != null) {
					progressBar.setIndeterminate(true);
					progressBar.setVisibility(View.VISIBLE);
				}

				// Do network operation in a new thread
				new Thread(new Runnable() {

					@Override
					public void run() {
						Log.d(AppConstants.TAG, "Check the status in a new thread");
						boolean isStub = Utils.isDevelopmentMode();
						IStatusService service;
						try {
							// Read from the preference what service we should use, default to indian rail service
							SharedPreferences preferences = PreferenceManager
									.getDefaultSharedPreferences(getBaseContext());
							String serviceTypeStr = preferences.getString("pref_service",
									StatusServiceFactory.INDIAN_RAIL_SERVICE + "");
							int serviceType = Integer.valueOf(serviceTypeStr);

							// Create the service object from the factory
							service = StatusServiceFactory.getService(serviceType);
							Log.i(AppConstants.TAG, "Using service " + service.getServiceName());
							final PNRStatusVo result = service.getResponse(pnrStatusVo.getPnrNumber(), isStub);
							Log.d(AppConstants.TAG, "got the response");

							// Update the UI from the main thread using the handler
							handler.post(new Runnable() {

								@Override
								public void run() {
									Log.d(AppConstants.TAG, "About to upate the ui");
									mDataManager.update(result);
								}
							});
						} catch (StatusException e) {
							handler.post(new Runnable() {

								@Override
								public void run() {
									// Show a toast with the reason for the Status Exception
									Toast.makeText(getApplicationContext(), R.string.str_error_parse_error,
											Toast.LENGTH_LONG).show();
								}
							});
						} catch (Exception e) {
							e.printStackTrace();
							Log.e(AppConstants.TAG, "error", e);
							Toast.makeText(getApplicationContext(), R.string.str_error_generic_error, Toast.LENGTH_LONG)
									.show();
						} finally {

							// Stop the ProgressBar on the main thread using the handler
							handler.post(new Runnable() {

								@Override
								public void run() {
									stopProgressbar(progressBar);
								}
							});
						}
					}
				}).start();

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "err " + e.getMessage(), Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), R.string.str_error_no_internet, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * This method will be executed to stop the progress bar state
	 * 
	 * @param progressBar
	 */
	private void stopProgressbar(ProgressBar progressBar) {
		if (progressBar != null) {
			progressBar.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public List<PNRStatusVo> getListData() {
		return mDataManager.getDataList();
	}

	@Override
	public void addPnr(PNRStatusVo pnrStatusVo) {
		mDataManager.add(pnrStatusVo);
	}

	@Override
	public void removePnr(PNRStatusVo pnrStatusVo) {
		mDataManager.remove(pnrStatusVo);
	}

	@Override
	public void setPNRStatusAdapter(BaseAdapter adapter) {
		mDataManager.setAdapter(adapter);
	}

	@Override
	public void showPNRStatusInfo(final PNRStatusVo pnrStatusVo) {
		DialogUtils.showPNRStatusInfo(this, pnrStatusVo);
	}

}