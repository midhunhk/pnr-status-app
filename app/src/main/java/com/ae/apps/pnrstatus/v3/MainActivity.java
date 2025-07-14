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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.ae.apps.pnrstatus.R;
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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;
import java.util.Objects;

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

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 pager = findViewById(R.id.pager);

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(this);
        pager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, pager,
                (tab, pos) -> tab.setText(adapter.getPageTitle(pos))
        ).attach();

        initAds();

        // Create a new Handler object in the main thread
        mHandler = new Handler();
    }

    private void initAds() {
        String packageName = getApplicationContext().getPackageName();
        // Do not load ads for debug version
        if (!packageName.contains(".debug")) {
            MobileAds.initialize(this);
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
        if (item.getItemId() == R.id.menu_settings) {
            startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_REQUEST);
            return true;
        } else {
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
                new Thread(() -> {
                    Logger.d(AppConstants.TAG, "Check the status in a new thread");
                    IStatusService service;
                    try {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                        String serviceTypePref = getServiceTypePref(preferences);

                        // Get an instance of the service object using the factory
                        service = StatusServiceFactory.INSTANCE.getService(Integer.parseInt(serviceTypePref));
                        Logger.i(AppConstants.TAG, "Using service " + service.getServiceName());

                        boolean useStub = preferences.getBoolean(AppConstants.PREF_KEY_DEV_STUB, false);
                        final PNRStatusVo result = service.getResponse(pnrStatusVo.pnrNumber, useStub);

                        // Update the UI from the main thread using the handler
                        mHandler.post(() -> {
                            Log.d(AppConstants.TAG, "About to update the ui");
                            mDataManager.update(result);
                        });
                    } catch (StatusException e) {
                        final String exceptionMessage = e.getMessage();
                        mHandler.post(() -> {
                            // Show a toast with the reason for the Status Exception
                            String message = getResources().getString(R.string.str_error_parse_error);
                            if (AppConstants.IS_DEV_MODE) {
                                message = message + " " + exceptionMessage;
                            }
                            Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                        });
                    } catch (InvalidServiceException e) {
                        // This shouldn't occur ideally
                        Logger.e(AppConstants.TAG, e.getMessage());
                    } finally {
                        // Stop the ProgressBar on the main thread using the handler
                        mHandler.post(() -> {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
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

        String[] validServiceIds = getResources().getStringArray(R.array.serviceValues);
        boolean isValidServiceSelected = false;
        for (String validServiceId : validServiceIds) {
            if (validServiceId.equals(serviceTypePref)) {
                isValidServiceSelected = true;
                break;
            }
        }
        if (!isValidServiceSelected) {
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
        String pnrNumber = pnrStatusVo.pnrNumber;
        for (PNRStatusVo statusVo : mDataManager.getDataList()) {
            if (Objects.equals(pnrNumber, statusVo.pnrNumber)) {
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