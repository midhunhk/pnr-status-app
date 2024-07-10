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

package com.ae.apps.pnrstatus.managers;

import android.database.Cursor;
import android.widget.BaseAdapter;

import androidx.appcompat.app.AppCompatActivity;

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
		dataList = new ArrayList<>();

		mDbHelper = new DataHelper(activity.getApplicationContext());
		mDbHelper.open();

		Cursor mCursor = mDbHelper.fetchAllPnrNumbers();

		// Create a list of PNRStatusVos from the cursor
		PNRStatusVo statusVo;
		if (null != mCursor) {
			while (mCursor.moveToNext()) {
				statusVo = new PNRStatusVo();
				statusVo.pnrNumber = mCursor.getString(1);
				statusVo.rowId = mCursor.getLong(0);
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
			String pnrNumber = pnrStatusVo.pnrNumber;
			String pnrNumber2 = statusVo.pnrNumber;
            assert pnrNumber != null;
            if (pnrNumber.equals(pnrNumber2)) {
				// Delete from the database
				mDbHelper.deletePnrNumber(pnrStatusVo.rowId);
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
		long result = mDbHelper.addPnrNumber(statusVo.pnrNumber);
		if (result > -1) {
			statusVo.rowId = result;
			dataList.add(statusVo);

			// refresh the ListAdapter
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
			Logger.i(TAG, "Added " + statusVo.pnrNumber);
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
			String pnrNumber = pnrStatusVo.pnrNumber;
			String pnrNumber2 = statusVo.pnrNumber;
            assert pnrNumber != null;
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