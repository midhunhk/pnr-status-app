package com.ae.apps.pnrstatus.managers;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.widget.BaseAdapter;

import com.ae.apps.pnrstatus.db.DataHelper;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;

/**
 * Does the operation on the device Database with the help of DataHelper
 * 
 * @author midhun_harikumar
 * 
 */
public class DataManager {

	private Cursor					mCursor;
	private DataHelper				mDbHelper;
	private Activity				activity;
	private BaseAdapter				adapter;
	private ArrayList<PNRStatusVo>	dataList;

	private static final String		TAG	= "DataManager";

	public DataManager(Activity activity) {
		this.activity = activity;

		// Read the data from the database
		readData();
	}

	public void setAdapter(final BaseAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * Reads the data from the database
	 */
	private void readData() {
		dataList = new ArrayList<PNRStatusVo>();

		mDbHelper = new DataHelper(activity.getApplicationContext());
		mDbHelper.open();

		mCursor = mDbHelper.fetchAllPnrNumbers();

		// Create a list of PNRStatusVos from the cursor
		PNRStatusVo statusVo = null;
		if (null != mCursor) {
			while (mCursor.moveToNext()) {
				statusVo = new PNRStatusVo();
				statusVo.setPnrNumber(mCursor.getString(1));
				statusVo.setRowId(mCursor.getLong(0));
				statusVo.setCurrentStatus("");
				dataList.add(statusVo);
			}
			mCursor.close();
		}
	}

	/**
	 * Returns the list of data
	 * 
	 * @return
	 */
	public ArrayList<PNRStatusVo> getDataList() {
		return dataList;
	}

	/**
	 * Removes a pnrVo from the list and notfies the adapter
	 * 
	 * @param statusVo
	 */
	public boolean remove(PNRStatusVo statusVo) {
		boolean isChanged = false;
		for (int i = 0; i < dataList.size(); i++) {
			PNRStatusVo pnrStatusVo = dataList.get(i);
			String pnrNumber = pnrStatusVo.getPnrNumber();
			String pnrNumber2 = statusVo.getPnrNumber();
			if (pnrNumber.equals(pnrNumber2)) {
				// Delete from the database
				mDbHelper.deletePnrNumber(pnrStatusVo.getRowId());
				dataList.remove(i);
				isChanged = true;
				break;
			}
		}

		// Update the list
		if (isChanged && adapter != null) {
			adapter.notifyDataSetChanged();
		}
		return isChanged;
	}

	/**
	 * Adds a pnrVo to the list and notifies the adapter
	 * 
	 * @param statusVo
	 */
	public boolean add(PNRStatusVo statusVo) {
		// Add to the dataSource
		long result = mDbHelper.addPnrNumber(statusVo.getPnrNumber());
		if (result > -1) {
			statusVo.setRowId(result);
			dataList.add(statusVo);

			// refresh the ListAdapter
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
			Log.i(TAG, "Added " + statusVo.getPnrNumber());
			return true;
		} else {
			Log.e(TAG, "Error in inserting row.");
		}
		return false;
	}

	/**
	 * Updates the statusVo in the list
	 * 
	 * @param statusVo
	 */
	public boolean update(PNRStatusVo statusVo) {
		boolean isUpdated = false;
		for (int i = 0; i < dataList.size(); i++) {
			PNRStatusVo pnrStatusVo = dataList.get(i);
			String pnrNumber = pnrStatusVo.getPnrNumber();
			String pnrNumber2 = statusVo.getPnrNumber();
			if (pnrNumber.equals(pnrNumber2)) {
				dataList.set(i, statusVo);
				isUpdated = true;
				// Notifiy the adapter
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				break;
			}
		}
		return isUpdated;
	}
}