/*
 * Copyright 2014 Midhun Harikumar
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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ae.apps.pnrstatus.adapters.SectionsPagerAdapter;
import com.ae.apps.pnrstatus.exceptions.InvalidServiceException;
import com.ae.apps.pnrstatus.exceptions.StatusException;
import com.ae.apps.pnrstatus.fragments.PnrStatusFragment;
import com.ae.apps.pnrstatus.managers.DataManager;
import com.ae.apps.pnrstatus.service.IStatusService;
import com.ae.apps.pnrstatus.service.StatusServiceFactory;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.DialogUtils;
import com.ae.apps.pnrstatus.utils.Logger;
import com.ae.apps.pnrstatus.utils.Utils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;

import java.util.List;

/**
 * @author Midhun
 */
public class MainActivity extends FragmentActivity
        implements PnrStatusFragment.OnCheckStatusListener {

    private static final String PREF_KEY_SERVICE = "pref_service";
    private static final String DEFAULT_SERVICE = StatusServiceFactory.PNR_STATUS_SERVICE + "";
    private static final int SETTINGS_REQUEST = 1001;
    //private  Context		mContext			= null;

    private Handler mHandler;
    private DataManager mDataManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = new DataManager(this);

        // Create the adapter that will return a fragment for each sections
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getBaseContext(), getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setCurrentItem(0);

        // Set the tab indicator color to default blue
        PagerTabStrip tabStrip = findViewById(R.id.pager_tab_strip);
        tabStrip.setTabIndicatorColorResource(R.color.default_blue);

        // Create a new Handler object in the main thread
        mHandler = new Handler();
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
    public void checkStatus(final PNRStatusVo pnrStatusVo, final ProgressBar progressBar) {
        boolean isInternetAvailable = Utils.isInternetAvailable(this);
        if (isInternetAvailable) {
            try {
                if (progressBar != null) {
                    // Show the progressbar
                    progressBar.setIndeterminate(true);
                    progressBar.setVisibility(View.VISIBLE);
                }

                // Do network operation in a new thread
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Logger.d(AppConstants.TAG, "Check the status in a new thread");
                        IStatusService service;
                        try {
                            // Read from the preference what service we should use
                            SharedPreferences preferences = PreferenceManager
                                    .getDefaultSharedPreferences(getBaseContext());
                            String serviceTypePref = preferences.getString(PREF_KEY_SERVICE, DEFAULT_SERVICE);

                            // Get an instance of the service object using the factory
                            service = StatusServiceFactory.getService(serviceTypePref);
                            Logger.i(AppConstants.TAG, "Using service " + service.getServiceName());

                            boolean useStub = preferences.getBoolean(AppConstants.PREF_KEY_DEV_STUB, false);
                            final PNRStatusVo result = service.getResponse(pnrStatusVo.getPnrNumber(), useStub);

                            // Update the UI from the main thread using the handler
                            mHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    Log.d(AppConstants.TAG, "About to update the ui");
                                    mDataManager.update(result);
                                }
                            });
                        } catch (StatusException e) {
                            final String exceptionMessage = e.getMessage();
                            mHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    // Show a toast with the reason for the Status Exception
                                    String message = getResources().getString(R.string.str_error_parse_error);
                                    if (AppConstants.IS_DEV_MODE) {
                                        message = message + " " + exceptionMessage;
                                    }
                                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (InvalidServiceException e) {
                            // This shouldn't occur ideally
                            Logger.e(AppConstants.TAG, e.getMessage());
                        } finally {
                            // Stop the ProgressBar on the main thread using the handler
                            mHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    if (progressBar != null) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                        }
                    }
                }).start();
            } catch (Exception e) {
                if (AppConstants.IS_DEV_MODE) {
                    // Detailed error message in case of dev mode
                    Toast.makeText(getBaseContext(), "err " + e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), R.string.str_error_generic_error, Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), R.string.str_error_no_internet, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public List<PNRStatusVo> getListData() {
        return mDataManager.getDataList();
    }

    @Override
    public void addPnr(PNRStatusVo pnrStatusVo) {
        boolean pnrNumberExists = false;
        // Lets see if this pnrNum is already present
        String pnrNumber = pnrStatusVo.getPnrNumber();
        for (PNRStatusVo statusVo : mDataManager.getDataList()) {
            if (pnrNumber.equals(statusVo.getPnrNumber())) {
                pnrNumberExists = true;
                break;
            }
        }
        // Only add this pnrNum if new
        if (pnrNumberExists) {
            Toast.makeText(getApplicationContext(), R.string.str_error_existing_pnr, Toast.LENGTH_LONG).show();
        } else {
            mDataManager.add(pnrStatusVo);
        }
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