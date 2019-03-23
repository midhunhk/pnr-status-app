/*
 * MIT License
 *
 * Copyright (c) 2019 Midhun Harikumar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ae.apps.pnrstatus.v3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

/**
 * @author Midhun
 */
public class MainActivity extends AppCompatActivity
        implements PnrStatusFragment.OnCheckStatusListener {

    private static final String PREF_KEY_SERVICE = "pref_service";
    private static final String DEFAULT_SERVICE =
            String.valueOf(StatusServiceFactory.TRAIN_PNR_STATUS_SERVICE);
    private static final int SETTINGS_REQUEST = 1001;

    private Handler mHandler;
    private DataManager mDataManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = new DataManager(this);

        initViewPager();

        initAds();

        // Create a new Handler object in the main thread
        mHandler = new Handler();
    }

    private void initViewPager() {
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getBaseContext(), getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setCurrentItem(0);

        PagerTabStrip tabStrip = findViewById(R.id.pager_tab_strip);
        tabStrip.setTabIndicatorColorResource(R.color.default_blue);
    }

    private void initAds() {
        String packageName = getApplicationContext().getPackageName();
        // Do not load ads for debug version
        if(!packageName.contains(".debug")){
            MobileAds.initialize(this, getString(R.string.google_admob_app_id) );
            AdView mAdView = findViewById(R.id.adView);
            mAdView.loadAd(new AdRequest.Builder().build());
        }
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
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                            String serviceTypePref = getServiceTypePref(preferences);

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

    private String getServiceTypePref(final SharedPreferences preferences) {
        String serviceTypePref = preferences.getString(PREF_KEY_SERVICE, DEFAULT_SERVICE);

        String validServiceIds[] = getResources().getStringArray(R.array.serviceValues);
        boolean isValidServiceSelected = false;
        for(String validServiceId: validServiceIds){
            if(validServiceId.equals(serviceTypePref)){
                isValidServiceSelected = true;
                break;
            }
        }
        if(!isValidServiceSelected){
            serviceTypePref = DEFAULT_SERVICE;
            preferences
                    .edit()
                    .putString(PREF_KEY_SERVICE, DEFAULT_SERVICE)
                    .apply();
        }
        return serviceTypePref;
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