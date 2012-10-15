package com.ae.apps.pnrstatus;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.ae.apps.pnrstatus.adapter.PnrRowAdapter;
import com.ae.apps.pnrstatus.db.DataHelper;
import com.ae.apps.pnrstatus.exception.InvalidServiceException;
import com.ae.apps.pnrstatus.exception.StatusException;
import com.ae.apps.pnrstatus.service.IStatusService;
import com.ae.apps.pnrstatus.service.PNRStatus;
import com.ae.apps.pnrstatus.service.StatusServiceFactory;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.R;

public class MainActivity extends ListActivity {
	
	//-------------------------------------------------------------------------
	//
	// Constants
	//
	//-------------------------------------------------------------------------

	private static final int MENU_ABOUT = Menu.FIRST + 1;
	private static final int MENU_PREFERENCE = Menu.FIRST;
	
	//-------------------------------------------------------------------------
	//
	// Variables
	//
	//-------------------------------------------------------------------------
	
	private Handler mHandler;
	private Cursor mNotesCursor;
	private DataHelper mDbHelper;
	private PnrRowAdapter mCustomAdapter;
	private ArrayList<PNRStatusVo> pnrList;
    
	//-------------------------------------------------------------------------
	//
	// Methods
	//
	//-------------------------------------------------------------------------
	
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set the layout
		setContentView(R.layout.main);
		
		// Create a Handler object
		mHandler = new Handler();
		
		// Get the initial list of PNRStatusVos
		mDbHelper = new DataHelper(this);
		mDbHelper.open();
		
		pnrList = getInitialList();
		mCustomAdapter = new PnrRowAdapter(this, pnrList);
		setListAdapter(mCustomAdapter);
		
