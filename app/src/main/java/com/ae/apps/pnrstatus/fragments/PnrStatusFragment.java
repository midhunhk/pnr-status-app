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

package com.ae.apps.pnrstatus.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.ae.apps.pnrstatus.R;
import com.ae.apps.pnrstatus.adapters.PnrRowAdapter;
import com.ae.apps.pnrstatus.utils.Logger;
import com.ae.apps.pnrstatus.utils.PNRUtils;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;

import java.util.List;

/**
 * The PNRStatus Fragment
 * 
 * @author midhun_harikumar
 * 
 */
public class PnrStatusFragment extends Fragment {

	private static final String		TAG	= "PnrStatusFragment";

	private Context					context;
    private OnCheckStatusListener	mCallback;
    private ProgressBar				progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity activity = super.getActivity();
		View layout = inflater.inflate(R.layout.pnr_list_view, null);
		context = activity.getApplicationContext();
		initActivity(layout);
		return layout;
	}

	@Override
	public void onAttach(@NonNull Activity activity) {
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
            PnrRowAdapter mCustomAdapter = new PnrRowAdapter(context, this, pnrList);

			// set the adapter for the list and the data manager. setAdapter for AbsListView was introduced in API 11
            AbsListView listView = (AbsListView) layout.findViewById(R.id.pnrListView);
			if (listView instanceof ListView) {
				((ListView) listView).setAdapter(mCustomAdapter);
			} else {
				((GridView) listView).setAdapter(mCustomAdapter);
			}
			mCallback.setPNRStatusAdapter(mCustomAdapter);

			// Read the length of a valid PNR
			final int validPNRLength = Integer.parseInt(context.getResources().getString(R.string.pnr_number_length));
			final EditText txtPnrNumber = layout.findViewById(R.id.new_pnr_text);

            final ImageButton btnAdd = layout.findViewById(R.id.add_pnr_btn);
			btnAdd.setFocusable(true);
			btnAdd.setOnClickListener(v -> {
                // Create a PNRStatusVo object
                String pnrNumber = txtPnrNumber.getText().toString();
                Logger.i(TAG, "Add new pnr " + pnrNumber);
                if (pnrNumber.trim().length() == validPNRLength) {
                    PNRStatusVo statusVo = PNRUtils.getEmptyPNRStatusObject();
                    statusVo.pnrNumber = pnrNumber;

                    // Clear the input
                    txtPnrNumber.setText("");
                    btnAdd.requestFocus();

                    // Add the PNRStatusVo to the list
                    mCallback.addPnr(statusVo);
                } else {
                    String message = context.getResources().getString(R.string.str_invalid_pnr_message, pnrNumber);
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                }
            });
			progressBar = layout.findViewById(R.id.ProgressBar);
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
		 */
		List<PNRStatusVo> getListData();

		/**
		 * Adds a PNRNumber to the list, and notify the adapter if needed
		 */
		void addPnr(PNRStatusVo pnrStatusVo);

		/**
		 * Removes a PNRNumber from the list
		 */
		void removePnr(PNRStatusVo pnrStatusVo);

		/**
		 * Checks the status of a PNR Number
		 */
		void checkStatus(PNRStatusVo pnrStatusVo, ProgressBar progressBar);

		/**
		 * Set an adapter to the list
		 */
		void setPNRStatusAdapter(BaseAdapter adapter);

		/**
		 * Show the PNRStatus information in detail
		 */
		void showPNRStatusInfo(PNRStatusVo pnrStatusVo);
	}

}