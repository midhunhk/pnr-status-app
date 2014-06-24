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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ae.apps.pnrstatus.adapters.StackAdapter;
import com.ae.apps.pnrstatus.managers.PNRMessagesManager;
import com.ae.apps.pnrstatus.managers.SMSManager;
import com.ae.apps.pnrstatus.utils.AppConstants;
import com.ae.apps.pnrstatus.utils.Logger;
import com.ae.apps.pnrstatus.utils.Utils;
import com.ae.apps.pnrstatus.v3.R;
import com.ae.apps.pnrstatus.vo.MessageVo;
import com.ae.apps.pnrstatus.vo.PNRStatusVo;
import com.ae.apps.pnrstatus.vo.PassengerDataVo;

public class MessagesFragment extends Fragment {

	private View				layout;
	private Context				context;
	private FragmentActivity	activity;
	private String				mCurrentPnr;
	private boolean				mCachedSettingsValue;

	private String				ADDRESS					= "VM-IRCTCi";
	private List<PNRStatusVo>	mPnrMessagesList		= null;
	private PNRMessagesManager	messagesManager			= null;
	private StackAdapter		adapter					= null;
	private TextView			mPassengerName			= null;
	private TextView			mCurrentStatus			= null;
	private TextView			mTravelDate				= null;
	private TextView			mBerthType				= null;
	private TextView			mTrainInfo				= null;
	private LinearLayout		mNoMessageHolder		= null;
	private LinearLayout		mListViewHolder			= null;
	private LinearLayout		mMessageDetailsHolder	= null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity = super.getActivity();
		layout = inflater.inflate(R.layout.pnr_messages_view, null);
		context = activity.getApplicationContext();

		if (Utils.isDevelopmentMode()) {
			ADDRESS = "11";
		}

		// Initialize the activity
		initActivity();

		return layout;
	}

	private void initActivity() {
		// Find the UI components from the layout's view
		mTrainInfo = (TextView) layout.findViewById(R.id.detailsTrain);
		mBerthType = (TextView) layout.findViewById(R.id.detailsBerthType);
		mTravelDate = (TextView) layout.findViewById(R.id.detailsTravelDate);
		mListViewHolder = (LinearLayout) layout.findViewById(R.id.listViewHolder);
		mPassengerName = (TextView) layout.findViewById(R.id.detailsPassengerName);
		mCurrentStatus = (TextView) layout.findViewById(R.id.detailsCurrentStatus);
		mNoMessageHolder = (LinearLayout) layout.findViewById(R.id.noMessagesHolder);
		mMessageDetailsHolder = (LinearLayout) layout.findViewById(R.id.messageDetailsLayout);

		// Create a MessageManager instance
		SMSManager smsManager = new SMSManager(context);

		// Fetch the list of messages by the address
		final List<MessageVo> allSmsList = smsManager.fetchMessagesByAddress(ADDRESS);

		// Create a PNRMessagesManager and load the parsed list of PNRs
		messagesManager = new PNRMessagesManager(allSmsList);

		// Cache the current value of the settings
		mCachedSettingsValue = getHidePastMessagePref();

		mPnrMessagesList = new ArrayList<PNRStatusVo>();
		// Update the list data for the view
		updateMessagesList();

		if (mPnrMessagesList == null || mPnrMessagesList.size() == 0) {
			// Hide the divider and show the invisible text
			ImageView dividerImage = (ImageView) layout.findViewById(R.id.dividerImage);
			dividerImage.setVisibility(View.GONE);
			mNoMessageHolder.setVisibility(View.VISIBLE);
		}

		// Create the custom adapter with the list of PNRs
		adapter = new StackAdapter(context, R.layout.message_item, mPnrMessagesList);

		// Get a supported instance of adapter view
		AdapterView<?> adapterView = Utils.getAdapterView(context, adapter);

		// Add the list to the holder
		mListViewHolder.addView(adapterView);

		// Add a click listener for the each item in the view
		adapterView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PNRStatusVo pnrStatusVo = (PNRStatusVo) parent.getAdapter().getItem(position);
				if (pnrStatusVo != null) {
					updateMessageDetails(pnrStatusVo);
				}
			}
		});

		mMessageDetailsHolder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCurrentPnr != null) {
					Utils.copyTextToClipboard(context, mCurrentPnr);
				}

			}
		});
	}

	/**
	 * Load the Messages list with PNR data. If an Adapter View is created, notify about data set changed
	 */
	public void updateMessagesList() {
		boolean hidePastMessages = getHidePastMessagePref();
		List<PNRStatusVo> tempList = messagesManager.getMessagesList(hidePastMessages);
		if (null != tempList && tempList.size() == 0) {
			mPnrMessagesList.removeAll(mPnrMessagesList);
			mNoMessageHolder.setVisibility(View.VISIBLE);
			mListViewHolder.setVisibility(View.INVISIBLE);
			mMessageDetailsHolder.setVisibility(View.INVISIBLE);
		} else {
			mPnrMessagesList.addAll(tempList);
			mNoMessageHolder.setVisibility(View.GONE);
			mListViewHolder.setVisibility(View.VISIBLE);
			mMessageDetailsHolder.setVisibility(View.VISIBLE);
		}
		Logger.i(AppConstants.TAG, "size " + mPnrMessagesList.size());
		// Tell the adapter that the data has changed
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// Save the current preference value
		mCachedSettingsValue = getHidePastMessagePref();
	}

	@Override
	public void onResume() {
		super.onResume();
		// If the cached value is different from the current prefernce value,
		if (mCachedSettingsValue != getHidePastMessagePref()) {
			updateMessagesList();
		}
	}

	/**
	 * Returns the current value for hide past messages
	 * 
	 * @return
	 */
	private boolean getHidePastMessagePref() {
		return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_key_hide_past_messages", false);
	}

	/**
	 * Update the details on the message pane
	 * 
	 * @param messageVo
	 */
	private void updateMessageDetails(PNRStatusVo pnrStatusVo) {
		PassengerDataVo passengerDataVo = pnrStatusVo.getFirstPassengerData();
		if (passengerDataVo != null) {
			// Get the values from the vo and update on the Message Details Pane
			mCurrentPnr = pnrStatusVo.getPnrNumber();
			Resources resources = context.getResources();
			mPassengerName.setText(resources.getString(R.string.str_passengner_name, passengerDataVo.getPassenger()));
			mTrainInfo.setText(context.getString(R.string.str_train_no, pnrStatusVo.getTrainNo()));
			String position = pnrStatusVo.getCurrentStatus();
			if (position.equals(AppConstants.BERTH_DEFAULT) == false) {
				position = position + " (" + passengerDataVo.getBerthPosition() + ")";
			}
			mTravelDate.setText(context.getString(R.string.str_journey_date, pnrStatusVo.getDateOfJourneyText()));
			mBerthType.setText(context.getString(R.string.str_ticket_class, pnrStatusVo.getTicketClass()));
			mCurrentStatus.setText(context.getString(R.string.str_booking_status, position));
		}
	}
}
