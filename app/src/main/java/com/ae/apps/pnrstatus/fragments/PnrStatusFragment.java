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

package com.ae.apps.pnrstatus.fragments;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ae.apps.pnrstatus.adapters.PnrRowAdapter;
import com.ae.apps.pnrstatus.utils.Logger;
import com.ae.apps.pnrstatus.utils.PNRUtils;
import com.ae.apps.pnrstatus.v3.R;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;

/**
 * The PNRStatus Fragment
 * 
 * @author midhun_harikumar
 * 
 */
public class PnrStatusFragment extends Fragment {

	private static final String		TAG	= "PnrStatusFragment";

	private Context					context;
	private PnrRowAdapter			mCustomAdapter;
	private FragmentActivity		activity;
	private OnCheckStatusListener	mCallback;
	private AbsListView				listView;
	private ProgressBar				progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = super.getActivity();
		View layout = inflater.inflate(R.layout.pnr_list_view, null);
		context = activity.getApplicationContext();
		initActivity(layout);
		return layout;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnCheckStatusListener) activity;
		} catch (ClassCastException ex) {
			throw new ClassCastException(activity.toString() + " must implement OnCheckStatusListener");
		}
	}

	/**
	 * Initialize stuff
	 */
	private void initActivity(View layout) {
		try {
			// Get the data list from the db and set the data adapter
			List<PNRStatusVo> pnrList = mCallback.getListData();
			mCustomAdapter = new PnrRowAdapter(context, this, pnrList);

			// set the adapter for the list and the data manager. setAdapter for AbsListView was introduced in API 11
			listView = (AbsListView) layout.findViewById(R.id.pnrListView);
			if (listView instanceof ListView) {
				((ListView) listView).setAdapter(mCustomAdapter);
			} else {
				((GridView) listView).setAdapter(mCustomAdapter);
			}
			mCallback.setPNRStatusAdapter(mCustomAdapter);

			// Read the length of a valid PNR
			final int validPNRLength = Integer.valueOf(context.getResources().getString(R.string.pnr_number_length));
			final EditText txtPnrNumber = (EditText) layout.findViewById(R.id.new_pnr_text);

			// Fix the textbox text color for lower android versions
			if (android.os.Build.VERSION.SDK_INT < 11) {
				txtPnrNumber.setTextColor(Color.GRAY);
			}

			final ImageButton btnAdd = (ImageButton) layout.findViewById(R.id.add_pnr_btn);
			btnAdd.setFocusable(true);
			btnAdd.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Create a PNRStatusVo object
					String pnrNumber = txtPnrNumber.getText().toString();
					Logger.i(TAG, "Add new pnr " + pnrNumber);
					if (pnrNumber != null && pnrNumber.trim().length() == validPNRLength) {
						PNRStatusVo statusVo = PNRUtils.getEmptyPNRStatusObject();
						statusVo.setPnrNumber(pnrNumber);

						// Clear the input
						txtPnrNumber.setText("");
						btnAdd.requestFocus();

						// Add the PNRStatusVo to the list
						mCallback.addPnr(statusVo);
					} else {
						String message = context.getResources().getString(R.string.str_invalid_pnr_message, pnrNumber);
						Toast.makeText(context, message, Toast.LENGTH_LONG).show();
					}
				}
			});
			progressBar = (ProgressBar) layout.findViewById(R.id.ProgressBar);
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage());
		}
	}

	public void checkStatus(PNRStatusVo pnrStatusVo) {
		mCallback.checkStatus(pnrStatusVo, progressBar);
	}

	public void removeRow(PNRStatusVo pnrStatusVo) {
		mCallback.removePnr(pnrStatusVo);
	}

	public void showPNRStatusDetail(PNRStatusVo pnrStatusVo) {
		mCallback.showPNRStatusInfo(pnrStatusVo);
	}

	/**
	 * Container activity must implement this method
	 * 
	 * @author midhun_harikumar
	 * 
	 */
	public interface OnCheckStatusListener {
		/**
		 * Returns a list of PNRStatusVos
		 * 
		 * @return
		 */
		List<PNRStatusVo> getListData();

		/**
		 * Adds a PNRNumber to the list, and notify the adapter if needed
		 * 
		 * @param pnrStatusVo
		 */
		void addPnr(PNRStatusVo pnrStatusVo);

		/**
		 * Removes a PNRNumber from the list
		 * 
		 * @param pnrStatusVo
		 */
		void removePnr(PNRStatusVo pnrStatusVo);

		/**
		 * Checks the status of a PNR Number
		 * 
		 * @param pnrStatusVo
		 */
		void checkStatus(PNRStatusVo pnrStatusVo, ProgressBar progressBar);

		/**
		 * Set an adapter to the list
		 * 
		 * @param adapter
		 */
		void setPNRStatusAdapter(BaseAdapter adapter);

		/**
		 * Show the PNRStatus information indetail
		 * 
		 * @param pnrStatusVo
		 */
		void showPNRStatusInfo(PNRStatusVo pnrStatusVo);
	}

}