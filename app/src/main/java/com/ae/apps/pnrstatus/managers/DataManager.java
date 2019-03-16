/*
 * Copyright 2012 Midhun Harikumar
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

package com.ae.apps.pnrstatus.managers;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.widget.BaseAdapter;

import com.ae.apps.pnrstatus.db.DataHelper;
import com.ae.apps.pnrstatus.utils.Logger;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;

import java.util.ArrayList;

/**
 * Does the operation on the device Database with the help of DataHelper
 * 
 * @author midhun_harikumar
 * 
 */
public class DataManager {

	private Cursor					mCursor;
	private DataHelper				mDbHelper;
	private final AppCompatActivity activity;
	private BaseAdapter				adapter;
	private ArrayList<PNRStatusVo>	dataList;

	private static final String		TAG	= "PNR_DataManager";

	public DataManager(AppCompatActivity activity) {
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
		boolean removed = false;
		for (int i = 0; i < dataList.size(); i++) {
			PNRStatusVo pnrStatusVo = dataList.get(i);
			String pnrNumber = pnrStatusVo.getPnrNumber();
			String pnrNumber2 = statusVo.getPnrNumber();
			if (pnrNumber.equals(pnrNumber2)) {
				// Delete from the database
				mDbHelper.deletePnrNumber(pnrStatusVo.getRowId());
				dataList.remove(i);
				removed = true;
				break;
			}
		}

		// Notify the adapter that the backing list has changed
		if (removed && adapter != null) {
			adapter.notifyDataSetChanged();
		}
		return removed;
	}

	/**
	 * Adds a pnrVo to the list and notifies the adapter
	 * 
	 * @param statusVo
	 */
	public boolean add(PNRStatusVo statusVo) {
		// Add a new PNR Number, note that we are not checking for duplicates
		long result = mDbHelper.addPnrNumber(statusVo.getPnrNumber());
		if (result > -1) {
			statusVo.setRowId(result);
			dataList.add(statusVo);

			// refresh the ListAdapter
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
			Logger.i(TAG, "Added " + statusVo.getPnrNumber());
			return true;
		} else {
			Logger.e(TAG, "Error in inserting row.");
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