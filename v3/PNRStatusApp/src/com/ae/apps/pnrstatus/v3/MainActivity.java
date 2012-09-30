package com.ae.apps.pnrstatus.v3;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ae.apps.pnrstatus.adapters.PassengerAdapter;
import com.ae.apps.pnrstatus.adapters.SectionsPagerAdapter;
import com.ae.apps.pnrstatus.fragments.PnrStatusFragment;
import com.ae.apps.pnrstatus.managers.DataManager;
import com.ae.apps.pnrstatus.service.IStatusService;
import com.ae.apps.pnrstatus.service.StatusServiceFactory;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.Utils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

public class MainActivity extends FragmentActivity implements PnrStatusFragment.OnCheckStatusListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every loaded fragment in memory.
	 * If this becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter	mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager				mViewPager;

	private DataManager		mDataManager;
	private Dialog			dialog;
	private Handler			handler;
	private ProgressBar		progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the datamanager instance here
		mDataManager = new DataManager(this);

		// Create the adapter that will return a fragment for each of the three primary sections
		// of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// Show the middle one by default
		mViewPager.setCurrentItem(1);

		// Create a new Handler object
		handler = new Handler();

		progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void checkStatus(final PNRStatusVo pnrStatusVo) {
		boolean isInternetAvailable = Utils.isInternetAvailable(getApplicationContext());
		if (isInternetAvailable) {
			try {
				if(progressBar != null){
					progressBar.setIndeterminate(true);
					progressBar.setVisibility(View.VISIBLE);
				}
				
				// Do network operation in a new thread
				new Thread(new Runnable() {

					@Override
					public void run() {
						Log.d(AppConstants.TAG, "pvr of thread");
						boolean isStub = Utils.isDevelopmentMode();
						IStatusService service;
						try {
							Log.d(AppConstants.TAG, "create service and ask for response");
							service = StatusServiceFactory.getService(StatusServiceFactory.INDIAN_RAIL_SERVICE);
							final PNRStatusVo result = service.getResponse(pnrStatusVo.getPnrNumber(), isStub);
							Log.d(AppConstants.TAG, "got the response");
							
							handler.post(new Runnable() {

								@Override
								public void run() {
									Log.d(AppConstants.TAG, "upate the ui here");
									mDataManager.update(result);
								}
							});
						} catch (Exception e) {
							if(progressBar != null){
								progressBar.setVisibility(View.INVISIBLE);
							}
							e.printStackTrace();
							Log.e(AppConstants.TAG, "error", e);
						}
					}
				}).start();

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "err " + e.getMessage(), Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Needs internet connectivity", Toast.LENGTH_LONG).show();
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
		final Context mContext = this;
		// Create the dialog if its not there
		if (dialog == null) {
			// Create a new dialog
			dialog = new Dialog(this, android.R.style.Theme_Holo_Dialog_NoActionBar);
			dialog.setContentView(R.layout.ticket_info);
			dialog.setCancelable(true);

		}
		// Set the data from the dataObject
		TextView pnrNumber = (TextView) dialog.findViewById(R.id.ticket_pnrnumber);
		pnrNumber.setText(pnrStatusVo.getPnrNumber());

		TextView line1 = (TextView) dialog.findViewById(R.id.ticket_info_line1);
		TextView line2 = (TextView) dialog.findViewById(R.id.ticket_info_line2);
		TextView line3 = (TextView) dialog.findViewById(R.id.ticket_info_line3);
		TextView line4 = (TextView) dialog.findViewById(R.id.ticket_info_line4);

		line1.setText("Train : " + pnrStatusVo.getTrainName() + " (" + pnrStatusVo.getTrainNo() + ")");
		line2.setText("Journey Date : " + pnrStatusVo.getTrainJourney());
		line3.setText("Current Status : " + pnrStatusVo.getCurrentStatus());
		line4.setText("Journey : " + pnrStatusVo.getTrainBoard() + " to " + pnrStatusVo.getTrainEmbark());

		// Handle the Share button click
		Button shareButton = (Button) dialog.findViewById(R.id.share_button);
		shareButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.shareStatus(mContext, pnrStatusVo);

			}
		});

		List<PassengerDataVo> passengersList = pnrStatusVo.getPassengers();
		PassengerAdapter adapter = new PassengerAdapter(this, passengersList);
		ListView lv = (ListView) dialog.findViewById(android.R.id.list);
		lv.setAdapter(adapter);

		// Show the dialog
		dialog.show();
	}

}