		try {
			// Get a reference to the EditText Object
			final EditText txtPnrNumber = (EditText) findViewById(R.id.EditText01);
			txtPnrNumber.setOnEditorActionListener( new OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_NEXT)
					{
						InputMethodManager imm = (InputMethodManager) 
							v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						return true;
					}
					return false;
				}
			});

			// Add Click Listener to the add Button
			final Button btnAdd = (Button) findViewById(R.id.add_pnr_btn);
			btnAdd.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Create a PNRStatusVo object
					String pnrNumber = txtPnrNumber.getText().toString();
					
					if( ! pnrNumber.equals("") && 10 == pnrNumber.length())
					{
						PNRStatusVo statusVo = new PNRStatusVo();
						statusVo.setPnrNumber(pnrNumber);
						statusVo.setTrainCurrentStatus("");
						statusVo.setFirstPassengerData(null);
						statusVo.setPassengers(null);
					
						// 	Clear the input
						txtPnrNumber.setText("");
						txtPnrNumber.requestFocus();
						btnAdd.requestFocus();
					
						//Add the PNRStatusVo to the list
						addPnrVo(statusVo);
					}
					else
					{
						String message = "The entered PNR Number " + pnrNumber + " is invalid!";
						Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
					}
				}
			});
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(AppConstants.TAG, "onNewIntent() ");
		// See if a call from an intent has happened
		Bundle extras = intent.getExtras();
		if (null != extras) {
			// Fetch the Action and the PNR number
			final String action 	= extras.getString(AppConstants.APP_ACTION);
			final String pnrNumber 	= extras.getString(AppConstants.PNR_NUMBER);

			// Create a PNRStatusVo Object
			PNRStatusVo statusVo = new PNRStatusVo();
			statusVo.setPnrNumber(pnrNumber);

			// See what the action is
			if (action.equals(AppConstants.ACTION_DELETE)) 
			{
				// Remove the PNRStatusVo from the list
				Log.d(AppConstants.TAG, "Delete : " + statusVo.getPnrNumber());
				removePnrVo(statusVo);
			} 
			else if (action.equals(AppConstants.ACTION_CHECK)) 
			{
				// Check the status for the current PNRStatusVo as a new thread
				Log.d(AppConstants.TAG, "Check Status : " + statusVo.getPnrNumber());
				
				//boolean isNetworked = true;
				boolean isNetworked = isNetworkAvailable();
				if(isNetworked == true)
				{
					final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
					progressBar.setVisibility(View.VISIBLE);
					
					// Perform the operation on a new Thread
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							
							String errorMessage = "";
							try 
							{
								// Get an implementation  for IStatusService from the Factory based 
								// on the current preference
								int serviceType = StatusServiceFactory.INDIAN_RAIL_SERVICE;
								IStatusService pnrService = 
									StatusServiceFactory.getService(serviceType);
								
								Log.d(AppConstants.TAG, "Using Service : " + pnrService.getServiceName());
								final PNRStatusVo pnrStatusVo = pnrService.getResponse(pnrNumber, false);
								pnrStatusVo.setPnrNumber(pnrNumber);
								
								// Update the UI on the Main Thread
								mHandler.post(new Runnable() {
									
									@Override
									public void run() {
										Log.d(AppConstants.TAG, "Call updatePnrVo() : " + pnrStatusVo.getPnrNumber());
										updatePnrVo(pnrStatusVo);
										
										// Notify the adapter of the data change
										Log.d(AppConstants.TAG, "Call notifyDataSetChanged() ");
										mCustomAdapter.notifyDataSetChanged();
									}
								});
							} 
							catch (IOException e) 
							{
								// Exception while reading response from the webserver
								errorMessage = e.getMessage();
								Log.e(AppConstants.TAG, "IOException " + Log.getStackTraceString(e));
								e.printStackTrace();
							}
							catch (JSONException e)
							{
								// Exception while parsing the response
								errorMessage = e.getMessage();
								Log.e(AppConstants.TAG, "JSONException " + e.getMessage());
							}
							catch(InvalidServiceException e)
							{
								// Exception while parsing the response
								errorMessage = e.getMessage();
								Log.e(AppConstants.TAG, "InvalidServiceException " + e.getMessage());
							}
							catch (StatusException e) 
							{
								errorMessage = e.getMessage();
								if(e.getStatusString().equals(PNRStatus.INVALID))
								{
									errorMessage = "The PNR Number is invalid";
								}
								else if(e.getStatusString().equals(PNRStatus.TIMEOUT))
								{
									errorMessage = "The Server has timed out";
								}
								
								Log.e(AppConstants.TAG, "StatusException " + errorMessage);
								Log.e(AppConstants.TAG, " StatusException " + e.getMessage());
								Log.e(AppConstants.TAG, " StatusException " + e.getStatusString());
							}
							finally
							{
								final String reasonString = errorMessage;
								
								// Perform some actions on the UI Thread.
								mHandler.post(new Runnable() {
									
									@Override
									public void run() {
										// Hide the ProgressBar
										progressBar.setVisibility(View.INVISIBLE);
										
										// show a toast with the error reason if its there
										if( reasonString.length() > 0)
										{
											Toast.makeText(getApplicationContext(), reasonString, 
													Toast.LENGTH_LONG).show();
										}
									}
								});
							}
						}
					}).start();
				}
				else
				{
					Log.d(AppConstants.TAG, "No internet connection." );
					Toast.makeText(getApplicationContext(), "Unable to connect to the Internet.", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	/**
	 * Removes a pnrVo from the list
	 * 
	 * @param statusVo
	 */
	private void removePnrVo(PNRStatusVo statusVo) {
		int pos = pnrList.indexOf(statusVo);
		System.out.println(pos);
		for (int i = 0; i < pnrList.size(); i++) {
			PNRStatusVo pnrStatusVo = pnrList.get(i);
			String pnrNumber = pnrStatusVo.getPnrNumber();
			String pnrNumber2 = statusVo.getPnrNumber();
			if (pnrNumber.equals(pnrNumber2)) {
				// Delete
				mDbHelper.deletePnrNumber(pnrStatusVo.getRowId());
				pnrList.remove(i);
				break;
			}
		}

		// Update the list
		mCustomAdapter.notifyDataSetChanged();
	}

	/**
	 * Adds a pnrVo to the list
	 * 
	 * @param statusVo
	 */
	private void addPnrVo(PNRStatusVo statusVo) {
		// Add to the dataSource
		long result = mDbHelper.addPnrNumber(statusVo.getPnrNumber());
		if(result > -1)
		{
			statusVo.setRowId(result);
			pnrList.add(statusVo);
			
			// refresh the ListAdapter
			mCustomAdapter.notifyDataSetChanged();
			Log.i(AppConstants.TAG, "Added " + statusVo.getPnrNumber());
		}
		else
		{
			Log.e(AppConstants.TAG, "Error in inserting row.");
		}
	}
	
	/**
	 * Updates the statusVo in the list
	 * @param statusVo
	 */
	private void updatePnrVo(PNRStatusVo statusVo)
	{
		Log.d(AppConstants.TAG, "Update PNR ");
		for (int i = 0; i < pnrList.size(); i++) {
			PNRStatusVo pnrStatusVo = pnrList.get(i);
			String pnrNumber = pnrStatusVo.getPnrNumber();
			String pnrNumber2 = statusVo.getPnrNumber();
			Log.d(AppConstants.TAG, "pnrNumber: " + pnrNumber + "; pnrNumber2: " + pnrNumber2);
			if (pnrNumber.equals(pnrNumber2)) {
				pnrList.set(i, statusVo);
				Log.d(AppConstants.TAG, "Item to update found at " + i);
				break;
			}
		}
	}
	
	/**
	 * This method returns the initial list of PNRNumbers
	 * @return
	 */
	private ArrayList<PNRStatusVo> getInitialList() {
		ArrayList<PNRStatusVo> results = new ArrayList<PNRStatusVo>();
		
		// Get rows from the database
		mNotesCursor = mDbHelper.fetchAllPnrNumbers();
        startManagingCursor(mNotesCursor);
        
        // Create a list of PNRStatusVos from the cursor
        PNRStatusVo statusVo = null;
        if(null != mNotesCursor)
        {
	        while( mNotesCursor.moveToNext() )
	        {
	        	statusVo = new PNRStatusVo(); 
	    		statusVo.setPnrNumber( mNotesCursor.getString(1) );
	    		statusVo.setRowId( mNotesCursor.getLong(0) );
	    		statusVo.setTrainCurrentStatus("");
	    		results.add(statusVo);
	        }
        }
		return results;
	}
	
	public boolean isNetworkAvailable()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if(networkInfo == null)
			return false;
		if(networkInfo.isConnected() == false)
			return false;
		if(networkInfo.isAvailable() == false)
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem mnuAbout = menu.add(Menu.NONE, MENU_ABOUT, MENU_ABOUT, "About");
		mnuAbout.setIcon(android.R.drawable.ic_menu_info_details);
		
		MenuItem mnuPreferences = menu.add(Menu.NONE, MENU_PREFERENCE, MENU_PREFERENCE, "Preferences");
		mnuPreferences.setIcon(android.R.drawable.ic_menu_preferences);
		return super.onCreateOptionsMenu(menu);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case MENU_ABOUT :
			 showAbout();
			return true;
		case MENU_PREFERENCE :
			// showPreferenceScreen
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Show the About View.
	 */
	private void showAbout()
	{
		Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
		this.startActivity(intent);
	}

}